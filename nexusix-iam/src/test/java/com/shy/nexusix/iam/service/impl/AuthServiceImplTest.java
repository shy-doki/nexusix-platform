package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.mapper.SysRolePolicyMapper;
import com.shy.nexusix.iam.mapper.SysUserPolicyMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * <p>AuthServiceImpl 单元测试</p>
 * <p>覆盖登录认证、两路径权限汇聚、级联禁用、字段级权限解析等核心逻辑</p>
 *
 * @author shy
 * @since 2026-06-10
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private SysUserPolicyMapper sysUserPolicyMapper;

    @Mock
    private SysPermPolicyMapper sysPermPolicyMapper;

    @Mock
    private SysRolePolicyMapper sysRolePolicyMapper;

    @Mock
    private SaSession saSession;

    private MockedStatic<StpUtil> stpUtilMockedStatic;

    @BeforeEach
    void setUp() {
        stpUtilMockedStatic = mockStatic(StpUtil.class);
    }

    @AfterEach
    void tearDown() {
        stpUtilMockedStatic.close();
    }

    // ==================== 登录前置校验测试 ====================

    @Nested
    @DisplayName("登录前置校验")
    class LoginValidationTests {

        @Test
        @DisplayName("用户已登录时直接返回成功")
        void testAlreadyLoggedIn() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("admin");
            param.setPassword("admin123");

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("admin")).thenReturn(true);

            // 执行
            ApiResponse result = authService.login(param);

            // 验证
            assertEquals(200, result.getCode());
            verify(sysUserPolicyMapper, never()).queryUserLoginJoin(anyString());
        }

        @Test
        @DisplayName("用户名不存在时抛出业务异常")
        void testUserNotFound() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("nonexistent");
            param.setPassword("password");

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("nonexistent")).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin("nonexistent")).thenReturn(null);

            // 执行 & 验证
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(param));
            assertEquals("用户名或密码不正确", exception.getMessage());
        }

        @Test
        @DisplayName("密码错误时抛出业务异常")
        void testWrongPassword() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("admin");
            param.setPassword("wrong_password");

            UserLoginJoinDTO loginJoinInfo = new UserLoginJoinDTO();
            loginJoinInfo.setUserId(1L);
            loginJoinInfo.setPassword("admin123");

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("admin")).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin("admin")).thenReturn(loginJoinInfo);

            // 执行 & 验证
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(param));
            assertEquals("用户名或密码不正确", exception.getMessage());
        }

        @Test
        @DisplayName("用户未设置默认租户时抛出业务异常")
        void testNoDefaultTenant() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("admin");
            param.setPassword("admin123");

            UserLoginJoinDTO loginJoinInfo = new UserLoginJoinDTO();
            loginJoinInfo.setUserId(1L);
            loginJoinInfo.setPassword("admin123");
            loginJoinInfo.setUserPolicyId(null);

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("admin")).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin("admin")).thenReturn(loginJoinInfo);

            // 执行 & 验证
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(param));
            assertEquals("用户未设置任何默认租户，请联系相关租户管理员进行设置", exception.getMessage());
        }

        @Test
        @DisplayName("租户已停用时抛出业务异常")
        void testTenantDisabled() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("admin");
            param.setPassword("admin123");

            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.DISABLED.getCode());

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("admin")).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin("admin")).thenReturn(loginJoinInfo);

            // 执行 & 验证
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(param));
            assertEquals("所属租户已停用", exception.getMessage());
        }

        @Test
        @DisplayName("租户已过期时抛出业务异常")
        void testTenantExpired() {
            // 准备
            LoginRTO param = new LoginRTO();
            param.setUsername("admin");
            param.setPassword("admin123");

            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.EXPIRED.getCode());

            stpUtilMockedStatic.when(() -> StpUtil.isLogin("admin")).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin("admin")).thenReturn(loginJoinInfo);

            // 执行 & 验证
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(param));
            assertEquals("所属租户已过期", exception.getMessage());
        }
    }

    // ==================== 权限计算核心逻辑测试 ====================

    @Nested
    @DisplayName("权限计算核心逻辑")
    class PermissionCalculationTests {

        @Test
        @DisplayName("两路径权限汇聚：ROLE + USER 路径权限合并")
        void testTwoPathPermissionAggregation() {
            // 准备
            LoginRTO param = buildLoginParam("zhang_san", "password123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(5L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            // 构造两路径权限数据（TENANT路径不作为用户直接权限参与计算）
            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // 路径1：角色级权限
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", null, null, null, "ROLE_PERM_B"));
            // 路径2：用户级权限
            permJoinList.add(buildPermJoinDTO(2L, 102L, "ACTIVE", null, null, null, "USER_PERM_C"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            ApiResponse result = authService.login(param);

            // 验证
            assertEquals(200, result.getCode());
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 全量权限编码应包含两路径的所有权限
            assertEquals(2, userContext.getPermissions().getAll().size());
            assertTrue(userContext.getPermissions().getAll().contains("ROLE_PERM_B"));
            assertTrue(userContext.getPermissions().getAll().contains("USER_PERM_C"));
            // 两路径权限均为ACTIVE 全部归入有效权限
            assertEquals(2, userContext.getPermissions().getValid().size());
            assertTrue(userContext.getPermissions().getInvalid().isEmpty());
        }

        @Test
        @DisplayName("同一权限ID去重：多路径返回同一权限时仅记录一次编码")
        void testPermissionDeduplication() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            // 同一权限ID(101) 通过不同路径返回两次
            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", null, null, null, "SYS_USER"));
            permJoinList.add(buildPermJoinDTO(2L, 101L, "DISABLED_TENANT_LEVEL", null, null, null, "SYS_USER"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 全量权限编码去重后只有一条
            assertEquals(1, userContext.getPermissions().getAll().size());
            assertEquals("SYS_USER", userContext.getPermissions().getAll().get(0));
            // 由于存在租户级禁用 该权限归入无效列表
            assertEquals(1, userContext.getPermissions().getInvalid().size());
            assertTrue(userContext.getPermissions().getValid().isEmpty());
        }

        @Test
        @DisplayName("级联禁用：系统级禁用不可被低级别覆盖")
        void testCascadeDisableSystemLevel() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // 权限101：系统级禁用 + 用户级ACTIVE（低级别无法覆盖高级别禁用）
            permJoinList.add(buildPermJoinDTO(1L, 101L, "DISABLED_SYSTEM_LEVEL", null, null, null, "PERM_X"));
            permJoinList.add(buildPermJoinDTO(2L, 101L, "ACTIVE", null, null, null, "PERM_X"));
            // 权限102：仅系统级禁用
            permJoinList.add(buildPermJoinDTO(3L, 102L, "DISABLED_SYSTEM_LEVEL", null, null, null, "PERM_Y"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 两个权限均为无效
            assertEquals(2, userContext.getPermissions().getInvalid().size());
            assertTrue(userContext.getPermissions().getValid().isEmpty());
            // 系统级禁用列表包含两个权限
            assertEquals(2, userContext.getPermissions().getDisabledDetail().getSystem().size());
            assertTrue(userContext.getPermissions().getDisabledDetail().getSystem().contains("PERM_X"));
            assertTrue(userContext.getPermissions().getDisabledDetail().getSystem().contains("PERM_Y"));
        }

        @Test
        @DisplayName("级联禁用：四级禁用分别归类到对应列表")
        void testCascadeDisableFourLevels() {
            // 准备
            LoginRTO param = buildLoginParam("test_user", "password");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(10L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "DISABLED_SYSTEM_LEVEL", null, null, null, "PERM_SYS"));
            permJoinList.add(buildPermJoinDTO(2L, 102L, "DISABLED_TENANT_LEVEL", null, null, null, "PERM_TENANT"));
            permJoinList.add(buildPermJoinDTO(3L, 103L, "DISABLED_ROLE_LEVEL", null, null, null, "PERM_ROLE"));
            permJoinList.add(buildPermJoinDTO(4L, 104L, "DISABLED_USER_LEVEL", null, null, null, "PERM_USER"));
            permJoinList.add(buildPermJoinDTO(5L, 105L, "ACTIVE", null, null, null, "PERM_ACTIVE"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 全量5个权限
            assertEquals(5, userContext.getPermissions().getAll().size());
            // 有效1个 无效4个
            assertEquals(1, userContext.getPermissions().getValid().size());
            assertEquals(4, userContext.getPermissions().getInvalid().size());
            // 四级禁用各1条
            assertEquals(1, userContext.getPermissions().getDisabledDetail().getSystem().size());
            assertEquals("PERM_SYS", userContext.getPermissions().getDisabledDetail().getSystem().get(0));
            assertEquals(1, userContext.getPermissions().getDisabledDetail().getTenant().size());
            assertEquals("PERM_TENANT", userContext.getPermissions().getDisabledDetail().getTenant().get(0));
            assertEquals(1, userContext.getPermissions().getDisabledDetail().getRole().size());
            assertEquals("PERM_ROLE", userContext.getPermissions().getDisabledDetail().getRole().get(0));
            assertEquals(1, userContext.getPermissions().getDisabledDetail().getUser().size());
            assertEquals("PERM_USER", userContext.getPermissions().getDisabledDetail().getUser().get(0));
        }

        @Test
        @DisplayName("同一权限多级别禁用：同时出现在多个禁用列表中")
        void testSamePermMultiLevelDisabled() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // 权限101同时被系统级和租户级禁用
            permJoinList.add(buildPermJoinDTO(1L, 101L, "DISABLED_SYSTEM_LEVEL", null, null, null, "PERM_MULTI"));
            permJoinList.add(buildPermJoinDTO(2L, 101L, "DISABLED_TENANT_LEVEL", null, null, null, "PERM_MULTI"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 权限编码只出现一次
            assertEquals(1, userContext.getPermissions().getAll().size());
            // 归入无效
            assertEquals(1, userContext.getPermissions().getInvalid().size());
            // 同时出现在系统级和租户级禁用列表
            assertTrue(userContext.getPermissions().getDisabledDetail().getSystem().contains("PERM_MULTI"));
            assertTrue(userContext.getPermissions().getDisabledDetail().getTenant().contains("PERM_MULTI"));
        }
    }

    // ==================== 字段级权限测试 ====================

    @Nested
    @DisplayName("字段级权限解析")
    class FieldPermissionTests {

        @Test
        @DisplayName("QUERY操作类型字段权限解析")
        void testQueryFieldPermission() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "QUERY",
                    "[\"tenantCode\",\"tenantName\"]", "sys_tenant", "SYS_TENANT_QUERY"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            UserContextDTO.TableFieldPermission fieldPerm = userContext.getPermissions().getFieldPermission().getQuery().get("sys_tenant");
            assertNotNull(fieldPerm);
            assertEquals(2, fieldPerm.getOperable().size());
            assertTrue(fieldPerm.getOperable().contains("tenantCode"));
            assertTrue(fieldPerm.getOperable().contains("tenantName"));
        }

        @Test
        @DisplayName("CREATE操作类型字段权限解析")
        void testCreateFieldPermission() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "CREATE",
                    "[\"tenantCode\"]", "sys_tenant", "SYS_TENANT_CREATE"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            UserContextDTO.TableFieldPermission fieldPerm = userContext.getPermissions().getFieldPermission().getCreate().get("sys_tenant");
            assertNotNull(fieldPerm);
            assertEquals(1, fieldPerm.getOperable().size());
            assertEquals("tenantCode", fieldPerm.getOperable().get(0));
        }

        @Test
        @DisplayName("UPDATE操作类型字段权限解析")
        void testUpdateFieldPermission() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "UPDATE",
                    "[\"tenantName\"]", "sys_tenant", "SYS_TENANT_UPDATE"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            UserContextDTO.TableFieldPermission fieldPerm = userContext.getPermissions().getFieldPermission().getUpdate().get("sys_tenant");
            assertNotNull(fieldPerm);
            assertEquals(1, fieldPerm.getOperable().size());
            assertEquals("tenantName", fieldPerm.getOperable().get(0));
        }

        @Test
        @DisplayName("禁用状态的字段权限归入不可见列表")
        void testDisabledFieldPermissionInvisible() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "DISABLED_TENANT_LEVEL", "QUERY",
                    "[\"contactPhone\"]", "sys_tenant", "SYS_TENANT_QUERY"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            UserContextDTO.TableFieldPermission fieldPerm = userContext.getPermissions().getFieldPermission().getQuery().get("sys_tenant");
            assertNotNull(fieldPerm);
            // 禁用字段归入不可见列表
            assertEquals(1, fieldPerm.getInoperable().size());
            assertEquals("contactPhone", fieldPerm.getInoperable().get(0));
            // 可见列表为空（初始化的空ArrayList）
            assertTrue(fieldPerm.getOperable().isEmpty());
        }

        @Test
        @DisplayName("同一表同一操作类型的字段权限合并")
        void testFieldPermissionMergeForSameTable() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // 两条策略均为ACTIVE 且操作类型为QUERY 表名相同
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "QUERY",
                    "[\"field1\",\"field2\"]", "sys_user", "USER_QUERY_1"));
            permJoinList.add(buildPermJoinDTO(2L, 102L, "ACTIVE", "QUERY",
                    "[\"field3\"]", "sys_user", "USER_QUERY_2"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            UserContextDTO.TableFieldPermission fieldPerm = userContext.getPermissions().getFieldPermission().getQuery().get("sys_user");
            assertNotNull(fieldPerm);
            // 两条策略的字段合并到可见列表
            assertEquals(3, fieldPerm.getOperable().size());
            assertTrue(fieldPerm.getOperable().contains("field1"));
            assertTrue(fieldPerm.getOperable().contains("field2"));
            assertTrue(fieldPerm.getOperable().contains("field3"));
        }

        @Test
        @DisplayName("字段操作为空时跳过字段权限解析")
        void testNullFieldOperatesSkipped() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // fieldOperates为空
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "QUERY", null, "sys_tenant", "PERM_A"));
            // accessType为空
            permJoinList.add(buildPermJoinDTO(2L, 102L, "ACTIVE", null, "[\"field1\"]", "sys_tenant", "PERM_B"));
            // tableName为空
            permJoinList.add(buildPermJoinDTO(3L, 103L, "ACTIVE", "QUERY", "[\"field1\"]", null, "PERM_C"));
            // fieldOperates为空字符串
            permJoinList.add(buildPermJoinDTO(4L, 104L, "ACTIVE", "QUERY", "", "sys_tenant", "PERM_D"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 所有字段权限Map均为空（无有效字段权限数据）
            assertTrue(userContext.getPermissions().getFieldPermission().getQuery().isEmpty());
            assertTrue(userContext.getPermissions().getFieldPermission().getCreate().isEmpty());
            assertTrue(userContext.getPermissions().getFieldPermission().getUpdate().isEmpty());
            // 但权限编码仍然被收集
            assertEquals(4, userContext.getPermissions().getAll().size());
        }

        @Test
        @DisplayName("未知操作类型跳过字段权限解析")
        void testUnknownAccessTypeSkipped() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "DELETE",
                    "[\"field1\"]", "sys_tenant", "PERM_DELETE"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 未知操作类型不产生字段权限
            assertTrue(userContext.getPermissions().getFieldPermission().getQuery().isEmpty());
            assertTrue(userContext.getPermissions().getFieldPermission().getCreate().isEmpty());
            assertTrue(userContext.getPermissions().getFieldPermission().getUpdate().isEmpty());
        }
    }

    // ==================== 角色信息测试 ====================

    @Nested
    @DisplayName("角色信息缓存")
    class RoleInfoTests {

        @Test
        @DisplayName("用户角色信息正确缓存到Session（含租户标识）")
        void testRoleInfoCached() {
            // 准备
            LoginRTO param = buildLoginParam("zhang_san", "password123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(5L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserRoleDTO> roleList = new ArrayList<>();
            UserRoleDTO role1 = buildUserRoleDTO("EMPLOYEE", "SELF", 100L, "WANXIANG", "万象集团", "ACTIVE");
            roleList.add(role1);

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), roleList, Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            assertEquals(1, userContext.getRoles().getCurrent().size());
            assertEquals("EMPLOYEE", userContext.getRoles().getCurrent().get(0).getRoleCode());
            assertEquals("SELF", userContext.getRoles().getCurrent().get(0).getDataScope());
            assertEquals("WANXIANG", userContext.getRoles().getCurrent().get(0).getTenantCode());
            assertEquals("万象集团", userContext.getRoles().getCurrent().get(0).getTenantName());
        }

        @Test
        @DisplayName("多角色用户在当前租户下角色信息正确缓存")
        void testMultiRoleInfoCached() {
            // 准备
            LoginRTO param = buildLoginParam("wang_wu", "password123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(8L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserRoleDTO> roleList = new ArrayList<>();
            UserRoleDTO role1 = buildUserRoleDTO("BRANCH_EMPLOYEE", "DEPT", 100L, "WANXIANG", "万象集团", "ACTIVE");
            roleList.add(role1);
            UserRoleDTO role2 = buildUserRoleDTO("EMPLOYEE", "SELF", 100L, "WANXIANG", "万象集团", "ACTIVE");
            roleList.add(role2);

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), roleList, Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            assertEquals(2, userContext.getRoles().getCurrent().size());
        }

        @Test
        @DisplayName("用户无角色时角色列表为空")
        void testNoRoleInfo() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), null, Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            assertNotNull(userContext.getRoles().getCurrent());
            assertTrue(userContext.getRoles().getCurrent().isEmpty());
            assertNotNull(userContext.getRoles().getAll());
            assertTrue(userContext.getRoles().getAll().isEmpty());
            assertNotNull(userContext.getRoles().getValid());
            assertTrue(userContext.getRoles().getValid().isEmpty());
            assertNotNull(userContext.getRoles().getInvalid());
            assertTrue(userContext.getRoles().getInvalid().isEmpty());
        }

        @Test
        @DisplayName("跨租户角色分类：有效/无效角色正确归类")
        void testAllRolesClassification() {
            // 准备 - 使用全力覆盖，queryUserAllRoleInfo 返回跨租户混合状态角色
            LoginRTO param = buildLoginParam("multi_user", "password123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(30L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserRoleDTO> currentTenantRoles = new ArrayList<>();
            UserRoleDTO currentRole = buildUserRoleDTO("EMPLOYEE", "SELF", 100L, "WANXIANG", "万象集团", "ACTIVE");
            currentTenantRoles.add(currentRole);

            // allRoles 包含跨三个租户的角色：2个ACTIVE + 1个DISABLED
            List<UserRoleDTO> allRoles = new ArrayList<>();
            allRoles.add(buildUserRoleDTO("EMPLOYEE", "SELF", 100L, "WANXIANG", "万象集团", "ACTIVE"));
            allRoles.add(buildUserRoleDTO("EMPLOYEE", "SELF", 200L, "DINGXIN", "鼎新集团", "ACTIVE"));
            allRoles.add(buildUserRoleDTO("ENGINEER", "DEPT", 300L, "XINGCHEN", "星辰科技", "DISABLED"));

            // 需要分开mock: queryUserRoleInfo 返回当前租户角色, queryUserAllRoleInfo 返回全部角色
            stpUtilMockedStatic.when(() -> StpUtil.isLogin(param.getUsername())).thenReturn(false);
            when(sysUserPolicyMapper.queryUserLoginJoin(param.getUsername())).thenReturn(loginJoinInfo);
            stpUtilMockedStatic.when(() -> StpUtil.login(param.getUsername())).thenAnswer(invocation -> null);
            stpUtilMockedStatic.when(StpUtil::getSession).thenReturn(saSession);
            when(sysPermPolicyMapper.queryUserPermJoin(loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId()))
                    .thenReturn(Collections.emptyList());
            when(sysRolePolicyMapper.queryUserRoleInfo(loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId()))
                    .thenReturn(currentTenantRoles);
            when(sysRolePolicyMapper.queryUserAllRoleInfo(loginJoinInfo.getUserId()))
                    .thenReturn(allRoles);
            when(sysUserPolicyMapper.queryUserAllTenants(loginJoinInfo.getUserId()))
                    .thenReturn(Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();

            // allRoles = 3
            assertEquals(3, userContext.getRoles().getAll().size());
            // validRoles = 2 (ACTIVE)
            assertEquals(2, userContext.getRoles().getValid().size());
            assertTrue(userContext.getRoles().getValid().stream()
                    .allMatch(r -> r.getTenantCode() != null));
            // invalidRoles = 1 (DISABLED)
            assertEquals(1, userContext.getRoles().getInvalid().size());
            assertEquals("星辰科技", userContext.getRoles().getInvalid().get(0).getTenantName());
            assertEquals("DISABLED", allRoles.get(2).getRolePolicyStatus());
            // roles = 当前租户(万象集团)角色 = 1
            assertEquals(1, userContext.getRoles().getCurrent().size());
            assertEquals("万象集团", userContext.getRoles().getCurrent().get(0).getTenantName());
        }
    }

    // ==================== 租户列表分类测试 ====================

    @Nested
    @DisplayName("租户列表分类")
    class TenantListTests {

        @Test
        @DisplayName("有效租户和无效租户正确分类")
        void testTenantClassification() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserTenantItemDTO> allTenants = new ArrayList<>();
            allTenants.add(buildTenantItemDTO(100L, "T001", "集团A", GlobalEnum.TenantStatus.ENABLED.getCode()));
            allTenants.add(buildTenantItemDTO(200L, "T002", "集团B", GlobalEnum.TenantStatus.DISABLED.getCode()));
            allTenants.add(buildTenantItemDTO(300L, "T003", "集团C", GlobalEnum.TenantStatus.EXPIRED.getCode()));
            allTenants.add(buildTenantItemDTO(400L, "T004", "集团D", GlobalEnum.TenantStatus.ENABLED.getCode()));

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), Collections.emptyList(), allTenants);

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 有效租户2个
            assertEquals(2, userContext.getTenants().getValid().size());
            assertTrue(userContext.getTenants().getValid().stream()
                    .allMatch(t -> GlobalEnum.TenantStatus.ENABLED.getCode().equals(t.getStatus())));
            // 无效租户2个
            assertEquals(2, userContext.getTenants().getInvalid().size());
            assertTrue(userContext.getTenants().getInvalid().stream()
                    .allMatch(t -> !GlobalEnum.TenantStatus.ENABLED.getCode().equals(t.getStatus())));
        }

        @Test
        @DisplayName("所有租户均为有效时无效列表为空")
        void testAllTenantsValid() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserTenantItemDTO> allTenants = new ArrayList<>();
            allTenants.add(buildTenantItemDTO(100L, "T001", "集团A", GlobalEnum.TenantStatus.ENABLED.getCode()));
            allTenants.add(buildTenantItemDTO(400L, "T004", "集团D", GlobalEnum.TenantStatus.ENABLED.getCode()));

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), Collections.emptyList(), allTenants);

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            assertEquals(2, userContext.getTenants().getValid().size());
            assertTrue(userContext.getTenants().getInvalid().isEmpty());
        }
    }

    // ==================== Session缓存完整性测试 ====================

    @Nested
    @DisplayName("Session缓存完整性")
    class SessionCacheTests {

        @Test
        @DisplayName("UserContext完整结构验证")
        void testUserContextCompleteStructure() {
            // 准备
            LoginRTO param = buildLoginParam("zhang_zong", "password123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(2L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "QUERY",
                    "[\"tenantCode\",\"tenantName\"]", "sys_tenant", "TENANT_QUERY"));
            permJoinList.add(buildPermJoinDTO(2L, 102L, "DISABLED_TENANT_LEVEL", "UPDATE",
                    "[\"contactPhone\"]", "sys_tenant", "TENANT_UPDATE"));

            List<UserRoleDTO> roleList = new ArrayList<>();
            UserRoleDTO role = buildUserRoleDTO("TENANT_ADMIN", "ALL", 100L, "T001", "集团A", "ACTIVE");
            roleList.add(role);

            List<UserTenantItemDTO> allTenants = new ArrayList<>();
            allTenants.add(buildTenantItemDTO(100L, "T001", "集团A", GlobalEnum.TenantStatus.ENABLED.getCode()));

            mockLoginFlow(param, loginJoinInfo, permJoinList, roleList, allTenants);

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();

            // 租户信息
            assertNotNull(userContext.getCurrentTenant());
            assertEquals("T001", userContext.getCurrentTenant().getTenantCode());
            assertEquals("集团A", userContext.getCurrentTenant().getTenantName());
            assertEquals(GlobalEnum.TenantStatus.ENABLED.getCode(), userContext.getCurrentTenant().getStatus());

            // 租户列表
            assertEquals(1, userContext.getTenants().getValid().size());
            assertTrue(userContext.getTenants().getInvalid().isEmpty());

            // 权限信息
            assertNotNull(userContext.getPermissions());
            assertEquals(2, userContext.getPermissions().getAll().size());
            assertEquals(1, userContext.getPermissions().getValid().size());
            assertEquals(1, userContext.getPermissions().getInvalid().size());

            // 级联禁用
            assertNotNull(userContext.getPermissions().getDisabledDetail());
            assertTrue(userContext.getPermissions().getDisabledDetail().getSystem().isEmpty());
            assertEquals(1, userContext.getPermissions().getDisabledDetail().getTenant().size());
            assertTrue(userContext.getPermissions().getDisabledDetail().getRole().isEmpty());
            assertTrue(userContext.getPermissions().getDisabledDetail().getUser().isEmpty());

            // 字段权限
            assertNotNull(userContext.getPermissions().getFieldPermission());
            assertNotNull(userContext.getPermissions().getFieldPermission().getQuery().get("sys_tenant"));
            assertNotNull(userContext.getPermissions().getFieldPermission().getUpdate().get("sys_tenant"));
            assertTrue(userContext.getPermissions().getFieldPermission().getCreate().isEmpty());

            // 角色信息
            assertEquals(1, userContext.getRoles().getCurrent().size());
            assertEquals("TENANT_ADMIN", userContext.getRoles().getCurrent().get(0).getRoleCode());
            assertEquals("ALL", userContext.getRoles().getCurrent().get(0).getDataScope());
            assertEquals("T001", userContext.getRoles().getCurrent().get(0).getTenantCode());
            assertEquals("集团A", userContext.getRoles().getCurrent().get(0).getTenantName());
            // allRoles / validRoles / invalidRoles 均存在
            assertEquals(1, userContext.getRoles().getAll().size());
            assertEquals(1, userContext.getRoles().getValid().size());
            assertTrue(userContext.getRoles().getInvalid().isEmpty());
        }
    }

    // ==================== 边界条件测试 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCaseTests {

        @Test
        @DisplayName("权限列表为空时有效和无效列表均为空")
        void testEmptyPermissionList() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            mockLoginFlow(param, loginJoinInfo, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            assertTrue(userContext.getPermissions().getAll().isEmpty());
            assertTrue(userContext.getPermissions().getValid().isEmpty());
            assertTrue(userContext.getPermissions().getInvalid().isEmpty());
        }

        @Test
        @DisplayName("权限编码为null时跳过该行权限编码收集")
        void testNullPermCodeSkipped() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            // permId为null
            permJoinList.add(buildPermJoinDTO(1L, null, "ACTIVE", null, null, null, null));
            // permCode为null
            permJoinList.add(buildPermJoinDTO(2L, 102L, "ACTIVE", null, null, null, null));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // permId为null的行不收集编码 permCode为null的行也不收集编码
            // 第二行permId=102但permCode=null 所以也不收集
            assertTrue(userContext.getPermissions().getAll().isEmpty());
        }

        @Test
        @DisplayName("fieldOperates解析为空数组时跳过字段权限")
        void testEmptyFieldOperatesArray() {
            // 准备
            LoginRTO param = buildLoginParam("admin", "admin123");
            UserLoginJoinDTO loginJoinInfo = buildLoginJoinInfo(1L, 100L, GlobalEnum.TenantStatus.ENABLED.getCode());

            List<UserPermJoinDTO> permJoinList = new ArrayList<>();
            permJoinList.add(buildPermJoinDTO(1L, 101L, "ACTIVE", "QUERY", "[]", "sys_tenant", "PERM_A"));

            mockLoginFlow(param, loginJoinInfo, permJoinList, Collections.emptyList(), Collections.emptyList());

            // 执行
            authService.login(param);

            // 验证
            ArgumentCaptor<UserContextDTO> captor = ArgumentCaptor.forClass(UserContextDTO.class);
            verify(saSession).set(eq("userContext"), captor.capture());

            UserContextDTO userContext = captor.getValue();
            // 空数组不产生字段权限
            assertTrue(userContext.getPermissions().getFieldPermission().getQuery().isEmpty());
            // 但权限编码仍被收集
            assertEquals(1, userContext.getPermissions().getAll().size());
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建登录请求参数
     */
    private LoginRTO buildLoginParam(String username, String password) {
        LoginRTO param = new LoginRTO();
        param.setUsername(username);
        param.setPassword(password);
        return param;
    }

    /**
     * 构建登录关联信息DTO
     */
    private UserLoginJoinDTO buildLoginJoinInfo(Long userId, Long tenantId, String tenantStatus) {
        UserLoginJoinDTO dto = new UserLoginJoinDTO();
        dto.setUserId(userId);
        dto.setPassword("password123");
        dto.setUserPolicyId(1000L);
        dto.setTenantId(tenantId);
        dto.setTenantName("测试租户");
        dto.setTenantCode("TEST_TENANT");
        dto.setTenantStatus(tenantStatus);
        return dto;
    }

    /**
     * 构建权限策略关联DTO
     */
    private UserPermJoinDTO buildPermJoinDTO(Long policyId, Long permId, String policyStatus,
                                              String accessType, String fieldOperates,
                                              String tableName, String permCode) {
        UserPermJoinDTO dto = new UserPermJoinDTO();
        dto.setPolicyId(policyId);
        dto.setPermId(permId);
        dto.setPolicyStatus(policyStatus);
        dto.setAccessType(accessType);
        dto.setFieldOperates(fieldOperates);
        dto.setTableName(tableName);
        dto.setPermCode(permCode);
        return dto;
    }

    /**
     * 构建租户列表项DTO
     */
    private UserTenantItemDTO buildTenantItemDTO(Long tenantId, String tenantCode,
                                                  String tenantName, String tenantStatus) {
        return buildTenantItemDTO(tenantId, tenantCode, tenantName, tenantStatus, "ENABLED");
    }

    /**
     * 构建租户列表项DTO（含用户策略状态）
     */
    private UserTenantItemDTO buildTenantItemDTO(Long tenantId, String tenantCode,
                                                  String tenantName, String tenantStatus,
                                                  String userPolicyStatus) {
        UserTenantItemDTO dto = new UserTenantItemDTO();
        dto.setTenantId(tenantId);
        dto.setTenantCode(tenantCode);
        dto.setTenantName(tenantName);
        dto.setTenantStatus(tenantStatus);
        dto.setUserPolicyStatus(userPolicyStatus);
        return dto;
    }

    /**
     * 构建用户角色DTO（含租户标识和策略状态）
     */
    private UserRoleDTO buildUserRoleDTO(String roleCode, String dataScope,
                                          Long tenantId, String tenantCode,
                                          String tenantName, String rolePolicyStatus) {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setRoleCode(roleCode);
        dto.setDataScope(dataScope);
        dto.setTenantId(tenantId);
        dto.setTenantCode(tenantCode);
        dto.setTenantName(tenantName);
        dto.setRolePolicyId(2000L + tenantId);
        dto.setRolePolicyStatus(rolePolicyStatus);
        return dto;
    }

    /**
     * 模拟完整登录流程的Mapper调用和Sa-Token调用
     */
    private void mockLoginFlow(LoginRTO param, UserLoginJoinDTO loginJoinInfo,
                                List<UserPermJoinDTO> permJoinList,
                                List<UserRoleDTO> roleList,
                                List<UserTenantItemDTO> tenantList) {
        stpUtilMockedStatic.when(() -> StpUtil.isLogin(param.getUsername())).thenReturn(false);
        when(sysUserPolicyMapper.queryUserLoginJoin(param.getUsername())).thenReturn(loginJoinInfo);
        stpUtilMockedStatic.when(() -> StpUtil.login(param.getUsername())).thenAnswer(invocation -> null);
        stpUtilMockedStatic.when(StpUtil::getSession).thenReturn(saSession);
        when(sysPermPolicyMapper.queryUserPermJoin(loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId()))
                .thenReturn(permJoinList);
        when(sysRolePolicyMapper.queryUserRoleInfo(loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId()))
                .thenReturn(roleList);
        when(sysRolePolicyMapper.queryUserAllRoleInfo(loginJoinInfo.getUserId()))
                .thenReturn(roleList);
        when(sysUserPolicyMapper.queryUserAllTenants(loginJoinInfo.getUserId()))
                .thenReturn(tenantList != null ? tenantList : Collections.emptyList());
    }

}
