package com.harmazing.spms.potaldefault.manager;

import java.util.HashMap;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;

public interface SpmsPortalDefaultManager extends IManager  {
	public Map<String, HashMap<String, Object>> getDefault();
	
	public Map<String,Object> changeDefault(Map<String , String > info);
}
