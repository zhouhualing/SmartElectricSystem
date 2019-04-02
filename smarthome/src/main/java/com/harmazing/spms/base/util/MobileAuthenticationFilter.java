package com.harmazing.spms.base.util;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;



/**
 * MobileAuthenticationFilter. 
 * @author Hualing
 * @date 2015-11-30
 */
@Service
public class MobileAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
	    ServletResponse response) throws Exception {
	        if (isLoginRequest(request, response)) {
	            if (isLoginSubmission(request, response)) {
	                return executeLogin(request, response);
	            } else {
	                //allow them to see the login page ;)
	                return true;
	            }
	        } else {

	            saveRequestAndRedirectToLogin(request, response);
	            return false;
	        }
	}
	
}