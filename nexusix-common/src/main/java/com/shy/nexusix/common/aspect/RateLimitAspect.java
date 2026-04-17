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
 * <p>
 * 接口限流切面 - 基于内存的请求频率控制组件
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>拦截带有@RateLimit注解的方法，实现接口级别的限流保护</li>
 *   <li>支持多种限流策略：按IP限流、按用户限流、全局限流、IP+用户组合限流</li>
 *   <li>基于滑动时间窗口算法实现限流计数</li>
 *   <li>支持自定义限流阈值、时间窗口和提示消息</li>
 *   <li>超出限流阈值时抛出BusinessException异常</li>
 * </ul>
 *
 * <h3>设计目的：</h3>
 * <p>
 * 保护系统免受恶意请求或突发流量的冲击，通过限流策略确保系统在高并发场景下的稳定性。
 * 采用注解驱动的方式，使限流配置更加灵活和细粒度，可以针对不同接口配置不同的限流策略。
 * </p>
 *
 * <h3>适用场景：</h3>
 * <ul>
 *   <li>API接口防刷保护</li>
 *   <li>高并发场景下的流量控制</li>
 *   <li>敏感操作频率限制（如登录、短信发送）</li>
 *   <li>资源保护（如数据库查询、第三方API调用）</li>
 * </ul>
 *
 * <h3>核心实现逻辑：</h3>
 * <ol>
 *   <li>通过注解切入点匹配所有带有@RateLimit注解的方法</li>
 *   <li>根据限流类型构建限流Key（包含IP、用户ID或全局标识）</li>
 *   <li>使用ConcurrentHashMap存储限流计数器，确保线程安全</li>
 *   <li>基于滑动时间窗口算法判断是否允许请求通过</li>
 *   <li>超出限流阈值时抛出BusinessException阻止请求</li>
 * </ol>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 按IP限流：每个IP每秒最多100次请求
 * &#64;RateLimit(key = "api_user", limit = 100, period = 1, limitType = LimitType.IP)
 * public ApiResponse getUserList() { ... }
 *
 * // 按用户限流：每个用户每分钟最多10次请求
 * &#64;RateLimit(key = "api_order", limit = 10, period = 1, timeUnit = TimeUnit.MINUTES, limitType = LimitType.USER)
 * public ApiResponse createOrder() { ... }
 * </pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li><b>重要</b>：此实现为单机限流，分布式环境建议使用Redis实现</li>
 *   <li>限流数据存储在内存中，应用重启后计数器会重置</li>
 *   <li>按用户限流时，需要确保请求中包含用户身份信息（userId属性或Authorization头）</li>
 *   <li>限流Key应具有唯一性，避免不同接口的限流计数相互干扰</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see com.shy.nexusix.common.annotation.RateLimit
 * @see com.shy.nexusix.common.annotation.RateLimit.LimitType
 */
