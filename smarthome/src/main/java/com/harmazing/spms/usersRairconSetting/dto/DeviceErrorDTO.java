package com.harmazing.spms.usersRairconSetting.dto;

import java.sql.Date;

import com.harmazing.spms.common.entity.CommonEntity;

public class DeviceErrorDTO extends CommonEntity {
	private String deviceId;//设备表
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	private String errorCode;//异常代码
	private Date OccurredTime;//发生时间 
	private String errorExplain;//说明
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Date getOccurredTime() {
		return OccurredTime;
	}
	public void setOccurredTime(Date occurredTime) {
		OccurredTime = occurredTime;
	}

	public String getErrorExplain() {
		return errorExplain;
	}
	public void setErrorExplain(String errorExplain) {
		this.errorExplain = errorExplain;
	}
	
	
}
