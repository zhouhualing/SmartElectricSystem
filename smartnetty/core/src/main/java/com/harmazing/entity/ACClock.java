package com.harmazing.entity;

public class ACClock {
	/**
	 * 应用日期：星期一：1 ；星期二：2；星期三：4；星期四：8；星期五：16；星期六：32；星期日:64;
	 * 哪天应用就加上该天对应的数值
	 */	
	private int appDate;
	private String deviceMac;
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String string) {
		this.deviceMac = string;
	}
	public int getAppDate() {
		return appDate;
	}
	public void setAppDate(int appDate) {
		this.appDate = appDate;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getAcState() {
		return acState;
	}
	public void setAcState(int acState) {
		this.acState = acState;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getFanMode() {
		return fanMode;
	}
	public void setFanMode(int fanMode) {
		this.fanMode = fanMode;
	}
	public int getOperMode() {
		return operMode;
	}
	public void setOperMode(int operMode) {
		this.operMode = operMode;
	}
	//开始时间
	private long startTime;
	//空调开关状态
	private int acState;
	//空调设定温度
	private int temperature;
	//空调风速模式
	private int fanMode;
	//空调运行模式
	private int operMode;
}
