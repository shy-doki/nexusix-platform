package com.shy.nexusix.common.aspect;

import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.annotation.OperationLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>操作日志切面，拦截@OperationLog注解方法记录用户操作行为</p>
 *
 * @author shy
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String POINTCUT_OPERATION_LOG = "@annotation(com.shy.nexusix.common.annotation.OperationLog)";

    /** 需要脱敏的参数名列表 */
    private static final List<String> EXCLUDE_PARAMS = Arrays.asList("password", "oldPassword", "newPassword", "confirmPassword", "token");

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private ThreadLocal<Map<String, Object>> logData = new ThreadLocal<>();

    /**
     * <p>操作日志切入点</p>
     */
    @Pointcut(POINTCUT_OPERATION_LOG)
    public void operationLogPointcut() {
    }

    /**
     * <p>前置通知，收集注解元信息和请求信息</p>
     *
     * @param joinPoint 连接点
     */
    @Before("operationLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录操作开始时间
        startTime.set(System.currentTimeMillis());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        // 构建日志数据
        Map<String, Object> data = new HashMap<>();
        data.put("title", operationLog.title());
        data.put("businessType", operationLog.businessType().getDescription());
        data.put("operatorType", operationLog.operatorType().getDescription());
        data.put("method", joinPoint.getTarget().getClass().getName() + "." + method.getName());
        data.put("startTime", LocalDateTime.now().format(DATETIME_FORMATTER));

        // 提取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            data.put("requestUrl", request.getRequestURI());
            data.put("requestMethod", request.getMethod());
            data.put("clientIp", getClientIp(request));

            // 根据注解配置决定是否记录请求参数
            if (operationLog.isSaveRequestData()) {
                data.put("requestParams", getRequestParams(joinPoint, operationLog.excludeParams()));
            }
        }

        logData.set(data);
    }

    /**
     * <p>返回通知，记录操作成功日志</p>
     *
     * @param joinPoint 连接点
     * @param result 返回值
     */
    @AfterReturning(pointcut = "operationLogPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, result, null);
    }

    /**
     * <p>异常通知，记录操作失败日志</p>
     *
     * @param joinPoint 连接点
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "operationLogPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, null, e);
    }

    /**
     * <p>处理操作日志记录</p>
     *
     * @param joinPoint 连接点
     * @param result 返回值
     * @param e 异常对象
     */
    private void handleLog(JoinPoint joinPoint, Object result, Exception e) {
        try {
            Map<String, Object> data = logData.get();
            if (data == null) {
                return;
            }

            long executeTime = System.currentTimeMillis() - startTime.get();
            data.put("executeTime", executeTime + "ms");
            data.put("endTime", LocalDateTime.now().format(DATETIME_FORMATTER));

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLog operationLog = method.getAnnotation(OperationLog.class);

            if (e != null) {
                // 异常情况
                data.put("status", "失败");
                data.put("errorMsg", e.getMessage());
                log.error("[操作日志] {}", formatLogData(data));
            } else {
                // 正常情况
                data.put("status", "成功");
                if (operationLog.isSaveResponseData() && result != null) {
                    String resultStr = JSON.toJSONString(result);
                    if (resultStr.length() > 1000) {
                        resultStr = resultStr.substring(0, 1000) + "...";
                    }
                    data.put("responseResult", resultStr);
                }
                log.info("[操作日志] {}", formatLogData(data));
            }
        } catch (Exception ex) {
            log.error("操作日志记录异常: {}", ex.getMessage());
        } finally {
            // 清理ThreadLocal资源
            startTime.remove();
            logData.remove();
        }
    }

    /**
     * <p>获取请求参数JSON字符串，排除敏感参数</p>
     *
     * @param joinPoint 连接点
     * @param excludeParams 需排除的参数名
     * @return 参数JSON字符串
     */
    private String getRequestParams(JoinPoint joinPoint, String[] excludeParams) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] paramValues = joinPoint.getArgs();

            if (paramNames == null || paramNames.length == 0) {
                return "{}";
            }

            Map<String, Object> params = new LinkedHashMap<>();
            Set<String> excludeSet = new HashSet<>(Arrays.asList(excludeParams));
            excludeSet.addAll(EXCLUDE_PARAMS);

            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];

                // 排除敏感参数和特殊类型参数
                if (excludeSet.contains(paramName)
                        || paramValue instanceof HttpServletRequest
                        || paramValue instanceof HttpServletResponse
                        || paramValue instanceof MultipartFile) {
                    continue;
                }

                try {
                    String jsonStr = JSON.toJSONString(paramValue);
                    if (jsonStr.length() > 500) {
                        jsonStr = jsonStr.substring(0, 500) + "...";
                    }
                    params.put(paramName, jsonStr);
                } catch (Exception ex) {
                    params.put(paramName, "[序列化失败]");
                }
            }

            return JSON.toJSONString(params);
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * <p>格式化日志数据为可读字符串</p>
     *
     * @param data 日志数据
     * @return 格式化后的日志字符串
     */
    private String formatLogData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("标题: ").append(data.get("title"));
        sb.append(" | 业务类型: ").append(data.get("businessType"));
        sb.append(" | 操作类型: ").append(data.get("operatorType"));
        sb.append(" | 方法: ").append(data.get("method"));
        sb.append(" | 状态: ").append(data.get("status"));
        sb.append(" | 耗时: ").append(data.get("executeTime"));
        sb.append(" | IP: ").append(data.get("clientIp"));

        if (data.containsKey("requestParams")) {
            sb.append(" | 请求参数: ").append(data.get("requestParams"));
        }
        if (data.containsKey("responseResult")) {
            sb.append(" | 响应结果: ").append(data.get("responseResult"));
        }
        if (data.containsKey("errorMsg")) {
            sb.append(" | 错误信息: ").append(data.get("errorMsg"));
        }

        return sb.toString();
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
