# NexusIX-Platform 系统配置模块功能设计

**文档版本**: v2.0  
**创建日期**: 2026-06-15  

---

## 3. nexusix-system (系统配置模块)

### 3.1 模块概述

**模块职责**: 系统参数配置、数据字典管理、登录日志、操作日志、系统监控

**核心表**:
- `sys_config` - 系统配置表
- `sys_dict_type` - 字典类型表
- `sys_dict_data` - 字典数据表
- `sys_login_log` - 登录日志表
- `sys_operation_log` - 操作日志表

### 3.2 数据库设计

```sql
-- 系统配置表
CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    config_type VARCHAR(20) NOT NULL,  -- STRING/NUMBER/BOOLEAN/JSON
    config_desc VARCHAR(200),
    is_system BOOLEAN DEFAULT FALSE,   -- 系统配置不可删除
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    -- 审计字段...
);

-- 字典类型表
CREATE TABLE sys_dict_type (
    id BIGINT PRIMARY KEY,
    dict_type_code VARCHAR(100) NOT NULL UNIQUE,
    dict_type_name VARCHAR(100) NOT NULL,
    dict_type_desc VARCHAR(200),
    is_system BOOLEAN DEFAULT FALSE,
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    -- 审计字段...
);

-- 字典数据表
CREATE TABLE sys_dict_data (
    id BIGINT PRIMARY KEY,
    dict_type_code VARCHAR(100) NOT NULL,
    dict_data_code VARCHAR(100) NOT NULL,
    dict_data_label VARCHAR(100) NOT NULL,
    dict_data_value VARCHAR(200) NOT NULL,
    dict_data_desc VARCHAR(200),
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    UNIQUE(dict_type_code, dict_data_code),
    -- 审计字段...
);

-- 登录日志表
CREATE TABLE sys_login_log (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    tenant_id BIGINT NOT NULL,
    login_time TIMESTAMP NOT NULL,
    login_ip VARCHAR(50),
    login_location VARCHAR(200),  -- IP归属地
    browser VARCHAR(100),
    os VARCHAR(100),
    login_status VARCHAR(20),     -- SUCCESS/FAILED
    login_message VARCHAR(200),
    -- 无审计字段，只记录...
);

-- 操作日志表
CREATE TABLE sys_operation_log (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    tenant_id BIGINT NOT NULL,
    operation_module VARCHAR(50),   -- 操作模块
    operation_type VARCHAR(20),     -- 操作类型：ADD/UPDATE/DELETE/QUERY/EXPORT/IMPORT
    operation_desc VARCHAR(200),    -- 操作描述
    request_method VARCHAR(10),     -- HTTP方法
    request_url VARCHAR(500),       -- 请求URL
    request_ip VARCHAR(50),
    request_params TEXT,            -- 请求参数（JSON）
    response_data TEXT,             -- 响应数据（JSON，可选）
    operation_time TIMESTAMP NOT NULL,
    cost_time INT,                  -- 耗时（毫秒）
    operation_status VARCHAR(20),   -- SUCCESS/FAILED
    error_message TEXT,
    -- 无审计字段...
);
```

### 3.3 目录结构

```
nexusix-system/src/main/java/com/shy/nexusix/system/
├── controller/
│   ├── SysConfigController.java        # 系统配置控制器
│   ├── SysDictController.java          # 数据字典控制器
│   ├── SysLoginLogController.java      # 登录日志控制器
│   ├── SysOperationLogController.java  # 操作日志控制器
│   └── SysMonitorController.java       # 系统监控控制器
├── converter/
│   ├── SysConfigConverter.java
│   ├── SysDictConverter.java
│   └── SysLogConverter.java
├── entity/
│   ├── SysConfig.java
│   ├── SysDictType.java
│   ├── SysDictData.java
│   ├── SysLoginLog.java
│   └── SysOperationLog.java
├── mapper/
│   ├── SysConfigMapper.java
│   ├── SysDictTypeMapper.java
│   ├── SysDictDataMapper.java
│   ├── SysLoginLogMapper.java
│   └── SysOperationLogMapper.java
├── rto/
│   ├── SysConfigAddRTO.java
│   ├── SysConfigUpdateRTO.java
│   ├── SysDictTypeAddRTO.java
│   ├── SysDictDataAddRTO.java
│   └── SysLogQueryRTO.java
├── service/
│   ├── ISysConfigService.java
│   ├── ISysDictService.java
│   ├── ISysLogService.java
│   └── impl/
└── vo/
    ├── SysConfigVO.java
    ├── SysDictTypeVO.java
    ├── SysDictDataVO.java
    ├── SysLoginLogVO.java
    ├── SysOperationLogVO.java
    └── SysMonitorVO.java
```

---

## 3.4 SysConfigController (系统配置控制器)

