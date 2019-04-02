package com.harmazing.spms.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.manager.LogManager;
import com.harmazing.spms.base.util.HttpRequestUtil;
import com.harmazing.spms.base.util.LogUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.common.log.ILog;

/**
 *LogFilter.java 
 *@author Zhaocaipeng
 * since 2014年3月12日 上午10:00:26
 */
@Component("logFilter")
public class LogFilter implements Filter, ILog {

	@Override
	public void destroy() {
	}

	@Override
	@Transactional
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		//不处理get请求
		if(HttpRequestUtil.METHOD_GET.equals(servletRequest.getMethod())) {
			chain.doFilter(request, response);
		}  else {
			LogEntity log = new LogEntity();
			String url = servletRequest.getRequestURI();
			if(url!=null && url.length() >=1) {
				url = url.substring(1);
				url = url.substring(url.indexOf("/"));
				if(LogUtil.getRequestControlMap().containsKey(url)) {
					
					LogManager logManager = SpringUtil.getBeanByName("logManager");
					
					log.setType(LogUtil.LOG_TYPE_ACCESS);
					
					log.setRemoteAddr(StringUtil.getRemoteAddr(servletRequest));
					
					log.setUserAgent(servletRequest.getHeader("user-agent"));
					
					log.setRequestUri(servletRequest.getRequestURI());
					
					log.setMethod(servletRequest.getMethod());
					
					log.setBusinessType(LogUtil.getRequestControlMap().get(url).getBusinessName());
					
					StringBuilder params = new StringBuilder();
					for (Object param : request.getParameterMap().keySet()){ 
						params.append("&" + param + "=");
						params.append(request.getParameter((String)param));
					}
					log.setParams(StringUtils.strip(params.toString().replaceFirst("&", "")));
			//		}
					log.setTerminalType(LogUtil.ACCESS_PC);
					if(url!=null && url.length() >=1) {
						url = url.substring(1);
						if(url.indexOf("/") != -1) {
							url = url.substring(0,url.indexOf("/"));
							if(LogUtil.ACCESS_PHONE_PREFIX.equals(url)) {
								log.setTerminalType(LogUtil.ACCESS_PHONE);
							}
						}
					}
					logManager.doSaveLog(log);
				}
			}
			try {
//				HttpResponseWrapper wrapper =  new HttpResponseWrapper(servletResponse);

//				chain.doFilter(request, wrapper);
				chain.doFilter(request, servletResponse);
//				String content = wrapper.toString();
				log.setMessage("success");
			} catch(Exception e) {
				e.printStackTrace();
				log.setMessage(e.getMessage());
				log.setException(e.getStackTrace().toString());
			}
			
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
}

