package com.harmazing.spms.user.dto;

import java.util.List;

import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;

public class SpmsUserBindDTO  extends CommonDTO {
	private static final long serialVersionUID = 1L;
	
	private String subscribeDate;//产品订阅时间
	
	private String userid;//对应的用户的ID
	
	private String productTypeName;//产品类型名
	
	private String productTypeId;//产品类型ID
	
	private String validPeriod;//产品类型对应的期限1.季度2.半年3.全年
	
	private String mulu;//产品类型对应的产品目录（智能空调服务）
	
	private String config;//产品类型的配置（制冷0+冷热1）
	
	private String isActivation;//产品是否激活1.激活   0.未激活
	
	private String lianDong;//产品是否允许传感器联动1.允许 0.不允许
	
	private Integer acMax; //产品类型中空调的最大绑定数
	
	private Integer sensorMax; //产品类型中传感器的最大绑定数
	
	private String area;//产品类型所属的区域中文名
	
	private String rmbType;//付费方式 1.预付全款  2.预付月付
	
	private double countRmb;//产品总费用
	
	private String remarks;// 产品类型的备注信息
	
	private List<SpmsDeviceDTO> spmsDevices;//该用户对应产品下绑定的所有设备的集合

	private String productId;//产品id

	public String getSubscribeDate() {
		return subscribeDate;
	}

	public void setSubscribeDate(String subscribeDate) {
		this.subscribeDate = subscribeDate;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(String validPeriod) {
		this.validPeriod = validPeriod;
	}

	public String getMulu() {
		return mulu;
	}

	public void setMulu(String mulu) {
		this.mulu = mulu;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getIsActivation() {
		return isActivation;
	}

	public void setIsActivation(String isActivation) {
		this.isActivation = isActivation;
	}

	public String getLianDong() {
		return lianDong;
	}

	public void setLianDong(String lianDong) {
		this.lianDong = lianDong;
	}

	public Integer getAcMax() {
		return acMax;
	}

	public void setAcMax(Integer acMax) {
		this.acMax = acMax;
	}

	public Integer getSensorMax() {
		return sensorMax;
	}

	public void setSensorMax(Integer sensorMax) {
		this.sensorMax = sensorMax;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRmbType() {
		return rmbType;
	}

	public void setRmbType(String rmbType) {
		this.rmbType = rmbType;
	}

	public double getCountRmb() {
		return countRmb;
	}

	public void setCountRmb(double countRmb) {
		this.countRmb = countRmb;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<SpmsDeviceDTO> getSpmsDevices() {
		return spmsDevices;
	}

	public void setSpmsDevices(List<SpmsDeviceDTO> spmsDevices) {
		this.spmsDevices = spmsDevices;
	}
	
	
	
}
