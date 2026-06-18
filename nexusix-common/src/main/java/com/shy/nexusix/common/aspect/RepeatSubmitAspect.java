package com.shy.nexusix.common.aspect;

import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.annotation.RepeatSubmit;
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

/**
 * <p>防重复提交切面，基于内存的防重记录实现请求重复提交防护</p>
 *
 * @author shy
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    private static final Logger log = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    private static final String POINTCUT_REPEAT_SUBMIT = "@annotation(com.shy.nexusix.common.annotation.RepeatSubmit)";

    /**
     * <p>防重记录存储，Key为防重标识，Value为上次提交时间戳</p>
     */
    private final Map<String, Long> submitRecordMap = new ConcurrentHashMap<>();

    /**
     * <p>防重复提交切入点</p>
     */
    @Pointcut(POINTCUT_REPEAT_SUBMIT)
    public void repeatSubmitPointcut() {
    }

    /**
     * <p>环绕通知，在目标方法执行前进行防重检查</p>
     *
     * @param joinPoint 连接点
     * @return 目标方法返回值
     * @throws Throwable 重复提交时抛出BusinessException
     */
    @Around("repeatSubmitPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);

        // 构建防重Key
        String submitKey = buildSubmitKey(repeatSubmit, joinPoint);
        if (submitKey == null) {
            return joinPoint.proceed();
        }

        long currentTime = System.currentTimeMillis();
        Long lastSubmitTime = submitRecordMap.get(submitKey);

        // 检查是否存在重复提交
        if (lastSubmitTime != null && (currentTime - lastSubmitTime) < repeatSubmit.interval()) {
            long remainingTime = repeatSubmit.interval() - (currentTime - lastSubmitTime);
            log.warn("[防重复提交] Key: {} | 间隔: {}ms | 剩余: {}ms | 消息: {}",
                    submitKey, repeatSubmit.interval(), remainingTime, repeatSubmit.message());
            throw new BusinessException(repeatSubmit.message());
        }

        // 防重检查通过，记录提交时间
        submitRecordMap.put(submitKey, currentTime);

        // 清理过期记录
        cleanExpiredRecords(currentTime, repeatSubmit.interval());

        return joinPoint.proceed();
    }

    /**
     * <p>根据防重类型构建防重Key</p>
     *
     * @param repeatSubmit 防重注解配置
     * @param joinPoint 连接点
     * @return 防重Key，无法构建时返回null
     */
    private String buildSubmitKey(RepeatSubmit repeatSubmit, ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        StringBuilder keyBuilder = new StringBuilder("repeat_submit:");
        keyBuilder.append(request.getRequestURI());

        switch (repeatSubmit.submitType()) {
            case IP:
                keyBuilder.append(":ip:").append(getClientIp(request));
                break;
            case USER:
                String userId = getUserId(request);
                if (userId == null) {
                    keyBuilder.append(":ip:").append(getClientIp(request));
                } else {
                    keyBuilder.append(":user:").append(userId);
                }
                break;
            case IP_AND_USER:
                String uid = getUserId(request);
                keyBuilder.append(":ip:").append(getClientIp(request));
                if (uid != null) {
                    keyBuilder.append(":user:").append(uid);
                }
                break;
            case PARAMS:
                keyBuilder.append(":params:").append(getParamsHash(joinPoint));
                break;
            default:
                break;
        }

        // 追加参数哈希
        if (repeatSubmit.includeParams()) {
            keyBuilder.append(":params:").append(getParamsHash(joinPoint));
        }

        return keyBuilder.toString();
    }

    /**
     * <p>获取请求参数的哈希值</p>
     *
     * @param joinPoint 连接点
     * @return 参数哈希值字符串
     */
    private String getParamsHash(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "empty";
            }

            String paramsJson = JSON.toJSONString(args);
            return String.valueOf(paramsJson.hashCode());
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * <p>获取当前用户ID</p>
     *
     * @param request HTTP请求对象
     * @return 用户ID，无法获取时返回null
     */
    private String getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId != null) {
            return userId.toString();
        }

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
     * <p>清理过期的防重记录，记录数超过1000时触发</p>
     *
     * @param currentTime 当前时间戳
     * @param maxInterval 最大防重间隔时间（毫秒）
     */
    private void cleanExpiredRecords(long currentTime, long maxInterval) {
        if (submitRecordMap.size() > 1000) {
            // 清理超过2倍间隔时间的记录
            submitRecordMap.entrySet().removeIf(entry ->
                    (currentTime - entry.getValue()) > maxInterval * 2);
        }
    }
}
