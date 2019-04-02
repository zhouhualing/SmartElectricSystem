package com.harmazing.spms.usersRairconSetting.dto;


import com.harmazing.spms.common.dto.IDTO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
public class ClocksettingDTO implements IDTO{
	private String id;
	private RairconSettingDTO rairconSettingDTO;//曲线表
	private Integer Temp ;//温度
	private String clocking;//设置时间
	private Integer on_off ;//开关(1：开启；0：关闭)
	private Integer windspeed ;//风速 (0,1,2......)d
	private Integer Mode ;//空调模式(制冷:1，制热:2，送风:3，除湿:4，自动:0)
	private Integer startend;//表示当前定时设置启用还是未启用……
	//用来设定本条设置作用在周几
	private Integer monday;//星期一
	private Integer tuesday;//星期二
	private Integer wednesday;//星期三
	private Integer thursday;//星期四
	private Integer friday;//星期五
	private Integer saturday;//星期六
	private Integer sunday;//星期日
	
	private SpmsDeviceDTO spmsDevice ;//设备表
	private Integer alone;//表示当前定时设置是否为独立的……
	
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
	
	public Integer getTemp() {
		return Temp;
	}
	public void setTemp(Integer temp) {
		Temp = temp;
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
		return Mode;
	}
	public void setMode(Integer mode) {
		Mode = mode;
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
	public SpmsDeviceDTO getSpmsDevice() {
		return spmsDevice;
	}
	public void setSpmsDevice(SpmsDeviceDTO spmsDevice) {
		this.spmsDevice = spmsDevice;
	}
	public Integer getAlone() {
		return alone;
	}
	public void setAlone(Integer alone) {
		this.alone = alone;
	}
	
}
