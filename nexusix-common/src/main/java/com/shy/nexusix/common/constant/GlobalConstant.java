package com.shy.nexusix.common.constant;

/**
 * <p>
 * 全局常量定义 (默认分页大小、状态码常量)
 * </p>
 * <p>
 * 该类定义了系统中通用的常量，包括分页参数、HTTP状态码、通用状态标记、
 * 租户相关常量以及各业务模块的状态码常量。所有常量均采用全大写字母命名，
 * 单词间用下划线分隔，符合Java编码规范。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public class GlobalConstant {

    private GlobalConstant() {
    }

    /**
     *  分页相关常量 
     */
    public static final class Page {
        
        private Page() {
        }

        /**
         * 默认页码
         * <p>取值范围：正整数，从1开始</p>
         * <p>使用场景：分页查询时未指定页码时的默认值</p>
         */
        public static final int DEFAULT_PAGE_NUM = 1;

        /**
         * 默认每页大小
         * <p>取值范围：正整数，建议10-50之间</p>
         * <p>使用场景：分页查询时未指定每页大小时的默认值</p>
         */
        public static final int DEFAULT_PAGE_SIZE = 10;

        /**
         * 最大每页大小
         * <p>取值范围：正整数，用于防止一次查询过多数据</p>
         * <p>使用场景：限制分页查询时每页最大记录数，防止内存溢出</p>
         */
        public static final int MAX_PAGE_SIZE = 100;

    }

    /**
     *  HTTP状态码常量 
     */
    public static final class HttpStatus {
        
        private HttpStatus() {
        }

        /**
         * 操作成功
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：请求处理成功时返回</p>
         */
        public static final int SUCCESS = 200;

        /**
         * 未授权
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：用户未登录或Token失效时返回</p>
         */
        public static final int UNAUTHORIZED = 401;

        /**
         * 禁止访问
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：用户无权限访问该资源时返回</p>
         */
        public static final int FORBIDDEN = 403;

        /**
         * 资源不存在
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：请求的资源不存在时返回</p>
         */
        public static final int NOT_FOUND = 404;

        /**
         * 服务器内部错误
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：服务器处理请求时发生异常</p>
         */
        public static final int INTERNAL_SERVER_ERROR = 500;

    }

    /**
     *  通用状态常量 
     */
    public static final class Status {
        
        private Status() {
        }

        /**
         * 启用/正常状态
         * <p>取值范围：0-禁用，1-启用</p>
         * <p>使用场景：通用状态标记，适用于租户、用户、角色、菜单等实体的状态字段</p>
         */
        public static final int ENABLE = 1;

        /**
         * 禁用/冻结状态
         * <p>取值范围：0-禁用，1-启用</p>
         * <p>使用场景：通用状态标记，适用于租户、用户、角色、菜单等实体的状态字段</p>
         */
        public static final int DISABLE = 0;

        /**
         * 未删除
         * <p>取值范围：0-未删除，1-已删除</p>
         * <p>使用场景：逻辑删除标记，适用于所有支持软删除的业务表</p>
         */
        public static final int NOT_DELETED = 0;

        /**
         * 已删除
         * <p>取值范围：0-未删除，1-已删除</p>
         * <p>使用场景：逻辑删除标记，适用于所有支持软删除的业务表</p>
         */
        public static final int DELETED = 1;

        /**
         * 是
         * <p>取值范围：布尔值，1-是，0-否</p>
         * <p>使用场景：布尔类型字段标记，如是否管理员、是否自动续费等</p>
         */
        public static final int YES = 1;

        /**
         * 否
         * <p>取值范围：布尔值，1-是，0-否</p>
         * <p>使用场景：布尔类型字段标记，如是否管理员、是否自动续费等</p>
         */
        public static final int NO = 0;

    }

    /**
     *  套餐相关常量 
     */
    public static final class Package {
        
        private Package() {
        }

        /**
         * 周期类型：天
         * <p>取值范围：1-天，2-月，3-年</p>
         * <p>使用场景：套餐订阅周期定义</p>
         */
        public static final int CYCLE_TYPE_DAY = 1;

        /**
         * 周期类型：月
         * <p>取值范围：1-天，2-月，3-年</p>
         * <p>使用场景：套餐订阅周期定义</p>
         */
        public static final int CYCLE_TYPE_MONTH = 2;

        /**
         * 周期类型：年
         * <p>取值范围：1-天，2-月，3-年</p>
         * <p>使用场景：套餐订阅周期定义</p>
         */
        public static final int CYCLE_TYPE_YEAR = 3;

        /**
         * 套餐状态：上架
         * <p>取值范围：1-上架，0-下架</p>
         * <p>使用场景：套餐产品管理，上架的套餐可供租户订阅</p>
         */
        public static final int PACKAGE_STATUS_ON_SHELF = 1;

        /**
         * 套餐状态：下架
         * <p>取值范围：1-上架，0-下架</p>
         * <p>使用场景：套餐产品管理，下架的套餐不可供租户订阅</p>
         */
        public static final int PACKAGE_STATUS_OFF_SHELF = 0;

    }

    /**
     *  订阅相关常量 
     */
    public static final class Subscription {
        
        private Subscription() {
        }

        /**
         * 订阅类型：自购
         * <p>取值范围：1-自购，2-父租户分配</p>
         * <p>使用场景：租户订阅套餐时区分订阅来源</p>
         */
        public static final int SUBSCRIPTION_TYPE_SELF_PURCHASE = 1;

        /**
         * 订阅类型：父租户分配
         * <p>取值范围：1-自购，2-父租户分配</p>
         * <p>使用场景：租户订阅套餐时区分订阅来源</p>
         */
        public static final int SUBSCRIPTION_TYPE_PARENT_GRANT = 2;

        /**
         * 订阅状态：生效
         * <p>取值范围：1-生效，0-过期</p>
         * <p>使用场景：租户订阅状态管理</p>
         */
        public static final int SUBSCRIPTION_STATUS_ACTIVE = 1;

        /**
         * 订阅状态：过期
         * <p>取值范围：1-生效，0-过期</p>
         * <p>使用场景：租户订阅状态管理</p>
         */
        public static final int SUBSCRIPTION_STATUS_EXPIRED = 0;

    }

    /**
     *  订单相关常量 
     */
    public static final class Order {
        
        private Order() {
        }

        /**
         * 产品类型：套餐
         * <p>取值范围：1-套餐，2-配额包</p>
         * <p>使用场景：订单产品类型区分</p>
         */
        public static final int PRODUCT_TYPE_PACKAGE = 1;

        /**
         * 产品类型：配额包
         * <p>取值范围：1-套餐，2-配额包</p>
         * <p>使用场景：订单产品类型区分</p>
         */
        public static final int PRODUCT_TYPE_QUOTA_PACKAGE = 2;

        /**
         * 订单状态：未支付
         * <p>取值范围：0-未支付，1-已支付，2-已取消</p>
         * <p>使用场景：订单支付状态管理</p>
         */
        public static final int ORDER_STATUS_UNPAID = 0;

        /**
         * 订单状态：已支付
         * <p>取值范围：0-未支付，1-已支付，2-已取消</p>
         * <p>使用场景：订单支付状态管理</p>
         */
        public static final int ORDER_STATUS_PAID = 1;

        /**
         * 订单状态：已取消
         * <p>取值范围：0-未支付，1-已支付，2-已取消</p>
         * <p>使用场景：订单支付状态管理</p>
         */
        public static final int ORDER_STATUS_CANCELLED = 2;

    }

    /**
     *  发票相关常量 
     */
    public static final class Invoice {
        
        private Invoice() {
        }

        /**
         * 发票类型：普通发票
         * <p>取值范围：1-普通发票，2-专用发票，3-电子发票</p>
         * <p>使用场景：发票类型区分</p>
         */
        public static final int INVOICE_TYPE_ORDINARY = 1;

        /**
         * 发票类型：专用发票
         * <p>取值范围：1-普通发票，2-专用发票，3-电子发票</p>
         * <p>使用场景：发票类型区分</p>
         */
        public static final int INVOICE_TYPE_SPECIAL = 2;

        /**
         * 发票类型：电子发票
         * <p>取值范围：1-普通发票，2-专用发票，3-电子发票</p>
         * <p>使用场景：发票类型区分</p>
         */
        public static final int INVOICE_TYPE_ELECTRONIC = 3;

        /**
         * 发票状态：待开具
         * <p>取值范围：0-待开具，1-已开具，2-已邮寄，3-已作废</p>
         * <p>使用场景：发票开具状态管理</p>
         */
        public static final int INVOICE_STATUS_PENDING = 0;

        /**
         * 发票状态：已开具
         * <p>取值范围：0-待开具，1-已开具，2-已邮寄，3-已作废</p>
         * <p>使用场景：发票开具状态管理</p>
         */
        public static final int INVOICE_STATUS_ISSUED = 1;

        /**
         * 发票状态：已邮寄
         * <p>取值范围：0-待开具，1-已开具，2-已邮寄，3-已作废</p>
         * <p>使用场景：发票开具状态管理</p>
         */
        public static final int INVOICE_STATUS_MAILED = 2;

        /**
         * 发票状态：已作废
         * <p>取值范围：0-待开具，1-已开具，2-已邮寄，3-已作废</p>
         * <p>使用场景：发票开具状态管理</p>
         */
        public static final int INVOICE_STATUS_CANCELLED = 3;

    }

    /**
     *  配额相关常量
     *  <p>TODO 后续重新定义这部分常量 根据业务需求 </p>
     */
    public static final class Quota {
        
        private Quota() {
        }

        /**
         * 调整类型：购买加油包
         * <p>取值范围：1-购买加油包，2-补偿，3-惩罚</p>
         * <p>使用场景：租户配额动态调整时区分调整原因</p>
         */
        public static final int ADJUST_TYPE_PURCHASE = 1;

        /**
         * 调整类型：补偿
         * <p>取值范围：1-购买加油包，2-补偿，3-惩罚</p>
         * <p>使用场景：租户配额动态调整时区分调整原因</p>
         */
        public static final int ADJUST_TYPE_COMPENSATION = 2;

        /**
         * 调整类型：惩罚
         * <p>取值范围：1-购买加油包，2-补偿，3-惩罚</p>
         * <p>使用场景：租户配额动态调整时区分调整原因</p>
         */
        public static final int ADJUST_TYPE_PENALTY = 3;

    }

    /**
     *  权限相关常量 
     */
    public static final class Permission {
        
        private Permission() {
        }

        /**
         * 权限目标类型：系统
         * <p>取值范围：1-系统，2-租户，3-角色，4-用户</p>
         * <p>使用场景：权限配置时指定策略应用的目标类型</p>
         */
        public static final int TARGET_TYPE_SYSTEM = 1;

        /**
         * 权限目标类型：租户
         * <p>取值范围：1-系统，2-租户，3-角色，4-用户</p>
         * <p>使用场景：权限配置时指定策略应用的目标类型</p>
         */
        public static final int TARGET_TYPE_TENANT = 2;

        /**
         * 权限目标类型：角色
         * <p>取值范围：1-系统，2-租户，3-角色，4-用户</p>
         * <p>使用场景：权限配置时指定策略应用的目标类型</p>
         */
        public static final int TARGET_TYPE_ROLE = 3;

        /**
         * 权限目标类型：用户
         * <p>取值范围：1-系统，2-租户，3-角色，4-用户</p>
         * <p>使用场景：权限配置时指定策略应用的目标类型</p>
         */
        public static final int TARGET_TYPE_USER = 4;

        /**
         * 权限层级：系统级
         * <p>取值范围：1-系统级，2-租户级，3-角色级，4-用户级</p>
         * <p>使用场景：权限管理时区分权限作用范围，系统级权限对所有租户生效</p>
         */
        public static final int LEVEL_SYSTEM = 1;

        /**
         * 权限层级：租户级
         * <p>取值范围：1-系统级，2-租户级，3-角色级，4-用户级</p>
         * <p>使用场景：权限管理时区分权限作用范围，租户级权限仅在所属租户内生效</p>
         */
        public static final int LEVEL_TENANT = 2;

        /**
         * 权限层级：角色级
         * <p>取值范围：1-系统级，2-租户级，3-角色级，4-用户级</p>
         * <p>使用场景：权限管理时区分权限作用范围，角色级权限仅对特定角色生效</p>
         */
        public static final int LEVEL_ROLE = 3;

        /**
         * 权限层级：用户级
         * <p>取值范围：1-系统级，2-租户级，3-角色级，4-用户级</p>
         * <p>使用场景：权限管理时区分权限作用范围，用户级权限为特定用户单独配置</p>
         */
        public static final int LEVEL_USER = 4;

        /**
         * 策略动作：允许
         * <p>取值范围：1-允许，2-拒绝</p>
         * <p>使用场景：权限策略配置时指定策略动作</p>
         */
        public static final int ACTION_ALLOW = 1;

        /**
         * 策略动作：拒绝
         * <p>取值范围：1-允许，2-拒绝</p>
         * <p>使用场景：权限策略配置时指定策略动作</p>
         */
        public static final int ACTION_DENY = 2;

    }

    /**
     *  角色相关常量 
     */
    public static final class Role {
        
        private Role() {
        }

        /**
         * 角色层级：系统级
         * <p>取值范围：1-系统级，2-租户级，3-用户级</p>
         * <p>使用场景：角色管理时区分角色层级，系统级角色对所有租户可见</p>
         */
        public static final int ROLE_LEVEL_SYSTEM = 1;

        /**
         * 角色层级：租户级
         * <p>取值范围：1-系统级，2-租户级，3-用户级</p>
         * <p>使用场景：角色管理时区分角色层级，租户级角色仅在所属租户内可见</p>
         */
        public static final int ROLE_LEVEL_TENANT = 2;

        /**
         * 角色层级：用户级
         * <p>取值范围：1-系统级，2-租户级，3-用户级</p>
         * <p>使用场景：角色管理时区分角色层级，用户级角色为临时角色</p>
         */
        public static final int ROLE_LEVEL_USER = 3;

    }

    /**
     *  数据范围相关常量
     */
    public static final class DataScopeRange {

        /**
         * 数据范围：全部
         * <p>取值范围：1-全部，2-本部门，3-本人，4-自定义</p>
         * <p>使用场景：角色数据权限范围定义，全部表示可查看所有数据</p>
         */
        public static final int DATA_SCOPE_ALL = 1;

        /**
         * 数据范围：本部门
         * <p>取值范围：1-全部，2-本部门，3-本人，4-自定义</p>
         * <p>使用场景：角色数据权限范围定义，本部门表示可查看本部门及子部门数据</p>
         */
        public static final int DATA_SCOPE_DEPT = 2;

        /**
         * 数据范围：本人
         * <p>取值范围：1-全部，2-本部门，3-本人，4-自定义</p>
         * <p>使用场景：角色数据权限范围定义，本人表示只能查看自己创建的数据</p>
         */
        public static final int DATA_SCOPE_SELF = 3;

        /**
         * 数据范围：自定义
         * <p>取值范围：1-全部，2-本部门，3-本人，4-自定义</p>
         * <p>使用场景：角色数据权限范围定义，自定义表示可查看指定部门的数据</p>
         */
        public static final int DATA_SCOPE_CUSTOM = 4;

    }

    /**
     *  消息相关常量
     *  <p>TODO 后续重新定义这部分常量 根据业务需求 </p>
     */
    public static final class Message {
        
        private Message() {
        }

        /**
         * 消息类型：通知
         * <p>取值范围：1-通知，2-营销，3-验证，4-提醒</p>
         * <p>使用场景：消息模板管理时区分消息类型</p>
         */
        public static final int MESSAGE_TYPE_NOTIFICATION = 1;

        /**
         * 消息类型：营销
         * <p>取值范围：1-通知，2-营销，3-验证，4-提醒</p>
         * <p>使用场景：消息模板管理时区分消息类型</p>
         */
        public static final int MESSAGE_TYPE_MARKETING = 2;

        /**
         * 消息类型：验证
         * <p>取值范围：1-通知，2-营销，3-验证，4-提醒</p>
         * <p>使用场景：消息模板管理时区分消息类型</p>
         */
        public static final int MESSAGE_TYPE_VERIFICATION = 3;

        /**
         * 消息类型：提醒
         * <p>取值范围：1-通知，2-营销，3-验证，4-提醒</p>
         * <p>使用场景：消息模板管理时区分消息类型</p>
         */
        public static final int MESSAGE_TYPE_REMINDER = 4;

    }

    /**
     *  定时任务相关常量
     */
    public static final class Task {

        /**
         * 定时任务目标类型：指定用户
         * <p>取值范围：1-指定用户，2-指定租户，3-符合条件的所有用户</p>
         * <p>使用场景：定时消息任务管理时指定消息发送目标</p>
         */
        public static final int TARGET_TYPE_USER = 1;

        /**
         * 定时任务目标类型：指定租户
         * <p>取值范围：1-指定用户，2-指定租户，3-符合条件的所有用户</p>
         * <p>使用场景：定时消息任务管理时指定消息发送目标</p>
         */
        public static final int TARGET_TYPE_TENANT = 2;

        /**
         * 定时任务目标类型：符合条件的所有用户
         * <p>取值范围：1-指定用户，2-指定租户，3-符合条件的所有用户</p>
         * <p>使用场景：定时消息任务管理时指定消息发送目标</p>
         */
        public static final int TARGET_TYPE_ALL_USERS = 3;

        /**
         * 定时任务触发类型：单次定时
         * <p>取值范围：1-单次定时，2-周期循环，3-事件触发</p>
         * <p>使用场景：定时消息任务管理时指定触发方式</p>
         */
        public static final int TRIGGER_TYPE_ONCE = 1;

        /**
         * 定时任务触发类型：周期循环
         * <p>取值范围：1-单次定时，2-周期循环，3-事件触发</p>
         * <p>使用场景：定时消息任务管理时指定触发方式</p>
         */
        public static final int TRIGGER_TYPE_PERIODIC = 2;

        /**
         * 定时任务触发类型：事件触发
         * <p>取值范围：1-单次定时，2-周期循环，3-事件触发</p>
         * <p>使用场景：定时消息任务管理时指定触发方式</p>
         */
        public static final int TRIGGER_TYPE_EVENT = 3;

        /**
         * 定时任务状态：待执行
         * <p>取值范围：0-待执行，1-执行中，2-已完成，3-已取消</p>
         * <p>使用场景：定时消息任务状态管理</p>
         */
        public static final int SCHEDULE_STATUS_PENDING = 0;

        /**
         * 定时任务状态：执行中
         * <p>取值范围：0-待执行，1-执行中，2-已完成，3-已取消</p>
         * <p>使用场景：定时消息任务状态管理</p>
         */
        public static final int SCHEDULE_STATUS_EXECUTING = 1;

        /**
         * 定时任务状态：已完成
         * <p>取值范围：0-待执行，1-执行中，2-已完成，3-已取消</p>
         * <p>使用场景：定时消息任务状态管理</p>
         */
        public static final int SCHEDULE_STATUS_COMPLETED = 2;

        /**
         * 定时任务状态：已取消
         * <p>取值范围：0-待执行，1-执行中，2-已完成，3-已取消</p>
         * <p>使用场景：定时消息任务状态管理</p>
         */
        public static final int SCHEDULE_STATUS_CANCELLED = 3;

    }

    /**
     *  菜单相关常量 
     */
    public static final class Menu {
        
        private Menu() {
        }

        /**
         * 菜单类型：目录
         * <p>取值范围：1-目录，2-菜单，3-按钮</p>
         * <p>使用场景：菜单管理时区分菜单类型，目录为菜单分组</p>
         */
        public static final int MENU_TYPE_DIRECTORY = 1;

        /**
         * 菜单类型：菜单
         * <p>取值范围：1-目录，2-菜单，3-按钮</p>
         * <p>使用场景：菜单管理时区分菜单类型，菜单为具体页面</p>
         */
        public static final int MENU_TYPE_MENU = 2;

        /**
         * 菜单类型：按钮
         * <p>取值范围：1-目录，2-菜单，3-按钮</p>
         * <p>使用场景：菜单管理时区分菜单类型，按钮为页面操作权限</p>
         */
        public static final int MENU_TYPE_BUTTON = 3;

    }

    /**
     *  公告相关常量 
     */
    public static final class Notice {
        
        private Notice() {
        }

        /**
         * 公告类型：公告
         * <p>取值范围：1-公告，2-通知</p>
         * <p>使用场景：公告管理时区分公告类型</p>
         */
        public static final int NOTICE_TYPE_ANNOUNCEMENT = 1;

        /**
         * 公告类型：通知
         * <p>取值范围：1-公告，2-通知</p>
         * <p>使用场景：公告管理时区分公告类型</p>
         */
        public static final int NOTICE_TYPE_NOTIFICATION = 2;

        /**
         * 公告目标类型：全员
         * <p>取值范围：1-全员，2-指定租户，3-指定用户</p>
         * <p>使用场景：公告发布时指定目标范围</p>
         */
        public static final int TARGET_TYPE_ALL = 1;

        /**
         * 公告目标类型：指定租户
         * <p>取值范围：1-全员，2-指定租户，3-指定用户</p>
         * <p>使用场景：公告发布时指定目标范围</p>
         */
        public static final int TARGET_TYPE_TENANT = 2;

        /**
         * 公告目标类型：指定用户
         * <p>取值范围：1-全员，2-指定租户，3-指定用户</p>
         * <p>使用场景：公告发布时指定目标范围</p>
         */
        public static final int TARGET_TYPE_USER = 3;

        /**
         * 阅读状态：未读
         * <p>取值范围：0-未读，1-已读</p>
         * <p>使用场景：公告阅读状态管理</p>
         */
        public static final int READ_STATUS_UNREAD = 0;

        /**
         * 阅读状态：已读
         * <p>取值范围：0-未读，1-已读</p>
         * <p>使用场景：公告阅读状态管理</p>
         */
        public static final int READ_STATUS_READ = 1;

    }

    /**
     *  审计日志相关常量 
     */
    public static final class Audit {
        
        private Audit() {
        }

        /**
         * 登录状态：成功
         * <p>取值范围：0-失败，1-成功</p>
         * <p>使用场景：登录日志记录时标记登录结果</p>
         */
        public static final int LOGIN_STATUS_SUCCESS = 1;

        /**
         * 登录状态：失败
         * <p>取值范围：0-失败，1-成功</p>
         * <p>使用场景：登录日志记录时标记登录结果</p>
         */
        public static final int LOGIN_STATUS_FAIL = 0;

        /**
         * 数据审计操作类型：新增
         * <p>取值范围：1-新增，2-删除，3-更新</p>
         * <p>使用场景：数据变更审计时记录操作类型</p>
         */
        public static final int AUDIT_TYPE_INSERT = 1;

        /**
         * 数据审计操作类型：删除
         * <p>取值范围：1-新增，2-删除，3-更新</p>
         * <p>使用场景：数据变更审计时记录操作类型</p>
         */
        public static final int AUDIT_TYPE_DELETE = 2;

        /**
         * 数据审计操作类型：更新
         * <p>取值范围：1-新增，2-删除，3-更新</p>
         * <p>使用场景：数据变更审计时记录操作类型</p>
         */
        public static final int AUDIT_TYPE_UPDATE = 3;

    }

    /**
     *  数据源相关常量 
     */
    public static final class Datasource {
        
        private Datasource() {
        }

        /**
         * 数据源类型：业务表
         * <p>取值范围：1-业务表，2-API接口，3-字典，4-SQL查询</p>
         * <p>使用场景：动态数据源配置时区分数据来源类型</p>
         */
        public static final int DATASOURCE_TYPE_TABLE = 1;

        /**
         * 数据源类型：API接口
         * <p>取值范围：1-业务表，2-API接口，3-字典，4-SQL查询</p>
         * <p>使用场景：动态数据源配置时区分数据来源类型</p>
         */
        public static final int DATASOURCE_TYPE_API = 2;

        /**
         * 数据源类型：字典
         * <p>取值范围：1-业务表，2-API接口，3-字典，4-SQL查询</p>
         * <p>使用场景：动态数据源配置时区分数据来源类型</p>
         */
        public static final int DATASOURCE_TYPE_DICT = 3;

        /**
         * 数据源类型：SQL查询
         * <p>取值范围：1-业务表，2-API接口，3-字典，4-SQL查询</p>
         * <p>使用场景：动态数据源配置时区分数据来源类型</p>
         */
        public static final int DATASOURCE_TYPE_SQL = 4;

    }

    /**
     *  打印模板相关常量
     *  <p> TODO 后续重新定义这部分常量 根据业务需求 </p>
     */
    public static final class PrintTemplate {
        
        private PrintTemplate() {
        }

        /**
         * 模板类型：HTML
         * <p>取值范围：1-HTML，2-Markdown，3-JSON配置</p>
         * <p>使用场景：打印模板管理时区分模板格式</p>
         */
        public static final int TEMPLATE_TYPE_HTML = 1;

        /**
         * 模板类型：Markdown
         * <p>取值范围：1-HTML，2-Markdown，3-JSON配置</p>
         * <p>使用场景：打印模板管理时区分模板格式</p>
         */
        public static final int TEMPLATE_TYPE_MARKDOWN = 2;

        /**
         * 模板类型：JSON配置
         * <p>取值范围：1-HTML，2-Markdown，3-JSON配置</p>
         * <p>使用场景：打印模板管理时区分模板格式</p>
         */
        public static final int TEMPLATE_TYPE_JSON = 3;

        /**
         * 纸张方向：纵向
         * <p>取值范围：portrait-纵向，landscape-横向</p>
         * <p>使用场景：打印模板管理时指定纸张方向</p>
         */
        public static final String PAPER_ORIENTATION_PORTRAIT = "portrait";

        /**
         * 纸张方向：横向
         * <p>取值范围：portrait-纵向，landscape-横向</p>
         * <p>使用场景：打印模板管理时指定纸张方向</p>
         */
        public static final String PAPER_ORIENTATION_LANDSCAPE = "landscape";

    }

    /**
     *  用户组相关常量
     *  <p> TODO 后续重新定义这部分常量 根据业务需求 </p>
     */
    public static final class UserGroup {
        
        private UserGroup() {
        }

        /**
         * 组类型：静态组
         * <p>取值范围：1-静态组，2-动态组</p>
         * <p>使用场景：用户组管理时区分组类型，静态组需手动添加成员</p>
         */
        public static final int GROUP_TYPE_STATIC = 1;

        /**
         * 组类型：动态组
         * <p>取值范围：1-静态组，2-动态组</p>
         * <p>使用场景：用户组管理时区分组类型，动态组根据规则自动匹配成员</p>
         */
        public static final int GROUP_TYPE_DYNAMIC = 2;

    }

    /**
     *  安全策略相关常量 
     */
    public static final class Security {
        
        private Security() {
        }

        /**
         * 密码复杂度：无要求
         * <p>取值范围：0-无要求，1-字母+数字，2-字母+数字+特殊字符</p>
         * <p>使用场景：租户安全策略配置时指定密码复杂度要求</p>
         */
        public static final int PWD_COMPLEXITY_NONE = 0;

        /**
         * 密码复杂度：字母+数字
         * <p>取值范围：0-无要求，1-字母+数字，2-字母+数字+特殊字符</p>
         * <p>使用场景：租户安全策略配置时指定密码复杂度要求</p>
         */
        public static final int PWD_COMPLEXITY_LETTER_NUMBER = 1;

        /**
         * 密码复杂度：字母+数字+特殊字符
         * <p>取值范围：0-无要求，1-字母+数字，2-字母+数字+特殊字符</p>
         * <p>使用场景：租户安全策略配置时指定密码复杂度要求</p>
         */
        public static final int PWD_COMPLEXITY_STRONG = 2;

        /**
         * 默认密码最小长度
         * <p>取值范围：正整数，建议6-20之间</p>
         * <p>使用场景：租户安全策略配置时未指定密码长度时的默认值</p>
         */
        public static final int DEFAULT_PWD_MIN_LENGTH = 6;

        /**
         * 默认登录失败锁定次数
         * <p>取值范围：正整数，建议3-10之间</p>
         * <p>使用场景：租户安全策略配置时未指定锁定次数时的默认值</p>
         */
        public static final int DEFAULT_LOGIN_FAIL_LOCK_COUNT = 5;

        /**
         * 默认锁定时长（分钟）
         * <p>取值范围：正整数，建议5-60之间</p>
         * <p>使用场景：租户安全策略配置时未指定锁定时长时的默认值</p>
         */
        public static final int DEFAULT_LOCK_DURATION_MINUTES = 30;

        /**
         * 密码永不过期
         * <p>取值范围：0表示永不过期，正整数表示过期天数</p>
         * <p>使用场景：租户安全策略配置时指定密码过期策略</p>
         */
        public static final int PWD_NEVER_EXPIRE = 0;

    }

    /**
     *  Token相关常量 
     */
    public static final class Token {
        
        private Token() {
        }

        /**
         * Token状态：有效
         * <p>取值范围：0-无效，1-有效</p>
         * <p>使用场景：用户Token状态管理</p>
         */
        public static final int TOKEN_STATUS_VALID = 1;

        /**
         * Token状态：无效
         * <p>取值范围：0-无效，1-有效</p>
         * <p>使用场景：用户Token状态管理，强制下线时将Token置为无效</p>
         */
        public static final int TOKEN_STATUS_INVALID = 0;

    }

    /**
     *  Sa-Token Session键常量
     */
    public static final class Session {

        private Session() {
        }

        /**
         * Session键：租户ID
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户ID</p>
         */
        public static final String TENANT_ID = "tenantId";

        /**
         * Session键：租户名称
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户名称</p>
         */
        public static final String TENANT_NAME = "tenantName";

        /**
         * Session键：用户名称
         * <p>使用场景：Sa-Token Session中存储当前用户的昵称</p>
         */
        public static final String USER_NAME = "userName";

    }

    /**
     *  Redis键常量
     */
    public static final class RedisKey {

        private RedisKey() {
        }

        /**
         * Redis键前缀：登录会话
         * <p>完整格式：nexusix:login:session:{userId}</p>
         * <p>使用场景：存储用户登录会话上下文数据（租户信息、用户信息等）</p>
         */
        public static final String LOGIN_SESSION_PREFIX = "nexusix:login:session:";

        /**
         * Redis键前缀：用户权限
         * <p>完整格式：nexusix:perm:{userId}</p>
         * <p>使用场景：缓存用户权限编码列表</p>
         */
        public static final String PERM_PREFIX = "nexusix:perm:";

        /**
         * Redis键前缀：用户角色
         * <p>完整格式：nexusix:role:{userId}</p>
         * <p>使用场景：缓存用户角色编码列表</p>
         */
        public static final String ROLE_PREFIX = "nexusix:role:";

        /**
         * Redis键前缀：租户上下文
         * <p>完整格式：nexusix:tenant:context:{userId}</p>
         * <p>使用场景：存储用户当前租户上下文信息（租户ID、租户名称等）</p>
         */
        public static final String TENANT_CONTEXT_PREFIX = "nexusix:tenant:context:";

    }

    /**
     *  角色编码常量
     */
    public static final class RoleCode {

        private RoleCode() {
        }

        /**
         * 系统管理员角色编码
         * <p>使用场景：判断用户是否为系统级管理员</p>
         */
        public static final String SYSTEM_ADMIN = "system_admin";

        /**
         * 租户管理员角色编码
         * <p>使用场景：判断用户是否为租户级管理员</p>
         */
        public static final String TENANT_ADMIN = "tenant_admin";

    }
}
