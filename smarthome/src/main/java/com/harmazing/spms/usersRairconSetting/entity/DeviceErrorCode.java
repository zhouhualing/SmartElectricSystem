package com.harmazing.spms.usersRairconSetting.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="dict_device_error")
public class DeviceErrorCode  extends CommonEntity{
	
	@JoinColumn(name="errorCode")
	private String errorCode;
	@JoinColumn(name="errorDetail")
	private String errorDetail;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	
}
