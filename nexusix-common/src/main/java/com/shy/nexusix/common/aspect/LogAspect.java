package com.shy.nexusix.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 日志切面 - 统一请求日志记录组件
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>自动拦截所有Controller层方法，记录请求日志</li>
 *   <li>记录请求开始时间、客户端IP、请求方法、请求参数等信息</li>
 *   <li>记录请求结束时间、执行耗时、响应结果等信息</li>
 *   <li>捕获并记录方法执行过程中的异常信息</li>
 *   <li>自动脱敏敏感参数（如密码字段）</li>
 * </ul>
 *
 * <h3>设计目的：</h3>
 * <p>
 * 通过AOP技术实现请求日志的统一管理，避免在每个Controller方法中手动编写日志记录代码，
 * 提高代码的可维护性和一致性，同时为问题排查和系统监控提供完整的请求链路信息。
 * </p>
 *
 * <h3>适用场景：</h3>
 * <ul>
 *   <li>Web应用请求日志记录</li>
 *   <li>API接口调用追踪</li>
 *   <li>系统性能监控与分析</li>
 *   <li>问题排查与故障定位</li>
 * </ul>
 *
 * <h3>核心实现逻辑：</h3>
 * <ol>
 *   <li>通过切入点表达式匹配所有Controller层方法</li>
 *   <li>使用ThreadLocal存储请求开始时间，确保线程安全</li>
 *   <li>前置通知记录请求基本信息，后置通知记录执行结果</li>
 *   <li>异常通知捕获并记录异常信息，确保异常情况下也能记录日志</li>
 * </ol>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>日志输出长度有限制，超长内容会被截断</li>
 *   <li>敏感参数（password等）会自动脱敏处理</li>
 *   <li>HttpServletRequest和MultipartFile类型参数不参与日志记录</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see org.aspectj.lang.annotation.Aspect
 * @see org.aspectj.lang.annotation.Pointcut
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String POINTCUT_CONTROLLER = "execution(* com.shy..controller..*.*(..))";

    private static final List<String> EXCLUDE_PARAMS = Arrays.asList("password", "oldPassword", "newPassword", "confirmPassword");

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * <p>
     * 定义Controller层切入点
     * </p>
     *
     * <h4>切入点表达式说明：</h4>
     * <pre>
     * execution(* com.shy..controller..*.*(..))
     * </pre>
     * <ul>
     *   <li><b>execution</b>: 表示在方法执行时触发</li>
     *   <li><b>*</b>: 匹配任意返回类型</li>
     *   <li><b>com.shy..controller..</b>: 匹配com.shy包及其子包下的controller包</li>
     *   <li><b>*.*</b>: 匹配任意类名和任意方法名</li>
     *   <li><b>(..)</b>: 匹配任意参数列表</li>
     * </ul>
     *
     * <h4>作用范围：</h4>
     * <p>
     * 该切入点会匹配项目中所有模块（com.shy包下）的Controller层方法，
     * 包括但不限于用户管理、系统管理、业务处理等所有Controller类中的公共方法。
     * </p>
     */
    @Pointcut(POINTCUT_CONTROLLER)
    public void controllerPointcut() {
    }

    /**
     * <p>
     * 前置通知 - 在Controller方法执行前触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当匹配切入点表达式的Controller方法被调用时，在目标方法执行之前立即执行此通知。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>记录当前时间戳作为请求开始时间</li>
     *   <li>获取当前HTTP请求上下文</li>
     *   <li>提取目标类名、方法名和请求参数</li>
     *   <li>对敏感参数进行脱敏处理</li>
     *   <li>输出请求开始日志</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     *                  <ul>
     *                    <li>joinPoint.getTarget(): 获取目标对象</li>
     *                    <li>joinPoint.getSignature(): 获取方法签名</li>
     *                    <li>joinPoint.getArgs(): 获取方法参数</li>
     *                  </ul>
     */
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录请求开始时间，使用ThreadLocal确保多线程环境下时间记录的准确性
        // 每个线程有独立的时间副本，避免线程安全问题
        startTime.set(System.currentTimeMillis());

        // 获取当前请求的上下文属性，包含HttpServletRequest等请求信息
        // RequestContextHolder是Spring提供的请求上下文持有者，通过它可以获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 边缘情况处理：如果请求上下文为空（如异步调用或非Web环境），直接返回不记录日志
        // 这种情况可能发生在定时任务、消息队列消费者等非HTTP请求场景
        if (attributes == null) {
            return;
        }

        // 从请求属性中获取HttpServletRequest对象，用于提取请求相关信息
        HttpServletRequest request = attributes.getRequest();

        // 获取目标类的全限定名，用于日志中标识请求来源
        // 例如：com.shy.nexusix.controller.UserController
        String className = joinPoint.getTarget().getClass().getName();

        // 获取目标方法名，用于日志中标识具体执行的方法
        // 例如：getUserList、addUser等
        String methodName = joinPoint.getSignature().getName();

        // 获取方法参数数组，用于记录请求参数
        // 参数数组可能包含各种类型，需要后续处理过滤
        Object[] args = joinPoint.getArgs();

        // 将参数数组转换为可读的字符串格式，同时进行脱敏和截断处理
        String params = getMethodParams(args);

        // 输出请求开始日志，包含时间、IP、方法、参数等关键信息
        // 使用info级别确保日志在生产环境中可见
        log.info("[请求开始] 时间: {} | IP: {} | 方法: {}.{} | 参数: {}",
                LocalDateTime.now().format(DATETIME_FORMATTER),
                getClientIp(request),
                className,
                methodName,
                params);
    }

    /**
     * <p>
     * 返回通知 - 在Controller方法正常返回后触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标Controller方法执行完成并正常返回结果后执行此通知。
     * 如果目标方法抛出异常，则此通知不会被执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>计算方法执行耗时</li>
     *   <li>记录方法执行状态为"成功"</li>
     *   <li>截取响应结果（超长内容截断）</li>
     *   <li>输出请求结束日志</li>
     *   <li>清理ThreadLocal中的开始时间</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     * @param result 目标方法的返回值，可能为null
     *               <ul>
     *                 <li>如果是ApiResponse类型，会记录其中的code、msg、data等信息</li>
     *                 <li>如果是其他类型，会调用toString()方法获取字符串表示</li>
     *               </ul>
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 调用统一的日志处理方法，传入返回结果，异常参数为null表示正常返回
        handleLog(joinPoint, result, null);
    }

    /**
     * <p>
     * 异常通知 - 在Controller方法抛出异常后触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标Controller方法执行过程中抛出异常时执行此通知。
     * 无论异常是否被后续的异常处理器捕获，此通知都会被执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>计算方法执行耗时</li>
     *   <li>记录方法执行状态为"失败"</li>
     *   <li>提取异常信息</li>
     *   <li>输出请求异常日志（ERROR级别）</li>
     *   <li>清理ThreadLocal中的开始时间</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     * @param e 目标方法抛出的异常对象
     *          <ul>
     *            <li>BusinessException: 业务异常，包含错误码和错误消息</li>
     *            <li>其他异常: 系统异常，记录异常类型和消息</li>
     *          </ul>
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        // 调用统一的日志处理方法，返回结果为null，传入异常对象表示异常情况
        handleLog(joinPoint, null, e);
    }

    /**
     * <p>
     * 处理日志记录的核心方法
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>计算方法执行耗时（当前时间 - 开始时间）</li>
     *   <li>根据是否存在异常判断执行状态</li>
     *   <li>格式化并输出日志信息</li>
     *   <li>清理ThreadLocal资源</li>
     * </ol>
     *
     * @param joinPoint 连接点对象
     * @param result 方法返回值，正常返回时传入，异常时为null
     * @param e 异常对象，异常时传入，正常返回时为null
     */
    private void handleLog(JoinPoint joinPoint, Object result, Exception e) {
        try {
            // 计算方法执行耗时：当前时间减去前置通知中记录的开始时间
            // 使用ThreadLocal.get()获取当前线程的开始时间，确保多线程环境下计算准确
            long executeTime = System.currentTimeMillis() - startTime.get();

            // 清理ThreadLocal资源，防止内存泄漏
            // ThreadLocal中的数据如果不清理，在线程池环境下会导致数据残留
            startTime.remove();

            // 获取目标类名和方法名，用于日志输出
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            // 根据是否存在异常决定日志输出内容和级别
            if (e != null) {
                // 异常情况：使用ERROR级别输出日志，包含异常信息
                // 异常信息通过e.getMessage()获取，避免打印完整堆栈影响性能
                log.error("[请求异常] 时间: {} | 方法: {}.{} | 耗时: {}ms | 异常: {}",
                        LocalDateTime.now().format(DATETIME_FORMATTER),
                        className,
                        methodName,
                        executeTime,
                        e.getMessage());
            } else {
                // 正常情况：使用INFO级别输出日志，包含响应结果
                String resultStr = result != null ? result.toString() : "null";

                // 对响应结果进行长度限制，避免日志过大影响性能和存储
                // 超过500字符的结果会被截断，并添加省略号标识
                if (resultStr.length() > 500) {
                    resultStr = resultStr.substring(0, 500) + "...";
                }

                log.info("[请求结束] 时间: {} | 方法: {}.{} | 耗时: {}ms | 结果: {}",
                        LocalDateTime.now().format(DATETIME_FORMATTER),
                        className,
                        methodName,
                        executeTime,
                        resultStr);
            }
        } catch (Exception ex) {
            // 边缘情况处理：日志记录本身出现异常时，使用error级别记录
            // 避免日志记录异常影响正常业务流程
            log.error("日志记录异常: {}", ex.getMessage());
        }
    }

    /**
     * <p>
     * 获取方法参数的字符串表示
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <ol>
     *   <li>过滤掉null、HttpServletRequest、MultipartFile类型参数</li>
     *   <li>对参数字符串进行长度截断（最大200字符）</li>
     *   <li>对敏感参数（password等）进行脱敏处理</li>
     *   <li>拼接所有参数为字符串数组格式</li>
     * </ol>
     *
     * @param args 方法参数数组
     * @return 格式化后的参数字符串，格式如：[param1, param2, ...]
     */
    private String getMethodParams(Object[] args) {
        // 边缘情况处理：参数数组为空或长度为0时，返回空数组字符串
        if (args == null || args.length == 0) {
            return "[]";
        }

        try {
            // 使用Stream API处理参数数组，进行过滤、转换和拼接
            return Arrays.stream(args)
                    // 过滤条件1：排除null参数，避免NullPointerException
                    .filter(arg -> arg != null
                            // 过滤条件2：排除HttpServletRequest类型参数
                            // HttpServletRequest包含大量请求信息，打印出来会非常冗长
                            && !(arg instanceof HttpServletRequest)
                            // 过滤条件3：排除MultipartFile类型参数
                            // MultipartFile是文件上传对象，toString()输出无意义
                            && !(arg instanceof MultipartFile))
                    // 对每个参数进行转换处理
                    .map(arg -> {
                        // 将参数对象转换为字符串
                        String str = arg.toString();

                        // 长度限制：超过200字符的参数会被截断
                        // 这是为了避免大对象（如长JSON字符串）导致日志过大
                        if (str.length() > 200) {
                            str = str.substring(0, 200) + "...";
                        }

                        // 敏感参数脱敏处理：遍历所有需要脱敏的参数名
                        // 使用正则表达式匹配JSON格式的敏感字段，将其值替换为******
                        // 例如："password":"123456" -> "password":"******"
                        for (String exclude : EXCLUDE_PARAMS) {
                            // 正则说明：
                            // (?i) - 忽略大小写匹配
                            // (\"参数名\"\\s*:\\s*\") - 匹配JSON字段名和冒号
                            // [^\"]*\" - 匹配字段值（非引号字符）直到结束引号
                            str = str.replaceAll("(?i)(\"" + exclude + "\"\\s*:\\s*\")[^\"]*\"", "$1******\"");
                        }
                        return str;
                    })
                    // 将所有处理后的参数用逗号分隔，并用方括号包围
                    .collect(Collectors.joining(", ", "[", "]"));
        } catch (Exception e) {
            // 边缘情况处理：参数处理过程中出现异常时返回固定字符串
            // 这可能是参数对象的toString()方法抛出异常导致
            return "[参数解析失败]";
        }
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
}
