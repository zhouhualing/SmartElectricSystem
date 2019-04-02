package com.harmazing.spms.desktop.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.product.dto.SpmsProductDTO;
import com.harmazing.spms.user.dto.SpmsUserDTO;


public class SpmsAccointBillDTO extends CommonDTO {
	 
	/* 生成账单日期 */
		private Date billDate;   
		
		
	//出账单日  由订户定义 
		//private Integer billOutDate;
		
		//每月账单金额  =订户所购买服务金额/月数 每月服务金额之和 
		private Double countMonthRMB;
		
		//private 
		
		/* 账单周期 */
		private String billCycle; //账单激活时间至下个月同一时间 
		
//		/* 服务集合 */ 
//		private List<SpmsProductDTO> spmsProductListDTO;//可以购买多个服务 对应产品
		
		/* 订户id *///多对一
		private SpmsUserDTO spmsUserDTO;
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
		/*本月应缴费用*/
		private Double payFee;
		/* 打印日期 */
		private Date printDate;
		
		private List<SpmsProductMFeeDTO> spmdList;
		
		public Date getBillDate() {
			return billDate;
		}
		public void setBillDate(Date billDate) {
			this.billDate = billDate;
		}
		public Double getCountMonthRMB() {
			return countMonthRMB;
		}
		public void setCountMonthRMB(Double countMonthRMB) {
			this.countMonthRMB = countMonthRMB;
		}
		public String getBillCycle() {
			return billCycle;
		}
		public void setBillCycle(String billCycle) {
			this.billCycle = billCycle;
		}
		//@JsonIgnore
		public SpmsUserDTO getSpmsUserDTO() {
			return spmsUserDTO;
		}
		public List<SpmsProductMFeeDTO> getSpmdList() {
			return spmdList;
		}
		public void setSpmdList(List<SpmsProductMFeeDTO> spmdList) {
			this.spmdList = spmdList;
		}
		public void setSpmsUserDTO(SpmsUserDTO spmsUserDTO) {
			this.spmsUserDTO = spmsUserDTO;
		}
		public Integer getSendMsgFlag() {
			return sendMsgFlag;
		}
		public void setSendMsgFlag(Integer sendMsgFlag) {
			this.sendMsgFlag = sendMsgFlag;
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
		
		public Double getPayFee() {
			return payFee;
//			if(monthCredit<0){
//				return Math.abs(monthCredit);
//			}else{
//				return 0.0;
//			}
		}
		public void setPayFee(Double payFee) {
			this.payFee = payFee;
		}
		public Date getPrintDate() {
			return printDate;
		}
		public void setPrintDate(Date printDate) {
			this.printDate = printDate;
		}
//		public List<SpmsProductMFeeDTO> getSpmdList() {
//			return spmdList;
//		}
//		public void setSpmdList(List<SpmsProductMFeeDTO> spmdList) {
//			this.spmdList = spmdList;
//		}
		
}
