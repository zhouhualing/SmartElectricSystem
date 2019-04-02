/**
 * 
 */
package com.harmazing.spms.base.dto;

import com.harmazing.spms.common.dto.CommonDTO;

public class PhoneCheckDTO extends CommonDTO {
	
	private String status;
	
	private Integer page;
	
	private String searchName;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	
}
