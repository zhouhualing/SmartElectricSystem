package com.harmazing.spms.user.dto;
import com.harmazing.spms.common.dto.CommonDTO;
public class SpmsUserDevGroupDTO extends CommonDTO {

	private String udId;
	private String groupId;	
	private String groupName;
	
	//1:add new 2: add to existing 4: delete existing
	private Integer opType;

	public String getUdId() {
		return udId;
	}

	public void setUdId(String udId) {
		this.udId = udId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	
}