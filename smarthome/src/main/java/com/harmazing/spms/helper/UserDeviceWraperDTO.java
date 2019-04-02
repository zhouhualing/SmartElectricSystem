package com.harmazing.spms.helper;

import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.user.dto.SpmsUserDeviceDTO;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserDeviceWraperDTO extends WraperDTO {
	private SpmsUserDeviceDTO userDevice;
	
	public SpmsUserDeviceDTO getUserDevice() {
		return userDevice;
	}
	public void setUserDevice(SpmsUserDeviceDTO userDevice) {
		this.userDevice = userDevice;
	}
	public UserDeviceWraperDTO(){}
	public UserDeviceWraperDTO(SpmsUserDeviceDTO userDevice){
		this.userDevice = userDevice;
	}
	
	public static UserDeviceWraperDTO getDefaultWraperDTO(){
		
		UserDeviceWraperDTO userWraperDTO = new UserDeviceWraperDTO(new SpmsUserDeviceDTO());		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
