package com.harmazing.spms.workflow.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.springframework.beans.BeansException;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.harmazing.spms.base.util.JackJsonUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;

public class WorkFlowCoreUtil {
	
	public static final String MULTILIST = "multiAssignees";
	
	public static final String MULTIVAR = "multiAssignee";
	
	public static final String CONDITIONNAME = "ACTION";
	
	public static WorkFlowDTO invokeMethod(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
		Map<String,HandlerMethod> map = SpringUtil.getWebApplicationContext(httpServletRequest);
		if(map.containsKey(workFlowDTO.getRequestUrl())) {
			HandlerMethod handlerMethod = map.get(workFlowDTO.getRequestUrl());
			Class clazz = null;
			for(MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
				clazz = methodParameter.getNestedParameterType();
			}
			Object paremeter = JackJsonUtil.jsonToBean(JackJsonUtil.beanToJson(workFlowDTO.getObject()),clazz);
			paremeter.getClass().getMethod("setWorkFlowDTO", WorkFlowDTO.class).invoke(paremeter, workFlowDTO);
			Object object = handlerMethod.getMethod().invoke(RequestContextUtils.getWebApplicationContext(httpServletRequest).getBean(handlerMethod.getBeanType()), paremeter);
			workFlowDTO = (WorkFlowDTO)object.getClass().getMethod("getWorkFlowDTO", null).invoke(object, null);
		}
		return workFlowDTO;
	}
	
	protected static UserTask createUserTask(String id, String name, String assignee) {
		UserTask userTask = new UserTask();
		userTask.setName(name);
		userTask.setId(id);
		userTask.setAssignee(assignee);
		return userTask;
	}
	
	protected static SequenceFlow createSequenceFlow(String from, String to) {
		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setSourceRef(from);
		sequenceFlow.setTargetRef(to);
		return sequenceFlow;
	}
	
	protected static StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("start");
		return startEvent;
	}
	
	protected static EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("end");
		return endEvent;
	}
	
}
