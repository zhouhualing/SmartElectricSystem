package com.harmazing.spms.jszc.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


/**
 * jszc工单额外属性
 * @author george
 *
 */
@Entity
@Table(name = "tb_workflow_variable")
@IdClass(JSZCEntityId.class)
public class JSZCEntity{
	private String taskId;
	private String processId;
	private String iKey;
	private String iValue;
	
	@Id
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	@Id
	public String getiKey() {
		return iKey;
	}
	public void setiKey(String iKey) {
		this.iKey = iKey;
	}
	public String getiValue() {
		return iValue;
	}
	public void setiValue(String iValue) {
		this.iValue = iValue;
	}
}
