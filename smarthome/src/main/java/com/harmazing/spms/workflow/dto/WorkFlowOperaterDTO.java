package com.harmazing.spms.workflow.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流操作DTO
 * @author zcp
 *
 */
public class WorkFlowOperaterDTO {
	
	/**
	 * 操作项文字
	 */
	private String text;
	
	/**
	 * 操作项id
	 */
	private String id;
	
	private String operaterId;
	
	/**
	 * 0001为单项 0002为操作组
	 */
	private String operaterType = "0001";
	
	/**
	 * 操作项controller
	 */
	private String operater;
	
	/**
	 * userTask sequenceFlow
	 */
	private String type;
	
	/**
	 * 操作项的值
	 */
	private String value;
	
	private String workFlowTypeId;
	
	private String requestUrl;
	
	private String createDataFun;
	
	private String callBackDataFun;
	
	private String markType;
	
	private String selectType;
	
	private String roleCodes;
	
	private String isShowDialog;
	
	private String isGiveUser;
	
	private String isShowDate;
	
	private String countersign;
	
	private Integer orderFlag;
	
	private Map <String,String> groupAction = new HashMap<String,String>();
	
	private String haveOperaterStr;
	
	private String phoneHaveOperaterStr;
	
	private String phoneMarkType;
	
	private String phoneActionType;
	
	private String userType;
	
	private String printControl;
	
	private String defaultAdvice;

	private Integer handleTime;

	public String getText() {
		return text;
	}

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public String getPrintControl() {
	    return printControl;
	}

	public void setPrintControl(String printControl) {
	    this.printControl = printControl;
	}

	public String getDefaultAdvice() {
	    return defaultAdvice;
	}

	public void setDefaultAdvice(String defaultAdvice) {
	    this.defaultAdvice = defaultAdvice;
	}

	public String getPhoneHaveOperaterStr() {
	    return phoneHaveOperaterStr;
	}

	public String getPhoneMarkType() {
	    return phoneMarkType;
	}

	public Integer getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Integer handleTime) {
		this.handleTime = handleTime;
	}

	public String getUserType() {
	    return userType;
	}

	public void setUserType(String userType) {
	    this.userType = userType;
	}

	public String getPhoneActionType() {
	    return phoneActionType;
	}

	public void setPhoneActionType(String phoneActionType) {
	    this.phoneActionType = phoneActionType;
	}

	public void setPhoneMarkType(String phoneMarkType) {
	    this.phoneMarkType = phoneMarkType;
	}

	public void setPhoneHaveOperaterStr(String phoneHaveOperaterStr) {
	    this.phoneHaveOperaterStr = phoneHaveOperaterStr;
	}

	public Map<String, String> getGroupAction() {
		return groupAction;
	}

	public void setGroupAction(Map<String, String> groupAction) {
		this.groupAction = groupAction;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHaveOperaterStr() {
	    return haveOperaterStr;
	}

	public void setHaveOperaterStr(String haveOperaterStr) {
	    this.haveOperaterStr = haveOperaterStr;
	}

	public String getOperaterType() {
		return operaterType;
	}

	public void setOperaterType(String operaterType) {
		this.operaterType = operaterType;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountersign() {
		return countersign;
	}

	public void setCountersign(String countersign) {
		this.countersign = countersign;
	}

	public String getIsShowDate() {
		return isShowDate;
	}

	public void setIsShowDate(String isShowDate) {
		this.isShowDate = isShowDate;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}

	public String getIsGiveUser() {
		return isGiveUser;
	}

	public void setIsGiveUser(String isGiveUser) {
		this.isGiveUser = isGiveUser;
	}

	public String getIsShowDialog() {
		return isShowDialog;
	}

	public void setIsShowDialog(String isShowDialog) {
		this.isShowDialog = isShowDialog;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleCodes() {
		return roleCodes;
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

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}

	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}

	public String getCreateDataFun() {
		return createDataFun;
	}

	public void setCreateDataFun(String createDataFun) {
		this.createDataFun = createDataFun;
	}

	public String getCallBackDataFun() {
		return callBackDataFun;
	}

	public void setCallBackDataFun(String callBackDataFun) {
		this.callBackDataFun = callBackDataFun;
	}

	public String getOperater() {
		return operater;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}
	
}
