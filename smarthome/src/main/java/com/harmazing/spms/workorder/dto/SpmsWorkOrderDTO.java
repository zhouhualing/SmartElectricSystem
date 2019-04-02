/**
 * 
 */
package com.harmazing.spms.workorder.dto;

import java.util.Date;

import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.base.dto.WorkFlowCommonDTO;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;

/**
 * describe:
 * @author yyx
 * since 2014年12月31日
 */
public class SpmsWorkOrderDTO extends WorkFlowCommonDTO {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 业务区域
     */
    private AreaDTO area;

	/**
	 * 用电区域
	 */
	private AreaDTO eleArea;

    /**
     * 工单类型（1业务开通/2业务变更/3设备报修/4技术支持/5业务退订
     * dict_workorder_type
     */
    private Integer type;

    private String typeText;

	/**
	 * 用户类型 1试用 2商用
	 */
	private Integer userType;

	private String userTypeText;
    
    /**
     * 工单状态（1待处理/2已指派/3处理中/4延时/5已完成/6废弃）
     * dict_workorder_status
     */
    private Integer status;
    
    private String statusText;
    /**
     *  分配策略（1自动/2手动） 
     *  dict_workorder_allottype
     */
    private Integer allottype;
    
    private String allottypeText;
    /**
     * 工单管理员
     */
    //private UserEntity supervisor;
    
    private String supervisorname;
    private String supervisorid;
    /**
     * 工单当前所有者
     */
    //private UserEntity owner ;
    
    private String ownername;
    private String ownerid;
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
	 * 订户姓名
	 */
	private String spmsUserName;

	/**
	 * 身份证号
	 */
	private String idNumber;

	private String address;

	private String email;

	/**
	 * 电表号
	 */
	private String meterNumber;

	/**
	 * 订户手机
	 */
	private String spmsUserMobile;

	/**
	 * 工单编号
	 */
	private String workOrderCode;

	/**
	 * 创建时间
	 */
	private Date createDate;

    /**
     * 产品类型
     */
    private SpmsProductTypeDTO spmsProductTypeDTO;

    /**
     * 工作流DTO
     */
    private WorkFlowDTO workFlowDTO = new WorkFlowDTO();
    
    private String exist;
    
    private String userId;
    private String imp;
    
	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.imp = imp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	public AreaDTO getArea() {
		return area;
	}

	public void setArea(AreaDTO area) {
		this.area = area;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public WorkFlowDTO getWorkFlowDTO() {
	    return workFlowDTO;
	}

	public void setWorkFlowDTO(WorkFlowDTO workFlowDTO) {
	    this.workFlowDTO = workFlowDTO;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getAllottypeText() {
		return allottypeText;
	}

	public void setAllottypeText(String allottypeText) {
		this.allottypeText = allottypeText;
	}

	public Date getCreateDate() {
		return createDate;
	}

    public SpmsProductTypeDTO getSpmsProductTypeDTO() {
        return spmsProductTypeDTO;
    }

    public void setSpmsProductTypeDTO(SpmsProductTypeDTO spmsProductTypeDTO) {
        this.spmsProductTypeDTO = spmsProductTypeDTO;
    }

    public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public AreaDTO getEleArea() {
		return eleArea;
	}

	public void setEleArea(AreaDTO eleArea) {
		this.eleArea = eleArea;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserTypeText() {
		return userTypeText;
	}

	public void setUserTypeText(String userTypeText) {
		this.userTypeText = userTypeText;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSpmsUserName() {
		return spmsUserName;
	}

	public void setSpmsUserName(String spmsUserName) {
		this.spmsUserName = spmsUserName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getWorkOrderCode() {
		return workOrderCode;
	}

	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSpmsUserMobile() {
		return spmsUserMobile;
	}

	public void setSpmsUserMobile(String spmsUserMobile) {
		this.spmsUserMobile = spmsUserMobile;
	}

	public Integer getType() {
		return type;
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
	/*public UserEntity getSupervisor() {
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
	}*/

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

	public void setActivityindex(Integer activityindex) {
		this.activityindex = activityindex;
	}

	public String getSupervisorname() {
		return supervisorname;
	}

	public void setSupervisorname(String supervisorname) {
		this.supervisorname = supervisorname;
	}

	public String getSupervisorid() {
		return supervisorid;
	}

	public void setSupervisorid(String supervisorid) {
		this.supervisorid = supervisorid;
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}
}
