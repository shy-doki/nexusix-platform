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
 * <p>
 * 防重复提交切面 - 请求重复提交防护组件
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>拦截带有@RepeatSubmit注解的方法，防止用户重复提交相同的请求</li>
 *   <li>支持多种防重策略：按IP防重、按用户防重、IP+用户组合防重、按参数防重</li>
 *   <li>基于内存的防重记录存储，支持自定义时间间隔</li>
 *   <li>支持可选的请求参数参与防重Key生成</li>
 *   <li>超出防重间隔时抛出BusinessException异常</li>
 *   <li>自动清理过期的防重记录，防止内存泄漏</li>
 * </ul>
 *
 * <h3>设计目的：</h3>
 * <p>
 * 防止用户在短时间内重复提交相同的请求，避免产生重复数据或重复执行业务操作。
 * 通过注解驱动的方式，使防重配置更加灵活，可以针对不同接口配置不同的防重策略和时间间隔。
 * </p>
 *
 * <h3>适用场景：</h3>
 * <ul>
 *   <li>表单提交防重（如订单创建、数据保存）</li>
 *   <li>支付接口防重</li>
 *   <li>敏感操作防重（如密码修改、权限变更）</li>
 *   <li>资源创建接口防重</li>
 * </ul>
 *
 * <h3>核心实现逻辑：</h3>
 * <ol>
 *   <li>通过注解切入点匹配所有带有@RepeatSubmit注解的方法</li>
 *   <li>根据防重类型构建防重Key（包含IP、用户ID、请求参数等）</li>
 *   <li>使用ConcurrentHashMap存储防重记录，记录上次提交时间</li>
 *   <li>检查当前请求与上次请求的时间间隔是否小于配置的间隔</li>
 *   <li>如果间隔小于配置值，抛出BusinessException阻止请求</li>
 *   <li>定期清理过期的防重记录，防止内存无限增长</li>
 * </ol>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 默认防重：5秒内不允许重复提交
 * &#64;RepeatSubmit
 * public ApiResponse submitOrder(&#64;RequestBody OrderDTO order) { ... }
 *
 * // 自定义间隔：10秒内不允许重复提交
 * &#64;RepeatSubmit(interval = 10000, message = "请勿频繁提交")
 * public ApiResponse saveData(&#64;RequestBody DataDTO data) { ... }
 *
 * // 按参数防重：相同参数10秒内不允许重复提交
 * &#64;RepeatSubmit(interval = 10000, submitType = SubmitType.PARAMS)
 * public ApiResponse processRequest(&#64;RequestBody RequestDTO request) { ... }
 * </pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li><b>重要</b>：此实现为单机防重，分布式环境建议使用Redis实现</li>
 *   <li>防重数据存储在内存中，应用重启后防重记录会重置</li>
 *   <li>按用户防重时，需要确保请求中包含用户身份信息（userId属性或Authorization头）</li>
 *   <li>防重Key包含请求URI，不同接口的防重记录相互独立</li>
 *   <li>当记录数超过1000条时会自动清理过期记录</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see com.shy.nexusix.common.annotation.RepeatSubmit
 * @see com.shy.nexusix.common.annotation.RepeatSubmit.SubmitType
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    private static final Logger log = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    private static final String POINTCUT_REPEAT_SUBMIT = "@annotation(com.shy.nexusix.common.annotation.RepeatSubmit)";

    /**
     * 防重记录存储
     * <p>
     * 使用ConcurrentHashMap存储防重记录，Key为防重标识，Value为上次提交时间戳。
     * ConcurrentHashMap保证多线程环境下的并发安全性。
     * </p>
     */
    private final Map<String, Long> submitRecordMap = new ConcurrentHashMap<>();

    /**
     * <p>
     * 定义防重复提交切入点
     * </p>
     *
     * <h4>切入点表达式说明：</h4>
     * <pre>
     * @annotation(com.shy.nexusix.common.annotation.RepeatSubmit)
     * </pre>
     * <ul>
     *   <li><b>@annotation</b>: 表示匹配带有指定注解的方法</li>
     *   <li><b>com.shy.nexusix.common.annotation.RepeatSubmit</b>: 指定要匹配的注解全限定名</li>
     * </ul>
     *
     * <h4>作用范围：</h4>
     * <p>
     * 该切入点会匹配项目中所有带有@RepeatSubmit注解的方法，无论其所在的包路径、类名或方法名。
     * 通常用于需要防重复提交的Controller层方法上，如表单提交、订单创建等接口。
     * </p>
     *
     * <h4>匹配条件：</h4>
     * <p>
     * 只要方法上标注了@RepeatSubmit注解，该方法就会被切面拦截处理。
     * </p>
     */
    @Pointcut(POINTCUT_REPEAT_SUBMIT)
    public void repeatSubmitPointcut() {
    }

    /**
     * <p>
     * 环绕通知 - 在目标方法执行前进行防重检查
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当带有@RepeatSubmit注解的方法被调用时，在目标方法执行之前进行防重检查。
     * 如果防重检查通过，则执行目标方法并记录提交时间；否则抛出异常阻止执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>解析@RepeatSubmit注解，获取防重配置参数</li>
     *   <li>根据防重类型构建防重Key</li>
     *   <li>检查是否存在上次提交记录</li>
     *   <li>如果存在且间隔时间小于配置值，抛出BusinessException阻止请求</li>
     *   <li>如果不存在或间隔时间大于配置值，记录当前提交时间并执行目标方法</li>
     *   <li>定期清理过期的防重记录</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     *                  <ul>
     *                    <li>joinPoint.getSignature(): 获取方法签名，用于提取注解信息</li>
     *                    <li>joinPoint.getArgs(): 获取方法参数，用于生成参数哈希</li>
     *                    <li>joinPoint.proceed(): 执行目标方法</li>
     *                  </ul>
     * @return 目标方法的返回值
     *
     * @throws Throwable 可能抛出的异常：
     *                   <ul>
     *                     <li>BusinessException: 当重复提交时抛出，包含配置的提示消息</li>
     *                     <li>其他异常: 目标方法执行过程中可能抛出的异常</li>
     *                   </ul>
     */
    @Around("repeatSubmitPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名并转换为MethodSignature类型，用于提取方法对象和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法对象，用于读取方法上的@RepeatSubmit注解
        Method method = signature.getMethod();

        // 获取方法上的@RepeatSubmit注解实例
        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);

        // 根据防重类型构建防重Key，Key用于标识不同的防重维度
        // 例如：按IP防重时Key包含IP地址，按用户防重时Key包含用户ID
        String submitKey = buildSubmitKey(repeatSubmit, joinPoint);

        // 边缘情况处理：如果无法构建有效的防重Key（如非Web环境）
        // 直接执行目标方法，不进行防重检查
        if (submitKey == null) {
            return joinPoint.proceed();
        }

        // 获取当前时间戳，用于时间间隔判断
        long currentTime = System.currentTimeMillis();

        // 从防重记录中获取上次提交时间
        // 如果不存在记录，lastSubmitTime为null
        Long lastSubmitTime = submitRecordMap.get(submitKey);

        // 检查是否存在重复提交
        // 条件：存在上次提交记录 且 当前时间与上次提交时间的间隔小于配置的防重间隔
        if (lastSubmitTime != null && (currentTime - lastSubmitTime) < repeatSubmit.interval()) {
            // 计算距离可以再次提交的剩余时间
            long remainingTime = repeatSubmit.interval() - (currentTime - lastSubmitTime);

            // 输出防重日志，包含Key、间隔、剩余时间等信息
            log.warn("[防重复提交] Key: {} | 间隔: {}ms | 剩余: {}ms | 消息: {}",
                    submitKey, repeatSubmit.interval(), remainingTime, repeatSubmit.message());

            // 抛出业务异常，阻止重复提交
            // 使用注解中配置的消息作为异常信息
            throw new BusinessException(repeatSubmit.message());
        }

        // 防重检查通过，记录当前提交时间
        // 这里的时间戳将用于下次防重检查
        submitRecordMap.put(submitKey, currentTime);

        // 清理过期的防重记录，防止内存无限增长
        cleanExpiredRecords(currentTime, repeatSubmit.interval());

        // 执行目标方法并返回结果
        return joinPoint.proceed();
    }

    /**
     * <p>
     * 构建防重Key
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <p>
     * 根据不同的防重类型构建不同的防重Key：
     * </p>
     * <ul>
     *   <li><b>IP</b>: repeat_submit:请求URI:ip:客户端IP</li>
     *   <li><b>USER</b>: repeat_submit:请求URI:user:用户ID（如果用户ID不存在则使用IP）</li>
     *   <li><b>IP_AND_USER</b>: repeat_submit:请求URI:ip:客户端IP:user:用户ID</li>
     *   <li><b>PARAMS</b>: repeat_submit:请求URI:params:参数哈希</li>
     * </ul>
     *
     * <h4>参数哈希：</h4>
     * <p>
     * 如果includeParams属性为true，会在Key后追加参数哈希值，
     * 确保相同参数的请求才会被防重，不同参数的请求互不影响。
     * </p>
     *
     * @param repeatSubmit 防重注解配置
     * @param joinPoint 连接点对象
     * @return 防重Key字符串，如果无法构建有效的Key则返回null
     */
    private String buildSubmitKey(RepeatSubmit repeatSubmit, ProceedingJoinPoint joinPoint) {
        // 获取当前请求的上下文属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 边缘情况处理：如果请求上下文为空（如异步调用或非Web环境），返回null跳过防重
        if (attributes == null) {
            return null;
        }

        // 从请求属性中获取HttpServletRequest对象
        HttpServletRequest request = attributes.getRequest();

        // 构建防重Key的基础部分：固定前缀 + 请求URI
        // 例如：repeat_submit:/api/order/create
        StringBuilder keyBuilder = new StringBuilder("repeat_submit:");
        keyBuilder.append(request.getRequestURI());

        // 根据防重类型构建不同的Key后缀
        switch (repeatSubmit.submitType()) {
            case IP:
                // 按IP防重：Key格式为 repeat_submit:uri:ip:客户端IP
                // 适用于需要限制单个IP提交频率的场景
                keyBuilder.append(":ip:").append(getClientIp(request));
                break;

            case USER:
                // 按用户防重：Key格式为 repeat_submit:uri:user:用户ID
                // 适用于需要限制单个用户提交频率的场景
                String userId = getUserId(request);
                if (userId == null) {
                    // 边缘情况：无法获取用户ID时，退化为按IP防重
                    // 这确保了未登录用户也能被防重保护
                    keyBuilder.append(":ip:").append(getClientIp(request));
                } else {
                    keyBuilder.append(":user:").append(userId);
                }
                break;

            case IP_AND_USER:
                // IP和用户组合防重：Key格式为 repeat_submit:uri:ip:IP:user:用户ID
                // 结合IP和用户ID进行防重，更加精细
                String uid = getUserId(request);
                // 始终包含IP部分
                keyBuilder.append(":ip:").append(getClientIp(request));
                if (uid != null) {
                    // 如果能获取用户ID，追加用户ID部分
                    keyBuilder.append(":user:").append(uid);
                }
                break;

            case PARAMS:
                // 按参数防重：Key格式为 repeat_submit:uri:params:参数哈希
                // 适用于需要根据请求参数区分防重的场景
                keyBuilder.append(":params:").append(getParamsHash(joinPoint));
                break;

            default:
                // 默认情况不做额外处理
                break;
        }

        // 如果配置了包含参数，在Key后追加参数哈希
        // 这样可以确保相同参数的请求才会被防重，不同参数的请求互不影响
        if (repeatSubmit.includeParams()) {
            keyBuilder.append(":params:").append(getParamsHash(joinPoint));
        }

        return keyBuilder.toString();
    }

    /**
     * <p>
     * 获取请求参数的哈希值
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <ol>
     *   <li>获取方法参数数组</li>
     *   <li>将参数数组序列化为JSON字符串</li>
     *   <li>计算JSON字符串的哈希值</li>
     * </ol>
     *
     * <h4>注意事项：</h4>
     * <ul>
     *   <li>如果参数为空或序列化失败，返回固定字符串</li>
     *   <li>哈希值用于区分不同参数的请求</li>
     * </ul>
     *
     * @param joinPoint 连接点对象
     * @return 参数哈希值字符串
     */
    private String getParamsHash(ProceedingJoinPoint joinPoint) {
        try {
            // 获取方法签名，用于获取参数信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            // 获取方法参数数组
            Object[] args = joinPoint.getArgs();

            // 边缘情况处理：参数数组为空时返回固定字符串
            if (args == null || args.length == 0) {
                return "empty";
            }

            // 使用fastjson2将参数数组序列化为JSON字符串
            String paramsJson = JSON.toJSONString(args);

            // 计算JSON字符串的哈希值作为参数标识
            // 哈希值可以快速比较参数是否相同
            return String.valueOf(paramsJson.hashCode());
        } catch (Exception e) {
            // 边缘情况处理：序列化失败时返回固定字符串
            // 这确保了异常情况下防重功能不会失效
            return "error";
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
     * 清理过期的防重记录
     * </p>
     *
     * <h4>清理策略：</h4>
     * <ul>
     *   <li>当记录数超过1000条时触发清理</li>
     *   <li>清理超过最大间隔时间2倍的记录</li>
     *   <li>使用removeIf方法进行原子性删除</li>
     * </ul>
     *
     * <h4>设计目的：</h4>
     * <p>
     * 防止内存无限增长，定期清理不再需要的防重记录。
     * 选择2倍间隔时间是为了保留一定的缓冲空间，避免误删有效的防重记录。
     * </p>
     *
     * @param currentTime 当前时间戳
     * @param maxInterval 最大防重间隔时间（毫秒）
     */
    private void cleanExpiredRecords(long currentTime, long maxInterval) {
        // 检查记录数量是否超过阈值
        // 只有超过1000条时才触发清理，避免频繁清理影响性能
        if (submitRecordMap.size() > 1000) {
            // 使用removeIf进行条件删除
            // 删除条件：记录时间距离当前时间超过最大间隔的2倍
            // 2倍间隔确保不会误删仍在有效期内的记录
            submitRecordMap.entrySet().removeIf(entry ->
                    (currentTime - entry.getValue()) > maxInterval * 2);
        }
    }
}
