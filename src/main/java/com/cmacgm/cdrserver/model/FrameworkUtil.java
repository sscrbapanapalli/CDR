package com.cmacgm.cdrserver.model;


import javax.servlet.http.HttpServletRequest;


public class FrameworkUtil {

	public static <T> RetValue<T> getResponseValue(Boolean status,
			String message, T data, Integer totalRecord) {
		return new RetValue<T>(status, message, data, totalRecord);
	}

	public static <T> RetValue<T> getResponseValue(Boolean status,
			String message, T data) {
		return new RetValue<T>(status, message, data);
	}

	public static String getClientIpAddress(HttpServletRequest request) {
	    for (String header : HTTP_HEADERS) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}
	
	public static String getUserAccessToken(HttpServletRequest request) {
	    return request.getHeader("userToken");
	}
	
	public static final String[] HTTP_HEADERS = {"X-Forwarded-For","Proxy-Client-IP",
	        "WL-Proxy-Client-IP",
	        "HTTP_X_FORWARDED_FOR",
	        "HTTP_X_FORWARDED",
	        "HTTP_X_CLUSTER_CLIENT_IP",
	        "HTTP_CLIENT_IP",
	        "HTTP_FORWARDED_FOR",
	        "HTTP_FORWARDED",
	        "HTTP_VIA",
	        "REMOTE_ADDR" };
	
}