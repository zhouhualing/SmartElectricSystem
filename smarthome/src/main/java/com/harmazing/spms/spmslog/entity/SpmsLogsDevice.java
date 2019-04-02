package com.harmazing.spms.spmslog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="spms_logs_device")
public class SpmsLogsDevice {
	private Long id;
	
	private String url;
	
	private String deviceId;
	
	private String operateDate;
	
	private String ip;
	
	private String data_before;
	
	private String data_after;
	
	private String modify_user;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name="deviceId")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name="operateDate")
	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	@Column(name="ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name="data_before")
	public String getData_before() {
		return data_before;
	}

	public void setData_before(String data_before) {
		this.data_before = data_before;
	}

	@Column(name="data_after")
	public String getData_after() {
		return data_after;
	}

	public void setData_after(String data_after) {
		this.data_after = data_after;
	}

	@Column(name="modify_user")
	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	
}
