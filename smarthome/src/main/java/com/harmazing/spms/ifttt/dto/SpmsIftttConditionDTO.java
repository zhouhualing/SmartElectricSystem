package com.harmazing.spms.ifttt.dto;

import java.io.Serializable;
import java.util.List;

public class SpmsIftttConditionDTO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	private String time;//hh:mm:ss

	private String period;//day; mon,tue,wed,thu,fri,sat,sun; 
	
	private List<SpmsIftttDeviceDTO> deviceDTOs;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}

	public List<SpmsIftttDeviceDTO> getDeviceDTOs() {
		return deviceDTOs;
	}

	public void setDeviceDTOs(List<SpmsIftttDeviceDTO> deviceDTOs) {
		this.deviceDTOs = deviceDTOs;
	}
}