### 3.4.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryConfigList()` | GET | `/config/list` | 查询配置列表 | CONFIG:VIEW |
| `queryConfigPage()` | GET | `/config/page` | 分页查询配置 | CONFIG:VIEW |
| `queryConfigDetail()` | GET | `/config/detail/{key}` | 查询配置详情 | CONFIG:VIEW |
| `addConfig()` | POST | `/config/add` | 新增配置 | CONFIG:ADD |
| `updateConfig()` | PUT | `/config/update` | 更新配置 | CONFIG:EDIT |
| `deleteConfig()` | DELETE | `/config/delete/{key}` | 删除配置 | CONFIG:DELETE |
| `refreshCache()` | POST | `/config/refresh-cache` | 刷新配置缓存 | CONFIG:REFRESH |

### 3.4.2 核心实现逻辑

**配置缓存机制**

```java
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    
    @Autowired
    private SysConfigMapper configMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CONFIG_CACHE_KEY = "system:config:";
    private static final long CACHE_EXPIRE_TIME = 24 * 60 * 60; // 24小时
    
    /**
     * 查询配置（优先从缓存读取）
     */
    @Override
    public String getConfigValue(String configKey) {
        // 1. 从Redis缓存读取
        String cacheKey = CONFIG_CACHE_KEY + configKey;
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedValue != null) {
            return cachedValue.toString();
        }
        
        // 2. 从数据库查询
        SysConfig config = configMapper.selectOne(
            new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getStatus, "ENABLED")
        );
        
        if (config == null) {
            return null;
        }
        
        // 3. 写入缓存
        redisTemplate.opsForValue().set(cacheKey, config.getConfigValue(), 
                                        CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        
        return config.getConfigValue();
    }
    
    /**
     * 更新配置（同时更新缓存）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateConfig(SysConfigUpdateRTO param) {
        // 1. 更新数据库
        SysConfig config = configMapper.selectOne(
            new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, param.getConfigKey())
        );
        
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        
        if (config.getIsSystem() && !param.getConfigValue().equals(config.getConfigValue())) {
            throw new BusinessException("系统配置不能修改值，只能修改描述");
        }
        
        config.setConfigValue(param.getConfigValue());
        config.setConfigDesc(param.getConfigDesc());
        
        int result = configMapper.updateById(config);
        
        // 2. 更新Redis缓存
        String cacheKey = CONFIG_CACHE_KEY + param.getConfigKey();
        redisTemplate.opsForValue().set(cacheKey, param.getConfigValue(), 
                                        CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        
        return result;
    }
    
    /**
     * 刷新所有配置缓存
     */
    @Override
    public void refreshAllCache() {
        // 1. 查询所有配置
        List<SysConfig> configList = configMapper.selectList(
            new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getStatus, "ENABLED")
        );
        
        // 2. 批量写入Redis
        for (SysConfig config : configList) {
            String cacheKey = CONFIG_CACHE_KEY + config.getConfigKey();
            redisTemplate.opsForValue().set(cacheKey, config.getConfigValue(), 
                                            CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
    }
}
```

### 3.4.3 RTO设计

```java
@Data
public class SysConfigAddRTO {
    
    @NotBlank(message = "配置键不能为空")
    @Pattern(regexp = "^[a-z0-9._]{3,100}$", message = "配置键格式错误：只能包含小写字母、数字、点、下划线")
    private String configKey;
    
    @NotBlank(message = "配置值不能为空")
    private String configValue;
    
    @NotBlank(message = "配置类型不能为空")
    @Pattern(regexp = "^(STRING|NUMBER|BOOLEAN|JSON)$", message = "配置类型错误")
    private String configType;
    
    @Length(max = 200, message = "配置描述不能超过200字符")
    private String configDesc;
    
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;
}
```

---

## 3.5 SysDictController (数据字典控制器)

### 3.5.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryDictTypeList()` | GET | `/dict/type/list` | 查询字典类型列表 | DICT:VIEW |
| `queryDictTypeDetail()` | GET | `/dict/type/detail/{code}` | 查询字典类型详情 | DICT:VIEW |
| `addDictType()` | POST | `/dict/type/add` | 新增字典类型 | DICT:ADD |
| `updateDictType()` | PUT | `/dict/type/update` | 更新字典类型 | DICT:EDIT |
| `deleteDictType()` | DELETE | `/dict/type/delete/{code}` | 删除字典类型 | DICT:DELETE |
| `queryDictDataList()` | GET | `/dict/data/list` | 查询字典数据列表 | DICT:VIEW |
| `queryDictDataByType()` | GET | `/dict/data/type/{typeCode}` | 根据类型查询字典数据 | - |
| `addDictData()` | POST | `/dict/data/add` | 新增字典数据 | DICT:ADD |
| `updateDictData()` | PUT | `/dict/data/update` | 更新字典数据 | DICT:EDIT |
| `deleteDictData()` | DELETE | `/dict/data/delete/{code}` | 删除字典数据 | DICT:DELETE |
| `refreshDictCache()` | POST | `/dict/refresh-cache` | 刷新字典缓存 | DICT:REFRESH |

