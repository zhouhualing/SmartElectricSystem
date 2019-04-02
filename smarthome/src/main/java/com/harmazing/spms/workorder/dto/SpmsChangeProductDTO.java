package com.harmazing.spms.workorder.dto;

import java.util.List;

import com.harmazing.spms.base.dto.WorkFlowCommonDTO;

public class SpmsChangeProductDTO extends WorkFlowCommonDTO{
	
	private List addDevices;
	public List getAddDevices() {
		return addDevices;
	}
	public void setAddDevices(List addDevices) {
		this.addDevices = addDevices;
	}
	public List getDelDevices() {
		return delDevices;
	}
	public void setDelDevices(List delDevices) {
		this.delDevices = delDevices;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	private List delDevices;
	private String processInstanceId;
	private String taskId;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
