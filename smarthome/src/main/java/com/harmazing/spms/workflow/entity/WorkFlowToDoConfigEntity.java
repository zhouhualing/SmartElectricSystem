package com.harmazing.spms.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="tb_workflow_todoconfig")
public class WorkFlowToDoConfigEntity extends CommonEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String workFlowTypeId;
	
	private String appId;
	
	private String pcUrl;
	
	private String phoneUrl;
	
	private String titleField;
	
	private String baseClassName;

	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}

	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getPhoneUrl() {
		return phoneUrl;
	}

	public void setPhoneUrl(String phoneUrl) {
		this.phoneUrl = phoneUrl;
	}

	public String getTitleField() {
		return titleField;
	}

	public void setTitleField(String titleField) {
		this.titleField = titleField;
	}

	public String getBaseClassName() {
		return baseClassName;
	}

	public void setBaseClassName(String baseClassName) {
		this.baseClassName = baseClassName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
