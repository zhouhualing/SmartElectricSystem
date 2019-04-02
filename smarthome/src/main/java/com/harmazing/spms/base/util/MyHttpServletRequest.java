/**
 * 
 */
package com.harmazing.spms.base.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年9月3日
 */
public class MyHttpServletRequest extends ShiroHttpServletRequest{

    /**
     * @param wrapped
     * @param servletContext
     * @param httpSessions
     */
    public MyHttpServletRequest(HttpServletRequest wrapped,
	    ServletContext servletContext, boolean httpSessions) {
	super(wrapped, servletContext, httpSessions);
	if(wrapped.getRequestURI().startsWith("/cmcp/doLogin")) {
	    this.setAttribute("_jit_userinfo", wrapped.getSession().getAttribute("_jit_userinfo"));
	}
    }

}
