package com.harmazing.spms.user.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.harmazing.spms.common.entity.GenericEntity;

@Entity
@Table(name = "spms_user_device")
public class SpmsUserDevice extends GenericEntity {
	private static final long serialVersionUID = 1L;	

	@Column(name = "user_id")
	@Length(min=1, max=100)
	private String userId;
	
	@Column(name = "mobile")
	@Length(min=1, max=100)
	private String mobile;
	
	@Column(name = "device_id")
	@Length(min=1, max=100)
	private String deviceId;
	
	@Column(name = "mac")
	@Length(min=1, max=100)
	private String mac;

	@Column(name = "device_type")
	private Integer deviceType = 1;
	
	@Column(name = "is_primary")
	private Integer isPrimary = 1;

	@OneToMany(cascade={CascadeType.REMOVE},mappedBy="ud")
//	private List<SpmsUdGroup> udGroups = Lists.newArrayList(); // 拥有子user device group entities
	private Set<SpmsUdGroup> udGroups = new HashSet<SpmsUdGroup>(); 

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Set<SpmsUdGroup> getUdGroups() {
		return udGroups;
	}

	public void setUdGroups(Set<SpmsUdGroup> udGroups) {
		this.udGroups = udGroups;
	}
}