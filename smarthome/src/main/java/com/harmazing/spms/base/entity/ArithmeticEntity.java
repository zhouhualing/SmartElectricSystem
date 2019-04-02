
package com.harmazing.spms.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.harmazing.spms.common.entity.GenericEntity;

@Entity
@Table(name="tb_user_arithmetic")
public class ArithmeticEntity extends GenericEntity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 算法名称
	 */
	@Column(name = "arithName")
	@Length(min=1, max=100)
	private String arithName;
	
	/**
	 * 算法的class全路径
	 */
	@Column(name = "className")
	@Length(min=1, max=100)
	private String className;	
	
}
