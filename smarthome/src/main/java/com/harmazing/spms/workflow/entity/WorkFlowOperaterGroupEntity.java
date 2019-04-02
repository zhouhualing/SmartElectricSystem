package com.harmazing.spms.workflow.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="tb_workflow_operatergroup")
public class WorkFlowOperaterGroupEntity extends CommonEntity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String workFlowTypeId;
	
	private String operater;
	
	@ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinTable(name="operater_group",joinColumns = {@JoinColumn(name = "groupId")},inverseJoinColumns = {@JoinColumn(name = "operaterId")})
	private List <WorkFlowOperaterEntity> workFlowOperaterEntities;
	
	private String actionText;

	public List<WorkFlowOperaterEntity> getWorkFlowOperaterEntities() {
		return workFlowOperaterEntities;
	}

	public void setWorkFlowOperaterEntities(
			List<WorkFlowOperaterEntity> workFlowOperaterEntities) {
		this.workFlowOperaterEntities = workFlowOperaterEntities;
	}

	public String getActionText() {
		return actionText;
	}

	public String getOperater() {
		return operater;
	}

	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}

	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public void setActionText(String actionText) {
		this.actionText = actionText;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
