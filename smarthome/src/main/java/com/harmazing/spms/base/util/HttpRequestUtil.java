package com.harmazing.spms.base.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

public class HttpRequestUtil {
	
	public static final String METHOD_GET = "GET";
	
	public static final String METHOD_POST = "POST";
	
	public static RequestMethod getRequestMethod(HttpServletRequest httpServletRequest) {
		RequestMethod requestMethod = null;
		switch (httpServletRequest.getMethod()) {
		
			case METHOD_GET: {
				requestMethod = RequestMethod.GET;
			}break;
			
			case METHOD_POST: {
				requestMethod = RequestMethod.POST;
			}break;	
			
			default: {
				requestMethod = RequestMethod.GET;
			}break;
		}
		
		return requestMethod;
	}
	
	
}
