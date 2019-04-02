package com.harmazing.spms.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="tb_log_log_lc")
public class LogEntity  extends CommonEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作用户的ip地址
	 */
	@Column(name="remoteAddr",length=100)
	private String remoteAddr;
	
	/**
	 * 操作的uri
	 */
	@Column(name="requestUri",length=200)
	private String requestUri;
	
	/**
	 * 操作的方式
	 */
	@Column(name="method",length=4000)
	private String method;
	
	/**
	 * 操作的数据
	 */
	@Column(name="params",length=4000)
	private String params;
	
	/**
	 * 用户代理信息
	 */
	@Column(name="userAgent",length=4000)
	private String userAgent;
	
	/**
	 * 错误信息
	 */
	@Column(name="exception",length=4000)
	private String exception;
	
	/**
	 * 日志类型【0001:接入日志, 0002:错误日志】
	 */
	@Column(name="type",length=4)
	private String type;
	
	/**
	 * 备注字段
	 */
	@Column(name="mark",length=4000)
	private String mark;
	
	/**
	 * 业务类型
	 * 【0001:登录登出日志，0002:业务日志】
	 */
	@Column(name="businessType",length=4)
	private String businessType = "0002";
	
	/**
	 * 终端类型
	 * 【0001pc，0002phone】
	 */
	private String terminalType;

	/**
	 * 日志信息
	 */
	@Column(name="message",length=4000)
	private String message;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public String getMethod() {
		return method;
	}

	public String getParams() {
		return params;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getMark() {
		return mark;
	}

	public String getBusinessType() {
		return businessType;
	}

	public String getMessage() {
		return message;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getException() {
		return exception;
	}

	public String getType() {
		return type;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
