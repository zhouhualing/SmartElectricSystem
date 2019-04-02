package com.harmazing.spms.helper;

import java.util.List;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserDeviceRespWraperDTO extends WraperDTO{
private List<SpmsDeviceDTO> devices = null;
private String userCode;

public String getUserCode() {
	return userCode;
}

public void setUserCode(String userCode) {
	this.userCode = userCode;
}

public List<SpmsDeviceDTO> getDevices() {
	return devices;
}

public void setDevices(List<SpmsDeviceDTO> devices) {
	this.devices = devices;
}
public static UserDeviceRespWraperDTO getDefaultWraperDTO(){
	
	UserDeviceRespWraperDTO userWraperDTO = new UserDeviceRespWraperDTO();
	List<SpmsDeviceDTO> devicesDTO = Lists.newArrayList();
	userWraperDTO.setDevices(devicesDTO);
	userWraperDTO.setUserResult(ErrorCodeConsts.Success);
	return userWraperDTO;
}

}
