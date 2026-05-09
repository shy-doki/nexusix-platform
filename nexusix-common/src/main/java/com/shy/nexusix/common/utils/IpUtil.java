package com.shy.nexusix.common.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * IP地址工具类 - 获取客户端真实IP地址
 * </p>
 * <p>
 * 支持多级代理场景下的IP解析，优先级：
 * X-Real-IP → X-Forwarded-For → Proxy-Client-IP → WL-Proxy-Client-IP → remoteAddr
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";

    private IpUtil() {
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求对象
     * @return 客户端真实IP地址，无法获取时返回 "0.0.0.0"
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }

        String ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            return ip;
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return convertIpv6ToIpv4(ip);
        }

        return "0.0.0.0";
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }

    private static String convertIpv6ToIpv4(String ip) {
        if (ip != null && ip.startsWith("0:0:0:0:0:ffff:")) {
            return ip.substring("0:0:0:0:0:ffff:".length());
        }
        return ip;
    }

}