package com.harmazing.spms.workflow.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="view_taskList")
public class TaskListView {
	
	@Id
	@Column
	private String taskId;
	
	private String sendTitle;
	
	private Long orderId;
	
	private String taskName;
	
	private String userCode;
	
	private String userName;
	
	private String orgCode;
	
	private String orgName;
	
	private Date startTime;
	
	private Date endTime;
	
	private String businessKey;
	
	private String businessType;
	
//	private String approvalFlag;
	
	private String message;
	
	/**
	 * 标志是否过期 1过期 2未过期
	 */
	private Integer flag;
	
	private Long handleTime;
	
	private Long avgHandleTime;
	

	public String getTaskId() {
		return taskId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Long handleTime) {
		this.handleTime = handleTime;
	}

	public Long getAvgHandleTime() {
		return avgHandleTime;
	}

	public void setAvgHandleTime(Long avgHandleTime) {
		this.avgHandleTime = avgHandleTime;
	}

	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	public Integer getFlag() {
	    return flag;
	}

	public void setFlag(Integer flag) {
	    this.flag = flag;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the sendTitle
	 */
	public String getSendTitle() {
		return sendTitle;
	}

	/**
	 * @param sendTitle the sendTitle to set
	 */
	public void setSendTitle(String sendTitle) {
		this.sendTitle = sendTitle;
	}
	
	
}
