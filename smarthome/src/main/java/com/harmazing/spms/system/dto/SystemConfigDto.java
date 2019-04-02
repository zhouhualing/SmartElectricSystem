package com.harmazing.spms.system.dto;


public class SystemConfigDto {
	//配置名称
	private String configName;
	//配置内容
	private String configValue;
	
	
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
	
}
