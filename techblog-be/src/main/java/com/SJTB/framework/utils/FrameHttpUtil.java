package com.SJTB.framework.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class FrameHttpUtil {

    /**
     * 요청자의 IP를 구한다.
     */
    public static String getUserIp(HttpServletRequest servletRequest) {
        String ip = null;

        ip = servletRequest.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = servletRequest.getRemoteAddr();
        }

        return ip;
    }

    /*
    * 접속유저의 기기OS, 브라우저 어플리케이션, IP를 취합하여 return
    * */
    public static String clientBroswserInfo(HttpServletRequest request){

        String agent = request.getHeader("USER-AGENT");

        String os = getClientOS(agent);
        String browser = getClientBrowser(agent);
        String ip = getUserIp(request);

        String result = FrameStringUtil.isNullDefaultValue(os,"") + "|" +
                        FrameStringUtil.isNullDefaultValue(browser,"") +
                        FrameStringUtil.isNullDefaultValue(ip, "");
        return result;
    }


    /*
    * 접속유저의 OS 정보 확인
    * 접속유저의 OS 또는 브라우저가 매우 낙후되는 OS일경우 봇의 공격으로 의심
    * */
    public static String getClientOS(String userAgent) {
        String os = "";
        userAgent = userAgent.toLowerCase();
        if (userAgent.indexOf("windows nt 10.0") > -1) {
            os = "Windows_10";
        }else if (userAgent.indexOf("windows nt 6.1") > -1) {
            os = "Windows_7";
        }else if (userAgent.indexOf("windows nt 6.2") > -1 || userAgent.indexOf("windows nt 6.3") > -1 ) {
            os = "Windows_8";
        }else if (userAgent.indexOf("windows nt 6.0") > -1) {
            os = "Windows_Vista";
        }else if (userAgent.indexOf("windows nt 5.1") > -1) {
            os = "Windows_XP";
        }else if (userAgent.indexOf("windows nt 5.0") > -1) {
            os = "Windows_2000";
        }else if (userAgent.indexOf("windows nt 4.0") > -1) {
            os = "Windows_NT";
        }else if (userAgent.indexOf("windows 98") > -1) {
            os = "Windows_98";
        }else if (userAgent.indexOf("windows 95") > -1) {
            os = "Windows_95";
        }else if (userAgent.indexOf("iphone") > -1) {
            os = "iPhone";
        }else if (userAgent.indexOf("ipad") > -1) {
            os = "iPad";
        }else if (userAgent.indexOf("android") > -1) {
            os = "android";
        }else if (userAgent.indexOf("mac") > -1) {
            os = "mac";
        }else if (userAgent.indexOf("linux") > -1) {
            os = "Linux";
        }else{
            os = "Other";
        }
        return os;
    }


    /*
    * 접속유저의 브라우저 정보확인
    * 접속유저의 OS 또는 브라우저가 매우 낙후되는 OS일경우 봇의 공격으로 의심
    * */
    public static String getClientBrowser(String userAgent) {
        String browser = "";

        if (userAgent.indexOf("Trident/7.0") > -1) {
            browser = "IE11";
        }
        else if (userAgent.indexOf("MSIE 10") > -1) {
            browser = "IE10";
        }
        else if (userAgent.indexOf("MSIE 9") > -1) {
            browser = "IE9";
        }
        else if (userAgent.indexOf("MSIE 8") > -1) {
            browser = "IE8";
        }
        else if (userAgent.indexOf("Chrome/") > -1) {
            browser = "Chrome";
        }
        else if (userAgent.indexOf("Chrome/") == -1 && userAgent.indexOf("Safari/") >= -1) {
            browser = "Safari";
        }
        else if (userAgent.indexOf("Firefox/") >= -1) {
            browser = "Firefox";
        }
        else {
            browser ="Other";
        }
        return browser;
    }

    public static String getCurrentUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); // http 또는 https
        String serverName = request.getServerName(); // localhost
        int serverPort = request.getServerPort(); // 8080
        String contextPath = request.getContextPath(); // 컨텍스트 경로
        String servletPath = request.getServletPath(); // 요청한 서블릿 경로
        String queryString = request.getQueryString(); // 쿼리 스트링

        // URL 조합
        StringBuilder url = new StringBuilder(scheme + "://" + serverName);
        if (serverPort != 80 && serverPort != 443) { // 기본 포트가 아닐 경우 포트 추가
            url.append(":").append(serverPort);
        }
        url.append(contextPath).append(servletPath);
        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }
}
