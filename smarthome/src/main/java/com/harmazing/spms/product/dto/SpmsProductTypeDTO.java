package com.harmazing.spms.product.dto;

import java.util.List;


import org.hibernate.validator.constraints.Length;

import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.product.entity.SpmsProduct;

public class SpmsProductTypeDTO extends CommonDTO {
    private static final long serialVersionUID = 1L;

    /* 产品类型名称 */
    private String names;

    /* 产品目录ID */
    private Integer muluId;

    private String muluIdText;

    private String mulu;

    private String muluText;

    private AreaDTO areaDTO;

    /**
     * 区域id
     */
    private String areaId;

    private String areaName;

    /**
     * 状态
     */
    private Integer status;

    private String statusText;

    /* 产品有效期限 */
    private String validPeriod;

    private String validPeriodText;

    /* 文字描述 */
    private String describes;
    /* 产品总费用 */
    private double countRmb;

    /* 收费方式（0预付全款1预付月付） */
    private Integer rmbType;

    private String rmbTypeText;

    private String type;

    private String typeText;

    /* 初始成本 */
    private double indexRmb;

    /* 电价成本 */
    private double electrovalenceRmb;

    /* 服务配置 */
    private Integer configurationInformation;

    private String configurationInformationText;
    
    private String config;
    

    public String getConfigurationInformationText() {
		return configurationInformationText;
	}

	public void setConfigurationInformationText(String configurationInformationText) {
		this.configurationInformationText = configurationInformationText;
	}

	/* 协议温度访问c1,c2,h1,h2 */
    private String temperatureRange;

    private String temp;
    private Integer lianDong; //传感器联动 0 是 1否

    private String lianDongText;
    private Integer zhiLengMix; //制冷最小温度
    private Integer zhiLengMax; //制冷最大温度
    private Integer zhiReMix; //制热最小温度
    private Integer zhiReMax;//制热最大温度

    private Integer kongTiaoCount; //空调最大绑定数
    private Integer chuangGanCount;//传感器最大绑定数

    /* 设备关联类型 */
    private String deviceType;

    /* 关联产品类型*/
    //private List<SpmsProduct> spmsproduct;
    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public Integer getMuluId() {
        return muluId;
    }

    public void setMuluId(Integer muluId) {
        this.muluId = muluId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLianDongText() {
        return lianDongText;
    }

    public void setLianDongText(String lianDongText) {
        this.lianDongText = lianDongText;
    }

    public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getMulu() {
        return mulu;
    }

    public void setMulu(String mulu) {
        this.mulu = mulu;
    }

    public AreaDTO getAreaDTO() {
        return areaDTO;
    }

    public void setAreaDTO(AreaDTO areaDTO) {
        this.areaDTO = areaDTO;
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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMuluIdText() {
        return muluIdText;
    }

    public void setMuluIdText(String muluIdText) {
        this.muluIdText = muluIdText;
    }

    public String getMuluText() {
        return muluText;
    }

    public void setMuluText(String muluText) {
        this.muluText = muluText;
    }

    public String getRmbTypeText() {
        return rmbTypeText;
    }

    public void setRmbTypeText(String rmbTypeText) {
        this.rmbTypeText = rmbTypeText;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public Integer getLianDong() {
        return lianDong;
    }

    public void setLianDong(Integer lianDong) {
        this.lianDong = lianDong;
    }

    public Integer getZhiLengMix() {
        return zhiLengMix;
    }

    public void setZhiLengMix(Integer zhiLengMix) {
        this.zhiLengMix = zhiLengMix;
    }

    public Integer getZhiLengMax() {
        return zhiLengMax;
    }

    public void setZhiLengMax(Integer zhiLengMax) {
        this.zhiLengMax = zhiLengMax;
    }

    public Integer getZhiReMix() {
        return zhiReMix;
    }

    public void setZhiReMix(Integer zhiReMix) {
        this.zhiReMix = zhiReMix;
    }

    public Integer getZhiReMax() {
        return zhiReMax;
    }

    public void setZhiReMax(Integer zhiReMax) {
        this.zhiReMax = zhiReMax;
    }

    public Integer getKongTiaoCount() {
        return kongTiaoCount;
    }

    public void setKongTiaoCount(Integer kongTiaoCount) {
        this.kongTiaoCount = kongTiaoCount;
    }

    public Integer getChuangGanCount() {
        return chuangGanCount;
    }

    public void setChuangGanCount(Integer chuangGanCount) {
        this.chuangGanCount = chuangGanCount;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public double getCountRmb() {
        return countRmb;
    }

    public void setCountRmb(double countRmb) {
        this.countRmb = countRmb;
    }

    public Integer getRmbType() {
        return rmbType;
    }

    public void setRmbType(Integer rmbType) {
        this.rmbType = rmbType;
    }

    public double getIndexRmb() {
        return indexRmb;
    }

    public void setIndexRmb(double indexRmb) {
        this.indexRmb = indexRmb;
    }

    public double getElectrovalenceRmb() {
        return electrovalenceRmb;
    }

    public void setElectrovalenceRmb(double electrovalenceRmb) {
        this.electrovalenceRmb = electrovalenceRmb;
    }

    public Integer getConfigurationInformation() {
        return configurationInformation;
    }

    public void setConfigurationInformation(Integer configurationInformation) {
        this.configurationInformation = configurationInformation;
    }

    public String getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getValidPeriodText() {
        return validPeriodText;
    }

    public void setValidPeriodText(String validPeriodText) {
        this.validPeriodText = validPeriodText;
    }

}

