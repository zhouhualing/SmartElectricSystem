package com.harmazing.spms.usersRairconSetting.entity;
import java.sql.Date;

import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import javax.persistence.*;

@Entity
@Table(name="spms_device_error")
public class DeviceErrorRecord extends CommonEntity {
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name = "deviceId")
    private SpmsAirCondition spmsDevice;//设备表
	private String errorCode;//异常代码
	
	
	public SpmsAirCondition getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsAirCondition spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
