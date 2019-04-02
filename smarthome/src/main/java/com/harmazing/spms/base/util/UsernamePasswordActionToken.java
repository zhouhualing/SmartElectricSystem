package com.harmazing.spms.base.util;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordActionToken extends UsernamePasswordToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 602216024162790647L;
	
	/**
	 * 来源,如果是从PAD上来的，值是pad
	 */
	private String action;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public UsernamePasswordActionToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}
	
	    public UsernamePasswordActionToken(final String username, final String password) {
	        this(username, password != null ? password.toCharArray() : null, false, null);
	    }
	
}
