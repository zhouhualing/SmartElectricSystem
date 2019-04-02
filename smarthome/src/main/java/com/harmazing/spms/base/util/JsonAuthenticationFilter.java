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
 * JsonAuthenticationFilter. 
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Service
public class JsonAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {


    	/* (non-Javadoc)
    	* @see org.apache.shiro.web.servlet.AdviceFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
    	*/
    	@Override
    	public void doFilterInternal(ServletRequest request,ServletResponse response, FilterChain chain)
    	    throws ServletException, IOException {
    	    HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    	    if("/cmcp/doLogin".equals(httpServletRequest.getRequestURI())&&("POST").equals(httpServletRequest.getMethod())) {
    		SecurityUtils.getSubject().logout();
    	    }
    	    super.doFilterInternal(request, response, chain);

    	}
    
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		UsernamePasswordActionToken token = new UsernamePasswordActionToken(username, password.toCharArray(), rememberMe, host);
		token.setAction(request.getParameter("action"));
		return token;
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		
		if("PHONE".equals(request.getParameter("client")==null?"":request.getParameter("client").toString())) {
			try {
				String json = "true";
				response.getWriter().write(json);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return false;
		} else {
			String url = "";
			if(UserUtil.getCurrentUser().getUserType() == 2) {
				//订户
				url = "/view/user/user_new.html";
			} else {
				//后台用户
				url = getSuccessUrl();
			}
			WebUtils.issueRedirect(request, response, url, null, true);
		}
		return false;
		
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if("PHONE".equals(request.getParameter("client")==null?"":request.getParameter("client").toString())) {
			try {
				String json = "false";
				response.getWriter().write(json);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return false;
		} else {
//			return super.onLoginFailure(token, e, request, response);
		    try {
			String error = "001";
			if(null != e && null != e.getMessage() && "user frozen".equals(e.getMessage())){
				error = "002";
			}
			WebUtils.issueRedirect(request, response, "/view/common/login.html?name=0001&err=" + error, null, true);
		    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		}
		return false;
	}
	
	/* (non-Javadoc)
	* @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	*/
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
	
	/* (non-Javadoc)
	* @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#cleanup(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Exception)
	*/
	@Override
	protected void cleanup(ServletRequest request, ServletResponse response,
	    Exception existing) throws ServletException, IOException {
	super.cleanup(request, response, existing);
	}
	
}