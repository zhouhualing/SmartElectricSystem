package com.harmazing.spms.ifttt.dto;

import java.io.Serializable;

public class SpmsIftttDeviceDTO implements Serializable {
	private String mac;
	private Integer type;
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
