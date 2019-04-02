package com.harmazing.spms.portalcustom.manager;

import java.util.HashMap;
import java.util.Map;

import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.manager.IManager;

public interface SpmsPortalCustomManager extends IManager  {
	public Map<Integer, HashMap<String, String>> getSort(UserEntity user);
	
	public Map<String,String> getCanAddSort(UserEntity user);
	
	public Map<String,Object> addSort(UserEntity user,String divName);
	
	public Map<String,Object> deleteSort(UserEntity user,String divName);
	
	public Map<String,Object> changeSort(UserEntity user,Map<String,String> sort);
}
