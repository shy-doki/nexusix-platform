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
 * <p>日志切面，统一记录Controller层请求日志</p>
 *
 * @author shy
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String POINTCUT_CONTROLLER = "execution(* com.shy..controller..*.*(..))";

    /** 需要脱敏的参数名列表 */
    private static final List<String> EXCLUDE_PARAMS = Arrays.asList("password", "oldPassword", "newPassword", "confirmPassword");

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * <p>Controller层切入点</p>
     */
    @Pointcut(POINTCUT_CONTROLLER)
    public void controllerPointcut() {
    }

    /**
     * <p>前置通知，记录请求开始时间和参数信息</p>
     *
     * @param joinPoint 连接点
     */
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录请求开始时间
        startTime.set(System.currentTimeMillis());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String params = getMethodParams(joinPoint.getArgs());

        log.info("[请求开始] 时间: {} | IP: {} | 方法: {}.{} | 参数: {}",
                LocalDateTime.now().format(DATETIME_FORMATTER),
                getClientIp(request),
                className,
                methodName,
                params);
    }

    /**
     * <p>返回通知，记录请求结束和响应结果</p>
     *
     * @param joinPoint 连接点
     * @param result 返回值
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, result, null);
    }

    /**
     * <p>异常通知，记录请求异常信息</p>
     *
     * @param joinPoint 连接点
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, null, e);
    }

    /**
     * <p>处理日志记录，计算耗时并输出日志</p>
     *
     * @param joinPoint 连接点
     * @param result 返回值
     * @param e 异常对象
     */
    private void handleLog(JoinPoint joinPoint, Object result, Exception e) {
        try {
            long executeTime = System.currentTimeMillis() - startTime.get();
            startTime.remove();

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            if (e != null) {
                // 异常情况
                log.error("[请求异常] 时间: {} | 方法: {}.{} | 耗时: {}ms | 异常: {}",
                        LocalDateTime.now().format(DATETIME_FORMATTER),
                        className, methodName, executeTime, e.getMessage());
            } else {
                // 正常情况，截断超长响应结果
                String resultStr = result != null ? result.toString() : "null";
                if (resultStr.length() > 500) {
                    resultStr = resultStr.substring(0, 500) + "...";
                }
                log.info("[请求结束] 时间: {} | 方法: {}.{} | 耗时: {}ms | 结果: {}",
                        LocalDateTime.now().format(DATETIME_FORMATTER),
                        className, methodName, executeTime, resultStr);
            }
        } catch (Exception ex) {
            log.error("日志记录异常: {}", ex.getMessage());
        }
    }

    /**
     * <p>获取方法参数字符串，过滤敏感参数并截断</p>
     *
     * @param args 方法参数数组
     * @return 格式化后的参数字符串
     */
    private String getMethodParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        try {
            return Arrays.stream(args)
                    .filter(arg -> arg != null
                            && !(arg instanceof HttpServletRequest)
                            && !(arg instanceof MultipartFile))
                    .map(arg -> {
                        String str = arg.toString();
                        // 截断超长参数
                        if (str.length() > 200) {
                            str = str.substring(0, 200) + "...";
                        }
                        // 敏感参数脱敏
                        for (String exclude : EXCLUDE_PARAMS) {
                            str = str.replaceAll("(?i)(\"" + exclude + "\"\\s*:\\s*\")[^\"]*\"", "$1******\"");
                        }
                        return str;
                    })
                    .collect(Collectors.joining(", ", "[", "]"));
        } catch (Exception e) {
            return "[参数解析失败]";
        }
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

        // X-Forwarded-For包含多个IP时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
