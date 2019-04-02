package com.harmazing.spms.usersRairconSetting.entity;


import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.SpmsUser;

import javax.persistence.*;
/**
 * 2015/3/25
 */
@Entity
@Table(name="RairconCurve_Clocksetting")
public class Clocksetting extends CommonEntity {
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name = "raircon_Setting_id")
	private RairconSetting rairconSetting;//曲线表
	private Integer temp ;//温度
	private String clocking;//设置时间
	private Integer on_off ;//开关(1：开启；0：关闭)
	private Integer windspeed ;//风速 (0,1,2......6)d
	private Integer mode ;//空调模式(制冷:1，制热:2，送风:3，除湿:4，自动:0)
	private Integer startend;//表示当前定时设置启用还是未启用……
	/*
	 * 用户ID（fpms）
	 */
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name = "spms_userId")
	private SpmsUser spmsUser;
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name = "spmsDevice_id")
    private SpmsAirCondition spmsDevice ;//设备表
	
	@JoinColumn(name = "alone")
	private Integer alone;//表示当前定时设置是否为独立的……
	
	@JoinColumn(name = "monday")
	private Integer monday;//星期一
	@JoinColumn(name = "tuesday")
	private Integer tuesday;//星期二
	@JoinColumn(name = "wednesday")
	private Integer wednesday;//星期三
	@JoinColumn(name = "thursday")
	private Integer thursday;//星期四
	@JoinColumn(name = "friday")
	private Integer friday;//星期五
	@JoinColumn(name = "saturday")
	private Integer saturday;//星期六
	@JoinColumn(name = "sunday")
	private Integer sunday;//星期日
	
	public RairconSetting getRairconSetting() {
		return rairconSetting;
	}
	public void setRairconSetting(RairconSetting rairconSetting) {
		this.rairconSetting = rairconSetting;
	}

	public Integer getTemp() {
		return temp;
	}
	public void setTemp(Integer temp) {
		this.temp = temp;
	}
	public String getClocking() {
		return clocking;
	}
	public void setClocking(String clocking) {
		this.clocking = clocking;
	}
	public Integer getOn_off() {
		return on_off;
	}
	public void setOn_off(Integer on_off) {
		this.on_off = on_off;
	}
	public Integer getWindspeed() {
		return windspeed;
	}
	public void setWindspeed(Integer windspeed) {
		this.windspeed = windspeed;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Integer getStartend() {
		return startend;
	}
	public void setStartend(Integer startend) {
		this.startend = startend;
	}
	public Integer getMonday() {
		return monday;
	}
	public void setMonday(Integer monday) {
		this.monday = monday;
	}
	public Integer getTuesday() {
		return tuesday;
	}
	public void setTuesday(Integer tuesday) {
		this.tuesday = tuesday;
	}
	public Integer getWednesday() {
		return wednesday;
	}
	public void setWednesday(Integer wednesday) {
		this.wednesday = wednesday;
	}
	public Integer getThursday() {
		return thursday;
	}
	public void setThursday(Integer thursday) {
		this.thursday = thursday;
	}
	public Integer getFriday() {
		return friday;
	}
	public void setFriday(Integer friday) {
		this.friday = friday;
	}
	public Integer getSaturday() {
		return saturday;
	}
	public void setSaturday(Integer saturday) {
		this.saturday = saturday;
	}
	public Integer getSunday() {
		return sunday;
	}
	public void setSunday(Integer sunday) {
		this.sunday = sunday;
	}
	public SpmsAirCondition getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsAirCondition spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	public Integer getAlone() {
		return alone;
	}
	public void setAlone(Integer alone) {
		this.alone = alone;
	}
	public SpmsUser getSpmsUser() {
		return spmsUser;
	}
	public void setSpmsUser(SpmsUser spmsUser) {
		this.spmsUser = spmsUser;
	}
	
}
