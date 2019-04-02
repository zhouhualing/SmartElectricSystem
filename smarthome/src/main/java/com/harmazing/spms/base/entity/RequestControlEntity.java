package com.harmazing.spms.base.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="tb_log_requestControl")
public class RequestControlEntity extends CommonEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 请求的url
	 */
	private String requestUrl;
	
	/**
	 * 权限名称
	 */
	private String businessName;

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
