/**
 * 
 */
package com.harmazing.spms.base.util;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年9月3日
 */
public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean{

    
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {

        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }

        FilterChainManager manager = createFilterChainManager();

        //Expose the constructed FilterChainManager by first wrapping it in a
        // FilterChainResolver implementation. The AbstractShiroFilter implementations
        // do not know about FilterChainManagers - only resolvers:
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        //Now create a concrete ShiroFilter instance and apply the acquired SecurityManager and built
        //FilterChainResolver.  It doesn't matter that the instance is an anonymous inner class
        //here - we're just using it because it is a concrete AbstractShiroFilter instance that accepts
        //injection of the SecurityManager and FilterChainResolver:
        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }
    
    
    @Override
    public Class <SpringShiroFilter> getObjectType() {
        return SpringShiroFilter.class;
    }
    
    private static final class SpringShiroFilter extends AbstractShiroFilter {

	
	/* (non-Javadoc)
	 * @see org.apache.shiro.web.servlet.AbstractShiroFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doFilterInternal(ServletRequest servletRequest,
		ServletResponse servletResponse, final FilterChain chain)
		throws ServletException, IOException {
	        Throwable t = null;

	        try {
	            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
	            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

	            final Subject subject = createSubject(request, response);

	            //noinspection unchecked
	            subject.execute(new Callable() {
	                public Object call() throws Exception {
	                    updateSessionLastAccessTime(request, response);
	                    executeChain(request, response, chain);
	                    return null;
	                }
	            });
	        } catch (ExecutionException ex) {
	            t = ex.getCause();
	        } catch (Throwable throwable) {
	            t = throwable;
	        }

	        if (t != null) {
	            if (t instanceof ServletException) {
	                throw (ServletException) t;
	            }
	            if (t instanceof IOException) {
	                throw (IOException) t;
	            }
	            //otherwise it's not one of the two exceptions expected by the filter method signature - wrap it in one:
	            String msg = "Filtered request failed.";
	            throw new ServletException(msg, t);
	        }
	}
	
	@Override
	protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
	    return new MyHttpServletRequest(orig, getServletContext(), isHttpSessions());
//	    return new ShiroHttpServletRequest(orig, getServletContext(), isHttpSessions());
	}
	
        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }

        
    }
}
