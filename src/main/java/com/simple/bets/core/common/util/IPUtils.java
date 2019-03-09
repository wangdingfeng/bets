package com.simple.bets.core.common.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {

	private static final String UNKNOWN = "unknown";

	protected IPUtils(){

	}

	/**
	 * 获取IP地址
	 * 使用 Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 *获取主机名
	 * @return
	 */
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return "未知";
	}

	public static String getHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return "127.0.0.1";
	}

}
