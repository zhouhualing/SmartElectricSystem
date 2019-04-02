 package com.harmazing.spms.spmsuc.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.collect.Lists;

public class SpmsUserDataDTO {
	//设备类型
	public static final Integer AC_TYPE = 2;
	public static final Integer DOOR_TYPE = 3;
	public static final Integer WIN_TYPE = 4;
	
	//电量
	public static final Integer REMAIN_FULL = 1;
	public static final Integer REMAIN_HIGH = 2;
	public static final Integer REMAIN_MIDDLE = 3;
	public static final Integer REMAIN_LOW = 4;
	public static final Integer REMAIN_NONE = 5;
	
	//设备ID
	private String deviceId;
	//设备SN
	private String sn;
	//设备mac
	private String mac;
	//设备类型(门、窗、空调)
	private Integer deviceType;	
	//设备名称
	private String deviceName;
	//开关状态
	private Integer onoffStatus;
	//室内温度
	private Integer rt;
	//设定温度
	private Integer act;
	//模式
	private Integer mode;
	//风速
	private Integer speed;
	//电量
	private Integer remain;
	//用电记录与开关记录
	private List<Object[]> datas = Lists.newArrayList();
	
	/*设备配置信息*/
	//制热最高温度
	private Integer maxTemp;
	//制冷最低温度
	private Integer minTemp;
	//允许制热否（1允许 、0不允许）
	private Integer allowHeat;
	
	//设备状态
	private Integer status;// 0离线，1在线，2异常
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Integer getOnoffStatus() {
		return onoffStatus;
	}
	public void setOnoffStatus(Integer onoffStatus) {
		this.onoffStatus = onoffStatus;
	}
	public Integer getRt() {
		return rt;
	}
	public void setRt(Integer rt) {
		this.rt = rt;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

/*	@JsonIgnore
	public String getRtHtml(){
		int ten = rt/10;
		int unit = rt%10;
		if(ten == 0){
			System.out.println("<span class='wd_"+unit+"'></span>");
			return "<span class='wd_"+ten+"'></span><span class='wd_"+unit+"'></span>";
		}else{
			System.out.println("<span class='wd_"+ten+"'></span><span class='wd_"+unit+"'></span>");
			return "<span class='wd_"+ten+"'></span><span class='wd_"+unit+"'></span>";
		}
	}
	
	@JsonIgnore
	public String getActHtml(){
		int ten = act/10;
		int unit = act%10;
		if(ten == 0){
			return "<span class='wd_"+unit+"'></span>";
		}else{
			System.out.println("<span class='wd_"+ten+"'></span><span class='wd_"+unit+"'></span>");
			return "<span class='wd_"+ten+"'></span><span class='wd_"+unit+"'></span>";
		}
	}*/
	public Integer getAct() {
		return act;
	}
	public void setAct(Integer act) {
		this.act = act;
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
	public Integer getRemain() {
		return remain;
	}
	public void setRemain(Integer remain) {
		this.remain = remain;
	}
	public List<Object[]> getDatas() {
		return datas;
	}
	public void setDatas(List<Object[]> datas) {
		this.datas = datas;
	}
	
	public Integer getMaxTemp() {
		return maxTemp;
	}
	public void setMaxTemp(Integer maxTemp) {
		this.maxTemp = maxTemp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getMinTemp() {
		return minTemp;
	}
	public void setMinTemp(Integer minTemp) {
		this.minTemp = minTemp;
	}
	public Integer getAllowHeat() {
		return allowHeat;
	}
	public void setAllowHeat(Integer allowHeat) {
		this.allowHeat = allowHeat;
	}
	
}
