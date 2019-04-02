package com.harmazing.spms.helper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.util.ErrorCodeConsts;

public class CurveWraperDTO extends WraperDTO {	
	private String deviceId;
	private Integer type; // 1 power 2 energy
	private String style;

	private Long endTime; //now
	
	private Integer dataLength;
	
	public Integer getDataLength() {
		return dataLength;
	}

	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}

	private List<Object[]> data;

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
		this.data = data;
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public Long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	public CurveWraperDTO(){}
	public CurveWraperDTO(List<Object[]>  data){
		this.setData(data);
	}
	
	public static CurveWraperDTO getDefaultWraperDTO(){
		List<Object[]> list = Lists.newArrayList();
		CurveWraperDTO userWraperDTO = new CurveWraperDTO(list);		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
