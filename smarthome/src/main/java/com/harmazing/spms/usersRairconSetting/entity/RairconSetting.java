package com.harmazing.spms.usersRairconSetting.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.SpmsUser;

import javax.persistence.*;

@Entity
@Table(name="spms_Raircon_Setting")
public class RairconSetting extends CommonEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 用户ID（fpms）
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "spms_userId")
	private SpmsUser spmsUser;
	
	/* 用户ID */
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "sys_user_id")
	private UserEntity user;
	private String week ;//表示作用在周几(1：周一，2：周二  ……  7：周日,0:未作用)(1,2,3,4:代表周一到周四启用此曲线)
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name = "spmsDevice_id")
    private SpmsAirCondition spmsDevice ;//设备表
	
	@JoinColumn(name = "startend")
	private Integer startend;//表示当前设置计划是否被启用
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
	/*
	 * 曲线相关
	 */
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "raircon_Setting_id")
	private List<Clocksetting> clocksettingList;
	
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "curve_id")
	private Set<deviceCurve> deviceCurveSet = new HashSet<deviceCurve>();
	
	public SpmsUser getSpmsUser() {
		return spmsUser;
	}
	public void setSpmsUser(SpmsUser spmsUser) {
		this.spmsUser = spmsUser;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
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
	public List<Clocksetting> getClocksettingList() {
		return clocksettingList;
	}
	public void setClocksettingList(List<Clocksetting> clocksettingList) {
		this.clocksettingList = clocksettingList;
	}
	public SpmsAirCondition getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsAirCondition spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getStartend() {
		return startend;
	}
	public void setStartend(Integer startend) {
		this.startend = startend;
	}
	public Set<deviceCurve> getDeviceCurveSet() {
		return deviceCurveSet;
	}
	public void setDeviceCurveSet(Set<deviceCurve> deviceCurveSet) {
		this.deviceCurveSet = deviceCurveSet;
	}

	
}
