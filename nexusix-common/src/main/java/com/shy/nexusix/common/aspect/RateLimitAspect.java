package com.shy.nexusix.common.aspect;

import com.shy.nexusix.common.annotation.RateLimit;
import com.shy.nexusix.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>接口限流切面，基于内存的滑动时间窗口算法实现请求频率控制</p>
 *
 * @author shy
 */
@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private static final String POINTCUT_RATE_LIMIT = "@annotation(com.shy.nexusix.common.annotation.RateLimit)";

    /**
     * <p>限流计数器存储，Key为限流标识，Value为计数器状态</p>
     */
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    /**
     * <p>限流切入点</p>
     */
    @Pointcut(POINTCUT_RATE_LIMIT)
    public void rateLimitPointcut() {
    }

    /**
     * <p>环绕通知，在目标方法执行前进行限流检查</p>
     *
     * @param joinPoint 连接点
     * @return 目标方法返回值
     * @throws Throwable 超出限流阈值时抛出BusinessException
     */
    @Around("rateLimitPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        // 构建限流Key
        String limitKey = buildLimitKey(rateLimit, joinPoint);
        if (limitKey == null) {
            return joinPoint.proceed();
        }

        // 尝试获取限流许可
        if (!tryAcquire(limitKey, rateLimit)) {
            log.warn("[限流拦截] Key: {} | 限制: {}/{}{} | 消息: {}",
                    limitKey, rateLimit.limit(), rateLimit.period(), rateLimit.timeUnit().name().toLowerCase(), rateLimit.message());
            throw new BusinessException(429, rateLimit.message());
        }

        return joinPoint.proceed();
    }

    /**
     * <p>根据限流类型构建限流Key</p>
     *
     * @param rateLimit 限流注解配置
     * @param joinPoint 连接点
     * @return 限流Key，无法构建时返回null
     */
    private String buildLimitKey(RateLimit rateLimit, ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        StringBuilder keyBuilder = new StringBuilder(rateLimit.key());
        keyBuilder.append(":").append(request.getRequestURI());

        switch (rateLimit.limitType()) {
            case IP:
                keyBuilder.append(":ip:").append(getClientIp(request));
                break;
            case USER:
                String userId = getUserId(request);
                if (userId == null) {
                    return null;
                }
                keyBuilder.append(":user:").append(userId);
                break;
            case GLOBAL:
                keyBuilder.append(":global");
                break;
            case IP_AND_USER:
                String uid = getUserId(request);
                if (uid == null) {
                    keyBuilder.append(":ip:").append(getClientIp(request));
                } else {
                    keyBuilder.append(":ip_user:").append(getClientIp(request)).append(":").append(uid);
                }
                break;
            default:
                break;
        }

        return keyBuilder.toString();
    }

    /**
     * <p>基于滑动时间窗口算法尝试获取限流许可</p>
     *
     * @param key 限流Key
     * @param rateLimit 限流配置
     * @return true-允许通过，false-超出限流阈值
     */
    private boolean tryAcquire(String key, RateLimit rateLimit) {
        long currentTime = System.currentTimeMillis();
        long windowSize = rateLimit.timeUnit().toMillis(rateLimit.period());

        RateLimitInfo info = rateLimitMap.computeIfAbsent(key, k -> new RateLimitInfo());

        synchronized (info) {
            // 滑动时间窗口判断
            if (currentTime - info.windowStart > windowSize) {
                info.windowStart = currentTime;
                info.currentCount.set(0);
            }

            long count = info.currentCount.incrementAndGet();
            return count <= rateLimit.limit();
        }
    }

    /**
     * <p>获取当前用户ID</p>
     *
     * @param request HTTP请求对象
     * @return 用户ID，无法获取时返回null
     */
    private String getUserId(HttpServletRequest request) {
        // 优先从请求属性获取
        Object userId = request.getAttribute("userId");
        if (userId != null) {
            return userId.toString();
        }

        // 从Authorization头获取token哈希
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            return token.hashCode() + "";
        }

        return null;
    }

    /**
     * <p>获取客户端真实IP地址，支持多级代理</p>
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * <p>限流计数器状态</p>
     */
    private static class RateLimitInfo {
        /** 时间窗口起始时间 */
        volatile long windowStart = System.currentTimeMillis();
        /** 当前时间窗口内的请求计数 */
        AtomicLong currentCount = new AtomicLong(0);
    }
}
