package com.harmazing.spms.portalcustom.dto;

import java.util.Map;

import com.harmazing.spms.common.dto.CommonDTO;

public class SpmsPortalCustomDTO  extends CommonDTO {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 返回自定义模块显示
	 * 如：
	 * {sort,modules}
	 * 例如：
	 * {
	 * 		sort,{divName,divSort}
	 * }
	 */
	private Map<Object,Object> modulesSorts;

	public Map<Object, Object> getModulesSorts() {
		return modulesSorts;
	}

	public void setModulesSorts(Map<Object, Object> modulesSorts) {
		this.modulesSorts = modulesSorts;
	}
	
}
