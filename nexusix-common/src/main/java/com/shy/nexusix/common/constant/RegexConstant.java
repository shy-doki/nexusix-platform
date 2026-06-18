package com.shy.nexusix.common.constant;

/**
 * <p>正则表达式常量定义 - 手机号、邮箱、身份证、用户名、密码、IP、URL等</p>
 *
 * @author shy
 */
public class RegexConstant {

    private RegexConstant() {
    }

    // 手机号相关正则
    public static final class Phone {

        private Phone() {
        }

        // 中国大陆手机号（1开头，第二位3-9，共11位）
        public static final String CHINA_MOBILE = "^1[3-9]\\d{9}$";

        // 国际手机号（支持国际区号，6-15位数字）
        public static final String INTERNATIONAL_MOBILE = "^\\+[1-9]\\d{5,14}$";

        // 固定电话（支持区号和分机号）
        public static final String LANDLINE = "^(0\\d{2,3}-?)?\\d{7,8}(-\\d{1,4})?$";

    }

    // 邮箱相关正则
    public static final class Email {

        private Email() {
        }

        // 邮箱（通用）
        public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // 邮箱（严格，限制域名后缀长度2-6）
        public static final String EMAIL_STRICT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    }

    // 身份证号相关正则
    public static final class IdCard {

        private IdCard() {
        }

        // 18位身份证号
        public static final String ID_CARD_18 = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$";

        // 15位身份证号（旧版）
        public static final String ID_CARD_15 = "^[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}$";

        // 身份证号（15位或18位）
        public static final String ID_CARD = "^([1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]|[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3})$";

    }

    // 用户名相关正则
    public static final class Username {

        private Username() {
        }

        // 用户名（字母开头，4-20位）
        public static final String USERNAME_LETTER_START = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

        // 用户名（字母或数字开头，4-20位）
        public static final String USERNAME_ALPHANUMERIC_START = "^[a-zA-Z0-9][a-zA-Z0-9_]{3,19}$";

        // 用户名（中文、字母、数字、下划线，2-20位）
        public static final String USERNAME_WITH_CHINESE = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$";

    }

    // 密码相关正则
    public static final class Password {

        private Password() {
        }

        // 密码（简单，6-20位任意字符）
        public static final String PASSWORD_SIMPLE = "^.{6,20}$";

        // 密码（中等，6-20位，必须含字母和数字）
        public static final String PASSWORD_MEDIUM = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

        // 密码（强，8-20位，必须含字母、数字和特殊字符）
        public static final String PASSWORD_STRONG = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

        // 密码（极强，8-20位，必须含大小写字母、数字和特殊字符）
        public static final String PASSWORD_VERY_STRONG = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

    }

    // IP地址相关正则
    public static final class IpAddress {

        private IpAddress() {
        }

        // IPv4地址
        public static final String IPV4 = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        // IPv6地址（支持完整格式和缩写格式）
        public static final String IPV6 = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^:((:[0-9a-fA-F]{1,4}){1,7}|:)$|^[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})$|^([0-9a-fA-F]{1,4}:){2}((:[0-9a-fA-F]{1,4}){1,5})$|^([0-9a-fA-F]{1,4}:){3}((:[0-9a-fA-F]{1,4}){1,4})$|^([0-9a-fA-F]{1,4}:){4}((:[0-9a-fA-F]{1,4}){1,3})$|^([0-9a-fA-F]{1,4}:){5}((:[0-9a-fA-F]{1,4}){1,2})$|^([0-9a-fA-F]{1,4}:){6}(:[0-9a-fA-F]{1,4}){1,2}$|^([0-9a-fA-F]{1,4}:){7}:$";

        // IP地址（IPv4或IPv6）
        public static final String IP = "^(" + IPV4.substring(1, IPV4.length() - 1) + "|" + IPV6.substring(1, IPV6.length() - 1) + ")$";