### 3.5.2 数据字典缓存实现

```java
@Service
public class SysDictServiceImpl implements ISysDictService {
    
    private static final String DICT_CACHE_KEY = "system:dict:";
    
    /**
     * 根据字典类型查询字典数据（优先缓存）
     */
    @Override
    public List<SysDictDataVO> queryDictDataByType(String dictTypeCode) {
        // 1. 从Redis缓存读取
        String cacheKey = DICT_CACHE_KEY + dictTypeCode;
        List<SysDictDataVO> cachedData = (List<SysDictDataVO>) 
            redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedData != null && !cachedData.isEmpty()) {
            return cachedData;
        }
        
        // 2. 从数据库查询
        List<SysDictData> dataList = dictDataMapper.selectList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictTypeCode, dictTypeCode)
                .eq(SysDictData::getStatus, "ENABLED")
                .orderByAsc(SysDictData::getSortOrder)
        );
        
        // 3. 转换VO
        List<SysDictDataVO> voList = dataList.stream()
            .map(dictConverter::toDataVO)
            .collect(Collectors.toList());
        
        // 4. 写入缓存（永久有效）
        redisTemplate.opsForValue().set(cacheKey, voList);
        
        return voList;
    }
}
```

### 3.5.3 VO设计

```java
@Data
public class SysDictDataVO {
    private String dictDataCode;
    private String dictDataLabel;
    private String dictDataValue;
    private String dictDataDesc;
    private Integer sortOrder;
    private String status;
}
```

---

## 3.6 SysLoginLogController (登录日志控制器)

### 3.6.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryLoginLogPage()` | GET | `/login-log/page` | 分页查询登录日志 | LOG:VIEW |
| `queryLoginLogDetail()` | GET | `/login-log/detail/{id}` | 查询登录日志详情 | LOG:VIEW |
| `exportLoginLog()` | POST | `/login-log/export` | 导出登录日志 | LOG:EXPORT |
| `deleteLoginLog()` | DELETE | `/login-log/delete/{id}` | 删除登录日志 | LOG:DELETE |
| `clearLoginLog()` | DELETE | `/login-log/clear` | 清空登录日志 | LOG:CLEAR |

### 3.6.2 记录登录日志

```java
@Component
public class LoginLogAspect {
    
    @Autowired
    private SysLoginLogMapper loginLogMapper;
    
    /**
     * 记录登录日志
     */
    public void recordLoginLog(String userName, String loginStatus, String message, 
                               HttpServletRequest request) {
        
        // 异步记录日志，不影响登录性能
        CompletableFuture.runAsync(() -> {
            SysLoginLog loginLog = new SysLoginLog();
            loginLog.setUserName(userName);
            loginLog.setLoginTime(LocalDateTime.now());
            loginLog.setLoginStatus(loginStatus);
            loginLog.setLoginMessage(message);
            
            // 获取IP
            String ip = getClientIp(request);
            loginLog.setLoginIp(ip);
            
            // 解析IP归属地（可选，使用第三方库）
            String location = parseIpLocation(ip);
            loginLog.setLoginLocation(location);
            
            // 解析浏览器和操作系统
            String userAgent = request.getHeader("User-Agent");
            UserAgentInfo agentInfo = parseUserAgent(userAgent);
            loginLog.setBrowser(agentInfo.getBrowser());
            loginLog.setOs(agentInfo.getOs());
            
            // 插入日志
            loginLogMapper.insert(loginLog);
        });
    }
}
```

---

## 3.7 SysOperationLogController (操作日志控制器)

### 3.7.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryOperationLogPage()` | GET | `/operation-log/page` | 分页查询操作日志 | LOG:VIEW |
| `queryOperationLogDetail()` | GET | `/operation-log/detail/{id}` | 查询操作日志详情 | LOG:VIEW |
| `exportOperationLog()` | POST | `/operation-log/export` | 导出操作日志 | LOG:EXPORT |
| `deleteOperationLog()` | DELETE | `/operation-log/delete/{id}` | 删除操作日志 | LOG:DELETE |
| `clearOperationLog()` | DELETE | `/operation-log/clear` | 清空操作日志 | LOG:CLEAR |

### 3.7.2 操作日志AOP实现

