package com.harmazing.spms.base.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

/**
 * CXFAuthcFilter.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class CXFAuthcFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		chain.doFilter(request, response);
	}

}
