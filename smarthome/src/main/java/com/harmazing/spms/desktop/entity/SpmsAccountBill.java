package com.harmazing.spms.desktop.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.user.entity.SpmsUser;

import javax.validation.constraints.NotNull;

/**
 * billEntity
 * 
 * @author hanhao
 * @version v1.0
 */
@Entity
@Table(name = "spms_account_bill")
public class SpmsAccountBill extends CommonEntity {
	/*
	 * 
	 * 假定一个订户购买多个服务 在同一时间激活 和期限相同
	 */
	private static final long serialVersionUID = 1L;

	/* 生成账单日期 */
	private Date billDate;

	// 出账单日 由订户定义
	// private Integer billOutDate;

	// 每月账单金额 =订户所购买服务金额/月数 每月服务金额之和 +所有的用电费用
	private Double countMonthRMB;

	/* 账单周期 */
	private String billCycle; // 账单激活时间至下个月同一时间

	

	/* 订户id */// 多对一
	@ManyToOne
	@JoinColumn(name = "uesr_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	private SpmsUser spmsUser;
	/* 是否已经发送信息(0代表未发送，1代表已发送) */
	private Integer sendMsgFlag;
	/* 账单年份 */
	private Integer year;
	/* 账单月份 */
	private Integer month;
	/* 本月账户余额 */
	private Double accountMonthRest;
	/* 本月信誉额度 */
	private Double monthCredit;
	/* 打印日期 */
	private Date printDate;

	public SpmsAccountBill() {
		super();
		this.sendMsgFlag = 0;
	}

	/* 编号 */

	@NotNull(message = "生成账单日期不能为空")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Length(min = 1, max = 32)
	public String getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}

	public Integer getSendMsgFlag() {
		return sendMsgFlag;
	}

	public void setSendMsgFlag(Integer sendMsgFlag) {
		this.sendMsgFlag = sendMsgFlag;
	}

	public Double getCountMonthRMB() {
		return countMonthRMB;
	}

	public void setCountMonthRMB(Double countMonthRMB) {
		this.countMonthRMB = countMonthRMB;
	}


	public SpmsUser getSpmsUser() {
		return spmsUser;
	}

	public void setSpmsUser(SpmsUser spmsUser) {
		this.spmsUser = spmsUser;
	}

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

	public Double getAccountMonthRest() {
		return accountMonthRest;
	}

	public void setAccountMonthRest(Double accountMonthRest) {
		this.accountMonthRest = accountMonthRest;
	}

	public Double getMonthCredit() {
		return monthCredit;
	}

	public void setMonthCredit(Double monthCredit) {
		this.monthCredit = monthCredit;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	/* 删除标记（0：正常；1：删除；2：审核；） */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";

}