package com.harmazing.spms.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_system_config")
public class SystemConfig {
	//配置名称
	@Id
	@Column(name="config_name")
	private String configName;
	//配置内容
	@Column(name="config_value")
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
