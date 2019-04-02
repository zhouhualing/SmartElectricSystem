package com.harmazing.spms.usersRairconSetting.entity;


import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.SpmsUser;

import javax.persistence.*;
@Entity
@Table(name="spms_device_curve")
public class deviceCurve extends CommonEntity {
	
	@ManyToOne
	@JoinColumn(name="spmsDevice_id")
	private SpmsAirCondition spmsDevice;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="curve_id")
	private RairconSetting rairconSetting;
	
	@Column(name = "monday")
	private Integer monday;//星期一
	@Column(name = "tuesday")
	private Integer tuesday;//星期二
	@Column(name = "wednesday")
	private Integer wednesday;//星期三
	@Column(name = "thursday")
	private Integer thursday;//星期四
	@Column(name = "friday")
	private Integer friday;//星期五
	@Column(name = "saturday")
	private Integer saturday;//星期六
	@Column(name = "sunday")
	private Integer sunday;//星期日
	
	public SpmsAirCondition getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsAirCondition spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	public RairconSetting getRairconSetting() {
		return rairconSetting;
	}
	public void setRairconSetting(RairconSetting rairconSetting) {
		this.rairconSetting = rairconSetting;
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
	
}