        // IPv4地址段（CIDR格式）
        public static final String IPV4_CIDR = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)/(\\d|[1-2]\\d|3[0-2])$";

    }

    // URL相关正则
    public static final class Url {

        private Url() {
        }

        // URL（通用，支持http/https/ftp）
        public static final String URL = "^(https?|ftp):\\/\\/([^:\\/\\s]+)(:[0-9]+)?(\\/[^\\s]*)?$";

        // URL（严格，含域名、端口、路径、查询参数、锚点）
        public static final String URL_STRICT = "^(https?|ftp):\\/\\/((([a-zA-Z0-9]\\.|[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,})|localhost|((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?))(:[0-9]+)?(\\/[a-zA-Z0-9-._~!$&'()*+,;=:@%]*)?(\\?[a-zA-Z0-9-._~!$&'()*+,;=:@%\\/?]*)?(#[a-zA-Z0-9-._~!$&'()*+,;=:@%\\/?]*)?$";

        // 域名
        public static final String DOMAIN = "^([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";

        // HTTP URL（仅http/https）
        public static final String HTTP_URL = "^https?:\\/\\/([^:\\/\\s]+)(:[0-9]+)?(\\/[^\\s]*)?$";

    }

    // 数字相关正则
    public static final class Number {

        private Number() {
        }

        // 整数（正/负/零）
        public static final String INTEGER = "^-?\\d+$";

        // 正整数
        public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

        // 非负整数
        public static final String NON_NEGATIVE_INTEGER = "^\\d+$";

        // 小数（支持正负号）
        public static final String DECIMAL = "^-?\\d+\\.\\d+$";

        // 金额（最多两位小数）
        public static final String MONEY = "^-?\\d+(\\.\\d{1,2})?$";

        // 正数（整数或小数）
        public static final String POSITIVE_NUMBER = "^[+]?\\d+(\\.\\d+)?$";

        // 百分比（0-100，最多两位小数）
        public static final String PERCENTAGE = "^(100(\\.0{1,2})?|\\d{1,2}(\\.\\d{1,2})?)$";

    }

    // 字符相关正则
    public static final class Character {

        private Character() {
        }

        // 纯字母
        public static final String LETTERS = "^[a-zA-Z]+$";

        // 纯数字
        public static final String DIGITS = "^\\d+$";

        // 字母和数字
        public static final String ALPHANUMERIC = "^[a-zA-Z0-9]+$";

        // 中文字符
        public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

        // 中文、字母、数字
        public static final String CHINESE_ALPHANUMERIC = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

        // 邮政编码（中国，6位数字）
        public static final String POSTAL_CODE = "^[1-9]\\d{5}$";

        // 车牌号（支持新能源）
        public static final String LICENSE_PLATE = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$";

        // 统一社会信用代码（18位）
        public static final String SOCIAL_CREDIT_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";

    }

    // 日期时间相关正则
    public static final class DateTime {

        private DateTime() {
        }

        // 日期（yyyy-MM-dd）
        public static final String DATE = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

        // 时间（HH:mm:ss）
        public static final String TIME = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

        // 日期时间（yyyy-MM-dd HH:mm:ss）
        public static final String DATETIME = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

        // 年月（yyyy-MM）
        public static final String YEAR_MONTH = "^\\d{4}-(0[1-9]|1[0-2])$";

        // 时间戳（毫秒，13位）
        public static final String TIMESTAMP_MILLIS = "^\\d{13}$";

        // 时间戳（秒，10位）
        public static final String TIMESTAMP_SECONDS = "^\\d{10}$";

    }

    // 文件相关正则
    public static final class File {

        private File() {
        }

        // 图片文件扩展名
        public static final String IMAGE_EXTENSION = "\\.(jpg|jpeg|png|gif|bmp|webp|svg)$";

        // 文档文件扩展名
        public static final String DOCUMENT_EXTENSION = "\\.(doc|docx|pdf|txt|xls|xlsx|ppt|pptx)$";

        // 视频文件扩展名
        public static final String VIDEO_EXTENSION = "\\.(mp4|avi|mov|wmv|flv|mkv|webm)$";

        // 音频文件扩展名
        public static final String AUDIO_EXTENSION = "\\.(mp3|wav|flac|aac|ogg|wma)$";

        // 压缩文件扩展名
        public static final String ARCHIVE_EXTENSION = "\\.(zip|rar|7z|tar|gz)$";

        // 文件名（不允许特殊字符）
        public static final String FILENAME = "^[^\\\\/:*?\"<>|]+$";

    }

    // 编码相关正则
    public static final class Code {

        private Code() {
        }

        // UUID（带连字符）
        public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        // UUID（不带连字符，32位）
        public static final String UUID_COMPACT = "^[0-9a-fA-F]{32}$";

        // 颜色代码（十六进制，3位或6位）
        public static final String COLOR_HEX = "^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$";

        // 雪花ID（19位数字）
        public static final String SNOWFLAKE_ID = "^\\d{19}$";

        // 订单号（字母或数字，10-32位）
        public static final String ORDER_NO = "^[a-zA-Z0-9]{10,32}$";

        // 验证码（4-8位数字）
        public static final String VERIFICATION_CODE = "^\\d{4,8}$";

    }

    // 公共常用正则
    public static final class Common {

        private Common() {
        }

        // 非空字符串
        public static final String NOT_EMPTY = "^\\S+$";

        // 空白字符串
        public static final String BLANK = "^\\s*$";

        // JSON字符串
        public static final String JSON = "^(\\{.*\\}|\\[.*\\])$";

        // HTML标签
        public static final String HTML_TAG = "<[^>]+>";

        // 中文姓名（2-20位，支持少数民族姓名中的点）
        public static final String CHINESE_NAME = "^[\\u4e00-\\u9fa5·]{2,20}$";

        // 英文姓名（2-50位）
        public static final String ENGLISH_NAME = "^[a-zA-Z\\s.'-]{2,50}$";

        // QQ号（5-11位数字）
        public static final String QQ = "^[1-9]\\d{4,10}$";

        // 微信号（字母开头，6-20位）
        public static final String WECHAT = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$";

        // 银行卡号（16-19位数字）
        public static final String BANK_CARD = "^\\d{16,19}$";

        // Base64字符串
        public static final String BASE64 = "^[A-Za-z0-9+/]*={0,2}$";

    }
}
