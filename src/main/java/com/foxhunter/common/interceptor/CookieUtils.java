package com.foxhunter.common.interceptor;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	public static final String USERINFO_COOKIE = "user_info";
	public static final int USERINFO_AGE = -1;
	public static void addCookie(String cookieValue,HttpServletResponse rsp) {
		try{
		  Cookie cookie = new Cookie(USERINFO_COOKIE,URLEncoder.encode(cookieValue,"utf-8"));
		  cookie.setMaxAge(USERINFO_AGE);
		  rsp.addCookie(cookie);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static String getCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		try{
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if (ck.getName().equals(USERINFO_COOKIE)) {
					return URLDecoder.decode(ck.getValue(),"utf-8");
				}
			}
		}}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
}
