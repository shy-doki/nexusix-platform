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
 * <p>
 * 操作日志切面 - 用户操作行为记录组件
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>拦截带有@OperationLog注解的方法，记录用户操作行为</li>
 *   <li>记录操作模块标题、业务类型、操作类型等元信息</li>
 *   <li>记录请求URL、HTTP方法、客户端IP等请求信息</li>
 *   <li>可选记录请求参数和响应结果</li>
 *   <li>记录操作执行状态（成功/失败）和执行耗时</li>
 *   <li>支持敏感参数排除和参数脱敏</li>
 * </ul>
 *
 * <h3>设计目的：</h3>
 * <p>
 * 为系统提供完整的用户操作审计能力，通过注解驱动的方式简化操作日志记录的实现，
 * 支持细粒度的日志配置（如是否记录请求参数、响应结果等），满足安全审计和问题追溯需求。
 * </p>
 *
 * <h3>适用场景：</h3>
 * <ul>
 *   <li>用户操作审计（如新增、修改、删除等关键操作）</li>
 *   <li>系统安全日志记录</li>
 *   <li>业务操作追溯与统计分析</li>
 *   <li>问题排查与责任界定</li>
 * </ul>
 *
 * <h3>核心实现逻辑：</h3>
 * <ol>
 *   <li>通过注解切入点匹配所有带有@OperationLog注解的方法</li>
 *   <li>使用ThreadLocal存储日志数据，确保线程安全</li>
 *   <li>前置通知收集注解元信息和请求信息</li>
 *   <li>后置通知/异常通知记录执行结果并输出日志</li>
 *   <li>支持自定义排除参数和敏感参数脱敏</li>
 * </ol>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * &#64;OperationLog(title = "用户管理", businessType = BusinessType.INSERT)
 * public ApiResponse addUser(&#64;RequestBody UserDTO user) {
 *     // 业务逻辑
 * }
 * </pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>请求参数和响应结果长度有限制，超长内容会被截断</li>
 *   <li>敏感参数（password、token等）会自动排除或脱敏</li>
 *   <li>HttpServletRequest、HttpServletResponse、MultipartFile类型参数不参与记录</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see com.shy.nexusix.common.annotation.OperationLog
 * @see com.shy.nexusix.common.annotation.OperationLog.BusinessType
 * @see com.shy.nexusix.common.annotation.OperationLog.OperatorType
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String POINTCUT_OPERATION_LOG = "@annotation(com.shy.nexusix.common.annotation.OperationLog)";

    private static final List<String> EXCLUDE_PARAMS = Arrays.asList("password", "oldPassword", "newPassword", "confirmPassword", "token");

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private ThreadLocal<Map<String, Object>> logData = new ThreadLocal<>();

    /**
     * <p>
     * 定义操作日志切入点
     * </p>
     *
     * <h4>切入点表达式说明：</h4>
     * <pre>
     * @annotation(com.shy.nexusix.common.annotation.OperationLog)
     * </pre>
     * <ul>
     *   <li><b>@annotation</b>: 表示匹配带有指定注解的方法</li>
     *   <li><b>com.shy.nexusix.common.annotation.OperationLog</b>: 指定要匹配的注解全限定名</li>
     * </ul>
     *
     * <h4>作用范围：</h4>
     * <p>
     * 该切入点会匹配项目中所有带有@OperationLog注解的方法，无论其所在的包路径、类名或方法名。
     * 通常用于Service层或Controller层的关键业务方法上。
     * </p>
     *
     * <h4>匹配条件：</h4>
     * <p>
     * 只要方法上标注了@OperationLog注解，该方法就会被切面拦截处理。
     * </p>
     */
    @Pointcut(POINTCUT_OPERATION_LOG)
    public void operationLogPointcut() {
    }

    /**
     * <p>
     * 前置通知 - 在目标方法执行前触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当带有@OperationLog注解的方法被调用时，在目标方法执行之前立即执行此通知。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>记录当前时间戳作为操作开始时间</li>
     *   <li>解析@OperationLog注解，提取操作标题、业务类型、操作类型等元信息</li>
     *   <li>获取当前HTTP请求上下文，提取请求URL、HTTP方法、客户端IP</li>
     *   <li>根据注解配置决定是否记录请求参数</li>
     *   <li>将所有信息存入ThreadLocal供后续通知使用</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     *                  <ul>
     *                    <li>joinPoint.getSignature(): 获取方法签名，用于提取注解信息</li>
     *                    <li>joinPoint.getTarget(): 获取目标对象，用于提取类名</li>
     *                    <li>joinPoint.getArgs(): 获取方法参数，用于记录请求参数</li>
     *                  </ul>
     */
    @Before("operationLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录操作开始时间，使用ThreadLocal确保多线程环境下时间记录的准确性
        startTime.set(System.currentTimeMillis());

        // 获取方法签名并转换为MethodSignature类型，用于提取方法对象和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法对象，用于读取方法上的@OperationLog注解
        Method method = signature.getMethod();

        // 获取方法上的@OperationLog注解实例
        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        // 创建日志数据容器，使用HashMap存储所有需要记录的信息
        // 这些信息将在后置通知中用于生成完整的日志记录
        Map<String, Object> data = new HashMap<>();

        // 从注解中提取操作模块标题，如"用户管理"、"订单管理"等
        data.put("title", operationLog.title());

        // 从注解中提取业务类型描述，如"新增"、"修改"、"删除"等
        // businessType是枚举类型，getDescription()返回中文描述
        data.put("businessType", operationLog.businessType().getDescription());

        // 从注解中提取操作类型描述，如"后台用户"、"手机端用户"等
        data.put("operatorType", operationLog.operatorType().getDescription());

        // 构建完整的方法路径：类全限定名.方法名
        // 例如：com.shy.nexusix.service.UserService.addUser
        data.put("method", joinPoint.getTarget().getClass().getName() + "." + method.getName());

        // 记录操作开始时间，使用格式化后的时间字符串便于阅读
        data.put("startTime", LocalDateTime.now().format(DATETIME_FORMATTER));

        // 获取当前请求的上下文属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 如果请求上下文存在（Web环境），提取请求相关信息
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // 记录请求URL，用于标识操作的具体接口路径
            data.put("requestUrl", request.getRequestURI());

            // 记录HTTP方法（GET、POST、PUT、DELETE等）
            data.put("requestMethod", request.getMethod());

            // 记录客户端IP，用于安全审计和问题追溯
            data.put("clientIp", getClientIp(request));

            // 根据注解配置决定是否记录请求参数
            // isSaveRequestData为true时记录，适用于需要详细审计的操作
            if (operationLog.isSaveRequestData()) {
                // 获取请求参数，并排除指定的敏感参数
                data.put("requestParams", getRequestParams(joinPoint, operationLog.excludeParams()));
            }
        }

        // 将日志数据存入ThreadLocal，供后置通知和异常通知使用
        // 使用ThreadLocal确保在多线程环境下数据隔离
        logData.set(data);
    }

    /**
     * <p>
     * 返回通知 - 在目标方法正常返回后触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标方法执行完成并正常返回结果后执行此通知。
     * 如果目标方法抛出异常，则此通知不会被执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>从ThreadLocal获取前置通知收集的日志数据</li>
     *   <li>计算方法执行耗时</li>
     *   <li>根据注解配置决定是否记录响应结果</li>
     *   <li>设置操作状态为"成功"</li>
     *   <li>格式化并输出操作日志（INFO级别）</li>
     *   <li>清理ThreadLocal资源</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     * @param result 目标方法的返回值，可能为null
     *               <ul>
     *                 <li>如果是ApiResponse类型，会记录其中的code、msg、data等信息</li>
     *                 <li>如果是其他类型，会使用JSON序列化为字符串</li>
     *               </ul>
     */
    @AfterReturning(pointcut = "operationLogPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 调用统一的日志处理方法，传入返回结果，异常参数为null表示正常返回
        handleLog(joinPoint, result, null);
    }

    /**
     * <p>
     * 异常通知 - 在目标方法抛出异常后触发
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标方法执行过程中抛出异常时执行此通知。
     * 无论异常是否被后续的异常处理器捕获，此通知都会被执行。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>从ThreadLocal获取前置通知收集的日志数据</li>
     *   <li>计算方法执行耗时</li>
     *   <li>设置操作状态为"失败"</li>
     *   <li>记录异常信息</li>
     *   <li>格式化并输出操作日志（ERROR级别）</li>
     *   <li>清理ThreadLocal资源</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     * @param e 目标方法抛出的异常对象
     *          <ul>
     *            <li>BusinessException: 业务异常，包含错误码和错误消息</li>
     *            <li>其他异常: 系统异常，记录异常类型和消息</li>
     *          </ul>
     */
    @AfterThrowing(pointcut = "operationLogPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        // 调用统一的日志处理方法，返回结果为null，传入异常对象表示异常情况
        handleLog(joinPoint, null, e);
    }

    /**
     * <p>
     * 处理操作日志记录的核心方法
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>从ThreadLocal获取前置通知收集的日志数据</li>
     *   <li>计算方法执行耗时（当前时间 - 开始时间）</li>
     *   <li>根据是否存在异常判断执行状态</li>
     *   <li>根据注解配置决定是否记录响应结果</li>
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
            // 从ThreadLocal获取前置通知中存储的日志数据
            Map<String, Object> data = logData.get();

            // 边缘情况处理：如果日志数据为空，说明前置通知未执行或执行失败
            // 这种情况不应该发生，但为了健壮性进行判断
            if (data == null) {
                return;
            }

            // 计算操作执行耗时：当前时间减去前置通知中记录的开始时间
            long executeTime = System.currentTimeMillis() - startTime.get();

            // 将执行耗时存入日志数据，格式为"XXms"
            data.put("executeTime", executeTime + "ms");

            // 记录操作结束时间
            data.put("endTime", LocalDateTime.now().format(DATETIME_FORMATTER));

            // 获取方法签名和注解，用于读取注解配置
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLog operationLog = method.getAnnotation(OperationLog.class);

            // 根据是否存在异常决定日志输出内容和级别
            if (e != null) {
                // 异常情况：设置操作状态为"失败"
                data.put("status", "失败");

                // 记录异常信息，只记录消息避免打印完整堆栈
                data.put("errorMsg", e.getMessage());

                // 使用ERROR级别输出日志，便于问题定位
                log.error("[操作日志] {}", formatLogData(data));
            } else {
                // 正常情况：设置操作状态为"成功"
                data.put("status", "成功");

                // 根据注解配置决定是否记录响应结果
                // isSaveResponseData为true且result不为null时记录
                if (operationLog.isSaveResponseData() && result != null) {
                    // 使用fastjson2将结果对象序列化为JSON字符串
                    String resultStr = JSON.toJSONString(result);

                    // 对响应结果进行长度限制，避免日志过大
                    // 超过1000字符的结果会被截断
                    if (resultStr.length() > 1000) {
                        resultStr = resultStr.substring(0, 1000) + "...";
                    }
                    data.put("responseResult", resultStr);
                }

                // 使用INFO级别输出日志
                log.info("[操作日志] {}", formatLogData(data));
            }
        } catch (Exception ex) {
            // 边缘情况处理：日志记录本身出现异常时，使用error级别记录
            // 避免日志记录异常影响正常业务流程
            log.error("操作日志记录异常: {}", ex.getMessage());
        } finally {
            // 无论成功还是失败，都要清理ThreadLocal资源
            // 防止在线程池环境下出现内存泄漏
            startTime.remove();
            logData.remove();
        }
    }

    /**
     * <p>
     * 获取请求参数的JSON字符串表示
     * </p>
     *
     * <h4>处理逻辑：</h4>
     * <ol>
     *   <li>获取方法参数名和参数值</li>
     *   <li>排除注解中指定的排除参数</li>
     *   <li>排除默认敏感参数（password、token等）</li>
     *   <li>排除HttpServletRequest、HttpServletResponse、MultipartFile类型参数</li>
     *   <li>对参数值进行JSON序列化，超长内容截断</li>
     * </ol>
     *
     * @param joinPoint 连接点对象
     * @param excludeParams 注解中指定的排除参数名数组
     * @return 参数的JSON字符串表示，格式如：{"param1": "value1", "param2": "value2"}
     */
    private String getRequestParams(JoinPoint joinPoint, String[] excludeParams) {
        try {
            // 获取方法签名，用于获取参数名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            // 获取方法参数名数组
            // Spring AOP支持获取参数名（需要编译时保留参数名信息）
            String[] paramNames = signature.getParameterNames();

            // 获取方法参数值数组
            Object[] paramValues = joinPoint.getArgs();

            // 边缘情况处理：参数名为空或长度为0时，返回空JSON对象
            if (paramNames == null || paramNames.length == 0) {
                return "{}";
            }

            // 使用LinkedHashMap保持参数顺序
            Map<String, Object> params = new LinkedHashMap<>();

            // 构建排除参数集合，包含注解指定的和默认的敏感参数
            Set<String> excludeSet = new HashSet<>(Arrays.asList(excludeParams));
            excludeSet.addAll(EXCLUDE_PARAMS);

            // 遍历参数名和参数值，进行过滤和处理
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];

                // 排除条件1：参数名在排除集合中（敏感参数）
                if (excludeSet.contains(paramName)) {
                    continue;
                }

                // 排除条件2：参数类型为HttpServletRequest
                // HttpServletRequest包含大量请求信息，不适合记录
                if (paramValue instanceof HttpServletRequest) {
                    continue;
                }

                // 排除条件3：参数类型为HttpServletResponse
                // HttpServletResponse是响应对象，无记录意义
                if (paramValue instanceof HttpServletResponse) {
                    continue;
                }

                // 排除条件4：参数类型为MultipartFile
                // MultipartFile是文件上传对象，无法直接序列化
                if (paramValue instanceof MultipartFile) {
                    continue;
                }

                // 尝试将参数值序列化为JSON字符串
                try {
                    String jsonStr = JSON.toJSONString(paramValue);

                    // 对参数值进行长度限制，避免大对象导致日志过大
                    if (jsonStr.length() > 500) {
                        jsonStr = jsonStr.substring(0, 500) + "...";
                    }
                    params.put(paramName, jsonStr);
                } catch (Exception ex) {
                    // 序列化失败时，记录固定标识
                    // 这可能是由于对象包含无法序列化的属性
                    params.put(paramName, "[序列化失败]");
                }
            }

            // 将参数Map转换为JSON字符串返回
            return JSON.toJSONString(params);
        } catch (Exception e) {
            // 边缘情况处理：整体处理失败时返回空JSON对象
            return "{}";
        }
    }

    /**
     * <p>
     * 格式化日志数据为可读字符串
     * </p>
     *
     * <h4>输出格式：</h4>
     * <pre>
     * 标题: xxx | 业务类型: xxx | 操作类型: xxx | 方法: xxx | 状态: xxx | 耗时: xxx | IP: xxx | 请求参数: xxx | 响应结果: xxx | 错误信息: xxx
     * </pre>
     *
     * @param data 日志数据Map
     * @return 格式化后的日志字符串
     */
    private String formatLogData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();

        // 拼接操作模块标题
        sb.append("标题: ").append(data.get("title"));

        // 拼接业务类型（新增、修改、删除等）
        sb.append(" | 业务类型: ").append(data.get("businessType"));

        // 拼接操作类型（后台用户、手机端用户等）
        sb.append(" | 操作类型: ").append(data.get("operatorType"));

        // 拼接执行方法的全限定名
        sb.append(" | 方法: ").append(data.get("method"));

        // 拼接操作执行状态（成功/失败）
        sb.append(" | 状态: ").append(data.get("status"));

        // 拼接执行耗时
        sb.append(" | 耗时: ").append(data.get("executeTime"));

        // 拼接客户端IP
        sb.append(" | IP: ").append(data.get("clientIp"));

        // 可选字段：请求参数（仅当存在时输出）
        if (data.containsKey("requestParams")) {
            sb.append(" | 请求参数: ").append(data.get("requestParams"));
        }

        // 可选字段：响应结果（仅当存在时输出）
        if (data.containsKey("responseResult")) {
            sb.append(" | 响应结果: ").append(data.get("responseResult"));
        }

        // 可选字段：错误信息（仅当存在时输出）
        if (data.containsKey("errorMsg")) {
            sb.append(" | 错误信息: ").append(data.get("errorMsg"));
        }

        return sb.toString();
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
