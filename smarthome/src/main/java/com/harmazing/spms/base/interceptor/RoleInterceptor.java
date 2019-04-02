package com.harmazing.spms.base.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.harmazing.spms.base.dao.LogDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.LogUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.UserUtil;

/**
 * role intercepter.
 * @author yyx
 * since 2015年3月19日
 */
public class RoleInterceptor implements HandlerInterceptor {
	@Autowired
	private QueryDAO queryDAO;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// doOther
		boolean falg;
		UserEntity user = UserUtil.getCurrentUser();
		List<RoleEntity> rlist = user.getRoleEntities();
		String  url=request.getServletPath().toString();
		//String[] urlList = url.split("/");
		for(int i = 0 ; i < rlist.size() ; i ++){
			//按照配置的权限和路径去tb_user_permission表里比对，
			
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//doOther
		
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}


}
