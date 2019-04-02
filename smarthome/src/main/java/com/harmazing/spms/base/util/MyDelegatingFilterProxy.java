/**
 * 
 */
package com.harmazing.spms.base.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014-07-17
 */
public class MyDelegatingFilterProxy extends DelegatingFilterProxy{

    /* (non-Javadoc)
     * @see org.springframework.web.filter.DelegatingFilterProxy#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doFilter(request, response, filterChain);
        
    }
}
