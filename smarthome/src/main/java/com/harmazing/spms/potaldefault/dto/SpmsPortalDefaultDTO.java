package com.harmazing.spms.potaldefault.dto;

import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.portalmodules.dto.SpmsPortalModulesDTO;

public class SpmsPortalDefaultDTO  extends CommonDTO {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色编码
	 */
	private String roleCode;
	
	/**
	 * 显示的模块
	 */
	private SpmsPortalModulesDTO modules;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public SpmsPortalModulesDTO getModules() {
		return modules;
	}

	public void setModules(SpmsPortalModulesDTO modules) {
		this.modules = modules;
	}
	
	
}
