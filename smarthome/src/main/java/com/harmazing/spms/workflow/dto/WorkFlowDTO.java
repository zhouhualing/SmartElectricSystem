package com.harmazing.spms.workflow.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.jszc.dto.JSZCDTO;

import org.activiti.engine.runtime.ProcessInstance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.harmazing.spms.base.util.SpringJsonStringToDate;

public class WorkFlowDTO extends CommonDTO {
	
	/**
	 * 工作流类型id
	 */
	private String workFlowTypeId;
	
	/**
	 * 是否是启动节点
	 */
	private Boolean isStartOpperator = false;
	
	/**
	 * 流程定义的id
	 */
	private String processDefinedId;
	
	/**
	 * 操作项dto
	 */
	private List <WorkFlowOperaterDTO> workFlowOperaterDTOs;
	
	/**
	 * 业务id
	 */
	private String businessKey;
	
	/**
	 * vars
	 */
	private Map<String,Object> vars;
	
	/**
	 * userTask;
	 */
	private String userTask;
	
	/**
	 * userType
	 */
	private String userType;
	
	/**
	 * processInstance
	 */
	@JsonIgnore
	private ProcessInstance processInstance;
	
	private Object object;
	
	/**
	 *  startEvent  userTask
	 */
	private String nodeType;
	
	/**
	 * 0 no 1 yes
	 */
	private Integer isConCurrent = 0;
	
	/**
	 * 备注
	 */
	private String mark;
	
	/**
	 * 备注类型
	 */
	private String markType;
	
	/**
	 * 完成时间
	 */
	private Date completeDate;
	
	/**
	 * 自定请求url
	 */
	private String requestUrl;
	
	/**
	 * 指定角色处理
	 */
	private String roleCodes;
	
	/**
	 * 办理角色
	 */
	private String assignRoleName;
	
	/**
	 * 办理角色
	 */
	private String assignRoleCode;
	
	/**
	 * 操作项id
	 */
	private String operaterId;
	
	/**
	 * 0001 通过 0002 拒绝
	 */
	private String approvalFlag;
	
	/**
	 * 0001秘书 0002 下一级
	 */
	private String reSendFlag;
	
	/**
	 * taskid
	 */
	private String taskId;
	
	/**
	 * processInstanceId
	 */
	private String processInstanceId;
	
	/**
	 * 0002当前节点是会签
	 */
	private String nowIsCounterSign;
	
	/**
	 * businessType
	 */
	private String businessType;
	
	/**
	 * 紧急程度用来计算超时时间的
	 */
	private String urgency;
	
	/**
	 * 0001 该公文已被处理
	 */
	private String errorType;
	
	/**
	 * 当前roleCode
	 */
	private String currentRoleCode;
	
	/**
	 * 0001 pc获取
	 * 0002 phone获取
	 */
	private String operaterSType = "0001";
	
	private String utilFiled="";
	
	/**
	 * 0001需要invoke 0002不需要
	 */
	private String isNeedInvoke="0001";

    /**
     * 是否继续执行
     */
    private Boolean isContinue = true;
	
	/**
	 * 选人规则
	 */
	private String selectType;
	
	/**
	 * 选择的用户编号
	 */
	private String userCodes;
	
	/**
	 * 短信内容
	 */
	private String mailMessage;
	
	/**
	 * 是否发送短信 0001发送 0002不发送
	 */
	private String messageType="0002";
	
	/**
	 * 打印章次数相关
	 */
	private Map <String,Object> printInfo;
	
	private String printControl;
	
	//附加信息
	private List<JSZCDTO> jszcdtos;
	
	public List<JSZCDTO> getJszcdtos() {
		return jszcdtos;
	}

