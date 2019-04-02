package com.harmazing.spms.base.entity;

import com.harmazing.spms.common.entity.IEntity;

public class _DSMEntity implements IEntity {
	private String faId;
	
	private String id;
	
	private Integer infoType;

	public String getFaId() {
		return faId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((faId == null) ? 0 : faId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((infoType == null) ? 0 : infoType.hashCode());
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
		_DSMEntity other = (_DSMEntity) obj;
		if (faId == null) {
			if (other.faId != null)
				return false;
		} else if (!faId.equals(other.faId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (infoType == null) {
			if (other.infoType != null)
				return false;
		} else if (!infoType.equals(other.infoType))
			return false;
		return true;
	}

	public void setFaId(String faId) {
		this.faId = faId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getInfoType() {
		return infoType;
	}

	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	
	
}
