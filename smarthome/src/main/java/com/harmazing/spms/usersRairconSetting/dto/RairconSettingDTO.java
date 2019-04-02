package com.harmazing.spms.usersRairconSetting.dto;

import java.util.Set;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.common.dto.IDTO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.usersRairconSetting.entity.deviceCurve;
public class RairconSettingDTO implements IDTO{
	private String id;
	private UserDTO user;
	private String week ;//表示作用在周几(1：周一，2：周二  ……  7：周日,0:未作用)(1,2,3,4:代表周一到周四启用此曲线)
	private SpmsDeviceDTO spmsDevice;
	private Integer monday;//星期一
	private Integer tuesday;//星期二
	private Integer wednesday;//星期三
	private Integer thursday;//星期四
	private Integer friday;//星期五
	private Integer saturday;//星期六
	private Integer sunday;//星期日
	/*private Set<deviceCurveDTO> deviceCurveSet;*/
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public SpmsDeviceDTO getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsDeviceDTO spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
/*	public Set<deviceCurveDTO> getDeviceCurveSet() {
		return deviceCurveSet;
	}
	public void setDeviceCurveSet(Set<deviceCurveDTO> deviceCurveSet) {
		this.deviceCurveSet = deviceCurveSet;
	}*/
	
}
