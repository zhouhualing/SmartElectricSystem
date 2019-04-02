package com.harmazing.spms.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dao.LogDAO;
import com.harmazing.spms.base.dao.RequestControlDAO;
import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.entity.RequestControlEntity;

/**
 * dict util.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class LogUtil {
	
	/**
	 * access success type
	 */
	public static String LOG_TYPE_ACCESS = "0001";
	
	/**
	 * access error type
	 */
	public static String LOG_TYPE_EXCEPTION = "0002";
	
	/**
	 * phone access prefix
	 */
	public static String ACCESS_PHONE_PREFIX= "phone";
	
	/**
	 * pc access
	 */
	public static String ACCESS_PC = "0001";
	
	/**
	 * phone access
	 */
	public static String ACCESS_PHONE = "0002";
	

	
	/**
	 * 需要处理的url及其对应的业务信息
	 */
	private static Map <String, RequestControlEntity> requestControlMap = new HashMap<String,RequestControlEntity>();
	
	public static void saveLog(LogType logType, String message) {
		
		LogEntity log = new LogEntity();

		log.setType(LOG_TYPE_ACCESS);
		
		LogDAO logDAO = (LogDAO)SpringUtil.getBeanByName("logDAO");
		logDAO.save(log);		
	}
	
	public static enum  LogType{
		LOGINOROUT("0001"),
		BUSINESS("0002");
		
		String value;
		
		private LogType(String value) {
			this.value = value;
		}
		
	}

	@SuppressWarnings("unchecked")
	public static Map<String, RequestControlEntity> getRequestControlMap() {
		if(requestControlMap.size() == 0) {
			RequestControlDAO requestControlDAO = SpringUtil.getBeanByName("requestControlDAO");
			List <RequestControlEntity> requestControlEntities =  requestControlDAO.cacheFindRequestControl();
			setRequestControlMap( CollectionUtil.extractToMap(requestControlEntities, "requestUrl"));
		}
		return requestControlMap;
	}

	private static void setRequestControlMap(Map<String, RequestControlEntity> requestControlMap) {
		LogUtil.requestControlMap = requestControlMap;
	}
	
}

