package com.harmazing.spms.spmsuc.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class OpenNetworkDTO {

	private Integer type; //MAC:1,IP:2,SN:3 
	private String userId;
	private String gwId;
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}	
	
	public String getGwId() {
		return gwId;
	}
	
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	
}
