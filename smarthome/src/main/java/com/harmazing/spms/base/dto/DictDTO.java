package com.harmazing.spms.base.dto;

import com.harmazing.spms.common.dto.IDTO;
public class DictDTO implements IDTO{

	/**
	 * code
	 */
	private String code;
	
	/**
	 * value
	 */
	private String value;
	
	/**
	 * order
	 */
	private String iOrder;

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getiOrder() {
		return iOrder;
	}

	public void setiOrder(String iOrder) {
		this.iOrder = iOrder;
	}

}
