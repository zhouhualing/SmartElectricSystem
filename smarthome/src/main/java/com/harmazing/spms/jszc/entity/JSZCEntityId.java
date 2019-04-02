package com.harmazing.spms.jszc.entity;

import java.io.Serializable;

public class JSZCEntityId implements Serializable{
	private String taskId;
	private String iKey;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getiKey() {
		return iKey;
	}
	public void setiKey(String iKey) {
		this.iKey = iKey;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iKey == null) ? 0 : iKey.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSZCEntityId other = (JSZCEntityId) obj;
		if (iKey == null) {
			if (other.iKey != null)
				return false;
		} else if (!iKey.equals(other.iKey))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	public JSZCEntityId(String taskId, String iKey) {
		super();
		this.taskId = taskId;
		this.iKey = iKey;
	}
	public JSZCEntityId(){
		super();
	}
}
