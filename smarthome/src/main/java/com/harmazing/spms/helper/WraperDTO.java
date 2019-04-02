package com.harmazing.spms.helper;

import com.harmazing.spms.base.util.ErrorCodeConsts;

public class WraperDTO {

	private String status;
	private String description;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUserResult(String value) {
		this.setStatus(value);
		this.setDescription(ErrorCodeConsts.getValue(value));
	}

}
