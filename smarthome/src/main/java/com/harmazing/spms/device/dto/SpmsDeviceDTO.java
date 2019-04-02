/**
 * 
 */
package com.harmazing.spms.device.dto;

import java.util.Date;

import com.harmazing.spms.common.dto.CommonDTO;

public class SpmsDeviceDTO extends CommonDTO {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String bindUserID;

	private String bindUserName;

	private String zigbee;// zigbee信息

	private String name;
	/* 设备SN */
	private String sn;

	/* 设备MAC */
	private String mac;

	/* 网关MAC */
	private String gwMac;

	/* 网关MAC */
	private String gwId;

	private Integer type;

	private String acRestriction;

	private String typeText;

	/* 厂家：供应商 */
	private String vender;

	/* 型号 */
	private String model;

	/* 硬件版本 */
	private String hardware;

	/* 出厂软件版本 */
	private String software;

	/* 所在仓库 */
	private Integer storage;

	private String storageText;

	/* 当前软件版本 */
	private String cursoft;

	/**
	 * 设备运营状态 dict_device_status 1库存 2正常运营 3维修 4报废
	 */
	private Integer status;

	private String statusText;

	/*
	 * 运行状态 dict_device_operstatus 0离线1在线2异常
	 */
	private Integer operStatus;

	/* 会话ID */
	private String sessionId;

	/* 是否可用 */
	private Integer disable = 0;

	/* 是不是主要用户 */
	private Integer isPrimary;
	private String tag;
	
	private Date updateTime;

	/*************************** 设备状态数据 *********************************/
	// TODO 具体业务含义 开关状态
	private Integer onOff;
	private Integer temp;
	private Integer acTemp;
	private Integer floorTemp;
	private Integer upperTemp;
	private Float power;
	private Integer speed;
	private Integer direction;
	private Date startTime;
	private Integer runDuration;
	private Integer mode;
	// 上下摆风
	private Integer upDownSwing;
	// 左右摆风
	private Integer leftRightSwing;
	// take effect only acType==3
	private Integer rcuId;
	private Integer remain;
	private Integer isPaired;

	private Integer humidity;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBindUserID() {
		return bindUserID;
	}

	public void setBindUserID(String bindUserID) {
		this.bindUserID = bindUserID;
	}

	public String getBindUserName() {
		return bindUserName;
	}

	public void setBindUserName(String bindUserName) {
		this.bindUserName = bindUserName;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getGwMac() {
		return gwMac;
	}

	public void setGwMac(String gwId) {
		this.gwMac = gwId;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public String getStorageText() {
		return storageText;
	}

	public void setStorageText(String storageText) {
		this.storageText = storageText;
	}

	public String getCursoft() {
		return cursoft;
	}

	public void setCursoft(String cursoft) {
		this.cursoft = cursoft;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public Integer getOperStatus() {
		return operStatus;
	}

	public void setOperStatus(Integer operStatus) {
		this.operStatus = operStatus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public Float getPower() {
		return power;
	}

	public void setPower(Float power) {
		this.power = power;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getRunDuration() {
		return runDuration;
	}

	public void setRunDuration(Integer runDuration) {
		this.runDuration = runDuration;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getZigbee() {
		return zigbee;
	}

	public void setZigbee(String zigbee) {
		this.zigbee = zigbee;
	}

	public Integer getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
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

	public Integer getRcuId() {
		return rcuId;
	}

	public void setRcuId(Integer rcuId) {
		this.rcuId = rcuId;
	}

	public String getAcRestriction() {
		return acRestriction;
	}

	public void setAcRestriction(String acRestriction) {
		this.acRestriction = acRestriction;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Integer getUpDownSwing() {
		return upDownSwing;
	}

	public void setUpDownSwing(Integer upDownSwing) {
		this.upDownSwing = upDownSwing;
	}

	public Integer getLeftRightSwing() {
		return leftRightSwing;
	}

	public void setLeftRightSwing(Integer leftRightSwing) {
		this.leftRightSwing = leftRightSwing;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsPaired() {
		return isPaired;
	}

	public void setIsPaired(Integer isPaired) {
		this.isPaired = isPaired;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
}