```java
@Aspect
@Component
public class OperationLogAspect {
    
    @Autowired
    private SysOperationLogMapper operationLogMapper;
    
    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 构建日志对象
        SysOperationLog log = new SysOperationLog();
        log.setUserId(UserContext.getCurrentUserId());
        log.setUserName(UserContext.getCurrentUserName());
        log.setTenantId(TenantContext.getCurrentTenantId());
        log.setOperationModule(operationLog.module());
        log.setOperationType(operationLog.type());
        log.setOperationDesc(operationLog.desc());
        log.setRequestMethod(request.getMethod());
        log.setRequestUrl(request.getRequestURI());
        log.setRequestIp(getClientIp(request));
        log.setOperationTime(LocalDateTime.now());
        
        // 记录请求参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args);
        log.setRequestParams(params);
        
        Object result = null;
        try {
            // 执行方法
            result = joinPoint.proceed();
            
            // 记录响应数据（可选，根据注解配置）
            if (operationLog.recordResponse()) {
                String responseData = JSON.toJSONString(result);
                log.setResponseData(responseData);
            }
            
            log.setOperationStatus("SUCCESS");
            
        } catch (Exception e) {
            log.setOperationStatus("FAILED");
            log.setErrorMessage(e.getMessage());
            throw e;
            
        } finally {
            // 计算耗时
            long costTime = System.currentTimeMillis() - startTime;
            log.setCostTime((int) costTime);
            
            // 异步插入日志
            CompletableFuture.runAsync(() -> operationLogMapper.insert(log));
        }
        
        return result;
    }
}
```

### 3.7.3 @OperationLog注解定义

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    
    /**
     * 操作模块
     */
    String module() default "";
    
    /**
     * 操作类型
     */
    String type() default "QUERY";
    
    /**
     * 操作描述
     */
    String desc() default "";
    
    /**
     * 是否记录响应数据
     */
    boolean recordResponse() default false;
}
```

### 3.7.4 使用示例

```java
@PostMapping("/add")
@OperationLog(module = "用户管理", type = "ADD", desc = "新增用户", recordResponse = false)
public ApiResponse addUser(@Valid @RequestBody SysUserAddRTO param) {
    // ...
}
```

---

## 3.8 SysMonitorController (系统监控控制器)

### 3.8.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `getServerInfo()` | GET | `/monitor/server` | 获取服务器信息 | MONITOR:VIEW |
| `getJvmInfo()` | GET | `/monitor/jvm` | 获取JVM信息 | MONITOR:VIEW |
| `getDatabaseInfo()` | GET | `/monitor/database` | 获取数据库信息 | MONITOR:VIEW |
| `getRedisInfo()` | GET | `/monitor/redis` | 获取Redis信息 | MONITOR:VIEW |
| `getOnlineUsers()` | GET | `/monitor/online-users` | 获取在线用户列表 | MONITOR:VIEW |
| `forceLogout()` | POST | `/monitor/force-logout` | 强制用户下线 | MONITOR:FORCE_LOGOUT |

### 3.8.2 VO设计

```java
@Data
public class SysMonitorVO {
    
    private ServerInfo server;
    private JvmInfo jvm;
    private DatabaseInfo database;
    private RedisInfo redis;
    
    @Data
    public static class ServerInfo {
        private String osName;
        private String osVersion;
        private String osArch;
        private String serverIp;
        private Integer cpuCores;
        private Double cpuUsage;       // CPU使用率
        private Long totalMemory;      // 总内存（字节）
        private Long usedMemory;       // 已用内存
        private Long freeMemory;       // 空闲内存
        private Double memoryUsage;    // 内存使用率
    }
    
    @Data
    public static class JvmInfo {
        private String javaVersion;
        private String javaVendor;
        private String javaHome;
        private Long jvmTotalMemory;
        private Long jvmUsedMemory;
        private Long jvmFreeMemory;
        private Double jvmMemoryUsage;
        private String startTime;      // JVM启动时间
        private Long runningTime;      // 运行时长（毫秒）
    }
    
    @Data
    public static class DatabaseInfo {
        private String dbType;
        private String dbVersion;
        private Integer connectionCount;
        private Integer activeConnections;
        private Integer idleConnections;
    }
    
    @Data
    public static class RedisInfo {
        private String redisVersion;
        private Long usedMemory;
        private Integer connectedClients;
        private Long totalCommandsProcessed;
    }
}
```

---

**文档总结**

本文档详细定义了系统配置模块的完整实现方案，包括：

- ✅ **系统配置管理**: 7个接口 + Redis缓存机制
- ✅ **数据字典管理**: 11个接口 + 缓存优化
- ✅ **登录日志**: 5个接口 + AOP自动记录
- ✅ **操作日志**: 5个接口 + 自定义注解 + AOP拦截
- ✅ **系统监控**: 6个接口 + 服务器/JVM/数据库/Redis监控

**核心特性**:
- 配置和字典支持Redis缓存
- 日志异步记录，不影响业务性能
- 操作日志通过注解声明式配置
- 系统监控实时获取运行状态

下一部分将继续完成审计日志、通知服务等模块的设计文档。
