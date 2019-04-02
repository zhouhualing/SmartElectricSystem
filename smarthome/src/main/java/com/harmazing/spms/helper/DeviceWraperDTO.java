package com.harmazing.spms.helper;

import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;

public class DeviceWraperDTO extends WraperDTO{
	private SpmsDeviceDTO device;

	public SpmsDeviceDTO getDevice() {
		return device;
	}

	public void setDevice(SpmsDeviceDTO dev) {
		this.device = dev;
	}
	
	public DeviceWraperDTO(){}
	public DeviceWraperDTO(SpmsDeviceDTO dev){
		this.device = dev;
	}
	
	public static DeviceWraperDTO getDefaultWraperDTO(){		
		DeviceWraperDTO userWraperDTO = new DeviceWraperDTO(new SpmsDeviceDTO());		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
