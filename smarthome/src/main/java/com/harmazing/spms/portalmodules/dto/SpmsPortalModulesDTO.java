package com.harmazing.spms.portalmodules.dto;

import com.harmazing.spms.common.dto.CommonDTO;

public class SpmsPortalModulesDTO  extends CommonDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * DIV 的 名字
	 */
	private String divName;
	/**
	 * DIV title 的名字， 既图表的中文名
	 */
	private String divTitle;
	public String getDivName() {
		return divName;
	}
	public void setDivName(String divName) {
		this.divName = divName;
	}
	public String getDivTitle() {
		return divTitle;
	}
	public void setDivTitle(String divTitle) {
		this.divTitle = divTitle;
	}
	
}
