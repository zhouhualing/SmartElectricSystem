package com.harmazing.spms.base.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.harmazing.spms.common.log.ILog;

/**
 * the util class of Spring,which can hold the ApplicationContext of Spring.
 * 
 */
public class SpringUtil implements ApplicationContextAware, DisposableBean, ILog {

	/**
	 * spring's ApplicationContext.
	 */
	private static ApplicationContext applicationContext = null;
	
	private static Map <String, HandlerMethod> webApplicationContext = null; 
	
	/**
	 * get the ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContextInjected();
		return applicationContext;
	}

	/**
	 * get the bean from the ApplicationContext of SpringHelper by bean's name.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBeanByName(String name) {
		checkApplicationContextInjected();
		return (T) applicationContext.getBean(name);
	}
	
	/**
	 * get the bean from the ApplicationContext of SpringHelper by bean's type.
	 */
	public static <T> T getBeanByType(Class<T> requiredType) {
		checkApplicationContextInjected();
		return applicationContext.getBean(requiredType);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext nowApplicationContext)
			throws BeansException {
		if (SpringUtil.applicationContext != null) {
			logger.warn("SpringHelper's ApplicationContext will be overrided, original ApplicationContext:" + applicationContext);
		} else {
			logger.info("Spring Helper init ApplicationContext");
		}

		SpringUtil.applicationContext = nowApplicationContext;
		
		//load properties
		try {
			PropertyUtil.loadProperties();
		} catch (IOException e) {
			logger.error("properties load failture");
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		//load query
		try {
			QueryUtil.loadQuerys();
		} catch (FileNotFoundException e) {
			logger.error("query config:file not find");
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			logger.error("query config:IO exception");
			e.printStackTrace();
			throw new RuntimeException();
		} catch (JAXBException e) {
			logger.error("query config:jaxb exception");
			e.printStackTrace();
			throw new RuntimeException();
		}
		try {
			TaskUtil.doExecuteTask();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		SpringUtil.clearHolder();
		
	}
	
	/**
	 * clear the applicationContext of SpringHelper to null.
	 */
	public static void clearHolder() {
		logger.debug("clear the applicationContext of SpringHelper:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * check if ApplicationContext is null
	 */
	private static void checkApplicationContextInjected() {
		Validate.validState(applicationContext != null, "applicaitonContext not injected, please define SpringContextHolder at applicationContext.xml.");
	}

	
	public static Map<String, HandlerMethod> getWebApplicationContext(HttpServletRequest httpServletRequest) {
		if(webApplicationContext != null) {
			return webApplicationContext;
		} else {
			WebApplicationContext webApplicationContext = RequestContextUtils.getWebApplicationContext(httpServletRequest);
			Map <RequestMappingInfo, HandlerMethod>  requestMapping = webApplicationContext.getBean(org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class).getHandlerMethods();
			setWebApplicationContext(requestMapping);
		}
		return webApplicationContext;
	}
	
	/**
	 * set webApplication
	 * @param webApplicationContext
	 */
	private synchronized static  void  setWebApplicationContext(Map <RequestMappingInfo, HandlerMethod> webApplicaitonInfo) {
		webApplicationContext = new HashMap<String, HandlerMethod>();
		for(Map.Entry<RequestMappingInfo, HandlerMethod> set : webApplicaitonInfo.entrySet()) {
			StringBuffer str = new StringBuffer();
			for(String tempStr : set.getKey().getPatternsCondition().getPatterns()) {
				str.append(tempStr);
			}
			webApplicationContext.put(str.toString(), set.getValue());
		}
	}
	
}
