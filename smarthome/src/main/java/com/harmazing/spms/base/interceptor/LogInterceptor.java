package com.harmazing.spms.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.harmazing.spms.base.dao.LogDAO;
import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.util.LogUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;

/**
 * log intercepter.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class LogInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// doOther
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//doOther
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
			LogEntity log = new LogEntity();
			if(exception !=null) {
				log.setType(LogUtil.LOG_TYPE_EXCEPTION);
				log.setException(exception.getMessage());
			} else {
				log.setType(LogUtil.LOG_TYPE_ACCESS);
			}
			
			log.setRemoteAddr(StringUtil.getRemoteAddr(request));
			
			log.setUserAgent(request.getHeader("user-agent"));
			
			log.setRequestUri(request.getRequestURI());
			
			log.setMethod(request.getMethod());

//			if (user!=null && user.getId()!=null){
			
			StringBuilder params = new StringBuilder();
			for (Object param : request.getParameterMap().keySet()){ 
				params.append("&" + param + "=");
				params.append(request.getParameter((String)param));
			}
			log.setParams(StringUtils.strip(params.toString().replaceFirst("&", "")));
//			}
			
			LogDAO logDAO = (LogDAO)SpringUtil.getBeanByName("logDAO");
			logDAO.save(log);
	}

}
