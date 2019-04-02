package com.harmazing.spms.task;

import java.util.List;

import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.entity.SpmsUser;
//import com.harmazing.spms.user.entity.SpmsUserProductBinding;

public class UserData {
	/**
	 * 获取系统中所有用户
	 * @return
	 */
	public List<SpmsUser> getAllUser(){
		SpmsUserDAO spmsUserDAO = (SpmsUserDAO)SpringUtil.getBeanByName("userDAO");
		return spmsUserDAO.findAll();
	}
	/**
	 * 获取该用户所有的产品与订户
	 * @param userId
	 */
//	public List<SpmsUserProductBinding> getAllDevice(String userId){
//		SpmsUserProductBindingDAO supbd = (SpmsUserProductBindingDAO)SpringUtil
//				.getBeanByName("userProductBindingDAO");
//		 return supbd.getUserBinding(userId, null);
//	}
}
