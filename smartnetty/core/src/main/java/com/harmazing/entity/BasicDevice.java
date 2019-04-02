package com.harmazing.entity;

import java.sql.Timestamp;

public class BasicDevice {
	protected String id;
	protected String type;
	protected String mac;
	protected String sn;
	protected String hardwareVer;
	protected String softwareVer;
	protected String currSoftwareVer;
	protected Integer disabled;
	protected Integer status;
	protected Integer operStatus;
	protected Integer onOff;
	protected Integer mode;
	protected Timestamp startTime;
	protected String vendor;
	protected String gw_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getHardwareVer() {
		return hardwareVer;
	}
	public void setHardwareVer(String hardwareVer) {
		this.hardwareVer = hardwareVer;
	}
	public String getSoftwareVer() {
		return softwareVer;
	}
	public void setSoftwareVer(String softwareVer) {
		this.softwareVer = softwareVer;
	}
	public String getCurrSoftwareVer() {
		return currSoftwareVer;
	}
	public void setCurrSoftwareVer(String currSoftwareVer) {
		this.currSoftwareVer = currSoftwareVer;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getOperStatus() {
		return operStatus;
	}
	public void setOperStatus(Integer operStatus) {
		this.operStatus = operStatus;
	}
	public Integer getOnOff() {
		return onOff;
	}
	public void setOnOff(Integer onOff) {
		this.onOff = onOff;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getGw_id() {
		return gw_id;
	}
	public void setGw_id(String gw_id) {
		this.gw_id = gw_id;
	}
	
	
	
	
}
