package com.harmazing.spms.desktop.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.user.entity.SpmsUser;

/**
 * 产品每月费用表
 * @author Administrator
 *
 */
@Entity
@Table(name = "spms_product_m_fee")
public class SpmsProductMFee extends CommonEntity{
	
	private static final long serialVersionUID = 1L;

	private Integer year;
	
	private Integer month;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private SpmsProduct productId;
	
	/*本月用电费用*/
	private Double elecFee;
	
	/*本月该产品套餐费用*/
	private Double productFee;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "spms_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private SpmsUser spmsUser;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public SpmsProduct getProductId() {
		return productId;
	}

	public void setProductId(SpmsProduct productId) {
		this.productId = productId;
	}

	public Double getElecFee() {
		return elecFee;
	}

	public void setElecFee(Double elecFee) {
		this.elecFee = elecFee;
	}

	public Double getProductFee() {
		return productFee;
	}

	public void setProductFee(Double productFee) {
		this.productFee = productFee;
	}

	public SpmsUser getSpmsUser() {
		return spmsUser;
	}

	public void setSpmsUser(SpmsUser spmsUser) {
		this.spmsUser = spmsUser;
	}
	
}
