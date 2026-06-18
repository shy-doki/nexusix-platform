package com.shy.nexusix.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * <p>日期工具类 - 格式化、解析、计算等常用操作，基于Java 8 java.time API</p>
 *
 * @author shy
 */
public final class DateUtils {

    // 默认日期格式：yyyy-MM-dd
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    // 默认时间格式：HH:mm:ss
    public static final String TIME_PATTERN = "HH:mm:ss";

    // 默认日期时间格式：yyyy-MM-dd HH:mm:ss
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 紧凑日期格式：yyyyMMdd
    public static final String DATE_COMPACT_PATTERN = "yyyyMMdd";

    // 紧凑日期时间格式：yyyyMMddHHmmss
    public static final String DATETIME_COMPACT_PATTERN = "yyyyMMddHHmmss";

    // ISO日期时间格式：yyyy-MM-dd'T'HH:mm:ss
    public static final String DATETIME_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    // 日期格式化器
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    // 时间格式化器
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    // 日期时间格式化器
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // 紧凑日期格式化器
    public static final DateTimeFormatter DATE_COMPACT_FORMATTER = DateTimeFormatter.ofPattern(DATE_COMPACT_PATTERN);

    // 紧凑日期时间格式化器
    public static final DateTimeFormatter DATETIME_COMPACT_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_COMPACT_PATTERN);

    // ISO日期时间格式化器
    public static final DateTimeFormatter DATETIME_ISO_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_ISO_PATTERN);

    // 默认时区
    public static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private DateUtils() {
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate nowDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前Instant
     */
    public static Instant nowInstant() {
        return Instant.now();
    }

    /**
     * 获取当前时区的ZonedDateTime
     *
     * @return 当前时区的ZonedDateTime
     */
    public static ZonedDateTime nowZonedDateTime() {
        return ZonedDateTime.now();
    }

    /**
     * 获取指定时区的ZonedDateTime
     *
     * @param zoneId 时区ID
     * @return 指定时区的ZonedDateTime
     */
    public static ZonedDateTime nowZonedDateTime(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    /**
     * 格式化LocalDate为字符串
     *
     * @param date    日期对象
     * @param pattern 格式模式
     * @return 格式化后的字符串，date为null时返回null
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * 格式化LocalDate（默认格式yyyy-MM-dd）
     *
     * @param date 日期对象
     * @return 格式化后的字符串，date为null时返回null
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * 格式化LocalTime为字符串
     *
     * @param time    时间对象
     * @param pattern 格式模式
     * @return 格式化后的字符串，time为null时返回null
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return time.format(formatter);
    }

    /**
     * 格式化LocalTime（默认格式HH:mm:ss）
     *
     * @param time 时间对象
     * @return 格式化后的字符串，time为null时返回null
     */
    public static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMATTER);
    }

    /**
     * 格式化LocalDateTime为字符串
     *
     * @param dateTime 日期时间对象
     * @param pattern  格式模式
     * @return 格式化后的字符串，dateTime为null时返回null
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime（默认格式yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTime 日期时间对象
     * @return 格式化后的字符串，dateTime为null时返回null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * 格式化Instant为字符串
     *
     * @param instant 时间戳对象
     * @param pattern 格式模式
     * @param zoneId  时区
     * @return 格式化后的字符串，instant为null时返回null
     */
    public static String format(Instant instant, String pattern, ZoneId zoneId) {
        if (instant == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
        return formatter.format(instant);
    }

    /**
     * 格式化Instant（默认格式和时区）
     *
     * @param instant 时间戳对象
     * @return 格式化后的字符串，instant为null时返回null
     */
    public static String formatDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return DATETIME_FORMATTER.withZone(DEFAULT_ZONE).format(instant);
    }

    /**
     * 解析字符串为LocalDate
     *
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return LocalDate对象，解析失败返回null
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDate（默认格式yyyy-MM-dd）
     *
     * @param dateStr 日期字符串
     * @return LocalDate对象，解析失败返回null
     */
    public static LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalTime
     *
     * @param timeStr 时间字符串
     * @param pattern 格式模式
     * @return LocalTime对象，解析失败返回null
     */
    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalTime.parse(timeStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalTime（默认格式HH:mm:ss）
     *
     * @param timeStr 时间字符串
     * @return LocalTime对象，解析失败返回null
     */
    public static LocalTime parseLocalTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDateTime
     *
     * @param dateStr 日期时间字符串
     * @param pattern 格式模式
     * @return LocalDateTime对象，解析失败返回null
     */
    public static LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDateTime（默认格式yyyy-MM-dd HH:mm:ss）
     *
     * @param dateStr 日期时间字符串
     * @return LocalDateTime对象，解析失败返回null
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DATETIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为Instant
     *
     * @param dateStr 日期时间字符串
     * @param pattern 格式模式
     * @param zoneId  时区
     * @return Instant对象，解析失败返回null
     */
    public static Instant parseInstant(String dateStr, String pattern, ZoneId zoneId) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
            return Instant.from(formatter.parse(dateStr));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为Instant（默认格式和时区）
     *
     * @param dateStr 日期时间字符串
     * @return Instant对象，解析失败返回null
     */
    public static Instant parseInstant(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Instant.from(DATETIME_FORMATTER.withZone(DEFAULT_ZONE).parse(dateStr));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * LocalDateTime转Instant
     *
     * @param localDateTime LocalDateTime对象
     * @return Instant对象，localDateTime为null时返回null
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(DEFAULT_ZONE).toInstant();
    }

    /**
     * LocalDateTime转Instant（指定时区）
     *
     * @param localDateTime LocalDateTime对象
     * @param zoneId        时区
     * @return Instant对象，localDateTime为null时返回null
     */
    public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(zoneId).toInstant();
    }

    /**
     * LocalDate转Instant（当天开始时间）
     *
     * @param localDate LocalDate对象
     * @return Instant对象，localDate为null时返回null
     */
    public static Instant toInstant(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.atStartOfDay(DEFAULT_ZONE).toInstant();
    }

    /**
     * Instant转LocalDateTime
     *
     * @param instant Instant对象
     * @return LocalDateTime对象，instant为null时返回null
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE);
    }

    /**
     * Instant转LocalDateTime（指定时区）
     *
     * @param instant Instant对象
     * @param zoneId  时区
     * @return LocalDateTime对象，instant为null时返回null
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Instant转LocalDate
     *
     * @param instant Instant对象
     * @return LocalDate对象，instant为null时返回null
     */
    public static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(DEFAULT_ZONE).toLocalDate();
    }

    /**
     * Instant转LocalDate（指定时区）
     *
     * @param instant Instant对象
     * @param zoneId  时区
     * @return LocalDate对象，instant为null时返回null
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差，任一参数为null时返回0
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的天数
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 天数差，任一参数为null时返回0
     */
    public static long daysBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的小时数
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 小时差，任一参数为null时返回0
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的分钟数
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 分钟差，任一参数为null时返回0
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的秒数
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 秒数差，任一参数为null时返回0
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个Instant之间的毫秒数
     *
     * @param start 开始Instant
     * @param end   结束Instant
     * @return 毫秒差，任一参数为null时返回0
     */
    public static long millisBetween(Instant start, Instant end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MILLIS.between(start, end);
    }

    /**
     * 日期加减天数
     *
     * @param date 日期对象
     * @param days 天数（正数加，负数减）
     * @return 计算后的日期，date为null时返回null
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * 日期时间加减天数
     *
     * @param dateTime 日期时间对象
     * @param days     天数（正数加，负数减）
     * @return 计算后的日期时间，dateTime为null时返回null
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusDays(days);
    }

    /**
     * 日期加减月数
     *
     * @param date   日期对象
     * @param months 月数（正数加，负数减）
     * @return 计算后的日期，date为null时返回null
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * 日期时间加减月数
     *
     * @param dateTime 日期时间对象
     * @param months   月数（正数加，负数减）
     * @return 计算后的日期时间，dateTime为null时返回null
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMonths(months);
    }

    /**
     * 日期加减年数
     *
     * @param date  日期对象
     * @param years 年数（正数加，负数减）
     * @return 计算后的日期，date为null时返回null
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        if (date == null) {
            return null;
        }
        return date.plusYears(years);
    }

    /**
     * 日期时间加减年数
     *
     * @param dateTime 日期时间对象
     * @param years    年数（正数加，负数减）
     * @return 计算后的日期时间，dateTime为null时返回null
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusYears(years);
    }

    /**
     * 日期时间加减小时数
     *
     * @param dateTime 日期时间对象
     * @param hours    小时数（正数加，负数减）
     * @return 计算后的日期时间，dateTime为null时返回null
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    /**
     * 日期时间加减分钟数
     *
     * @param dateTime 日期时间对象
     * @param minutes  分钟数（正数加，负数减）
     * @return 计算后的日期时间，dateTime为null时返回null
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutes);
    }

    /**
     * 获取当天的开始时间（00:00:00）
     *
     * @param date 日期对象
     * @return 当天开始时间，date为null时返回null
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * 获取当天的开始时间（00:00:00）
     *
     * @param dateTime 日期时间对象
     * @return 当天开始时间，dateTime为null时返回null
     */
    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取当天的结束时间（23:59:59.999999999）
     *
     * @param date 日期对象
     * @return 当天结束时间，date为null时返回null
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(23, 59, 59, 999999999);
    }

    /**
     * 获取当天的结束时间（23:59:59.999999999）
     *
     * @param dateTime 日期时间对象
     * @return 当天结束时间，dateTime为null时返回null
     */
    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atTime(23, 59, 59, 999999999);
    }

    /**
     * 获取本周的第一天（周一）
     *
     * @param date 日期对象
     * @return 本周第一天，date为null时返回null
     */
    public static LocalDate getFirstDayOfWeek(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(DayOfWeek.MONDAY);
    }

    /**
     * 获取本周的最后一天（周日）
     *
     * @param date 日期对象
     * @return 本周最后一天，date为null时返回null
     */
    public static LocalDate getLastDayOfWeek(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(DayOfWeek.SUNDAY);
    }

    /**
     * 获取本月的第一天
     *
     * @param date 日期对象
     * @return 本月第一天，date为null时返回null
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }

    /**
     * 获取本月的最后一天
     *
     * @param date 日期对象
     * @return 本月最后一天，date为null时返回null
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取本年的第一天
     *
     * @param date 日期对象
     * @return 本年第一天，date为null时返回null
     */
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfYear(1);
    }

    /**
     * 获取本年的最后一天
     *
     * @param date 日期对象
     * @return 本年最后一天，date为null时返回null
     */
    public static LocalDate getLastDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 判断是否为今天
     *
     * @param date 日期对象
     * @return true-是今天
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }

    /**
     * 判断是否为今天
     *
     * @param dateTime 日期时间对象
     * @return true-是今天
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now());
    }

    /**
     * 判断是否为昨天
     *
     * @param date 日期对象
     * @return true-是昨天
     */
    public static boolean isYesterday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now().minusDays(1));
    }

    /**
     * 判断是否为明天
     *
     * @param date 日期对象
     * @return true-是明天
     */
    public static boolean isTomorrow(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now().plusDays(1));
    }

    /**
     * 获取年龄
     *
     * @param birthDate 出生日期
     * @return 年龄，birthDate为null时返回0
     */
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * 获取年龄（精确到月）
     *
     * @param birthDate 出生日期
     * @return 年龄字符串，格式：X岁Y个月
     */
    public static String getAgeWithMonth(LocalDate birthDate) {
        if (birthDate == null) {
            return "";
        }
        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears() + "岁" + period.getMonths() + "个月";
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳（毫秒）
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     *
     * @return 当前时间戳（秒）
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 时间戳（毫秒）转Instant
     *
     * @param timestamp 时间戳（毫秒）
     * @return Instant对象
     */
    public static Instant fromTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp);
    }

    /**
     * 时间戳（秒）转Instant
     *
     * @param timestamp 时间戳（秒）
     * @return Instant对象
     */
    public static Instant fromTimestampSeconds(long timestamp) {
        return Instant.ofEpochSecond(timestamp);
    }

    /**
     * Instant转时间戳（毫秒）
     *
     * @param instant Instant对象
     * @return 时间戳（毫秒），instant为null时返回0
     */
    public static long toTimestamp(Instant instant) {
        if (instant == null) {
            return 0;
        }
        return instant.toEpochMilli();
    }

    /**
     * LocalDateTime转时间戳（毫秒）
     *
     * @param localDateTime LocalDateTime对象
     * @return 时间戳（毫秒），localDateTime为null时返回0
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        }
        return localDateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /**
     * LocalDate转时间戳（毫秒，当天开始时间）
     *
     * @param localDate LocalDate对象
     * @return 时间戳（毫秒），localDate为null时返回0
     */
    public static long toTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return 0;
        }
        return localDate.atStartOfDay(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /**
     * 判断日期是否在指定范围内
     *
     * @param date      待判断的日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return true-在范围内
     */
    public static boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * 判断日期时间是否在指定范围内
     *
     * @param dateTime      待判断的日期时间
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return true-在范围内
     */
    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (dateTime == null || startDateTime == null || endDateTime == null) {
            return false;
        }
        return !dateTime.isBefore(startDateTime) && !dateTime.isAfter(endDateTime);
    }

    /**
     * 判断Instant是否在指定范围内
     *
     * @param instant 待判断的Instant
     * @param start   开始Instant
     * @param end     结束Instant
     * @return true-在范围内
     */
    public static boolean isBetween(Instant instant, Instant start, Instant end) {
        if (instant == null || start == null || end == null) {
            return false;
        }
        return !instant.isBefore(start) && !instant.isAfter(end);
    }

    /**
     * 比较两个日期
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 0-相等，正数-date1>date2，负数-date1<date2
     */
    public static int compare(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    /**
     * 比较两个日期时间
     *
     * @param dateTime1 日期时间1
     * @param dateTime2 日期时间2
     * @return 0-相等，正数-dateTime1>dateTime2，负数-dateTime1<dateTime2
     */
    public static int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            return 0;
        }
        if (dateTime1 == null) {
            return -1;
        }
        if (dateTime2 == null) {
            return 1;
        }
        return dateTime1.compareTo(dateTime2);
    }

    /**
     * 比较两个Instant
     *
     * @param instant1 Instant1
     * @param instant2 Instant2
     * @return 0-相等，正数-instant1>instant2，负数-instant1<instant2
     */
    public static int compare(Instant instant1, Instant instant2) {
        if (instant1 == null && instant2 == null) {
            return 0;
        }
        if (instant1 == null) {
            return -1;
        }
        if (instant2 == null) {
            return 1;
        }
        return instant1.compareTo(instant2);
    }

    /**
     * 判断是否为闰年
     *
     * @param year 年份
     * @return true-是闰年
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * 判断指定日期所在年份是否为闰年
     *
     * @param date 日期对象
     * @return true-是闰年
     */
    public static boolean isLeapYear(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isLeapYear();
    }

    /**
     * 获取指定月份的天数
     *
     * @param year  年份
     * @param month 月份（1-12）
     * @return 天数
     */
    public static int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    /**
     * 获取指定年份的天数
     *
     * @param year 年份
     * @return 天数
     */
    public static int getDaysInYear(int year) {
        return Year.of(year).length();
    }

    /**
     * 判断是否为工作日（周一到周五）
     *
     * @param date 日期对象
     * @return true-是工作日
     */
    public static boolean isWeekday(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * 判断是否为周末
     *
     * @param date 日期对象
     * @return true-是周末
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 获取季度（1-4）
     *
     * @param date 日期对象
     * @return 季度，date为null时返回0
     */
    public static int getQuarter(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * 获取指定日期所在季度的第一天
     *
     * @param date 日期对象
     * @return 季度第一天，date为null时返回null
     */
    public static LocalDate getFirstDayOfQuarter(LocalDate date) {
        if (date == null) {
            return null;
        }
        int quarter = getQuarter(date);
        int month = (quarter - 1) * 3 + 1;
        return LocalDate.of(date.getYear(), month, 1);
    }

    /**
     * 获取指定日期所在季度的最后一天
     *
     * @param date 日期对象
     * @return 季度最后一天，date为null时返回null
     */
    public static LocalDate getLastDayOfQuarter(LocalDate date) {
        if (date == null) {
            return null;
        }
        int quarter = getQuarter(date);
        int month = quarter * 3;
        return LocalDate.of(date.getYear(), month, 1).with(TemporalAdjusters.lastDayOfMonth());
    }
}
