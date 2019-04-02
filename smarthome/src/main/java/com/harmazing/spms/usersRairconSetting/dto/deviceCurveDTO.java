package com.harmazing.spms.usersRairconSetting.dto;


import com.harmazing.spms.common.dto.IDTO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
public class deviceCurveDTO implements IDTO{
	
	private String id;
	private RairconSettingDTO rairconSettingDTO;//曲线表
	//用来设定本条设置作用在周几
	private Integer monday;//星期一
	private Integer tuesday;//星期二
	private Integer wednesday;//星期三
	private Integer thursday;//星期四
	private Integer friday;//星期五
	private Integer saturday;//星期六
	private Integer sunday;//星期日
	
	private SpmsDeviceDTO spmsDevice ;//设备表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RairconSettingDTO getRairconSettingDTO() {
		return rairconSettingDTO;
	}

	public void setRairconSettingDTO(RairconSettingDTO rairconSettingDTO) {
		this.rairconSettingDTO = rairconSettingDTO;
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

	public SpmsDeviceDTO getSpmsDevice() {
		return spmsDevice;
	}

	public void setSpmsDevice(SpmsDeviceDTO spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	
}