	public void setJszcdtos(List<JSZCDTO> jszcdtos) {
		this.jszcdtos = jszcdtos;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public String getSelectType() {
	    return selectType;
	}

	public String getMailMessage() {
	    return mailMessage;
	}

	public void setMailMessage(String mailMessage) {
	    this.mailMessage = mailMessage;
	}

	public String getMessageType() {
	    return messageType;
	}

	public String getPrintControl() {
	    return printControl;
	}

	public void setPrintControl(String printControl) {
	    this.printControl = printControl;
	}

	public void setMessageType(String messageType) {
	    this.messageType = messageType;
	}

    public Boolean getIsContinue() {
        return isContinue;
    }

    public void setIsContinue(Boolean isContinue) {
        this.isContinue = isContinue;
    }

    public Map<String, Object> getPrintInfo() {
	    return printInfo;
	}

	public void setPrintInfo(Map<String, Object> printInfo) {
	    this.printInfo = printInfo;
	}

	public void setSelectType(String selectType) {
	    this.selectType = selectType;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public String getCurrentRoleCode() {
	    return currentRoleCode;
	}

	public String getUserCodes() {
	    return userCodes;
	}

	public void setUserCodes(String userCodes) {
	    this.userCodes = userCodes;
	}

	public void setCurrentRoleCode(String currentRoleCode) {
	    this.currentRoleCode = currentRoleCode;
	}

	public String getIsNeedInvoke() {
	    return isNeedInvoke;
	}

	public void setIsNeedInvoke(String isNeedInvoke) {
	    this.isNeedInvoke = isNeedInvoke;
	}

	public String getOperaterSType() {
	    return operaterSType;
	}

	public String getUtilFiled() {
	    return utilFiled;
	}

	public void setUtilFiled(String utilFiled) {
	    this.utilFiled = utilFiled;
	}

	public void setOperaterSType(String operaterSType) {
	    this.operaterSType = operaterSType;
	}

	public String getBusinessType() {
	    return businessType;
	}

	public String getUserType() {
	    return userType;
	}

	public void setUserType(String userType) {
	    this.userType = userType;
	}

	public String getErrorType() {
	    return errorType;
	}

	public void setErrorType(String errorType) {
	    this.errorType = errorType;
	}

	public void setBusinessType(String businessType) {
	    this.businessType = businessType;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getUrgency() {
	    return urgency;
	}

	public String getMarkType() {
	    return markType;
	}

	public void setMarkType(String markType) {
	    this.markType = markType;
	}

	public void setUrgency(String urgency) {
	    this.urgency = urgency;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Integer getIsConCurrent() {
		return isConCurrent;
	}

	public String getNowIsCounterSign() {
		return nowIsCounterSign;
	}

	public void setNowIsCounterSign(String nowIsCounterSign) {
		this.nowIsCounterSign = nowIsCounterSign;
	}

	public void setIsConCurrent(Integer isConCurrent) {
		this.isConCurrent = isConCurrent;
	}

	/**
	 * 重新提交orGiveUp flag
	 * 【RESUBMIT/GIVEUP】
	 */
	private String reSubmit;
	
	/**
	 * 审批人编号
	 */
	private String assignUserCode;
	
	/**
	 * 审批人姓名
	 */
	private String assignUserName;
	
	/**
	 * 0001最后一步
	 */
	private String isTheLastStep;
	
	/**
	 * 
	 */
	private String currentUserCode;
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public String getReSubmit() {
		return reSubmit;
	}

	public String getAssignRoleCode() {
		return assignRoleCode;
	}

	public void setAssignRoleCode(String assignRoleCode) {
		this.assignRoleCode = assignRoleCode;
	}

	public Object getObject() {
		return object;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	@JsonDeserialize(using= SpringJsonStringToDate.class)
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getAssignRoleName() {
		return assignRoleName;
	}

	public void setAssignRoleName(String assignRoleName) {
		this.assignRoleName = assignRoleName;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getCurrentUserCode() {
		return currentUserCode;
	}

	public void setCurrentUserCode(String currentUserCode) {
		this.currentUserCode = currentUserCode;
	}

	public String getProcessDefinedId() {
		return processDefinedId;
	}

	public void setProcessDefinedId(String processDefinedId) {
		this.processDefinedId = processDefinedId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getReSendFlag() {
		return reSendFlag;
	}

	public void setReSendFlag(String reSendFlag) {
		this.reSendFlag = reSendFlag;
	}

	public String getIsTheLastStep() {
		return isTheLastStep;
	}

	public void setIsTheLastStep(String isTheLastStep) {
		this.isTheLastStep = isTheLastStep;
	}

	public void setReSubmit(String reSubmit) {
		this.reSubmit = reSubmit;
	}

	public String getApprovalFlag() {
		return approvalFlag;
	}

	public List<WorkFlowOperaterDTO> getWorkFlowOperaterDTOs() {
		return workFlowOperaterDTOs;
	}

	public void setWorkFlowOperaterDTOs(
			List<WorkFlowOperaterDTO> workFlowOperaterDTOs) {
		this.workFlowOperaterDTOs = workFlowOperaterDTOs;
	}

	public String getAssignUserCode() {
		return assignUserCode;
	}

	public void setAssignUserCode(String assignUserCode) {
		this.assignUserCode = assignUserCode;
	}

	public void setApprovalFlag(String approvalFlag) {
		this.approvalFlag = approvalFlag;
	}

	public String getMark() {
		return mark;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Map<String, Object> getVars() {
		return vars;
	}

	public String getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(String assignUserName) {
		this.assignUserName = assignUserName;
	}

	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}

	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Boolean getIsStartOpperator() {
		return isStartOpperator;
	}

	public void setIsStartOpperator(Boolean isStartOpperator) {
		this.isStartOpperator = isStartOpperator;
	}

	public String getUserTask() {
		return userTask;
	}

	public void setUserTask(String userTask) {
		this.userTask = userTask;
	}
	
}
