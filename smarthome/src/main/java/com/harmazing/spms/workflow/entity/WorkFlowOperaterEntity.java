package com.harmazing.spms.workflow.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name="tb_workflow_operater")
public class WorkFlowOperaterEntity extends CommonEntity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作项的id
	 */
	private String operatorId;
	
	/**
	 * 工作流类型id
	 */
	private String workFlowTypeId;
	
	/**
	 * userTask sequenceFlow
	 */
	private String type;
	
	/**
	 * 操作显示的字
	 */
	private String text;

	private String userType;
	
	/**
	 * 自定义操作的url
	 */
	private String requestUrl;
	
	/**
	 * 自定义操作的js fun
	 */
	private String createDataFun;
	
	/**
	 * 自定义回调的js fun 
	 */
	private String callBackDataFun;
	
	/**
	 * 显示备注
	 * 0001 显示 0002不显示
	 */
	private String markType;
	
	/**
	 * 显示选人
	 * 0001 多选角色 0002不显示选人 0003 单选角色 0004选人（不选角色）0005多选人
	 * 0001 直接分配
	 */
	private String selectType;
	
	/**
	 * roleCode 以逗号分开
	 */
	@Column(name="roleCodes",length=4000)
	private String roleCodes;
	
	/**
	 * 0001 给角色 0002给人 0003 给人所属的角色
	 */
	private String isGiveUser;
	
	/**
	 * 0001 显示 0002不显示
	 */
	private String isShowDate;
	
	/**
	 * 是否弹出选角色选人框
	 * 0001 显示 0002不显示
	 */
	private String isShowDialog;
	
	/**
	 * 0002为角色会签，003为人会签
	 */
	private String countersign;
	
	private Integer orderFlag;
	
	/**
	 * 0001 为存储意见为备注的操作按钮 0002为下一步
	 */
	private String phoneActionType;
	
	/**
	 * 0001可输入[在流程跟踪显示] 0002不可输入 0003 默认意见[在流程跟踪显示] 0004 可输入[在流程跟踪不显示] 0005 默认意见[在流程跟踪不显示] 0006退回意见[流程跟踪显示] 0007退回意见[流程跟踪不显示]
	 */
	private String phoneMarkType ;
	
	/**
	 * 操作标识
	 */
	private String completeFlag;

	/**
	 * 单节点处理时长，单位为小时
	 */
	private Integer handleTime;
	
	/**
	 * 按钮组
	 */
	@ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinTable(name="operater_group",joinColumns = {@JoinColumn(name = "operaterId")},inverseJoinColumns = {@JoinColumn(name = "groupId")})
	@JsonIgnore
	private List <WorkFlowOperaterGroupEntity> workFlowOperaterGroupEntities;

	/**
	 * 获取当前任务节点的方法
	 * beanName.method
	 */
	@Column(length = 100)
	private String businessInfoMethod;
	
	/**
	 * 以该sequenceFlow流向指定的userTask，该userTask所具有的sequenceFlow
	 * 逗号分开
	 */
	private String haveOperaterStr;
	
	/**
	 * 以该sequenceFlow流向指定的userTask，手机端该userTask所具有的sequenceFlow
	 */
	private String phoneHaveOperaterStr;
	
	/**
	 * phoneMarkType=0003时默认意见
	 */
	private String defaultAdvice="确认收到";
	
	/**
	 * 代办中跳转的url
	 */
	@Column(length=100)
	private String approvalUrl;
	
	public String getOperatorId() {
		return operatorId;
	}

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public String getPhoneActionType() {
		return phoneActionType;
	}

	public String getApprovalUrl() {
	    return approvalUrl;
	}

	public void setApprovalUrl(String approvalUrl) {
	    this.approvalUrl = approvalUrl;
	}

	public String getDefaultAdvice() {
	    return defaultAdvice;
	}

	public void setDefaultAdvice(String defaultAdvice) {
	    this.defaultAdvice = defaultAdvice;
	}

	public String getHaveOperaterStr() {
	    return haveOperaterStr;
	}

	public String getBusinessInfoMethod() {
		return businessInfoMethod;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Integer handleTime) {
		this.handleTime = handleTime;
	}

	public void setBusinessInfoMethod(String businessInfoMethod) {
		this.businessInfoMethod = businessInfoMethod;
	}

	public String getPhoneMarkType() {
	    return phoneMarkType;
	}

	public void setPhoneMarkType(String phoneMarkType) {
	    this.phoneMarkType = phoneMarkType;
	}

	public String getPhoneHaveOperaterStr() {
	    return phoneHaveOperaterStr;
	}

	public String getUserType() {
	    return userType;
	}

	public void setUserType(String userType) {
	    this.userType = userType;
	}

	public void setPhoneHaveOperaterStr(String phoneHaveOperaterStr) {
	    this.phoneHaveOperaterStr = phoneHaveOperaterStr;
	}

	public void setHaveOperaterStr(String haveOperaterStr) {
	    this.haveOperaterStr = haveOperaterStr;
	}

	public void setPhoneActionType(String phoneActionType) {
		this.phoneActionType = phoneActionType;
	}

	public String getCompleteFlag() {
	    return completeFlag;
	}

	public void setCompleteFlag(String completeFlag) {
	    this.completeFlag = completeFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String getCreateDataFun() {
		return createDataFun;
	}

	public List<WorkFlowOperaterGroupEntity> getWorkFlowOperaterGroupEntities() {
		return workFlowOperaterGroupEntities;
	}

	public void setWorkFlowOperaterGroupEntities(
			List<WorkFlowOperaterGroupEntity> workFlowOperaterGroupEntities) {
		this.workFlowOperaterGroupEntities = workFlowOperaterGroupEntities;
	}

	public void setCreateDataFun(String createDataFun) {
		this.createDataFun = createDataFun;
	}

	public String getCountersign() {
		return countersign;
	}

	public String getIsShowDate() {
		return isShowDate;
	}

	public void setIsShowDate(String isShowDate) {
		this.isShowDate = isShowDate;
	}

	public void setCountersign(String countersign) {
		this.countersign = countersign;
	}

	public String getCallBackDataFun() {
		return callBackDataFun;
	}

	public String getIsGiveUser() {
		return isGiveUser;
	}

	public String getIsShowDialog() {
		return isShowDialog;
	}

	public void setIsShowDialog(String isShowDialog) {
		this.isShowDialog = isShowDialog;
	}

	public void setIsGiveUser(String isGiveUser) {
		this.isGiveUser = isGiveUser;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public void setCallBackDataFun(String callBackDataFun) {
		this.callBackDataFun = callBackDataFun;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}

	public String getMarkType() {
		return markType;
	}

	public void setMarkType(String markType) {
		this.markType = markType;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
