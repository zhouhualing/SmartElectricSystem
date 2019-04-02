package com.harmazing.spms.workflow.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.workflow.dto.WorkFlowDTO;
import com.harmazing.spms.workflow.manager.WorkFlowManager;


/**
 * the phone access controller
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Controller
public class WorkFlowPhoneController {
	
	@Autowired
	private WorkFlowManager workFlowManager;
	
	@RequestMapping("/phone/getApprovalUserInfo")
	@ResponseBody
	public Map<String, Object> getApprovalUserInfo(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) {
		return workFlowManager.phoneGetApprovalUserInfo(workFlowDTO, httpServletRequest);
	}
	
	@RequestMapping("/phone/getCurrentUserTask")
	@ResponseBody
	public Map<String, Object> getCurrentUserTask(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) {
		return workFlowManager.getCurrentUserTask(workFlowDTO, httpServletRequest);
	}
	
	@RequestMapping("/phone/doApproval")
	@ResponseBody
	public WorkFlowDTO doApproval(@RequestBody WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		return workFlowManager.doPhoneCompleteTask(workFlowDTO, httpServletRequest);
	}
}
