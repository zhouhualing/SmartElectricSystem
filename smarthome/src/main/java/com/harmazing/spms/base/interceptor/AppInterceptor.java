package com.harmazing.spms.base.interceptor;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.org.eclipse.jdt.core.IField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.harmazing.spms.base.dao.LogDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.QueryManager;
import com.harmazing.spms.base.util.AESUtils;
import com.harmazing.spms.base.util.JedisClient;
import com.harmazing.spms.base.util.LogUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.UserUtil;

public class AppInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String token = request.getParameter("token");
		if (StringUtil.isNUllOrEmpty(token))
			return false;
		
		//TODO hard code,not know why PUT is different with other methods
		if(request.getMethod().toLowerCase().equals("put")){
			token = URLDecoder.decode(token, "UTF-8");
		}
		try {
			Map<String, Object> map;
			map = AESUtils.ParseDecrypt(token);
			//double check
			if(map==null || map.isEmpty() || !map.containsKey(AESUtils.IConstants.PARSE_DECRYPT_CONTENT)){
				return false;
			}

			String content = (String) map.get(AESUtils.IConstants.PARSE_DECRYPT_CONTENT);
			if (!StringUtil.isNUllOrEmpty(content)) {
				String userId = content;
				if (!StringUtil.isNUllOrEmpty(userId)) {
					UserDAO  userDAO = SpringUtil.getBeanByName("userDAO");
					UserEntity ue = userDAO.getByMobile(userId);
					if(ue != null)
					    return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}
}
