package com.harmazing.spms.system.manager;

import com.harmazing.spms.system.dto.SystemConfigDto;

public interface SystemConfigManager {
	/**
	 * 获取系统配置
	 * @param systemConfigDto
	 * @return
	 */
	public SystemConfigDto getSystemConfig(SystemConfigDto systemConfigDto);

	/**
	 * 保存系统配置
	 * @param systemConfigDto
	 * @return
	 * @throws Exception 
	 */
	public SystemConfigDto saveSystemConfig(SystemConfigDto systemConfigDto) throws Exception;
}
