package com.harmazing.spms.helper;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.user.dto.SpmsUserDeviceDTO;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserDevicesWraperDTO extends WraperDTO {
	private List<SpmsUserDeviceDTO> userDevices;

	/**
	 * @return the userDevice
	 */
	public List<SpmsUserDeviceDTO> getUserDevices() {
		return userDevices;
	}

	/**
	 * @param userDevice the userDevice to set
	 */
	public void setUserDevices(List<SpmsUserDeviceDTO> userDevices) {
		this.userDevices = userDevices;
	}
	
	public UserDevicesWraperDTO(){}
	public UserDevicesWraperDTO(List<SpmsUserDeviceDTO>  userDevices){
		this.userDevices = userDevices;
	}
	
	public static UserDevicesWraperDTO getDefaultWraperDTO(){
		
		UserDevicesWraperDTO userWraperDTO = new UserDevicesWraperDTO(Lists.newArrayList());	
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
