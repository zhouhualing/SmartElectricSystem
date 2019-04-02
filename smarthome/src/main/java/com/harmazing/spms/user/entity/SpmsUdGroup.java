package com.harmazing.spms.user.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.GenericEntity;

@Entity
@Table(name = "spms_ud_group")
public class SpmsUdGroup extends GenericEntity {
	private static final long serialVersionUID = 1L;


	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="ud_group_id")
    private UdGroupEntity udGroup;
    

	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="ud_id")
    private SpmsUserDevice ud;


	private Integer groupType;
	
	public UdGroupEntity getUdGroup() {
		return udGroup;
	}


	public void setUdGroup(UdGroupEntity udGroup) {
		this.udGroup = udGroup;
	}


	public SpmsUserDevice getUd() {
		return ud;
	}


	public void setUd(SpmsUserDevice ud) {
		this.ud = ud;
	}


	public Integer getGroupType() {
		return groupType;
	}


	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}    
}
