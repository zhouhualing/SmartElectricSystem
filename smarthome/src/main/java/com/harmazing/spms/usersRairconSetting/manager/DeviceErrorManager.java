package com.harmazing.spms.usersRairconSetting.manager;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.usersRairconSetting.dto.DeviceErrorDTO;

public interface DeviceErrorManager extends IManager{
	public DeviceErrorDTO queryDeviceError(String deviceId);
}
