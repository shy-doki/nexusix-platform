package com.shy.nexusix.common.utils;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

import java.util.UUID;

/**
 * <p>
 * ID生成工具类
 * </p>
 * <p>
 * 提供UUID、雪花ID等唯一标识符的生成功能。
 * 雪花ID采用MyBatis-Plus内置的雪花算法生成器实现，确保线程安全和分布式唯一性。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public final class IdUtils {

    private IdUtils() {
    }

    private static final IdentifierGenerator IDENTIFIER_GENERATOR = new DefaultIdentifierGenerator();

    /**
     * 生成UUID（带连字符）
     *
     * @return UUID字符串，格式：550e8400-e29b-41d4-a716-446655440000
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成UUID（不带连字符）
     *
     * @return UUID字符串，格式：550e8400e29b41d4a716446655440000
     */
    public static String uuidCompact() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成简化UUID（16位）
     *
     * @return 16位UUID字符串
     */
    public static String uuidShort() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 生成数字型UUID（纯数字）
     *
     * @return 纯数字UUID字符串
     */
    public static String uuidNumeric() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        return String.valueOf(Math.abs(mostSigBits)) + String.valueOf(Math.abs(leastSigBits));
    }

    /**
     * 生成雪花ID（使用MyBatis-Plus内置雪花算法）
     * <p>
     * MyBatis-Plus的DefaultIdentifierGenerator会自动处理：
     * - 时间戳回拨问题
     * - 序列号溢出
     * - 工作机器ID分配
     * </p>
     *
     * @return 雪花ID
     */
    public static long snowflakeId() {
        return IDENTIFIER_GENERATOR.nextId(null).longValue();
    }

    /**
     * 生成雪花ID字符串（使用MyBatis-Plus内置雪花算法）
     *
     * @return 雪花ID字符串
     */
    public static String snowflakeIdStr() {
        return String.valueOf(IDENTIFIER_GENERATOR.nextId(null));
    }

    /**
     * 生成订单号（时间戳 + 随机数）
     *
     * @return 订单号
     */
    public static String orderNo() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 10000);
        return String.format("%d%04d", timestamp, random);
    }

    /**
     * 生成订单号（带前缀）
     *
     * @param prefix 订单号前缀
     * @return 订单号
     */
    public static String orderNo(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 10000);
        return String.format("%s%d%04d", prefix, timestamp, random);
    }

    /**
     * 生成验证码（纯数字）
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public static String verificationCode(int length) {
        if (length <= 0) {
            length = 6;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串（包含大小写字母和数字）
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        if (length <= 0) {
            length = 16;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串（只包含小写字母和数字）
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomStringLower(int length) {
        if (length <= 0) {
            length = 16;
        }
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串（只包含大写字母和数字）
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomStringUpper(int length) {
        if (length <= 0) {
            length = 16;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