@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private static final String POINTCUT_RATE_LIMIT = "@annotation(com.shy.nexusix.common.annotation.RateLimit)";

    /**
     * 限流计数器存储
     * <p>
     * 使用ConcurrentHashMap存储限流信息，Key为限流标识，Value为限流计数器。
     * ConcurrentHashMap保证多线程环境下的并发安全性。
     * </p>
     */
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    /**
     * <p>
     * 定义限流切入点
     * </p>
     *
     * <h4>切入点表达式说明：</h4>
     * <pre>
     * @annotation(com.shy.nexusix.common.annotation.RateLimit)
     * </pre>
     * <ul>
     *   <li><b>@annotation</b>: 表示匹配带有指定注解的方法</li>
     *   <li><b>com.shy.nexusix.common.annotation.RateLimit</b>: 指定要匹配的注解全限定名</li>
     * </ul>
     *
     * <h4>作用范围：</h4>
     * <p>
     * 该切入点会匹配项目中所有带有@RateLimit注解的方法，无论其所在的包路径、类名或方法名。
     * 通常用于需要限流保护的Controller层方法上。
     * </p>
     *
     * <h4>匹配条件：</h4>
     * <p>
     * 只要方法上标注了@RateLimit注解，该方法就会被切面拦截处理。
     * </p>
     */
    @Pointcut(POINTCUT_RATE_LIMIT)
    public void rateLimitPointcut() {
    }

    /**
     * <p>
     * 环绕通知 - 在目标方法执行前后进行限流检查
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当带有@RateLimit注解的方法被调用时，在目标方法执行之前进行限流检查。
     * 如果限流检查通过，则执行目标方法；否则抛出异常阻止执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>解析@RateLimit注解，获取限流配置参数</li>
     *   <li>根据限流类型构建限流Key</li>
     *   <li>检查当前时间窗口内的请求计数</li>
     *   <li>如果未超出限流阈值，允许请求通过并执行目标方法</li>
     *   <li>如果超出限流阈值，抛出BusinessException阻止请求</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     *                  <ul>
     *                    <li>joinPoint.getSignature(): 获取方法签名，用于提取注解信息</li>
     *                    <li>joinPoint.proceed(): 执行目标方法</li>
     *                  </ul>
     * @return 目标方法的返回值
     *
     * @throws Throwable 可能抛出的异常：
     *                   <ul>
     *                     <li>BusinessException: 当超出限流阈值时抛出，错误码429</li>
     *                     <li>其他异常: 目标方法执行过程中可能抛出的异常</li>
     *                   </ul>
     */
    @Around("rateLimitPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名并转换为MethodSignature类型，用于提取方法对象和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法对象，用于读取方法上的@RateLimit注解
        Method method = signature.getMethod();

        // 获取方法上的@RateLimit注解实例
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        // 根据限流类型构建限流Key，Key用于标识不同的限流维度
        // 例如：按IP限流时Key包含IP地址，按用户限流时Key包含用户ID
        String limitKey = buildLimitKey(rateLimit, joinPoint);

        // 边缘情况处理：如果无法构建有效的限流Key（如非Web环境或无法获取用户ID）
        // 直接执行目标方法，不进行限流检查
        if (limitKey == null) {
            return joinPoint.proceed();
        }

        // 尝试获取限流许可
        // tryAcquire方法会检查当前时间窗口内的请求计数，并决定是否允许通过
        if (!tryAcquire(limitKey, rateLimit)) {
            // 限流触发：计算剩余等待时间，用于日志记录
            long currentTime = System.currentTimeMillis();
            // 获取限流配置的时间窗口大小（转换为毫秒）
            long windowSize = rateLimit.timeUnit().toMillis(rateLimit.period());
            // 从限流计数器中获取当前窗口信息
            RateLimitInfo info = rateLimitMap.get(limitKey);
            // 计算距离窗口重置的剩余时间
            long remainingTime = windowSize - (currentTime - info.windowStart);

            // 输出限流日志，包含Key、限制阈值、剩余时间等信息
            log.warn("[限流拦截] Key: {} | 限制: {}/{}{} | 消息: {}",
                    limitKey, rateLimit.limit(), rateLimit.period(), rateLimit.timeUnit().name().toLowerCase(), rateLimit.message());

            // 抛出业务异常，HTTP状态码429表示请求过多
            // 使用注解中配置的消息作为异常信息
            throw new BusinessException(429, rateLimit.message());
        }

        // 限流检查通过，执行目标方法并返回结果
        return joinPoint.proceed();
    }

    /**
     * <p>
     * 构建限流Key
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <p>
     * 根据不同的限流类型构建不同的限流Key：
     * </p>
     * <ul>
     *   <li><b>IP</b>: key:请求URI:ip:客户端IP</li>
     *   <li><b>USER</b>: key:请求URI:user:用户ID（如果用户ID不存在则跳过限流）</li>
     *   <li><b>GLOBAL</b>: key:请求URI:global</li>
     *   <li><b>IP_AND_USER</b>: key:请求URI:ip_user:客户端IP:用户ID</li>
     * </ul>
     *
     * @param rateLimit 限流注解配置
     * @param joinPoint 连接点对象
     * @return 限流Key字符串，如果无法构建有效的Key则返回null
     */
    private String buildLimitKey(RateLimit rateLimit, ProceedingJoinPoint joinPoint) {
        // 获取当前请求的上下文属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 边缘情况处理：如果请求上下文为空（如异步调用或非Web环境），返回null跳过限流
        if (attributes == null) {
            return null;
        }

        // 从请求属性中获取HttpServletRequest对象
        HttpServletRequest request = attributes.getRequest();

        // 构建限流Key的基础部分：注解配置的key前缀 + 请求URI
        // 例如：rate_limit:/api/user/list
        StringBuilder keyBuilder = new StringBuilder(rateLimit.key());
        keyBuilder.append(":").append(request.getRequestURI());

        // 根据限流类型构建不同的Key后缀
        switch (rateLimit.limitType()) {
            case IP:
                // 按IP限流：Key格式为 key:uri:ip:客户端IP
                // 适用于需要限制单个IP请求频率的场景
                keyBuilder.append(":ip:").append(getClientIp(request));
                break;

            case USER:
                // 按用户限流：Key格式为 key:uri:user:用户ID
                // 适用于需要限制单个用户请求频率的场景
                String userId = getUserId(request);
                if (userId == null) {
                    // 边缘情况：无法获取用户ID时返回null，跳过限流
                    // 这种情况下可能是未登录用户，根据业务需求决定是否限流
                    return null;
                }
                keyBuilder.append(":user:").append(userId);
                break;

            case GLOBAL:
                // 全局限流：Key格式为 key:uri:global
                // 适用于需要限制接口总请求量的场景
                // 所有用户共享同一个限流计数器
                keyBuilder.append(":global");
                break;

            case IP_AND_USER:
                // IP和用户组合限流：Key格式为 key:uri:ip_user:IP:用户ID
                // 结合IP和用户ID进行限流，更加精细
                String uid = getUserId(request);
                if (uid == null) {
                    // 无法获取用户ID时，退化为按IP限流
                    keyBuilder.append(":ip:").append(getClientIp(request));
                } else {
                    // 同时包含IP和用户ID
                    keyBuilder.append(":ip_user:").append(getClientIp(request)).append(":").append(uid);
                }
                break;

            default:
                // 默认情况不做额外处理
                break;
        }

        return keyBuilder.toString();
    }

    /**
     * <p>
     * 尝试获取限流许可
     * </p>
     *
     * <h4>算法说明：</h4>
     * <p>
     * 基于滑动时间窗口算法实现限流：
     * </p>
     * <ol>
     *   <li>检查当前时间是否超出时间窗口，如果超出则重置计数器</li>
     *   <li>递增当前计数器</li>
     *   <li>判断计数器是否超出限流阈值</li>
     *   <li>返回是否允许请求通过</li>
     * </ol>
     *
     * <h4>线程安全：</h4>
     * <p>
     * 使用synchronized关键字确保同一限流Key的计数操作是原子性的。
     * </p>
     *
     * @param key 限流Key
     * @param rateLimit 限流配置
     * @return true-允许请求通过，false-超出限流阈值
     */
    private boolean tryAcquire(String key, RateLimit rateLimit) {
        // 获取当前时间戳，用于时间窗口判断
        long currentTime = System.currentTimeMillis();

        // 将注解配置的时间窗口转换为毫秒
        // 例如：period=1, timeUnit=SECONDS -> windowSize=1000ms
        long windowSize = rateLimit.timeUnit().toMillis(rateLimit.period());

        // 从ConcurrentHashMap中获取或创建限流计数器
        // computeIfAbsent是原子操作，确保并发安全
        RateLimitInfo info = rateLimitMap.computeIfAbsent(key, k -> new RateLimitInfo());

        // 使用synchronized对限流计数器加锁，确保同一Key的操作原子性
        // 这里的锁粒度是单个限流Key，不会影响其他Key的并发处理
        synchronized (info) {
            // 滑动时间窗口判断：检查当前时间是否超出时间窗口
            // currentTime - info.windowStart > windowSize 表示已经进入下一个时间窗口
            if (currentTime - info.windowStart > windowSize) {
                // 时间窗口重置：更新窗口起始时间为当前时间
                info.windowStart = currentTime;
                // 重置计数器为0，开始新的计数周期
                info.currentCount.set(0);
            }

            // 原子递增计数器，并获取递增后的值
            // incrementAndGet是原子操作，确保计数准确
            long count = info.currentCount.incrementAndGet();

            // 判断是否超出限流阈值
            // count > rateLimit.limit() 表示已超出限制
            if (count > rateLimit.limit()) {
                // 超出限流阈值，返回false拒绝请求
                return false;
            }

            // 未超出限流阈值，返回true允许请求通过
            return true;
        }
    }

    /**
     * <p>
     * 获取当前用户ID
     * </p>
     *
     * <h4>获取顺序：</h4>
     * <ol>
     *   <li>从请求属性中获取userId</li>
     *   <li>从Authorization请求头中获取token并计算hash值</li>
     * </ol>
     *
     * @param request HTTP请求对象
     * @return 用户ID字符串，如果无法获取则返回null
     */
    private String getUserId(HttpServletRequest request) {
        // 优先级1：从请求属性中获取userId
        // 通常在认证拦截器中将用户ID存入请求属性
        Object userId = request.getAttribute("userId");
        if (userId != null) {
            return userId.toString();
        }

        // 优先级2：从Authorization请求头中获取token
        // 如果请求属性中没有userId，尝试从token中提取
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            // 使用token的hashCode作为用户标识
            // 这是一种简化的处理方式，实际项目中可能需要解析JWT获取用户ID
            return token.hashCode() + "";
        }

        // 无法获取用户ID，返回null
        return null;
    }

    /**
     * <p>
     * 获取客户端真实IP地址
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <p>
     * 按照以下顺序依次尝试获取客户端IP：
     * </p>
     * <ol>
     *   <li>X-Forwarded-For头（反向代理场景）</li>
     *   <li>Proxy-Client-IP头（Apache服务器）</li>
     *   <li>WL-Proxy-Client-IP头（WebLogic服务器）</li>
     *   <li>HTTP_CLIENT_IP头</li>
     *   <li>HTTP_X_FORWARDED_FOR头</li>
     *   <li>request.getRemoteAddr()（直连场景）</li>
     * </ol>
     *
     * <h4>注意事项：</h4>
     * <ul>
     *   <li>X-Forwarded-For可能包含多个IP（逗号分隔），取第一个</li>
     *   <li>如果获取到的IP为"unknown"则继续尝试下一个来源</li>
     * </ul>
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址字符串
     */
    private String getClientIp(HttpServletRequest request) {
        // 优先级1：尝试从X-Forwarded-For头获取IP
        // 这是最常用的反向代理传递客户端IP的方式
        // Nginx、HAProxy等反向代理通常会设置此头
        String ip = request.getHeader("X-Forwarded-For");

        // 判断条件：IP为空、空字符串或"unknown"时尝试下一个来源
        // "unknown"是某些代理服务器无法获取客户端IP时设置的默认值
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 优先级2：尝试从Proxy-Client-IP头获取IP
            // 这是Apache服务器使用的方式
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 优先级3：尝试从WL-Proxy-Client-IP头获取IP
            // 这是WebLogic服务器使用的方式
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 优先级4：尝试从HTTP_CLIENT_IP头获取IP
            // 某些代理服务器使用此头传递客户端IP
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 优先级5：尝试从HTTP_X_FORWARDED_FOR头获取IP
            // 这是另一种常见的传递方式
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 优先级6（最后）：直接从请求中获取远程地址
            // 这是直连场景下的客户端IP，没有经过代理
            ip = request.getRemoteAddr();
        }

        // 特殊处理：X-Forwarded-For可能包含多个IP（客户端IP, 代理1IP, 代理2IP...）
        // 格式如：192.168.1.100, 10.0.0.1, 172.16.0.1
        // 取第一个IP作为真实客户端IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * <p>
     * 限流信息内部类 - 存储限流计数器状态
     * </p>
     *
     * <h4>字段说明：</h4>
     * <ul>
     *   <li><b>windowStart</b>: 时间窗口起始时间（毫秒时间戳）</li>
     *   <li><b>currentCount</b>: 当前时间窗口内的请求计数</li>
     * </ul>
     *
     * <h4>线程安全：</h4>
     * <p>
     * windowStart使用volatile修饰确保可见性，currentCount使用AtomicLong确保原子性。
     * </p>
     */
    private static class RateLimitInfo {
        /**
         * 时间窗口起始时间
         * <p>
         * 使用volatile修饰确保多线程环境下的可见性。
         * 当一个线程更新了windowStart，其他线程能立即看到最新值。
         * </p>
         */
        volatile long windowStart = System.currentTimeMillis();

        /**
         * 当前时间窗口内的请求计数
         * <p>
         * 使用AtomicLong确保计数操作的原子性。
         * incrementAndGet()方法是原子操作，无需额外同步。
         * </p>
         */
        AtomicLong currentCount = new AtomicLong(0);
    }
}
