package com.harmazing.spms.workorder.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harmazing.spms.product.entity.SpmsProductType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.entity.CommonEntity;


/**
 * work order entity 
 * @author yyx
 *
 */
@Entity
@Table(name="spms_workorder")
public class SpmsWorkOrder  extends CommonEntity {
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
	 * 运营区域sp
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private Area area;

	/**
	 * 用电区域
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ele_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private Area eleArea;

	/**
	 * 工单编号
	 */
	private String workOrderCode;

    /**
     * 工单类型（1业务开通/2业务变更/3设备报修/4技术支持/5业务退订
     * dict_workorder_type
     */
    private Integer type;
    
    /**
     * 工单状态（1待认领/2已下发/3处理中（认领、指派）/4延时/5已完成/6废弃）
     * dict_workorder_status
     */
    private Integer status;
    
    /**
     * 订户姓名
     */
    @Column(length=100)
    private String spmsUserName;
    
    /**
     * 订户手机
     */
    @Column(length=15)
    private String spmsUserMobile;

	/**
	 * 身份证号
	 */
	@Column(length=20)
	private String idNumber;

	@Column(length=300)
	private String address;

	@Column(length=50)
	private String email;

	/**
	 * 1试用 2商用
	 */
	private Integer userType;

	/**
	 * 电表号
	 */
	@Column(length=50)
	private String meterNumber;
    
    
    /**
     *  分配策略（1自动/2手动） 
     *  dict_workorder_allottype
     */
    private Integer allottype;
    
    
    /**
     * 工单管理员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    @JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
    private UserEntity supervisor;
    
    /**
     * 工单当前所有者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
    private UserEntity owner ;
    
    /**
     * 工单生成时间
     */
    private Date creationdate;
    
    /**
     * 预期完成时间
     */
    private Date duration;
    
    /**
     * 工单内存在活动数
     */
    private Integer activitycount; 
    
    /**
     * 当前活动下标
     */
    private Integer activityindex;

    /**
     * 作废原因
     */
    @Column(length=4000)
    private String deleteReason;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="spmsProductType_id")
    @JsonIgnore
    private SpmsProductType spmsProductType;
    
    @Column
    private String userId;
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Area getEleArea() {
		return eleArea;
	}

	public void setEleArea(Area eleArea) {
		this.eleArea = eleArea;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	/**
	 * 流程实例id
	 */
	private String processInstanceId;

	/**
	 * 流程定义id
	 */
	private String processDefineId;
	
	
	/* 工单批注列表 */
	// TO DO	
 	/* 工单活动列表*/
	//TO DO
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	public String getProcessDefineId() {
		return processDefineId;
	}

	public void setProcessDefineId(String processDefineId) {
		this.processDefineId = processDefineId;
	}

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getSpmsUserName() {
	    return spmsUserName;
	}

	public void setSpmsUserName(String spmsUserName) {
	    this.spmsUserName = spmsUserName;
	}

	public String getSpmsUserMobile() {
	    return spmsUserMobile;
	}

    public SpmsProductType getSpmsProductType() {
        return spmsProductType;
    }

    public void setSpmsProductType(SpmsProductType spmsProductType) {
        this.spmsProductType = spmsProductType;
    }

    public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setSpmsUserMobile(String spmsUserMobile) {
	    this.spmsUserMobile = spmsUserMobile;
	}

	public Integer getType() {
	    return type;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}

	public void setType(Integer type) {
	    this.type = type;
	}

	public Integer getStatus() {
	    return status;
	}

	public void setStatus(Integer status) {
	    this.status = status;
	}

	public Integer getAllottype() {
	    return allottype;
	}

	public void setAllottype(Integer allottype) {
	    this.allottype = allottype;
	}

	public static long getSerialversionuid() {
	    return serialVersionUID;
	}

	public UserEntity getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(UserEntity supervisor) {
		this.supervisor = supervisor;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	public Integer getActivitycount() {
		return activitycount;
	}

	public void setActivitycount(Integer activitycount) {
		this.activitycount = activitycount;
	}

	public Integer getActivityindex() {
		return activityindex;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getWorkOrderCode() {
		return workOrderCode;
	}

	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
	}

	public void setActivityindex(Integer activityindex) {
		this.activityindex = activityindex;
	}
	
	
	
}
