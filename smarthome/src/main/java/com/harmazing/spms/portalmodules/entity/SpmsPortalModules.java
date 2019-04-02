package com.harmazing.spms.portalmodules.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.harmazing.spms.common.entity.CommonEntity;

/**
 * 
 * @author yyx since 2015年1月7日 首页显示的模块的Entity
 */

@Entity
@Table(name = "spms_portal_modules")
public class SpmsPortalModules extends CommonEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DIV 的 名字
	 */
	@JoinColumn(name = "div_name")
	@Length(min = 1, max = 255)
	private String divName;

	/**
	 * DIV title 的名字， 既图表的中文名
	 */
	@JoinColumn(name = "div_title")
	@Length(min = 1, max = 255)
	private String divTitle;

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getDivTitle() {
		return divTitle;
	}

	public void setDivTitle(String divTitle) {
		this.divTitle = divTitle;
	}

}
