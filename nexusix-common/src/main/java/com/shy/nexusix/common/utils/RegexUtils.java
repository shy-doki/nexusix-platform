package com.shy.nexusix.common.utils;

import com.shy.nexusix.common.constant.RegexConstant;
import java.util.regex.Pattern;

/**
 * <p>正则表达式工具类 - 基于RegexConstant提供常用格式验证</p>
 *
 * @author shy
 */
public final class RegexUtils {

    private RegexUtils() {
    }

    /**
     * 验证字符串是否匹配指定正则表达式
     *
     * @param str   待验证的字符串
     * @param regex 正则表达式
     * @return true-匹配
     */
    public static boolean matches(String str, String regex) {
        if (str == null || regex == null) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 检查字符串中是否包含匹配指定正则的子串
     * <p>matches()要求完全匹配，find()只需存在子串匹配</p>
     *
     * @param str   待检查的字符串
     * @param regex 正则表达式
     * @return true-包含匹配子串
     */
    public static boolean find(String str, String regex) {
        if (str == null || regex == null) {
            return false;
        }
        return Pattern.compile(regex).matcher(str).find();
    }

    /** 验证中国大陆手机号 */
    public static boolean isValidChinaMobile(String mobile) {
        return matches(mobile, RegexConstant.Phone.CHINA_MOBILE);
    }

    /** 验证国际手机号 */
    public static boolean isValidInternationalMobile(String mobile) {
        return matches(mobile, RegexConstant.Phone.INTERNATIONAL_MOBILE);
    }

    /** 验证固定电话 */
    public static boolean isValidLandline(String phone) {
        return matches(phone, RegexConstant.Phone.LANDLINE);
    }

    /** 验证邮箱 */
    public static boolean isValidEmail(String email) {
        return matches(email, RegexConstant.Email.EMAIL);
    }

    /** 验证邮箱（严格模式） */
    public static boolean isValidEmailStrict(String email) {
        return matches(email, RegexConstant.Email.EMAIL_STRICT);
    }

    /** 验证18位身份证号 */
    public static boolean isValidIdCard18(String idCard) {
        return matches(idCard, RegexConstant.IdCard.ID_CARD_18);
    }

    /** 验证15位身份证号 */
    public static boolean isValidIdCard15(String idCard) {
        return matches(idCard, RegexConstant.IdCard.ID_CARD_15);
    }

    /** 验证身份证号（15位或18位） */
    public static boolean isValidIdCard(String idCard) {
        return matches(idCard, RegexConstant.IdCard.ID_CARD);
    }

    /** 验证用户名（字母开头） */
    public static boolean isValidUsername(String username) {
        return matches(username, RegexConstant.Username.USERNAME_LETTER_START);
    }

    /** 验证用户名（字母或数字开头） */
    public static boolean isValidUsernameAlphanumeric(String username) {
        return matches(username, RegexConstant.Username.USERNAME_ALPHANUMERIC_START);
    }

    /** 验证用户名（支持中文） */
    public static boolean isValidUsernameWithChinese(String username) {
        return matches(username, RegexConstant.Username.USERNAME_WITH_CHINESE);
    }

    /** 验证密码（简单） */
    public static boolean isValidPasswordSimple(String password) {
        return matches(password, RegexConstant.Password.PASSWORD_SIMPLE);
    }

    /** 验证密码（中等强度） */
    public static boolean isValidPasswordMedium(String password) {
        return matches(password, RegexConstant.Password.PASSWORD_MEDIUM);
    }

    /** 验证密码（高强度） */
    public static boolean isValidPasswordStrong(String password) {
        return matches(password, RegexConstant.Password.PASSWORD_STRONG);
    }

    /** 验证密码（超高强度） */
    public static boolean isValidPasswordVeryStrong(String password) {
        return matches(password, RegexConstant.Password.PASSWORD_VERY_STRONG);
    }

    /** 验证IPv4地址 */
    public static boolean isValidIPv4(String ip) {
        return matches(ip, RegexConstant.IpAddress.IPV4);
    }

    /** 验证IPv6地址 */
    public static boolean isValidIPv6(String ip) {
        return matches(ip, RegexConstant.IpAddress.IPV6);
    }

    /** 验证IP地址（IPv4或IPv6） */
    public static boolean isValidIP(String ip) {
        return isValidIPv4(ip) || isValidIPv6(ip);
    }

    /** 验证IPv4地址段（CIDR格式） */
    public static boolean isValidIPv4CIDR(String cidr) {
        return matches(cidr, RegexConstant.IpAddress.IPV4_CIDR);
    }

    /** 验证URL */
    public static boolean isValidURL(String url) {
        return matches(url, RegexConstant.Url.URL);
    }

    /** 验证URL（严格模式） */
    public static boolean isValidURLStrict(String url) {
        return matches(url, RegexConstant.Url.URL_STRICT);
    }

    /** 验证域名 */
    public static boolean isValidDomain(String domain) {
        return matches(domain, RegexConstant.Url.DOMAIN);
    }

    /** 验证HTTP URL */
    public static boolean isValidHttpURL(String url) {
        return matches(url, RegexConstant.Url.HTTP_URL);
    }

    /** 验证整数 */
    public static boolean isValidInteger(String number) {
        return matches(number, RegexConstant.Number.INTEGER);
    }

    /** 验证正整数 */
    public static boolean isValidPositiveInteger(String number) {
        return matches(number, RegexConstant.Number.POSITIVE_INTEGER);
    }

    /** 验证非负整数 */
    public static boolean isValidNonNegativeInteger(String number) {
        return matches(number, RegexConstant.Number.NON_NEGATIVE_INTEGER);
    }

    /** 验证小数 */
    public static boolean isValidDecimal(String number) {
        return matches(number, RegexConstant.Number.DECIMAL);
    }

    /** 验证金额（最多两位小数） */
    public static boolean isValidMoney(String money) {
        return matches(money, RegexConstant.Number.MONEY);
    }

    /** 验证正数 */
    public static boolean isValidPositiveNumber(String number) {
        return matches(number, RegexConstant.Number.POSITIVE_NUMBER);
    }

    /** 验证百分比 */
    public static boolean isValidPercentage(String percentage) {
        return matches(percentage, RegexConstant.Number.PERCENTAGE);
    }

    /** 验证纯字母 */
    public static boolean isLetters(String str) {
        return matches(str, RegexConstant.Character.LETTERS);
    }

    /** 验证纯数字 */
    public static boolean isDigits(String str) {
        return matches(str, RegexConstant.Character.DIGITS);
    }

    /** 验证字母和数字 */
    public static boolean isAlphanumeric(String str) {
        return matches(str, RegexConstant.Character.ALPHANUMERIC);
    }

    /** 验证中文字符 */
    public static boolean isChinese(String str) {
        return matches(str, RegexConstant.Character.CHINESE);
    }

    /** 验证中文、字母、数字 */
    public static boolean isChineseAlphanumeric(String str) {
        return matches(str, RegexConstant.Character.CHINESE_ALPHANUMERIC);
    }

    /** 验证邮政编码 */
    public static boolean isValidPostalCode(String postalCode) {
        return matches(postalCode, RegexConstant.Character.POSTAL_CODE);
    }

    /** 验证车牌号 */
    public static boolean isValidLicensePlate(String licensePlate) {
        return matches(licensePlate, RegexConstant.Character.LICENSE_PLATE);
    }

    /** 验证统一社会信用代码 */
    public static boolean isValidSocialCreditCode(String code) {
        return matches(code, RegexConstant.Character.SOCIAL_CREDIT_CODE);
    }

    /** 验证日期（yyyy-MM-dd） */
    public static boolean isValidDate(String date) {
        return matches(date, RegexConstant.DateTime.DATE);
    }

    /** 验证时间（HH:mm:ss） */
    public static boolean isValidTime(String time) {
        return matches(time, RegexConstant.DateTime.TIME);
    }

    /** 验证日期时间（yyyy-MM-dd HH:mm:ss） */
    public static boolean isValidDateTime(String datetime) {
        return matches(datetime, RegexConstant.DateTime.DATETIME);
    }

    /** 验证年月（yyyy-MM） */
    public static boolean isValidYearMonth(String yearMonth) {
        return matches(yearMonth, RegexConstant.DateTime.YEAR_MONTH);
    }

    /** 验证时间戳（毫秒） */
    public static boolean isValidTimestampMillis(String timestamp) {
        return matches(timestamp, RegexConstant.DateTime.TIMESTAMP_MILLIS);
    }

    /** 验证时间戳（秒） */
    public static boolean isValidTimestampSeconds(String timestamp) {
        return find(timestamp, RegexConstant.DateTime.TIMESTAMP_SECONDS);
    }

    /** 验证图片文件扩展名 */
    public static boolean isImageFile(String filename) {
        return find(filename, RegexConstant.File.IMAGE_EXTENSION);
    }

    /** 验证文档文件扩展名 */
    public static boolean isDocumentFile(String filename) {
        return find(filename, RegexConstant.File.DOCUMENT_EXTENSION);
    }

    /** 验证视频文件扩展名 */
    public static boolean isVideoFile(String filename) {
        return find(filename, RegexConstant.File.VIDEO_EXTENSION);
    }

    /** 验证音频文件扩展名 */
    public static boolean isAudioFile(String filename) {
        return find(filename, RegexConstant.File.AUDIO_EXTENSION);
    }

    /** 验证压缩文件扩展名 */
    public static boolean isArchiveFile(String filename) {
        return find(filename, RegexConstant.File.ARCHIVE_EXTENSION);
    }

    /** 验证UUID */
    public static boolean isValidUUID(String uuid) {
        return matches(uuid, RegexConstant.Code.UUID);
    }

    /** 验证UUID（不带连字符） */
    public static boolean isValidUUIDCompact(String uuid) {
        return matches(uuid, RegexConstant.Code.UUID_COMPACT);
    }

    /** 验证颜色代码（十六进制） */
    public static boolean isValidColorHex(String color) {
        return matches(color, RegexConstant.Code.COLOR_HEX);
    }

    /** 验证雪花ID */
    public static boolean isValidSnowflakeId(String id) {
        return matches(id, RegexConstant.Code.SNOWFLAKE_ID);
    }

    /** 验证订单号 */
    public static boolean isValidOrderNo(String orderNo) {
        return matches(orderNo, RegexConstant.Code.ORDER_NO);
    }

    /** 验证验证码 */
    public static boolean isValidVerificationCode(String code) {
        return matches(code, RegexConstant.Code.VERIFICATION_CODE);
    }

    /** 验证非空字符串 */
    public static boolean isNotEmpty(String str) {
        return matches(str, RegexConstant.Common.NOT_EMPTY);
    }

    /** 验证空白字符串 */
    public static boolean isBlank(String str) {
        return matches(str, RegexConstant.Common.BLANK);
    }

    /** 验证JSON字符串 */
    public static boolean isValidJSON(String json) {
        return matches(json, RegexConstant.Common.JSON);
    }

    /** 验证中文姓名 */
    public static boolean isValidChineseName(String name) {
        return matches(name, RegexConstant.Common.CHINESE_NAME);
    }

    /** 验证英文姓名 */
    public static boolean isValidEnglishName(String name) {
        return matches(name, RegexConstant.Common.ENGLISH_NAME);
    }

    /** 验证QQ号 */
    public static boolean isValidQQ(String qq) {
        return matches(qq, RegexConstant.Common.QQ);
    }

    /** 验证微信号 */
    public static boolean isValidWechat(String wechat) {
        return matches(wechat, RegexConstant.Common.WECHAT);
    }

    /** 验证银行卡号 */
    public static boolean isValidBankCard(String bankCard) {
        return matches(bankCard, RegexConstant.Common.BANK_CARD);
    }

    /** 验证Base64字符串 */
    public static boolean isValidBase64(String base64) {
        return matches(base64, RegexConstant.Common.BASE64);
    }
}
