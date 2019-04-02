package com.harmazing.spms.workflow.util;

public class WorkFlowUtil {
	
	/**
	 * 开始节点类型
	 */
	public static final String WF_NODE_STARTEVENT="startEvent";
	/**
	 * 用户任务节点类型
	 */
	public static final String WF_NODE_USERTASK="userTask";
	/**
	 * 结束节点类型
	 */
	public static final String WF_NODE_ENDEVENT="endEvent";
	/**
	 * 在PvmActiviti中获取节点类型的key
	 */
	public static final String WF_NODE_TYPE_KEY ="type";
	/**
	 * 在PvmActiviti中获取节点名称的key
	 */
	public static final String WF_NODE_NAME_KEY="name";
	/**
	 * 开始节点类型
	 */
	public static final int TIME_NODE_TYPES=1;
	/**
	 * 开始节点类型
	 */
	public static final int TIME_NODE_TYPEE=2;

	/**
	 * 工单状态：待认领
	 */
	public static final int STATUS_WORKORDER_PREHAND = 1;
	/**
	 * 工单状态：处理中
	 */
	public static final int STATUS_WORKORDER_HADDDING= 3;
	/**
	 * 工单状态：已完成
	 */
	public static final int STATUS_WORKORDER_COMPLETED = 5;
	/**
	 * 工单状态：作废
	 */
	public static final int STATUS_WORKORDER_VOID = 6;

	/**
	 * 请示报告工作流
	 */
	public static final String WF_TYPE_OPENSERVICE="openService";
	
	/**
	 * 具有主动分配工单权限的角色编号
	 */
	public static final String WF_CLAIM_ADMIN_ROLECODE="claimAdmin";
	
}
