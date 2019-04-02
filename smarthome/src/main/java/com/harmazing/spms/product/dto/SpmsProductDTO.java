package com.harmazing.spms.product.dto;

import java.util.Date;

import com.harmazing.spms.base.dto.WorkFlowCommonDTO;
import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.user.dto.SpmsUserDTO;

public class SpmsProductDTO extends WorkFlowCommonDTO {

    private static final long serialVersionUID = 1L;
    /* 产品类型DTO */

    private SpmsProductTypeDTO spmsProductTypeDTO;
    //产品类型名称
    private String spmsTypeName;

    /* 产品状态 */
    private Integer status;

    /* 订户ID */
    private String userName;
    private String userId;

    //状态名称
    private String stat;

    private Integer lianDong; //传感器联动 0 是 1否
    private Integer zhiLengMix; //制冷最小温度
    private Integer zhiLengMax; //制冷最大温度
    private Integer zhiReMix; //制热最小温度
    private Integer zhiReMax;//制热最大温度

    private Integer kongTiaoCount; //空调最大绑定数
    private Integer chuangGanCount;//传感器最大绑定数

    private SpmsUserDTO spmsUserDTO;
	/* 产品订阅时间 */

    private Date subscribeDate;
    /* 产品激活时间 */
    private Date activateDate;

    /* 产品到期时间 */
    private Date expireDate;

    private String subDate;
    private String actDate;
    private String expDate;
 		  
	/* 初始成本 */

    private double initialCostRmb;
	/* 用电成本 */

    private double electricityCostRmb;
	/* 关联设备类型ID */

    /**
     * 设备mac
     */
    private String spmsDeviceMac;

    private String gwMac;

    public String getSpmsDeviceMac() {
        return spmsDeviceMac;
    }

    public void setSpmsDeviceMac(String spmsDeviceMac) {
        this.spmsDeviceMac = spmsDeviceMac;
    }

    public SpmsUserDTO getSpmsUserDTO() {
        return spmsUserDTO;
    }

    public void setSpmsUserDTO(SpmsUserDTO spmsUserDTO) {
        this.spmsUserDTO = spmsUserDTO;
    }

    public String getGwMac() {
        return gwMac;
    }

    public void setGwMac(String gwMac) {
        this.gwMac = gwMac;
    }

    public Integer getStatus() {
        return status;
    }

    public String getSubDate() {
        return subDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }

    public SpmsProductTypeDTO getSpmsProductTypeDTO() {
        return spmsProductTypeDTO;
    }

    public void setSpmsProductTypeDTO(SpmsProductTypeDTO spmsProductTypeDTO) {
        this.spmsProductTypeDTO = spmsProductTypeDTO;
    }

    public String getSpmsTypeName() {
        return spmsTypeName;
    }

    public void setSpmsTypeName(String spmsTypeName) {
        this.spmsTypeName = spmsTypeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(Date subscribeDate) {
        this.subscribeDate = subscribeDate;
    }

    public Date getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(Date activateDate) {
        this.activateDate = activateDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public double getInitialCostRmb() {
        return initialCostRmb;
    }

    public void setInitialCostRmb(double initialCostRmb) {
        this.initialCostRmb = initialCostRmb;
    }

    public double getElectricityCostRmb() {
        return electricityCostRmb;
    }

    public void setElectricityCostRmb(double electricityCostRmb) {
        this.electricityCostRmb = electricityCostRmb;
    }


    private Integer updateFlag;

    public Integer getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Integer updateFlag) {
        this.updateFlag = updateFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
