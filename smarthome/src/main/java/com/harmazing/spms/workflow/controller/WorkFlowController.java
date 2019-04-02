package com.harmazing.spms.workflow.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.workflow.dto.TimeLineDTO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.workflow.dto.WorkFlowDTO;
import com.harmazing.spms.workflow.dto.WorkFlowOperaterDTO;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;
import com.harmazing.spms.workflow.manager.WorkFlowManager;
import com.harmazing.spms.workflow.manager.WorkFlowOperaterManager;

@Controller
public class WorkFlowController {
	
	@Autowired
	private WorkFlowManager workFlowManager;
	
	@Autowired
	private WorkFlowOperaterManager workFlowOperaterManager;
	
	@RequestMapping("/workflow/getWorkFlowOperator")
	@ResponseBody
	public WorkFlowDTO getWorkFlowOperator(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		workFlowDTO = workFlowManager.getOpperatorBtns(workFlowDTO, httpServletRequest);
		return workFlowDTO;
	}
	
	@RequestMapping("/workflow/doClaim")
	@ResponseBody
	public WorkFlowDTO doClaim(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		workFlowManager.doClaimTask(workFlowDTO, httpServletRequest);
		return workFlowDTO;
	}
	
	@RequestMapping("/workflow/doAssingee")
	@ResponseBody
	public WorkFlowDTO doAssingee(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		workFlowManager.doClaimTask(workFlowDTO, httpServletRequest);
		return workFlowDTO;
	}
	
	@RequestMapping("/workflow/doComplete")
	@ResponseBody
	public WorkFlowDTO doComplete(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		workFlowManager.doCompleteTask(workFlowDTO, httpServletRequest);
		return workFlowDTO;
	}
	
	@RequestMapping("/workflow/doStartWorkFlow")
	@ResponseBody
	public WorkFlowDTO doStartWorkFlow(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalStateException, Exception {
		workFlowManager.startWorkFlowByKey(workFlowDTO, httpServletRequest);
		return workFlowDTO;
	}
	
	@RequestMapping("/workflow/doSaveOperater")
	@ResponseBody
	public WorkFlowOperaterEntity doSaveOperater(@RequestBody WorkFlowOperaterDTO workFlowOperaterDTO) {
		
		return workFlowOperaterManager.doSaveOperater(workFlowOperaterDTO);
	}

	@RequestMapping("/workflow/initWorkFlowGroup")
	@ResponseBody
	public List initWorkFlowGroup(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalStateException, Exception {
		return workFlowManager.initWorkFlowGroup(workFlowDTO);
	}
	
	@RequestMapping("/workflow/getMarkInfo")
	@ResponseBody
	public Map<String,Object> getMarkInfo(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalStateException, Exception {
		return workFlowManager.getMarkInfo(workFlowDTO);
	}

	/**
	 * 获取时间线
	 * @param workFlowDTO
	 * @return TimeLineDTO
	 */
	@RequestMapping("/workflow/getTimeLine")
	@ResponseBody
	public TimeLineDTO getTimeLine(@RequestBody WorkFlowDTO workFlowDTO) throws ParseException {
		return workFlowManager.getTimeLine(workFlowDTO);
	}
	
}
