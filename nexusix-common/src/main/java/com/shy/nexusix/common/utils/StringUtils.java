package com.shy.nexusix.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * <p>字符串工具类 - 判空、截取、转换、拼接等常用操作</p>
 *
 * @author shy
 */
public final class StringUtils {

    // 空字符串
    public static final String EMPTY = "";

    // 空格字符串
    public static final String SPACE = " ";

    // 逗号字符串
    public static final String COMMA = ",";

    // 点字符串
    public static final String DOT = ".";

    // 换行符
    public static final String LF = "\n";

    // 制表符
    public static final String TAB = "\t";

    private StringUtils() {
    }

    /**
     * 判断字符串是否为null
     *
     * @param str 字符串
     * @return true-为null
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * 判断字符串是否不为null
     *
     * @param str 字符串
     * @return true-不为null
     */
    public static boolean isNotNull(String str) {
        return str != null;
    }

    /**
     * 判断字符串是否为空（null或空字符串）
     *
     * @param str 字符串
     * @return true-为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return true-不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白（null、空字符串或仅包含空白字符）
     *
     * @param str 字符串
     * @return true-为空白
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 字符串
     * @return true-不为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 去除字符串两端的空白字符
     *
     * @param str 字符串
     * @return 去除空白后的字符串，str为null时返回null
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除空白，结果为空字符串则返回null
     *
     * @param str 字符串
     * @return 去除空白后的字符串，结果为空则返回null
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * 去除空白，为null则返回空字符串
     *
     * @param str 字符串
     * @return 去除空白后的字符串，str为null时返回空字符串
     */
    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * 判断两个字符串是否相等（区分大小写）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 判断两个字符串是否相等（不区分大小写）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断是否以指定前缀开头（区分大小写）
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return true-以指定前缀开头
     */
    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    /**
     * 判断是否以指定前缀开头（不区分大小写）
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return true-以指定前缀开头
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * 判断是否以指定后缀结尾（区分大小写）
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return true-以指定后缀结尾
     */
    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.endsWith(suffix);
    }

    /**
     * 判断是否以指定后缀结尾（不区分大小写）
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return true-以指定后缀结尾
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 判断是否包含指定子串（区分大小写）
     *
     * @param str       字符串
     * @param searchStr 子串
     * @return true-包含
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    /**
     * 判断是否包含指定子串（不区分大小写）
     *
     * @param str       字符串
     * @param searchStr 子串
     * @return true-包含
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * 获取字符串长度
     *
     * @param str 字符串
     * @return 字符串长度，str为null时返回0
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的字符串，str为null时返回null
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return EMPTY;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    /**
     * 截取字符串（从开始位置到结束）
     *
     * @param str   字符串
     * @param start 开始位置（包含）
     * @return 截取后的字符串，str为null时返回null
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }
        return str.substring(start);
    }

    /**
     * 截取字符串的前n个字符
     *
     * @param str 字符串
     * @param len 长度
     * @return 截取后的字符串，str为null时返回null
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 截取字符串的后n个字符
     *
     * @param str 字符串
     * @param len 长度
     * @return 截取后的字符串，str为null时返回null
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * 替换字符串中的指定子串
     *
     * @param text         原字符串
     * @param searchString 被替换的子串
     * @param replacement  替换字符串
     * @return 替换后的字符串，text为null时返回null
     */
    public static String replace(String text, String searchString, String replacement) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null) {
            return text;
        }
        return text.replace(searchString, replacement);
    }

    /**
     * 替换字符串中的所有匹配子串（正则）
     *
     * @param text         原字符串
     * @param regex        正则表达式
     * @param replacement  替换字符串
     * @return 替换后的字符串，text为null时返回null
     */
    public static String replaceAll(String text, String regex, String replacement) {
        if (isEmpty(text) || isEmpty(regex) || replacement == null) {
            return text;
        }
        return text.replaceAll(regex, replacement);
    }

    /**
     * 分割字符串
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 分割后的字符串数组，str为null时返回null
     */
    public static String[] split(String str, String separator) {
        if (str == null) {
            return null;
        }
        if (isEmpty(separator)) {
            return new String[]{str};
        }
        return str.split(separator);
    }

    /**
     * 分割字符串（使用逗号作为分隔符）
     *
     * @param str 字符串
     * @return 分割后的字符串数组，str为null时返回null
     */
    public static String[] splitByComma(String str) {
        return split(str, COMMA);
    }

    /**
     * 连接数组元素为字符串
     *
     * @param array     数组
     * @param separator 分隔符
     * @return 连接后的字符串，array为null时返回null
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            if (array[i] != null) {
                sb.append(array[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 连接集合元素为字符串
     *
     * @param collection 集合
     * @param separator  分隔符
     * @return 连接后的字符串，collection为null时返回null
     */
    public static String join(Collection<?> collection, String separator) {
        if (collection == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object obj : collection) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }
            if (obj != null) {
                sb.append(obj);
            }
        }
        return sb.toString();
    }

    /**
     * 连接Map元素为字符串
     *
     * @param map               Map对象
     * @param separator         分隔符
     * @param keyValueSeparator 键值分隔符
     * @return 连接后的字符串，map为null时返回null
     */
    public static String join(Map<?, ?> map, String separator, String keyValueSeparator) {
        if (map == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        if (keyValueSeparator == null) {
            keyValueSeparator = EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }
            sb.append(entry.getKey());
            sb.append(keyValueSeparator);
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 转换为大写
     *
     * @param str 字符串
     * @return 大写字符串，str为null时返回null
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    /**
     * 转换为小写
     *
     * @param str 字符串
     * @return 小写字符串，str为null时返回null
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写后的字符串，str为null时返回null
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            return str;
        }
        return Character.toTitleCase(firstChar) + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写后的字符串，str为null时返回null
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return str;
        }
        return Character.toLowerCase(firstChar) + str.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param str 字符串
     * @return 反转后的字符串，str为null时返回null
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 重复字符串
     *
     * @param str    字符串
     * @param repeat 重复次数
     * @return 重复后的字符串，str为null时返回null
     */
    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 左填充字符串
     *
     * @param str     字符串
     * @param length  目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串，str为null时返回null
     */
    public static String leftPad(String str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padLength; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 右填充字符串
     *
     * @param str     字符串
     * @param length  目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串，str为null时返回null
     */
    public static String rightPad(String str, int length, char padChar) {
        if (str == null) {
            return null;
        }
        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < padLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 去除HTML标签
     *
     * @param html HTML字符串
     * @return 去除HTML标签后的字符串，html为null时返回null
     */
    public static String stripHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("<[^>]*>", EMPTY);
    }

    /**
     * 将字符串转换为驼峰命名
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 驼峰命名字符串，str为null时返回null
     */
    public static String toCamelCase(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        String[] parts = str.split(separator);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                sb.append(parts[i].toLowerCase());
            } else {
                sb.append(capitalize(parts[i].toLowerCase()));
            }
        }
        return sb.toString();
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 字符串
     * @return 下划线命名字符串，str为null时返回null
     */
    public static String toUnderlineCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将对象转换为字符串
     *
     * @param obj 对象
     * @return 字符串，obj为null时返回null
     */
    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    /**
     * 将对象转换为字符串，为null则返回默认值
     *
     * @param obj          对象
     * @param defaultValue 默认值
     * @return 字符串
     */
    public static String toString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : obj.toString();
    }

    /**
     * 格式化字符串
     *
     * @param format 格式字符串
     * @param args   参数
     * @return 格式化后的字符串
     */
    public static String format(String format, Object... args) {
        if (format == null) {
            return null;
        }
        return String.format(format, args);
    }
}
