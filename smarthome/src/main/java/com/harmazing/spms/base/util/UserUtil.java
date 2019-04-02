package com.harmazing.spms.base.util;

import java.util.Map;

import org.apache.logging.log4j.core.tools.Generate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;

/**
 * user util.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class UserUtil {
	
	/**
	 * @return usercode
	 */
	public static String getCurrentUserCode() {
		Subject subject = SecurityUtils.getSubject();
		if(subject == null) {
			return null;
		}
		return (String)subject.getPrincipal();
	}
	
	/**
	 * @return user
	 */
	public static UserEntity getCurrentUser() {
		UserDAO userDAO = (UserDAO)SpringUtil.getBeanByName("userDAO");
		String userCode = getCurrentUserCode();
		if(userCode == null) {
			return null;
		}
		UserEntity userEntity = userDAO.findByUserCode(userCode);
		return userEntity;
	}
	
	//init default result of operation	
	public  static Map<String, Object> GenerateResult() {
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("success", true);
		result.put("code",201);
		return result;
	}
	
}
