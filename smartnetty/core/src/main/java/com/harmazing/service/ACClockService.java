package com.harmazing.service;

import java.util.List;
import java.util.Map;

import com.harmazing.entity.ACClock;

public interface ACClockService {
	public Map<String,List<ACClock>> getACClockByUser(String userId);
	
	public List<ACClock> getClockSettingByDevice(String deviceId);
}
