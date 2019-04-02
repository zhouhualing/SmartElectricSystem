package com.harmazing.spms.main.manager;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;

public interface SpmsStatisticsManager extends IManager {
	public List<Map<String,Object>> userCountByArea(int type);
	
	public List<Map<String,Object>> userCountByRule();
	
	public List<Map<String,Object>> userCountByOnlineStatus();
	
	public List<Map<String,Object>> userIncrementByDate() throws ParseException;
	
	public List<Map<String,Object>> deviceByModel();
	
	public List<Map<String,Object>> deviceByOptStatus();
	
	public List<Map<String,Object>> userCountByProductRemaining() throws ParseException ;

	public Map<String, Object> getSoftwareVersionStatisticalData();
}
