package com.harmazing.spms.device.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SpmsDeviceData {
	private Integer status;// 0离线，1在线，2异常
	private Integer onOff;
	private Integer temp;
	private Integer acTemp;
	private Integer floorTemp;
	private Integer upperTemp;
	private Integer mode;
	private Integer speed;
	private String deviceId;
	private Integer type;
	private String mac;
	
	private String gwMac;	

	private Integer remain;

	/* 功率刷新 */
	private BigDecimal power;
	private Date startTime;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getGwMac() {
		return gwMac;
	}

	public void setGwMac(String gwMac) {
		this.gwMac = gwMac;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOnOff() {
		return onOff;
	}

	public void setOnOff(Integer onOff) {
		this.onOff = onOff;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

	public Integer getAcTemp() {
		return acTemp;
	}

	public void setAcTemp(Integer acTemp) {
		this.acTemp = acTemp;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
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

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public BigDecimal getPower() {
		return power;
	}

	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Integer getFloorTemp() {
		return floorTemp;
	}

	public void setFloorTemp(Integer floorTemp) {
		this.floorTemp = floorTemp;
	}

	public Integer getUpperTemp() {
		return upperTemp;
	}

	public void setUpperTemp(Integer upperTemp) {
		this.upperTemp = upperTemp;
	}
}
