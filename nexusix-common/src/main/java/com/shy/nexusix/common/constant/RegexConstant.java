package com.shy.nexusix.common.constant;

/**
 * <p>
 * 正则表达式常量定义
 * </p>
 * <p>
 * 该类定义了系统中常用的正则表达式常量，包括手机号、邮箱、身份证号、
 * 用户名、密码、IP地址、URL等常用格式验证正则。所有常量均采用全大写字母命名，
 * 单词间用下划线分隔，符合Java编码规范。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public class RegexConstant {

    private RegexConstant() {
    }

    /**
     *  手机号相关正则 
     */
    public static final class Phone {
        
        private Phone() {
        }

        /**
         * 中国大陆手机号正则
         * <p>正则规则：以1开头，第二位为3-9，共11位数字</p>
         * <p>使用场景：用户注册、登录、修改手机号时的格式验证</p>
         * <p>示例：13800138000、15912345678</p>
         */
        public static final String CHINA_MOBILE = "^1[3-9]\\d{9}$";

        /**
         * 国际手机号正则（通用）
         * <p>正则规则：支持国际区号，手机号为6-15位数字</p>
         * <p>使用场景：国际用户注册时的手机号格式验证</p>
         * <p>示例：+8613800138000、+12125551234</p>
         */
        public static final String INTERNATIONAL_MOBILE = "^\\+[1-9]\\d{5,14}$";

        /**
         * 固定电话正则
         * <p>正则规则：支持区号3-4位，电话号7-8位，可选分机号</p>
         * <p>使用场景：企业联系电话、传真号码验证</p>
         * <p>示例：010-12345678、0755-87654321-123</p>
         */
        public static final String LANDLINE = "^(0\\d{2,3}-?)?\\d{7,8}(-\\d{1,4})?$";

    }

    /**
     *  邮箱相关正则 
     */
    public static final class Email {
        
        private Email() {
        }

        /**
         * 邮箱正则（通用）
         * <p>正则规则：符合RFC 5322标准的邮箱格式</p>
         * <p>使用场景：用户注册、找回密码、修改邮箱时的格式验证</p>
         * <p>示例：user@example.com、user.name@sub.domain.co.uk</p>
         */
        public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        /**
         * 邮箱正则（严格）
         * <p>正则规则：更严格的邮箱格式验证，限制域名后缀长度</p>
         * <p>使用场景：需要更严格邮箱格式验证的场景</p>
         * <p>示例：user@example.com、user.name@company.cn</p>
         */
        public static final String EMAIL_STRICT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    }

    /**
     *  身份证号相关正则 
     */
    public static final class IdCard {
        
        private IdCard() {
        }

        /**
         * 18位身份证号正则
         * <p>正则规则：6位地区码 + 8位出生日期 + 3位顺序码 + 1位校验码</p>
         * <p>使用场景：用户实名认证、身份信息验证</p>
         * <p>示例：110101199001011234</p>
         */
        public static final String ID_CARD_18 = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$";

        /**
         * 15位身份证号正则（旧版）
         * <p>正则规则：6位地区码 + 6位出生日期 + 3位顺序码</p>
         * <p>使用场景：兼容旧版身份证号验证</p>
         * <p>示例：110101900101123</p>
         */
        public static final String ID_CARD_15 = "^[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}$";

        /**
         * 身份证号正则（15位或18位）
         * <p>正则规则：支持15位和18位身份证号</p>
         * <p>使用场景：通用身份证号验证</p>
         * <p>示例：110101199001011234、110101900101123</p>
         */
        public static final String ID_CARD = "^([1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]|[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3})$";

    }

    /**
     *  用户名相关正则 
     */
    public static final class Username {
        
        private Username() {
        }

        /**
         * 用户名正则（字母开头）
         * <p>正则规则：字母开头，允许字母、数字、下划线，长度4-20位</p>
         * <p>使用场景：用户注册时的用户名格式验证</p>
         * <p>示例：admin、user123、zhang_san</p>
         */
        public static final String USERNAME_LETTER_START = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

        /**
         * 用户名正则（字母或数字开头）
         * <p>正则规则：字母或数字开头，允许字母、数字、下划线，长度4-20位</p>
         * <p>使用场景：更灵活的用户名格式验证</p>
         * <p>示例：admin、123user、zhang_san</p>
         */
        public static final String USERNAME_ALPHANUMERIC_START = "^[a-zA-Z0-9][a-zA-Z0-9_]{3,19}$";

        /**
         * 用户名正则（中文、字母、数字、下划线）
         * <p>正则规则：支持中文、字母、数字、下划线，长度2-20位</p>
         * <p>使用场景：支持中文用户名的系统</p>
         * <p>示例：张三、admin、用户123</p>
         */
        public static final String USERNAME_WITH_CHINESE = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$";

    }

    /**
     *  密码相关正则 
     */
    public static final class Password {
        
        private Password() {
        }

        /**
         * 密码正则（简单）
         * <p>正则规则：6-20位任意字符</p>
         * <p>使用场景：密码复杂度要求较低的系统</p>
         * <p>示例：123456、password</p>
         */
        public static final String PASSWORD_SIMPLE = "^.{6,20}$";

        /**
         * 密码正则（中等强度）
         * <p>正则规则：6-20位，必须包含字母和数字</p>
         * <p>使用场景：中等密码复杂度要求的系统</p>
         * <p>示例：abc123、password123</p>
         */
        public static final String PASSWORD_MEDIUM = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

        /**
         * 密码正则（高强度）
         * <p>正则规则：8-20位，必须包含字母、数字和特殊字符</p>
         * <p>使用场景：高安全要求的系统</p>
         * <p>示例：abc123!@#、Pass@word123</p>
         */
        public static final String PASSWORD_STRONG = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

        /**
         * 密码正则（包含大小写字母、数字和特殊字符）
         * <p>正则规则：8-20位，必须包含大小写字母、数字和特殊字符</p>
         * <p>使用场景：最高安全要求的系统</p>
         * <p>示例：PassWord123!@、Admin@2024</p>
         */
        public static final String PASSWORD_VERY_STRONG = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

    }

    /**
     *  IP地址相关正则 
     */
    public static final class IpAddress {
        
        private IpAddress() {
        }

        /**
         * IPv4地址正则
         * <p>正则规则：标准IPv4格式，4段0-255的数字，用点分隔</p>
         * <p>使用场景：IP地址验证、访问控制</p>
         * <p>示例：192.168.1.1、10.0.0.1</p>
         */
        public static final String IPV4 = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        /**
         * IPv6地址正则
         * <p>正则规则：标准IPv6格式，支持完整格式和缩写格式</p>
         * <p>使用场景：IPv6地址验证</p>
         * <p>示例：2001:0db8:85a3:0000:0000:8a2e:0370:7334、::1</p>
         */
        public static final String IPV6 = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^:((:[0-9a-fA-F]{1,4}){1,7}|:)$|^[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})$|^([0-9a-fA-F]{1,4}:){2}((:[0-9a-fA-F]{1,4}){1,5})$|^([0-9a-fA-F]{1,4}:){3}((:[0-9a-fA-F]{1,4}){1,4})$|^([0-9a-fA-F]{1,4}:){4}((:[0-9a-fA-F]{1,4}){1,3})$|^([0-9a-fA-F]{1,4}:){5}((:[0-9a-fA-F]{1,4}){1,2})$|^([0-9a-fA-F]{1,4}:){6}(:[0-9a-fA-F]{1,4}){1,2}$|^([0-9a-fA-F]{1,4}:){7}:$";

        /**
         * IP地址正则（IPv4或IPv6）
         * <p>正则规则：支持IPv4和IPv6两种格式</p>
         * <p>使用场景：通用IP地址验证</p>
         * <p>示例：192.168.1.1、2001:0db8:85a3::8a2e:0370:7334</p>
         */
        public static final String IP = "^(" + IPV4.substring(1, IPV4.length() - 1) + "|" + IPV6.substring(1, IPV6.length() - 1) + ")$";

        /**
         * IP地址段正则（CIDR格式）
         * <p>正则规则：IPv4地址/掩码格式</p>
         * <p>使用场景：IP地址段配置、访问控制列表</p>
         * <p>示例：192.168.1.0/24、10.0.0.0/8</p>
         */
        public static final String IPV4_CIDR = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)/(\\d|[1-2]\\d|3[0-2])$";

    }

    /**
     *  URL相关正则 
     */
    public static final class Url {
        
        private Url() {
        }

        /**
         * URL正则（通用）
         * <p>正则规则：支持http、https、ftp协议</p>
         * <p>使用场景：URL格式验证、链接校验</p>
         * <p>示例：https://www.example.com、http://localhost:8080</p>
         */
        public static final String URL = "^(https?|ftp):\\/\\/([^:\\/\\s]+)(:[0-9]+)?(\\/[^\\s]*)?$";

        /**
         * URL正则（严格）
         * <p>正则规则：更严格的URL格式验证，包含域名、端口、路径等</p>
         * <p>使用场景：需要严格URL格式验证的场景</p>
         * <p>示例：https://www.example.com/path?query=value#anchor</p>
         */
        public static final String URL_STRICT = "^(https?|ftp):\\/\\/((([a-zA-Z0-9]\\.|[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,})|localhost|((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?))(:[0-9]+)?(\\/[a-zA-Z0-9-._~!$&'()*+,;=:@%]*)?(\\?[a-zA-Z0-9-._~!$&'()*+,;=:@%\\/?]*)?(#[a-zA-Z0-9-._~!$&'()*+,;=:@%\\/?]*)?$";

        /**
         * 域名正则
         * <p>正则规则：标准域名格式，支持多级域名</p>
         * <p>使用场景：域名验证、网站配置</p>
         * <p>示例：www.example.com、example.cn</p>
         */
        public static final String DOMAIN = "^([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";

        /**
         * HTTP URL正则
         * <p>正则规则：仅支持http和https协议</p>
         * <p>使用场景：HTTP URL验证</p>
         * <p>示例：http://www.example.com、https://example.com</p>
         */
        public static final String HTTP_URL = "^https?:\\/\\/([^:\\/\\s]+)(:[0-9]+)?(\\/[^\\s]*)?$";

    }

    /**
     *  数字相关正则 
     */
    public static final class Number {
        
        private Number() {
        }

        /**
         * 整数正则
         * <p>正则规则：正整数、负整数、零</p>
         * <p>使用场景：整数格式验证</p>
         * <p>示例：123、-456、0</p>
         */
        public static final String INTEGER = "^-?\\d+$";

        /**
         * 正整数正则
         * <p>正则规则：大于零的整数</p>
         * <p>使用场景：数量、次数等必须为正整数的场景</p>
         * <p>示例：1、123、999</p>
         */
        public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

        /**
         * 非负整数正则
         * <p>正则规则：零和正整数</p>
         * <p>使用场景：数量、次数等可以为零的场景</p>
         * <p>示例：0、1、123</p>
         */
        public static final String NON_NEGATIVE_INTEGER = "^\\d+$";

        /**
         * 小数正则
         * <p>正则规则：整数部分和小数部分，支持正负号</p>
         * <p>使用场景：金额、价格等小数验证</p>
         * <p>示例：123.45、-0.5、3.14</p>
         */
        public static final String DECIMAL = "^-?\\d+\\.\\d+$";

        /**
         * 金额正则（最多两位小数）
         * <p>正则规则：整数或最多两位小数，支持正负号</p>
         * <p>使用场景：金额、价格验证</p>
         * <p>示例：100、99.99、-10.5</p>
         */
        public static final String MONEY = "^-?\\d+(\\.\\d{1,2})?$";

        /**
         * 正数正则
         * <p>正则规则：大于零的整数或小数</p>
         * <p>使用场景：必须为正数的场景</p>
         * <p>示例：1、0.5、123.45</p>
         */
        public static final String POSITIVE_NUMBER = "^[+]?\\d+(\\.\\d+)?$";

        /**
         * 百分比正则
         * <p>正则规则：0-100之间的整数或小数</p>
         * <p>使用场景：百分比验证</p>
         * <p>示例：0、50、99.99、100</p>
         */
        public static final String PERCENTAGE = "^(100(\\.0{1,2})?|\\d{1,2}(\\.\\d{1,2})?)$";

    }

    /**
     *  字符相关正则 
     */
    public static final class Character {
        
        private Character() {
        }

        /**
         * 纯字母正则
         * <p>正则规则：仅包含大小写字母</p>
         * <p>使用场景：编码、标识符验证</p>
         * <p>示例：abc、ABC、AbCdEf</p>
         */
        public static final String LETTERS = "^[a-zA-Z]+$";

        /**
         * 纯数字正则
         * <p>正则规则：仅包含数字</p>
         * <p>使用场景：验证码、编号验证</p>
         * <p>示例：123、000、999</p>
         */
        public static final String DIGITS = "^\\d+$";

        /**
         * 字母和数字正则
         * <p>正则规则：仅包含字母和数字</p>
         * <p>使用场景：编码、标识符验证</p>
         * <p>示例：abc123、ABC123、a1b2c3</p>
         */
        public static final String ALPHANUMERIC = "^[a-zA-Z0-9]+$";

        /**
         * 中文字符正则
         * <p>正则规则：仅包含中文字符</p>
         * <p>使用场景：中文姓名、中文内容验证</p>
         * <p>示例：张三、李四、王五</p>
         */
        public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

        /**
         * 中文、字母、数字正则
         * <p>正则规则：包含中文、字母、数字</p>
         * <p>使用场景：名称、标题验证</p>
         * <p>示例：张三123、abc李四、用户A</p>
         */
        public static final String CHINESE_ALPHANUMERIC = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

        /**
         * 邮政编码正则（中国）
         * <p>正则规则：6位数字</p>
         * <p>使用场景：邮政编码验证</p>
         * <p>示例：100000、510000</p>
         */
        public static final String POSTAL_CODE = "^[1-9]\\d{5}$";

        /**
         * 车牌号正则（中国）
         * <p>正则规则：标准车牌号格式，支持新能源车牌</p>
         * <p>使用场景：车牌号验证</p>
         * <p>示例：京A12345、粤B12345D</p>
         */
        public static final String LICENSE_PLATE = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$";

        /**
         * 统一社会信用代码正则
         * <p>正则规则：18位统一社会信用代码</p>
         * <p>使用场景：企业统一社会信用代码验证</p>
         * <p>示例：91110108MA01ABCD12</p>
         */
        public static final String SOCIAL_CREDIT_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";

    }

    /**
     *  日期时间相关正则 
     */
    public static final class DateTime {
        
        private DateTime() {
        }

        /**
         * 日期正则（yyyy-MM-dd）
         * <p>正则规则：标准日期格式</p>
         * <p>使用场景：日期格式验证</p>
         * <p>示例：2024-01-01、1999-12-31</p>
         */
        public static final String DATE = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

        /**
         * 时间正则（HH:mm:ss）
         * <p>正则规则：标准时间格式</p>
         * <p>使用场景：时间格式验证</p>
         * <p>示例：12:30:45、23:59:59</p>
         */
        public static final String TIME = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

        /**
         * 日期时间正则（yyyy-MM-dd HH:mm:ss）
         * <p>正则规则：标准日期时间格式</p>
         * <p>使用场景：日期时间格式验证</p>
         * <p>示例：2024-01-01 12:30:45</p>
         */
        public static final String DATETIME = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

        /**
         * 年月正则（yyyy-MM）
         * <p>正则规则：年月格式</p>
         * <p>使用场景：月份、账期验证</p>
         * <p>示例：2024-01、1999-12</p>
         */
        public static final String YEAR_MONTH = "^\\d{4}-(0[1-9]|1[0-2])$";

        /**
         * 时间戳正则（毫秒）
         * <p>正则规则：13位数字时间戳</p>
         * <p>使用场景：时间戳验证</p>
         * <p>示例：1704067200000</p>
         */
        public static final String TIMESTAMP_MILLIS = "^\\d{13}$";

        /**
         * 时间戳正则（秒）
         * <p>正则规则：10位数字时间戳</p>
         * <p>使用场景：时间戳验证</p>
         * <p>示例：1704067200</p>
         */
        public static final String TIMESTAMP_SECONDS = "^\\d{10}$";

    }

    /**
     *  文件相关正则 
     */
    public static final class File {
        
        private File() {
        }

        /**
         * 图片文件扩展名正则
         * <p>正则规则：常见图片格式扩展名</p>
         * <p>使用场景：图片上传验证</p>
         * <p>示例：.jpg、.png、.gif</p>
         */
        public static final String IMAGE_EXTENSION = "\\.(jpg|jpeg|png|gif|bmp|webp|svg)$";

        /**
         * 文档文件扩展名正则
         * <p>正则规则：常见文档格式扩展名</p>
         * <p>使用场景：文档上传验证</p>
         * <p>示例：.doc、.docx、.pdf、.txt</p>
         */
        public static final String DOCUMENT_EXTENSION = "\\.(doc|docx|pdf|txt|xls|xlsx|ppt|pptx)$";

        /**
         * 视频文件扩展名正则
         * <p>正则规则：常见视频格式扩展名</p>
         * <p>使用场景：视频上传验证</p>
         * <p>示例：.mp4、.avi、.mov</p>
         */
        public static final String VIDEO_EXTENSION = "\\.(mp4|avi|mov|wmv|flv|mkv|webm)$";

        /**
         * 音频文件扩展名正则
         * <p>正则规则：常见音频格式扩展名</p>
         * <p>使用场景：音频上传验证</p>
         * <p>示例：.mp3、.wav、.flac</p>
         */
        public static final String AUDIO_EXTENSION = "\\.(mp3|wav|flac|aac|ogg|wma)$";

        /**
         * 压缩文件扩展名正则
         * <p>正则规则：常见压缩格式扩展名</p>
         * <p>使用场景：压缩文件上传验证</p>
         * <p>示例：.zip、.rar、.7z</p>
         */
        public static final String ARCHIVE_EXTENSION = "\\.(zip|rar|7z|tar|gz)$";

        /**
         * 文件名正则（不允许特殊字符）
         * <p>正则规则：文件名不允许包含特殊字符</p>
         * <p>使用场景：文件名验证</p>
         * <p>示例：document.pdf、report_2024.xlsx</p>
         */
        public static final String FILENAME = "^[^\\\\/:*?\"<>|]+$";

    }

    /**
     *  编码相关正则 
     */
    public static final class Code {
        
        private Code() {
        }

        /**
         * UUID正则
         * <p>正则规则：标准UUID格式，支持带连字符和不带连字符</p>
         * <p>使用场景：UUID验证</p>
         * <p>示例：550e8400-e29b-41d4-a716-446655440000</p>
         */
        public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        /**
         * UUID正则（不带连字符）
         * <p>正则规则：32位十六进制字符串</p>
         * <p>使用场景：UUID验证（紧凑格式）</p>
         * <p>示例：550e8400e29b41d4a716446655440000</p>
         */
        public static final String UUID_COMPACT = "^[0-9a-fA-F]{32}$";

        /**
         * 颜色代码正则（十六进制）
         * <p>正则规则：6位或3位十六进制颜色代码</p>
         * <p>使用场景：颜色代码验证</p>
         * <p>示例：#FF0000、#F00</p>
         */
        public static final String COLOR_HEX = "^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$";

        /**
         * 雪花ID正则
         * <p>正则规则：19位数字</p>
         * <p>使用场景：雪花算法生成的ID验证</p>
         * <p>示例：1234567890123456789</p>
         */
        public static final String SNOWFLAKE_ID = "^\\d{19}$";

        /**
         * 订单号正则
         * <p>正则规则：字母或数字，长度10-32位</p>
         * <p>使用场景：订单号验证</p>
         * <p>示例：ORD20240101123456</p>
         */
        public static final String ORDER_NO = "^[a-zA-Z0-9]{10,32}$";

        /**
         * 验证码正则（数字）
         * <p>正则规则：4-8位数字</p>
         * <p>使用场景：短信验证码、邮箱验证码验证</p>
         * <p>示例：1234、123456</p>
         */
        public static final String VERIFICATION_CODE = "^\\d{4,8}$";

    }

    /**
     *  公共常用正则
     */
    public static final class Common {
        
        private Common() {
        }

        /**
         * 非空字符串正则
         * <p>正则规则：至少包含一个非空白字符</p>
         * <p>使用场景：必填字段验证</p>
         * <p>示例：abc、123、张三</p>
         */
        public static final String NOT_EMPTY = "^\\S+$";

        /**
         * 空白字符串正则
         * <p>正则规则：仅包含空白字符或为空</p>
         * <p>使用场景：空值判断</p>
         */
        public static final String BLANK = "^\\s*$";

        /**
         * JSON字符串正则
         * <p>正则规则：以{开头}结尾或以[开头]结尾</p>
         * <p>使用场景：JSON格式验证</p>
         * <p>示例：{"key":"value"}、[1,2,3]</p>
         */
        public static final String JSON = "^(\\{.*\\}|\\[.*\\])$";

        /**
         * HTML标签正则
         * <p>正则规则：匹配HTML标签</p>
         * <p>使用场景：HTML标签过滤、XSS防护</p>
         */
        public static final String HTML_TAG = "<[^>]+>";

        /**
         * 中文姓名正则
         * <p>正则规则：2-20个中文字符，支持少数民族姓名中的点</p>
         * <p>使用场景：中文姓名验证</p>
         * <p>示例：张三、买买提·艾力</p>
         */
        public static final String CHINESE_NAME = "^[\\u4e00-\\u9fa5·]{2,20}$";

        /**
         * 英文姓名正则
         * <p>正则规则：字母、空格、点、连字符，2-50个字符</p>
         * <p>使用场景：英文姓名验证</p>
         * <p>示例：John Smith、Mary-Jane O'Connor</p>
         */
        public static final String ENGLISH_NAME = "^[a-zA-Z\\s.'-]{2,50}$";

        /**
         * QQ号正则
         * <p>正则规则：5-11位数字，首位不为0</p>
         * <p>使用场景：QQ号验证</p>
         * <p>示例：12345、123456789</p>
         */
        public static final String QQ = "^[1-9]\\d{4,10}$";

        /**
         * 微信号正则
         * <p>正则规则：字母开头，6-20位字母、数字、下划线、减号</p>
         * <p>使用场景：微信号验证</p>
         * <p>示例：wechat_id、user-123</p>
         */
        public static final String WECHAT = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$";

        /**
         * 银行卡号正则
         * <p>正则规则：16-19位数字</p>
         * <p>使用场景：银行卡号验证</p>
         * <p>示例：6222021234567890123</p>
         */
        public static final String BANK_CARD = "^\\d{16,19}$";

        /**
         * Base64字符串正则
         * <p>正则规则：Base64编码格式</p>
         * <p>使用场景：Base64编码验证</p>
         */
        public static final String BASE64 = "^[A-Za-z0-9+/]*={0,2}$";

    }
}
