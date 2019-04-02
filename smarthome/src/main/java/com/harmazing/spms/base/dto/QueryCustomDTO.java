package com.harmazing.spms.base.dto;

import com.harmazing.spms.common.dto.CommonDTO;

public class QueryCustomDTO extends CommonDTO {
	
	/**
	 * the queryId of xml file.
	 */
	private String queryId;
	
	/**
	 * the display of columns.
	 */
	private String columnNames;
	
	/**
	 * usercode
	 */
	private String userCode;

	public String getQueryId() {
		return queryId;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	
}
