package com.harmazing.spms.base.entity;

import com.harmazing.spms.common.entity.GenericEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="tb_code_generater")
public class CodeGeneraterEntity extends GenericEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 为00001
	 */
	private String code;
	
	/**
	 * 1 为业务开通
	 */
	private Integer type;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
