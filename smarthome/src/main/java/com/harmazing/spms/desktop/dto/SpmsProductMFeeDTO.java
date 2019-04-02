package com.harmazing.spms.desktop.dto;

import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.user.dto.SpmsUserDTO;

/**
 * 产品每月费用表
 * @author Administrator
 *
 */
public class SpmsProductMFeeDTO extends CommonDTO{
	
	private static final long serialVersionUID = 1L;

	private Integer year;
	
	private Integer month;
	
	private String prodductTypeName;
	
	/*本月用电费用*/
	private Double elecFee;
	
	/*本月该产品套餐费用*/
	private Double productFee;
	
	private SpmsUserDTO spmsUserDTO;
	
	private Double electricityCostRmb;

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

	

	

	public String getProdductTypeName() {
		return prodductTypeName;
	}

	public void setProdductTypeName(String prodductTypeName) {
		this.prodductTypeName = prodductTypeName;
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

	public SpmsUserDTO getSpmsUserDTO() {
		return spmsUserDTO;
	}

	public void setSpmsUserDTO(SpmsUserDTO spmsUserDTO) {
		this.spmsUserDTO = spmsUserDTO;
	}

	public Double getElectricityCostRmb() {
		return electricityCostRmb;
	}

	public void setElectricityCostRmb(Double electricityCostRmb) {
		this.electricityCostRmb = electricityCostRmb;
	}

	
	
}
