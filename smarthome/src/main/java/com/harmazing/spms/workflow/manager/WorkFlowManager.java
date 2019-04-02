package com.harmazing.spms.workflow.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.workflow.dto.TimeLineDTO;
import org.springframework.beans.BeansException;

import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;

/**
 * WorkFlowManager .
 * @author Zhaocaipeng
 * since 2013-12-19
 */
public interface WorkFlowManager extends IManager {
	
	
	public WorkFlowDTO approvalWorkFlow(WorkFlowDTO workFlowDTO);
	
	public WorkFlowDTO getTask(WorkFlowDTO workFlowDTO);
	
	/**
	 * 启动工作流
	 * @param worFlowDTO
	 * @return WorkFlowDTO
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeansException 
	 */
	public WorkFlowDTO startWorkFlowByKey(WorkFlowDTO worFlowDTO, HttpServletRequest httpServletRequest) throws FileNotFoundException, BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	/**
	 * 获取开始节点的操作项。
	 * @param workFlowDTO
	 * @return WorkFlowDTO
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeansException 
	 */
	public WorkFlowDTO getStartOpperatorBtns(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	/**
	 * 获取工作流的操作项。
	 * @param workFlowDTO
	 * @return WorkFlowDTO
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeansException 
	 */
	public WorkFlowDTO getOpperatorBtns(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	/**
	 * 获取需要认领的工单。
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 * @throws IOException 
	 */
	public QueryTranDTO getNeedClaimList(QueryTranDTO queryTranDTO) throws IOException;
	
	/**
	 * 获取可以分配的工单。
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 * @throws IOException 
	 */
	public QueryTranDTO getNeedAssingList(QueryTranDTO queryTranDTO) throws IOException;
	
	/**
	 * 获取我的代办事项。
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 * @throws IOException 
	 */
	public QueryTranDTO getMeApprovaling(QueryTranDTO queryTranDTO) throws IOException;
	
	/**
	 * 认领任务。
	 * @param WorkFlowDTO
	 * @return WorkFlowDTO
	 * @throws IOException 
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeansException 
	 */
	public WorkFlowDTO doClaimTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws IOException, BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	/**
	 * 审批工作流。
	 * @param workFlowDTO
	 * @return WorkFlowDTO
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeansException 
	 */
	public WorkFlowDTO doCompleteTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	public WorkFlowDTO doGetElementInfo(WorkFlowDTO workFlowDTO);
	
	public QueryTranDTO doGetProcessInfo(QueryTranDTO queryTranDTO) throws IOException;
	
	public QueryTranDTO doGetProcessNodeInfo(QueryTranDTO queryTranDTO) throws IOException;
	
	/**
	 * 获取审批历史信息
	 * @param queryTranDTO
	 * @return
	 * @throws IOException
	 */
	public QueryTranDTO getTaskListQuery(QueryTranDTO queryTranDTO) throws IOException;
	
	
	public void cleanProcessDefineCache(String processDefineKey);
	
	public QueryTranDTO decorationData(QueryTranDTO queryTranDTO);
	
	public Map <String,Object> phoneGetApprovalUserInfo(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest);
	
	public WorkFlowDTO doPhoneCompleteTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception;
	
	public void doPhoneApprovalHandle(WorkFlowDTO workFlowDTO);
	
	public List initWorkFlowGroup(WorkFlowDTO workFlowDTO);
	
	public Map<String,Object> getMarkInfo(WorkFlowDTO workFlowDTO);
	
	public Map <String,Object> getCurrentUserTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest);

	/**
	 * 获取时间线
	 * @param workFlowDTO
	 * @return TimeLineDTO
	 */
	public TimeLineDTO getTimeLine(WorkFlowDTO workFlowDTO) throws ParseException;
	
}
