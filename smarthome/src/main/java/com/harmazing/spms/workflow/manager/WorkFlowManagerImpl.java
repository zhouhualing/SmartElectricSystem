package com.harmazing.spms.workflow.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.base.util.*;
import com.harmazing.spms.jszc.dao.JSZCDao;
import com.harmazing.spms.jszc.dto.JSZCDTO;
import com.harmazing.spms.jszc.entity.JSZCEntity;
import com.harmazing.spms.product.dao.SpmsProductDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.workflow.dto.TimeLineDTO;
import com.harmazing.spms.workflow.dto.TimeLineNodeDTO;
import com.harmazing.spms.workorder.dao.SpmsPhoneVisitDAO;
import com.harmazing.spms.workorder.entity.SpmsPhoneVisit;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dao.RoleDAO;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.base.dto.TreeDTO;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.exception.HasHandleException;
import com.harmazing.spms.base.exception.WorkFlowConfigException;
import com.harmazing.spms.base.manager.RoleManager;
import com.harmazing.spms.base.manager.UserManager;
import com.harmazing.spms.base.manager.QueryManagerImpl;
import com.harmazing.spms.base.util.SearchFilter.Operator;
import com.harmazing.spms.common.entity.IEntity;
import com.harmazing.spms.workflow.dao.InboxViewDAO;
import com.harmazing.spms.workflow.dao.TaskListViewDAO;
import com.harmazing.spms.workflow.dao.WorkFlowGroupDAO;
import com.harmazing.spms.workflow.dao.WorkFlowOperaterDAO;
import com.harmazing.spms.workflow.dao.WorkFlowOperaterGroupDAO;
import com.harmazing.spms.workflow.dao.WorkFlowStatusDAO;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;
import com.harmazing.spms.workflow.dto.WorkFlowOperaterDTO;
import com.harmazing.spms.workflow.entity.WorkFlowGroupEntity;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterGroupEntity;
import com.harmazing.spms.workflow.entity.WorkFlowStatusEntity;
import com.harmazing.spms.workflow.manager.WorkFlowManager;
import com.harmazing.spms.workflow.manager.WorkFlowOperaterManager;
import com.harmazing.spms.workflow.util.WorkFlowCoreUtil;
import com.harmazing.spms.workflow.util.WorkFlowUtil;
import com.harmazing.spms.workorder.dao.SpmsWorkOrderDAO;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;

/**
 * WorkFlowManagerImpl .
 *
 * 
 *         
 */
@Service("workFlowManager")
public class WorkFlowManagerImpl extends QueryManagerImpl implements WorkFlowManager {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private WorkFlowOperaterDAO workFlowOperaterDAO;

    @Autowired
    private WorkFlowOperaterGroupDAO workFlowOperaterGroupDAO;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    private UserManager userManager;

    @Autowired
    private WorkFlowOperaterManager workFlowOperaterManager;

    @Autowired
    private TaskListViewDAO taskListViewDAO;

    @Autowired
    private QueryDAO queryDAO;

//	@Autowired
//	private TodoManager todoManager;

    @Autowired
    private SpmsWorkOrderDAO spmsWorkOrderDAO;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private InboxViewDAO inboxViewDAO;

    @Autowired
    private WorkFlowGroupDAO workFlowGroupDAO;

    @Autowired
    private WorkFlowStatusDAO workFlowStatusDAO;

    @Autowired
    private CommentEntityManager commentEntityManager;

    @Autowired
    private SpmsUserDAO spmsUserDAO;

    @Autowired
    private SpmsUserProductBindingDAO spmsUserProductBindingDAO;

    @Autowired
    private SpmsPhoneVisitDAO spmsPhoneVisitDAO;
    
    @Autowired
    private JSZCDao jszcDao;

    @Override
    @Transactional
    public WorkFlowDTO startWorkFlowByKey(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {

        //invoke customer method
        workFlowDTO = WorkFlowCoreUtil.invokeMethod(workFlowDTO, httpServletRequest);
        if(!workFlowDTO.getIsContinue()) {
            return workFlowDTO;
        }
        //cache the old roleCodes
        String oldRoleCodes = workFlowDTO.getRoleCodes();
        String oldCondition = workFlowDTO.getVars().get("ACTION").toString();
        String oldUserCodes = workFlowDTO.getUserCodes();

        //set the createUser
        identityService.setAuthenticatedUserId(UserUtil.getCurrentUserCode());

        //get the lastest version of processDefine
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workFlowDTO.getWorkFlowTypeId()).latestVersion().singleResult();

        //get the readonlyprocessDefine to get pvmActivity
        ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());


        //as the method only hanlde the start. Thus here just handle the startEvent
        List<PvmTransition> transitions = readOnlyProcessDefinition.getInitial().getOutgoingTransitions();
        //assume after the startEven, has only one userTask
        PvmTransition pvmTransition = transitions.get(0);
        //get WorkFlowOperaterEntitys for get the startSequeceFLow's roles
        List<WorkFlowOperaterEntity> workFlowOperaterEntitiesT = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId()), WorkFlowOperaterEntity.class));
        Map<String, WorkFlowOperaterEntity> cacheMap = CollectionUtil.extractToMap(workFlowOperaterEntitiesT, "operatorId");

        //默认分配给所有角色
        if ("0001".equals(workFlowDTO.getSelectType())) {
            oldRoleCodes = cacheMap.get(oldCondition).getRoleCodes();
        }
        //set the vars for startWorkFLow
//		workFlowDTO.getVars().put("ROLECODE", cacheMap.get(pvmTransition.getId()).getRoleCodes());
        workFlowDTO.getVars().put("ACTION", getOperaterAction(pvmTransition.getProperty("conditionText").toString()));
        List<Object[]> infos = roleDAO.findByUserCodeAndINRoleCodes(UserUtil.getCurrentUserCode(), Arrays.asList(cacheMap.get(pvmTransition.getId()).getRoleCodes().split(",")));
        if (infos.size() <= 0) {
            logger.error("对不起，您没有该流程的操作权限。");
            throw new WorkFlowConfigException("对不起，您没有该流程的操作权限。");
        } else {
            workFlowDTO.setRoleCodes(UserUtil.getCurrentUserCode());
        }

        Long dateLong = new Date().getTime();
        if (workFlowDTO.getCompleteDate() != null) {
            dateLong = workFlowDTO.getCompleteDate().getTime();
        }
        workFlowDTO.getVars().put("theDeadLineTime", dateLong);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(workFlowDTO.getWorkFlowTypeId(), workFlowDTO.getBusinessKey(), workFlowDTO.getVars());

        //add processInstance vars
        Map<String,Object> timeInfos = Maps.newHashMap();
        for(String key : cacheMap.keySet()) {
            if(key.startsWith("usertask")) {
                timeInfos.put(key,cacheMap.get(key).getHandleTime());
            }
        }
        runtimeService.setVariables(processInstance.getProcessInstanceId(),timeInfos);

        //计算预计完成时间
        int hours = 0;
        for(WorkFlowOperaterEntity workFlowOperaterEntity : workFlowOperaterEntitiesT) {
            if(WorkFlowUtil.WF_NODE_USERTASK.equals(workFlowOperaterEntity.getUserType())) {
                hours = hours+(workFlowOperaterEntity.getHandleTime()==null?0:workFlowOperaterEntity.getHandleTime());
            }
        }
        
//        ((SpmsWorkOrder)workFlowDTO.getObject()).setDuration(duration);
        ((SpmsWorkOrder)workFlowDTO.getObject()).setProcessInstanceId(processInstance.getId());
        ((SpmsWorkOrder)workFlowDTO.getObject()).setProcessDefineId(processInstance.getProcessDefinitionId());

        //此处一个流程实例，编辑人编辑环节只会有一个执行实例。
        Task nowTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        //add candidate group
        workFlowDTO.setTaskId(nowTask.getId());
        taskService.claim(nowTask.getId(), UserUtil.getCurrentUserCode());
        taskService.addComment(nowTask.getId(), nowTask.getProcessInstanceId(), "0001", workFlowDTO.getMark() == null ? "" : workFlowDTO.getMark());

        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(nowTask.getExecutionId()).singleResult();
        Integer concurrentFlag = 0;
        if (executionEntity.isConcurrent()) {
            concurrentFlag = 1;
        } else {
            concurrentFlag = 0;
        }
        workFlowDTO.setIsConCurrent(concurrentFlag);

        SequenceFlow sequenceFlow = (SequenceFlow) repositoryService.getBpmnModel(processDefinition.getId()).getFlowElement(workFlowDTO.getOperaterId());
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        searchFilters.add(new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId()));
        searchFilters.add(new SearchFilter("operatorId", sequenceFlow.getTargetRef()));
        List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilters, WorkFlowOperaterEntity.class));
        if ((workFlowOperaterEntities.size() > 0) && ("0002".equals(workFlowOperaterEntities.get(0).getCountersign()) || "0003".equals(workFlowOperaterEntities.get(0).getCountersign()))) {
            List<String> codes = null;
            if ("0004".equals(workFlowDTO.getSelectType()) || "0005".equals(workFlowDTO.getSelectType())) {
                codes = Arrays.asList(oldUserCodes.split(","));
            } else {
                codes = Arrays.asList(oldRoleCodes.split(","));
            }
            workFlowDTO.getVars().put("multiAssignees", codes);
            workFlowDTO.setNowIsCounterSign(workFlowOperaterEntities.get(0).getCountersign());
        }
//        workFlowDTO.getVars().put("ROLECODE", oldRoleCodes);
        workFlowDTO.getVars().put("ACTION", oldCondition);
        workFlowDTO.getVars().put("ACTION1", runtimeService.getVariable(nowTask.getExecutionId(), "ACTION").toString());

        //add mark
        if (workFlowDTO.getMark() != null) {
            taskService.addComment(nowTask.getId(), nowTask.getProcessInstanceId(), "0004", workFlowDTO.getMark() + "_" + new Date().getTime() + "_" + UserUtil.getCurrentUserCode() + "_" + UserUtil.getCurrentUser().getUserName());
        }
        taskService.complete(nowTask.getId(), workFlowDTO.getVars());

        //set nextTask info
        workFlowDTO.setProcessInstanceId(processInstance.getId());
        workFlowDTO.setUserCodes(oldUserCodes);

        //0001 自动分配，启动环节，自动获取配置的角色的所有用户
        if("0001".equals(workFlowDTO.getSelectType())) {
             if(!StringUtil.isNUll(oldRoleCodes)) {
                 List<Object[]> userInfos = roleDAO.findUserInfoByRoleCodes(Arrays.asList(oldRoleCodes.split(",")));
                 if(userInfos.size() > 0) {
                     StringBuilder userCodes = new StringBuilder();
                     for(Object[] object : userInfos) {
                         userCodes.append(object[1]).append(",");
                     }
                     workFlowDTO.setUserCodes(userCodes.toString());
                 } else {
                     ExceptionUtil.throwWorkFlowConfigException("启动流程环节：流程启动工单环节的可操作角色中不存在用户。");
                 }
             } else {
                 ExceptionUtil.throwWorkFlowConfigException("启动流程环节：未配置流程启动工单环节的可操作角色。");
             }
        }
        
        //在设置下一步前 保存附加信息
        List<JSZCDTO> list = workFlowDTO.getJszcdtos();
        if(list!=null){
	        for (JSZCDTO jszcdto : list) {
	        	jszcdto.setProcessId(workFlowDTO.getProcessInstanceId());
	        	jszcdto.setTaskId(workFlowDTO.getTaskId());
	        	JSZCEntity entity = new JSZCEntity();
	        	BeanUtils.copyProperties(jszcdto, entity);
	        	jszcDao.save(entity);
			}
        }
       
        
        setNextTaskInfo(workFlowDTO);
        workFlowDTO.setProcessDefinedId(processInstance.getProcessDefinitionId());
        return workFlowDTO;
    }

    private WorkFlowDTO setNextTaskInfo(WorkFlowDTO workFlowDTO) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, UnsupportedEncodingException {
        //the next tasks
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(workFlowDTO.getProcessInstanceId()).list();
        for (Task task : tasks) {
            workFlowDTO.setTaskId(task.getId());
            setCandidateGroup(workFlowDTO);
        }
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO approvalWorkFlow(WorkFlowDTO workFlowDTO) {
        Task task = taskService.createTaskQuery().taskId(workFlowDTO.getTaskId()).singleResult();
        if (workFlowDTO.getMark() != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), "0002", workFlowDTO.getMark());
        }
        if (workFlowDTO.getApprovalFlag() != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), "0001", workFlowDTO.getApprovalFlag());
        }
        taskService.complete(task.getId(), workFlowDTO.getVars());
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO getTask(WorkFlowDTO workFlowDTO) {
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        SearchFilter searchFilter1 = new SearchFilter();
        searchFilter1.setFieldName("actInstanceEntity.id");
        searchFilter1.setOperator(SearchFilter.Operator.EQ);
        searchFilter1.setValue(workFlowDTO.getProcessInstanceId());
        searchFilters.add(searchFilter1);
        if (workFlowDTO.getAssignUserCode() != null) {
            SearchFilter searchFilter2 = new SearchFilter();
            searchFilter2.setFieldName("assignee.userCode");
            searchFilter2.setOperator(SearchFilter.Operator.EQ);
            searchFilter2.setValue(workFlowDTO.getAssignUserCode());
            searchFilters.add(searchFilter2);
        }
        //ToDO
//		ActHisTaskInstanceDAO actHisTaskInstanceDAO = SpringUtil.getBeanByName("actHisTaskInstanceDAO");
//		List <ActHisTaskInstance> actTaskEntitys = actHisTaskInstanceDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilters, ActHisTaskInstance.class));
//		workFlowDTO.setActTaskEntity(actTaskEntitys.get(0));
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO getOpperatorBtns(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {

        workFlowDTO = WorkFlowCoreUtil.invokeMethod(workFlowDTO, httpServletRequest);
        String processDefinitionId = null;

        Task nowTask = null;
        /**
         * 是否是找启动节点的操作项。
         */
        if (workFlowDTO.getTaskId() == null) {
            /**
             * 启动默认都按照最新版本的启动。
             */
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workFlowDTO.getWorkFlowTypeId()).latestVersion().singleResult();
            processDefinitionId = processDefinition.getId();
            workFlowDTO.setIsStartOpperator(true);
        } else {

            /**
             * 已启动
             */
            nowTask = taskService.createTaskQuery().taskId(workFlowDTO.getTaskId()).singleResult();
            processDefinitionId = nowTask.getProcessDefinitionId();
            workFlowDTO.setIsStartOpperator(false);
        }

        ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        workFlowDTO.setWorkFlowTypeId(readOnlyProcessDefinition.getKey());
        PvmActivity pvmActivity = null;

        //获取指定工作流的操作项
        SearchFilter searchFilter = new SearchFilter("workFlowTypeId", readOnlyProcessDefinition.getKey());
        List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter, WorkFlowOperaterEntity.class));
        Map<String, WorkFlowOperaterEntity> cacheMap = CollectionUtil.extractToMap(workFlowOperaterEntities, "operatorId");
        if (workFlowDTO.getIsStartOpperator()) {
            workFlowDTO.setNodeType(StringUtil.toString(readOnlyProcessDefinition.getInitial().getProperty("type")));
            List<PvmTransition> transitions = readOnlyProcessDefinition.getInitial().getOutgoingTransitions();
            //此处认为启动节点之后一定是提交人编辑提交
            PvmTransition pvmTransition = transitions.get(0);
            WorkFlowOperaterEntity workFlowOperaterEntity = cacheMap.get(pvmTransition.getId());
            if (workFlowOperaterEntity == null) {
                //TODO throw workflow Exception
                throw new RuntimeException();
            }
            workFlowDTO.setAssignRoleCode(workFlowOperaterEntity.getRoleCodes());
            PvmActivity pvmActivityD = pvmTransition.getDestination();
            pvmActivity = pvmActivityD;
        } else {
            pvmActivity = readOnlyProcessDefinition.findActivity(nowTask.getTaskDefinitionKey());
            workFlowDTO.setNodeType(StringUtil.toString(pvmActivity.getProperty("type")));
        }

        List<WorkFlowOperaterDTO> workFlowOperaterDTOs = new ArrayList<WorkFlowOperaterDTO>();

        String haveOperaterStr = null;
        List<String> haveOperaterList = Lists.newArrayList();

        if (!workFlowDTO.getIsStartOpperator()) {
            String beforAction = runtimeService.getVariable(nowTask.getExecutionId(), "ACTION1").toString();
            WorkFlowOperaterEntity workFlowOperaterEntity = cacheMap.get(beforAction);

            if ("0001".equals(workFlowDTO.getOperaterSType())) {
                haveOperaterStr = workFlowOperaterEntity.getHaveOperaterStr();
                if ((haveOperaterStr != null) && (haveOperaterStr.trim().length() > 0)) {
                    haveOperaterList = Arrays.asList(haveOperaterStr.split(","));
                }
            } else if ("0002".equals(workFlowDTO.getOperaterSType())) {
                haveOperaterStr = workFlowOperaterEntity.getPhoneHaveOperaterStr();
                if ((haveOperaterStr != null) && (haveOperaterStr.trim().length() > 0)) {
                    haveOperaterList = Arrays.asList(haveOperaterStr.split(","));
                }
            }

            //添加一个放弃办理按钮
            WorkFlowOperaterDTO workFlowOperaterDTO = new WorkFlowOperaterDTO();
            workFlowOperaterDTO.setText("放弃");
            workFlowOperaterDTO.setId("wf_giveup");
            workFlowOperaterDTO.setCallBackDataFun("goBack");
            workFlowOperaterDTO.setIsShowDialog("0002");
            workFlowOperaterDTOs.add(workFlowOperaterDTO);
        }

        Boolean tflag = true;
        List<String> currentOperaterIds = new ArrayList<String>();
        workFlowDTO.setUserTask(pvmActivity.getId());
        if (cacheMap.get(pvmActivity.getId()) != null) {
            workFlowDTO.setUserType(cacheMap.get(pvmActivity.getId()).getUserType());
        }

        for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
            if (!tflag) {
                continue;
            }
            currentOperaterIds.add(pvmTransition.getId());
            if (haveOperaterList.contains(pvmTransition.getId()) || (haveOperaterList.size() == 0)) {
                WorkFlowOperaterDTO workFlowOperaterDTO = new WorkFlowOperaterDTO();
                workFlowOperaterDTO.setText(StringUtil.toString(pvmTransition.getProperty("name")));
                workFlowOperaterDTO.setId(pvmTransition.getId());
                workFlowOperaterDTO.setValue(getOperaterAction(StringUtil.toString(pvmTransition.getProperty("conditionText"))));
                WorkFlowOperaterEntity workFlowOperaterEntity = cacheMap.get(pvmTransition.getId());
                if (workFlowOperaterEntity != null) {
                    workFlowOperaterDTO.setRequestUrl(workFlowOperaterEntity.getRequestUrl());
                    workFlowOperaterDTO.setCreateDataFun(workFlowOperaterEntity.getCreateDataFun());
                    workFlowOperaterDTO.setCallBackDataFun(workFlowOperaterEntity.getCallBackDataFun());
                    workFlowOperaterDTO.setRoleCodes(workFlowOperaterEntity.getRoleCodes());
                    workFlowOperaterDTO.setIsShowDialog(workFlowOperaterEntity.getIsShowDialog());
                    workFlowOperaterDTO.setSelectType(workFlowOperaterEntity.getSelectType());
                    workFlowOperaterDTO.setMarkType(workFlowOperaterEntity.getMarkType());
                    workFlowOperaterDTO.setIsShowDate(workFlowOperaterEntity.getIsShowDate());
                    workFlowOperaterDTO.setPhoneMarkType(workFlowOperaterEntity.getPhoneMarkType());
                    workFlowOperaterDTO.setDefaultAdvice(workFlowOperaterEntity.getDefaultAdvice());
                }
                workFlowOperaterDTOs.add(workFlowOperaterDTO);
            }


        }

        workFlowDTO.setWorkFlowOperaterDTOs(workFlowOperaterDTOs);

        //获取该工作流类型所有的操作组
        List<WorkFlowOperaterGroupEntity> workFlowOperaterGroupEntities = workFlowOperaterGroupDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("workFlowTypeId", readOnlyProcessDefinition.getKey()), WorkFlowOperaterGroupEntity.class));
        Iterator<WorkFlowOperaterGroupEntity> workFlowOperaterGroupIterator = workFlowOperaterGroupEntities.iterator();
        while (workFlowOperaterGroupIterator.hasNext()) {
            Boolean flag = true;
            WorkFlowOperaterGroupEntity workFlowOperaterGroupEntity = workFlowOperaterGroupIterator.next();
            for (WorkFlowOperaterEntity workFlowOperaterEntity : workFlowOperaterGroupEntity.getWorkFlowOperaterEntities()) {
                if (currentOperaterIds.contains(workFlowOperaterEntity.getOperatorId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                workFlowOperaterGroupIterator.remove();
            }

        }

        for (WorkFlowOperaterGroupEntity workFlowOperaterGroupEntity : workFlowOperaterGroupEntities) {
            if (haveOperaterList.contains(workFlowOperaterGroupEntity.getId().toString() + "##") || (haveOperaterList.size() == 0)) {
                WorkFlowOperaterEntity workFlowOperaterEntity = workFlowOperaterGroupEntity.getWorkFlowOperaterEntities().get(0);
                WorkFlowOperaterDTO workFlowOperaterDTO = new WorkFlowOperaterDTO();
                workFlowOperaterDTO.setText(workFlowOperaterGroupEntity.getActionText());
                workFlowOperaterDTO.setId(workFlowOperaterGroupEntity.getOperater());
                workFlowOperaterDTO.setValue(workFlowOperaterGroupEntity.getId().toString() + "##");
                workFlowOperaterDTO.setRequestUrl(workFlowOperaterEntity.getRequestUrl());
                workFlowOperaterDTO.setCreateDataFun(workFlowOperaterEntity.getCreateDataFun());
                workFlowOperaterDTO.setCallBackDataFun(workFlowOperaterEntity.getCallBackDataFun());
                workFlowOperaterDTO.setPhoneMarkType(workFlowOperaterEntity.getPhoneMarkType());
                workFlowOperaterDTO.setOperaterType("0002");
                List<String> roleCodes = new ArrayList<String>();
                for (WorkFlowOperaterEntity workFlowOperaterEntityTemp : workFlowOperaterGroupEntity.getWorkFlowOperaterEntities()) {
                    workFlowOperaterDTO.getGroupAction().put(workFlowOperaterEntityTemp.getOperatorId(), workFlowOperaterEntityTemp.getRoleCodes());
                    roleCodes.add(workFlowOperaterEntityTemp.getRoleCodes());
                }
                workFlowOperaterDTO.setRoleCodes(roleCodes.toString().substring(1, roleCodes.toString().length() - 1).replaceAll(" ", ""));
                workFlowOperaterDTO.setIsShowDialog(workFlowOperaterEntity.getIsShowDialog());
                workFlowOperaterDTO.setSelectType(workFlowOperaterEntity.getSelectType());
                workFlowOperaterDTO.setMarkType(workFlowOperaterEntity.getMarkType());
                workFlowOperaterDTO.setIsShowDate(workFlowOperaterEntity.getIsShowDate());
                workFlowOperaterDTOs.add(workFlowOperaterDTO);
            }
        }

        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO getStartOpperatorBtns(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
        workFlowDTO.setIsStartOpperator(true);
        return getOpperatorBtns(workFlowDTO, httpServletRequest);
    }

    @Override
    public QueryTranDTO getNeedClaimList(QueryTranDTO queryTranDTO) throws IOException {
        String workOrderType = null;
        for(SearchFilter searchFilter : queryTranDTO.getSearchFilters()) {
            if("workOrderType".equals(searchFilter.getFieldName())) {
                if(searchFilter.getValue() != null) {
                    workOrderType = searchFilter.getValue().toString();
                }
            }
        }

        //有“认领”管理员权限的人，获取全部
	    List <String> adminCodes = Lists.newArrayList();
	    adminCodes.add(WorkFlowUtil.WF_CLAIM_ADMIN_ROLECODE);
	    List <Object[]> roleInfos = roleDAO.findByUserCodeAndINRoleCodes(UserUtil.getCurrentUserCode(), adminCodes);
        StringBuilder stringBuffer = new StringBuilder();
        String countSql = "";
        String sourceSql = "";
	    if(roleInfos.size() <= 0) {
            stringBuffer = stringBuffer.append(" where t2.USER_ID_ ='").append(UserUtil.getCurrentUserCode()).append("'  and t1.ASSIGNEE_ is null");
            countSql = "SELECT" +
                    " count(*)" +
                    " FROM" +
                    " 	act_ru_task t1" +
                    " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                    " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                    " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                    " join tb_workflow_operater t5 " +
                    " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                    stringBuffer.toString()+
                    (StringUtil.isNUll(workOrderType)?"":" and t3.key_ = '"+workOrderType+"'");
            sourceSql = "SELECT" +
                    " 	t1.id_ taskId," +
                    " 	t1.TASK_DEF_KEY_ taskKey," +
                    "	t1.NAME_ taskName," +
                    " 	t2.USER_ID_ userCode," +
                    " 	t3.KEY_ workflowType," +
                    "	t4.BUSINESS_KEY_ businessKey," +
                    "	t5.approvalUrl," +
                    "	t3.id_ processDefineId," +
                    "	t4.PROC_INST_ID_ processInstanceId" +
                    " FROM" +
                    " 	act_ru_task t1" +
                    " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                    " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                    " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                    " join tb_workflow_operater t5 " +
                    " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                    stringBuffer.toString()+
                    (StringUtil.isNUll(workOrderType)?"":" and t3.key_ = '"+workOrderType+"'")+
                    " order by t1.create_time_ desc";
        } else {
            stringBuffer = stringBuffer.append(" where t1.ASSIGNEE_ is null");
            countSql = "SELECT" +
                    " count(*)" +
                    " FROM" +
                    " 	act_ru_task t1" +
                    " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                    " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                    " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                    " join tb_workflow_operater t5 " +
                    " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                    stringBuffer.toString()+
                    (StringUtil.isNUll(workOrderType)?"":" and t3.key_ = '"+workOrderType+"'")+
                    " group by t1.id_,t1.TASK_DEF_KEY_ ,t1.NAME_ ,t3.KEY_,t4.BUSINESS_KEY_,t5.approvalUrl";

            sourceSql = "SELECT" +
                    " 	t1.id_ taskId," +
                    " 	t1.TASK_DEF_KEY_ taskKey," +
                    "	t1.NAME_ taskName," +
                    " 	group_concat(t2.USER_ID_) userCodes," +
                    " 	t3.KEY_ workflowType," +
                    "	t4.BUSINESS_KEY_ businessKey," +
                    "	t5.approvalUrl," +
                    "	t3.id_ processDefineId," +
                    "	t4.PROC_INST_ID_ processInstanceId" +
                    " FROM" +
                    " 	act_ru_task t1" +
                    " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                    " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                    " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                    " join tb_workflow_operater t5 " +
                    " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                    stringBuffer.toString()+
                    (StringUtil.isNUll(workOrderType)?"":" and t3.key_ = '"+workOrderType+"'")+
                    " group by t1.id_,t1.TASK_DEF_KEY_ ,t1.NAME_ ,t3.KEY_,t4.BUSINESS_KEY_,t5.approvalUrl"+
                    " order by t1.create_time_ desc";
            Map <String,Object> map = Maps.newHashMap();
            map.put("role", WorkFlowUtil.WF_CLAIM_ADMIN_ROLECODE);
            queryTranDTO.setReturnInfos(map);
        }
        queryTranDTO.setCountSql(countSql);
        queryTranDTO.setSourceSql(sourceSql);
        this.customerQuery(queryTranDTO);

        List<String> businessKeies = Lists.newArrayList();
        for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
            businessKeies.add(map.get("businessKey").toString());
        }

        //工单相关信息

        if (businessKeies.size() > 0) {
            List<SpmsWorkOrder> workOrderEntities = spmsWorkOrderDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("id", Operator.IN, businessKeies), SpmsWorkOrder.class));
            Map<String, SpmsWorkOrder> cacheMaps = CollectionUtil.extractToMap(workOrderEntities, "id");

            //enhance the data
            StringBuilder variSql = new StringBuilder("select * from act_hi_varinst t1 where t1.EXECUTION_ID_ in (");
            for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
                variSql.append("'").append(map.get("processInstanceId")).append("'").append(",");
                SpmsWorkOrder tempSpmsWorkOrder = cacheMaps.get(map.get("businessKey").toString());
                if(tempSpmsWorkOrder != null) {
                    if(map.get("approvalUrl") != null) {
                        map.put("approvalUrl", "/spms" + map.get("approvalUrl").toString() + "?workOrderId=" + map.get("businessKey") + "&taskId=" + map.get("taskId")+"&processInstanceId=" + map.get("processInstanceId"));
                        map.put("createUser", tempSpmsWorkOrder.getCreateUser().getUserName());
                        map.put("createDate", tempSpmsWorkOrder.getCreateDate());
                        map.put("type", tempSpmsWorkOrder.getType());
                        map.put("workOrderCode",tempSpmsWorkOrder.getWorkOrderCode());
                        if(roleInfos.size() > 0) {
                            map.put("flag", (Arrays.asList(map.get("userCodes").toString().split(",")).contains(UserUtil.getCurrentUserCode())==true)?1:2);
                        } else {
                            map.put("flag", 3);
                        }
                    } else {
                        logger.error("获取代办列表：任务的待办跳转url未配置:"+map.get("taskKey").toString());
                    }
                } else {
                    logger.error("has workorder had deleted which have workflowinstance:id:"+map.get("businessKey").toString());
                }
            }
            List <HistoricVariableInstance> varais = historyService.createNativeHistoricVariableInstanceQuery().sql(variSql.deleteCharAt(variSql.length() - 1).append(") and t1.NAME_ like 'usertask%' order by PROC_INST_ID_,NAME_").toString()).list();
            Map <String,Map<String,Integer>> timeMapsDetail = Maps.newLinkedHashMap();
            Map <String,Integer> timeMapsAllTime = Maps.newHashMap();
            for(HistoricVariableInstance historicVariableInstance : varais) {
                if(timeMapsDetail.containsKey(historicVariableInstance.getProcessInstanceId())) {
                    timeMapsDetail.get(historicVariableInstance.getProcessInstanceId()).put(historicVariableInstance.getVariableName(),Integer.valueOf((historicVariableInstance.getValue() == null) ? "0" : historicVariableInstance.getValue().toString()));
                    timeMapsAllTime.put(historicVariableInstance.getProcessInstanceId(),timeMapsAllTime.get(historicVariableInstance.getProcessInstanceId())+Integer.valueOf((historicVariableInstance.getValue() == null) ? "0" : historicVariableInstance.getValue().toString()));
                } else {
                    Map <String,Integer> map = Maps.newLinkedHashMap();
                    map.put(historicVariableInstance.getVariableName(), Integer.valueOf((historicVariableInstance.getValue()==null)?"0":historicVariableInstance.getValue().toString()));
                    timeMapsDetail.put(historicVariableInstance.getProcessInstanceId(), map);
                    timeMapsAllTime.put(historicVariableInstance.getProcessInstanceId(),Integer.valueOf((historicVariableInstance.getValue()==null)?"0":historicVariableInstance.getValue().toString()));
                }
            }
            Date nowDate = new Date();
            //set timeOutFlag 1超时 2没有超时
            for (Map<String, Object> map : queryTranDTO.getCustomDatas()){
                Map <String,Integer> tempMap = timeMapsDetail.get(map.get("processInstanceId"));
                Integer costTime = 0;
                Boolean flag = true;
                for(Map.Entry<String,Integer> entry : tempMap.entrySet()) {
                    if(flag) {
                        costTime = costTime + entry.getValue();
                    }
                    if(map.get("taskKey").toString().equals(entry.getKey())) {
                        flag = false;
                    }
                }
                //if the cost time bigger than the plan time  then timeout
                //timeout 1 intime 2
                if((nowDate.getTime()-((Date)map.get("createDate")).getTime())/3600000.00 > costTime) {
                    map.put("timeOutFlag",1);
                } else {
                    map.put("timeOutFlag",2);
                }
            }
        }
        return queryTranDTO;
    }

    /* (non-Javadoc)
     * @see com.harmazing.spms.workflow.manager.WorkFlowManager#getNeedAssingList(com.harmazing.spms.base.dto.QueryTranDTO)
     */
    @Override
    public QueryTranDTO getNeedAssingList(QueryTranDTO queryTranDTO)
            throws IOException {
        String countSql = "SELECT" +
                " count(*)" +
                " FROM" +
                " 	act_ru_task t1" +
                " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                " join tb_workflow_operater t5 " +
                " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                " join tb_user_user t6 on t2.user_id_ = t6.userCode" +
                " where t1.ASSIGNEE_ is null";
        String sourceSql = "SELECT" +
                " 	t1.id_ taskId," +
                " 	t1.TASK_DEF_KEY_ taskKey," +
                "	t1.NAME_ taskName," +
                " 	t2.USER_ID_ userCode," +
                " 	t3.KEY_ workflowType," +
                "	t4.BUSINESS_KEY_ businessKey," +
                "	t5.approvalUrl," +
                "	t6.userName" +
                " FROM" +
                " 	act_ru_task t1" +
                " JOIN act_ru_identitylink t2 ON t1.ID_ = t2.TASK_ID_" +
                " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                " join tb_workflow_operater t5 " +
                " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                " join tb_user_user t6 on t2.user_id_ = t6.userCode" +
                " where t1.ASSIGNEE_ is null"+
                " order by t1.create_time_ desc";

        queryTranDTO.setCountSql(countSql);
        queryTranDTO.setSourceSql(sourceSql);
        this.customerQuery(queryTranDTO);

        List<String> businessKeies = Lists.newArrayList();
        for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
            businessKeies.add(map.get("businessKey").toString());
        }

        //工单相关信息

        if (businessKeies.size() > 0) {
            List<SpmsWorkOrder> workOrderEntities = spmsWorkOrderDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("id", Operator.IN, businessKeies), SpmsWorkOrder.class));
            Map<String, SpmsWorkOrder> cacheMaps = CollectionUtil.extractToMap(workOrderEntities, "id");
            //enhance the data
            for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
                SpmsWorkOrder tempSpmsWorkOrder = cacheMaps.get(map.get("businessKey").toString());
                map.put("approvalUrl", "/spms" + map.get("approvalUrl").toString() + "?workOrderId=" + map.get("businessKey") + "&taskId=" + map.get("taskId")+"&processInstanceId=" + map.get("processInstanceId"));
                if(tempSpmsWorkOrder != null) {
                    map.put("createUser", tempSpmsWorkOrder.getCreateUser().getUserName());
                    map.put("createDate", tempSpmsWorkOrder.getCreateDate());
                    map.put("type", tempSpmsWorkOrder.getType());
                    map.put("workOrderCode",tempSpmsWorkOrder.getWorkOrderCode());
                } else {
                    logger.error("has workorder had deleted which have workflowinstance:id:"+map.get("businessKey").toString());
                }
            }
        }
        return queryTranDTO;
    }

    @Override
    public QueryTranDTO getMeApprovaling(QueryTranDTO queryTranDTO) throws IOException {
        String workOrderType = null;
        for(SearchFilter searchFilter : queryTranDTO.getSearchFilters()) {
            if("workOrderType".equals(searchFilter.getFieldName())) {
                if(searchFilter.getValue() != null) {
                    workOrderType = searchFilter.getValue().toString();
                }
            }
        }


        String countSql = "SELECT" +
                " count(*)" +
                " FROM" +
                " 	act_ru_task t1" +
                " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                " join tb_workflow_operater t5 " +
                " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                " where t1.ASSIGNEE_ ='" + UserUtil.getCurrentUserCode() + "'" +
                (StringUtil.isNUll(workOrderType)?"":"and t3.key_ = '"+workOrderType+"'");

        String sourceSql = "SELECT" +
                " 	t1.id_ taskId," +
                " 	t1.TASK_DEF_KEY_ taskKey," +
                "	t1.NAME_ taskName," +
                "   t1.ASSIGNEE_ userCode," +
                " 	t3.KEY_ workflowType," +
                "	t4.BUSINESS_KEY_ businessKey," +
                "	t5.approvalUrl," +
                "	t3.id_ processDefineId," +
                "	t4.PROC_INST_ID_ processInstanceId" +
                " FROM" +
                " 	act_ru_task t1" +
                " JOIN act_re_procdef t3 ON t1.PROC_DEF_ID_ = t3.ID_" +
                " join act_ru_execution t4 on t1.EXECUTION_ID_ = t4.ID_" +
                " join tb_workflow_operater t5 " +
                " on t3.KEY_ = t5.workFlowTypeId and t1.TASK_DEF_KEY_ = t5.operatorId" +
                " where t1.ASSIGNEE_ ='" + UserUtil.getCurrentUserCode() + "'" +
                (StringUtil.isNUll(workOrderType)?"":"and t3.key_ = '"+workOrderType+"'")+
                " order by t1.create_time_ desc";

        queryTranDTO.setCountSql(countSql);
        queryTranDTO.setSourceSql(sourceSql);
        this.customerQuery(queryTranDTO);

        List<String> businessKeies = Lists.newArrayList();
        for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
            businessKeies.add(map.get("businessKey").toString());
        }

        //工单相关信息
        if (businessKeies.size() > 0) {
            List<SpmsWorkOrder> workOrderEntities = spmsWorkOrderDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("id", Operator.IN, businessKeies), SpmsWorkOrder.class));
            Map<String, SpmsWorkOrder> cacheMaps = CollectionUtil.extractToMap(workOrderEntities, "id");

            //enhance the data
            StringBuilder variSql = new StringBuilder("select * from act_hi_varinst t1 where t1.EXECUTION_ID_ in (");
            for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
                variSql.append("'").append(map.get("processInstanceId")).append("'").append(",");
                SpmsWorkOrder tempSpmsWorkOrder = cacheMaps.get(map.get("businessKey").toString());
                if(tempSpmsWorkOrder != null) {
                    map.put("approvalUrl", "/spms" + map.get("approvalUrl").toString() + "?workOrderId=" + map.get("businessKey") + "&taskId=" + map.get("taskId")+"&processInstanceId=" + map.get("processInstanceId"));
                    map.put("createUser", tempSpmsWorkOrder.getCreateUser().getUserName());
                    map.put("createDate", tempSpmsWorkOrder.getCreateDate());
                    map.put("type", tempSpmsWorkOrder.getType());
                    map.put("workOrderCode",tempSpmsWorkOrder.getWorkOrderCode());
                } else {
                    logger.error("has workorder had deleted which have workflowinstance:id:"+map.get("businessKey").toString());
                }
            }
            List <HistoricVariableInstance> varais = historyService.createNativeHistoricVariableInstanceQuery().sql(variSql.deleteCharAt(variSql.length() - 1).append(") and t1.NAME_ like 'usertask%' order by PROC_INST_ID_,NAME_").toString()).list();
            Map <String,Map<String,Integer>> timeMapsDetail = Maps.newLinkedHashMap();
            Map <String,Integer> timeMapsAllTime = Maps.newHashMap();
            for(HistoricVariableInstance historicVariableInstance : varais) {
                if(timeMapsDetail.containsKey(historicVariableInstance.getProcessInstanceId())) {
                    timeMapsDetail.get(historicVariableInstance.getProcessInstanceId()).put(historicVariableInstance.getVariableName(),Integer.valueOf((historicVariableInstance.getValue() == null) ? "0" : historicVariableInstance.getValue().toString()));
                    timeMapsAllTime.put(historicVariableInstance.getProcessInstanceId(),timeMapsAllTime.get(historicVariableInstance.getProcessInstanceId())+Integer.valueOf((historicVariableInstance.getValue() == null) ? "0" : historicVariableInstance.getValue().toString()));
                } else {
                    Map <String,Integer> map = Maps.newLinkedHashMap();
                    map.put(historicVariableInstance.getVariableName(), Integer.valueOf((historicVariableInstance.getValue()==null)?"0":historicVariableInstance.getValue().toString()));
                    timeMapsDetail.put(historicVariableInstance.getProcessInstanceId(), map);
                    timeMapsAllTime.put(historicVariableInstance.getProcessInstanceId(),Integer.valueOf((historicVariableInstance.getValue()==null)?"0":historicVariableInstance.getValue().toString()));
                }
            }
            Date nowDate = new Date();
            //set timeOutFlag 1超时 2没有超时
            for (Map<String, Object> map : queryTranDTO.getCustomDatas()){
                Map <String,Integer> tempMap = timeMapsDetail.get(map.get("processInstanceId"));
                Integer costTime = 0;
                Boolean flag = true;
                for(Map.Entry<String,Integer> entry : tempMap.entrySet()) {
                    if(flag) {
                        costTime = costTime + entry.getValue();
                    }
                    if(map.get("taskKey").toString().equals(entry.getKey())) {
                        flag = false;
                    }
                }
                //if the cost time bigger than the plan time  then timeout
                //timeout 1 intime 2
                if((nowDate.getTime()-((Date)map.get("createDate")).getTime())/3600000.00 > costTime) {
                    map.put("timeOutFlag",1);
                } else {
                    map.put("timeOutFlag",2);
                }
            }
        }
        return queryTranDTO;
    }


    /* (non-Javadoc)
     * @see com.harmazing.spms.workflow.manager.WorkFlowManager#getTaskListQuery(com.harmazing.spms.base.dto.QueryTranDTO)
     */
    @Override
    public QueryTranDTO getTaskListQuery(QueryTranDTO queryTranDTO)
            throws IOException {
        String taskId = null;
        String processInstanceId = null;
        for (SearchFilter searchFilter : queryTranDTO.getSearchFilters()) {
            if ("taskId".equals(searchFilter.getFieldName())) {
                taskId = searchFilter.getValue().toString();
            }
            if("processInstanceId".equals(searchFilter.getFieldName())) {
                processInstanceId = searchFilter.getValue().toString();
            }
        }
        List<HistoricTaskInstance> historicTaskInstances = null;
        if(processInstanceId != null) {
            historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
        } else {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByTaskCreateTime().asc().list();
        }

        List<Map<String, Object>> datas = Lists.newArrayList();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            Map<String, Object> tempMap = Maps.newHashMap();
            tempMap.put("assignee", historicTaskInstance.getAssignee());
            tempMap.put("taskName", historicTaskInstance.getName());
            tempMap.put("startTime", historicTaskInstance.getStartTime());
            tempMap.put("claimTime", historicTaskInstance.getClaimTime());
            tempMap.put("endTime", historicTaskInstance.getEndTime());
            datas.add(tempMap);
        }
        queryTranDTO.setCustomDatas(datas);
        queryTranDTO.setShowColumnDTOs(queryTranDTO.getQueryDTO().getColumn());
        return queryTranDTO;
    }

    @Override
    @Transactional
    public WorkFlowDTO doClaimTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {
        workFlowDTO = WorkFlowCoreUtil.invokeMethod(workFlowDTO, httpServletRequest);
        SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(workFlowDTO.getBusinessKey());
        spmsWorkOrder.setStatus(WorkFlowUtil.STATUS_WORKORDER_HADDDING);
        spmsWorkOrderDAO.save(spmsWorkOrder);
        if (workFlowDTO.getAssignUserCode() != null) {
            //指派
            taskService.claim(workFlowDTO.getTaskId(), workFlowDTO.getAssignUserCode());
        } else {
            //自己认领
            taskService.claim(workFlowDTO.getTaskId(), UserUtil.getCurrentUserCode());
        }

        return workFlowDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test(String taskId) {
        taskService.claim(taskId, UserUtil.getCurrentUserCode());
    }

    @Transactional
    @Override
    public WorkFlowDTO doPhoneCompleteTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {


        return doCompleteTask(workFlowDTO, httpServletRequest);
    }

    @Override
    public void doPhoneApprovalHandle(WorkFlowDTO workFlowDTO) {
        Map vars = new HashMap();
        //the reject request need to search the reject action.
        if ("REJECTED".equals(workFlowDTO.getApprovalFlag())) {

            Task task = taskService.createTaskQuery().taskId(workFlowDTO.getTaskId()).singleResult();
            ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());

            PvmActivity pvmActivity = null;
            List<PvmActivity> pvmActivitys = (List<PvmActivity>) readOnlyProcessDefinition.getActivities();
            for (PvmActivity pvmActivityTemp : pvmActivitys) {
                if (pvmActivityTemp.getId().equals(task.getTaskDefinitionKey())) {
//						pvmActivity = pvmActivityTemp;
                    pvmActivity = pvmActivityTemp;
                    break;
                }
            }

            List<String> operatorIds = new ArrayList<String>();
            Map<String, String> map = new HashMap<String, String>();
            for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
                operatorIds.add(pvmTransition.getId());
                map.put(pvmTransition.getId(), pvmTransition.getProperty("conditionText").toString());
            }
            List<SearchFilter> searchFilter = Lists.newArrayList();
            searchFilter.add(new SearchFilter("operatorId", SearchFilter.Operator.IN, operatorIds));
            searchFilter.add(new SearchFilter("phoneActionType", "0001"));
            searchFilter.add(new SearchFilter("workFlowTypeId", readOnlyProcessDefinition.getProcessDefinition().getKey()));
            List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilter, WorkFlowOperaterEntity.class));
            if (workFlowOperaterEntities.size() <= 0) {
                throw new RuntimeException("plase config the workflowOperator");
            }
            workFlowDTO.setOperaterId(getOperaterAction(map.get(workFlowOperaterEntities.get(0).getOperatorId())));
            vars.put("ACTION", getOperaterAction(map.get(workFlowOperaterEntities.get(0).getOperatorId())));
        } else {
            vars.put("ACTION", workFlowDTO.getApprovalFlag());
            workFlowDTO.setOperaterId(workFlowDTO.getApprovalFlag());
            workFlowDTO.setMark(workFlowDTO.getMark());
        }
        workFlowDTO.setVars(vars);
    }

    @Override
    @Transactional
    public WorkFlowDTO doCompleteTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) throws BeansException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalStateException, Exception {

        //get actRunTask
        Task task = taskService.createTaskQuery().taskId(workFlowDTO.getTaskId()).singleResult();
        if (task == null) {
            throw new HasHandleException("");
        }
        //判断是否为放弃操作
        if("wf_giveup".equals(workFlowDTO.getOperaterId())) {
            taskService.unclaim(task.getId());
            return workFlowDTO;
        }

        workFlowDTO.setWorkFlowTypeId(repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult().getKey());

        //invoke customer method.
        try {
            if ((workFlowDTO.getObject() != null) && (((Map) workFlowDTO.getObject()).get("workFlowDTO") != null) && (((Map) ((Map) workFlowDTO.getObject()).get("workFlowDTO")).get("isNeedInvoke") != null) && ("0002".equals(((Map) ((Map) workFlowDTO.getObject()).get("workFlowDTO")).get("isNeedInvoke")))) {

            } else {
                if (!"0002".equals(workFlowDTO.getIsNeedInvoke())) {
                    workFlowDTO = WorkFlowCoreUtil.invokeMethod(workFlowDTO, httpServletRequest);
                }
            }
        } catch (Exception e) {
            if (!"0002".equals(workFlowDTO.getIsNeedInvoke())) {
                workFlowDTO = WorkFlowCoreUtil.invokeMethod(workFlowDTO, httpServletRequest);
            }
        }

//        taskService.claim(task.getId(), UserUtil.getCurrentUserCode());

        List<SearchFilter> searchFilters_1 = Lists.newArrayList();
        SearchFilter searchFilter_1 = new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId());
        SearchFilter searchFilter_2 = new SearchFilter("operatorId", workFlowDTO.getOperaterId());
        searchFilters_1.add(searchFilter_1);
        searchFilters_1.add(searchFilter_2);
        WorkFlowOperaterEntity workFlowOperaterEntity = workFlowOperaterDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilters_1, WorkFlowOperaterEntity.class));
        workFlowDTO.setSelectType(workFlowOperaterEntity.getSelectType());
        //add mark
        if (workFlowDTO.getMark() != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), "0004", workFlowDTO.getMark() + "_" + new Date().getTime() + "_" + UserUtil.getCurrentUserCode() + "_" + UserUtil.getCurrentUser().getUserName());
        }

        if (workFlowDTO.getOperaterId() != null) {
            if ("0001".equals(workFlowOperaterEntity.getPhoneMarkType())) {
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "0001", workFlowDTO.getMark() == null ? "" : workFlowDTO.getMark());
            } else if ("0002".equals(workFlowOperaterEntity.getPhoneMarkType())) {
            } else if ("0003".equals(workFlowOperaterEntity.getPhoneMarkType())) {
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "0001", workFlowOperaterEntity.getDefaultAdvice() == null ? "确认收到" : workFlowOperaterEntity.getDefaultAdvice());
            } else if ("0004".equals(workFlowOperaterEntity.getPhoneMarkType())) {
            } else if ("0005".equals(workFlowOperaterEntity.getPhoneMarkType())) {
            } else if ("0006".equals(workFlowOperaterEntity.getPhoneMarkType())) {
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "0001", workFlowDTO.getMark() == null ? "" : workFlowDTO.getMark());
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "0003", workFlowDTO.getMark() == null ? "" : workFlowDTO.getMark());
            } else if ("0007".equals(workFlowOperaterEntity.getPhoneMarkType())) {
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "0003", workFlowDTO.getMark() == null ? "" : workFlowDTO.getMark());
            }
        }

        String processInstanceId = task.getProcessInstanceId();


        SequenceFlow sequenceFlow = (SequenceFlow) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(workFlowDTO.getOperaterId());
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        searchFilters.add(new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId()));
        searchFilters.add(new SearchFilter("operatorId", sequenceFlow.getTargetRef()));
        List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilters, WorkFlowOperaterEntity.class));
        if ((workFlowOperaterEntities.size() > 0) && ("0002".equals(workFlowOperaterEntities.get(0).getCountersign()) || "0003".equals(workFlowOperaterEntities.get(0).getCountersign()))) {
            List<String> codes = Arrays.asList(workFlowDTO.getRoleCodes().split(","));
            workFlowDTO.getVars().put("multiAssignees", codes);
            workFlowDTO.setNowIsCounterSign("0002");
        }

        SequenceFlow sequenceFlow1 = (SequenceFlow) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(workFlowDTO.getOperaterId());
        List<SearchFilter> searchFilters1 = new ArrayList<SearchFilter>();
        searchFilters1.add(new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId()));
        searchFilters1.add(new SearchFilter("operatorId", sequenceFlow1.getSourceRef()));
        Boolean sourceFlag = false;
        List<WorkFlowOperaterEntity> workFlowOperaterEntities1 = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilters1, WorkFlowOperaterEntity.class));
        if ((workFlowOperaterEntities1.size() > 0) && ("0002".equals(workFlowOperaterEntities1.get(0).getCountersign()) || "0003".equals(workFlowOperaterEntities1.get(0).getCountersign()))) {
            sourceFlag = true;
        }

        //is cincurrent 0 no 1 yes
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        Integer concurrentFlag = 0;
        if (executionEntity.isConcurrent()) {
            concurrentFlag = 1;
        } else {
            concurrentFlag = 0;
        }

        if (!sourceFlag) {
            workFlowDTO.getVars().put("ACTION1", workFlowDTO.getOperaterId());
        } else {
            workFlowDTO.setUtilFiled("yes");
        }

        taskService.complete(workFlowDTO.getTaskId(), workFlowDTO.getVars());

        //remove toDOevent
        workFlowDTO.setBusinessKey(executionEntity.getBusinessKey());
        workFlowDTO.setIsConCurrent(concurrentFlag);

        //is completed?
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).finished().singleResult();
        SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(workFlowDTO.getBusinessKey());
        if (historicProcessInstance == null) {
            workFlowDTO.setProcessInstanceId(processInstanceId);
            //0001 自动分配，启动环节，自动获取配置的角色的所有用户
            if("0001".equals(workFlowDTO.getSelectType())) {
                if(!StringUtil.isNUll(workFlowDTO.getRoleCodes())) {
                    List<Object[]> userInfos = roleDAO.findUserInfoByRoleCodes(Arrays.asList(workFlowDTO.getRoleCodes().split(",")));
                    if(userInfos.size() > 0) {
                        StringBuilder userCodes = new StringBuilder();
                        for(Object[] object : userInfos) {
                            userCodes.append(object[1]).append(",");
                        }
                        workFlowDTO.setUserCodes(userCodes.toString());
                    } else {
                        ExceptionUtil.throwWorkFlowConfigException("启动流程环节：流程启动工单环节的可操作角色中不存在用户。");
                    }
                } else {
                    ExceptionUtil.throwWorkFlowConfigException("启动流程环节：未配置流程启动工单环节的可操作角色。");
                }
            }
            setNextTaskInfo(workFlowDTO);
            spmsWorkOrder.setStatus(WorkFlowUtil.STATUS_WORKORDER_PREHAND);
        } else {
            spmsWorkOrder.setStatus(WorkFlowUtil.STATUS_WORKORDER_COMPLETED);
        }
        spmsWorkOrderDAO.save(spmsWorkOrder);

        return workFlowDTO;
    }

    public static Specification<UserEntity> findUserByRoleCodeSpec(final String roleCode) {
        return new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(final Root<UserEntity> userEntity, final CriteriaQuery<?> query,
                                         final CriteriaBuilder cb) {
                SetJoin<UserEntity, RoleEntity> roleJoin = userEntity.join(userEntity.getModel().getSet("roleEntities", RoleEntity.class), JoinType.INNER);
                Predicate predicate = cb.equal(roleJoin.get("roleCode"), roleCode);
                return predicate;
            }
        };
    }

    public static Specification<RoleEntity> findRoleByUserCodeSpec(final String userCode) {
        return new Specification<RoleEntity>() {

            @Override
            public Predicate toPredicate(final Root<RoleEntity> roleEntity, final CriteriaQuery<?> query,
                                         final CriteriaBuilder cb) {
                SetJoin<RoleEntity, UserEntity> roleJoin = roleEntity.join(roleEntity.getModel().getSet("userEntities", UserEntity.class), JoinType.INNER);
                Predicate predicate = cb.equal(roleJoin.get("userCode"), userCode);
                return predicate;
            }
        };
    }

    public static Specification<RoleEntity> findRolesByRoleCodesAndUserCode(final String userCode, final List<String> roleCodes) {
        return new Specification<RoleEntity>() {

            @Override
            public Predicate toPredicate(final Root<RoleEntity> roleEntity, final CriteriaQuery<?> query,
                                         final CriteriaBuilder cb) {
                SetJoin<RoleEntity, UserEntity> roleJoin = roleEntity.join(roleEntity.getModel().getSet("userEntities", UserEntity.class), JoinType.INNER);
                Predicate predicate = cb.and(cb.equal(roleJoin.get("userCode"), userCode), roleEntity.get("roleCode").in(roleCodes));
                return predicate;
            }
        };
    }

    private String getOperaterAction(String conditionText) {
        if ((conditionText != null) && conditionText.length() > 3) {
            conditionText = conditionText.substring(2, conditionText.length() - 1);
            String[] conditionArr = conditionText.split("\"");
            //认为格式为ACTION="xxxx"
            return conditionArr[1];
        }
        return null;
    }

    @Override
    public WorkFlowDTO doGetElementInfo(WorkFlowDTO workFlowDTO) {
        for (FlowElement flowElement : repositoryService.getBpmnModel(workFlowDTO.getProcessDefinedId()).getProcesses().get(0).getFlowElements()) {

        }

        ProcessDefinitionEntity processDefinitionEntity = processEngine.getManagementService().executeCommand(new GetDeploymentProcessDefinitionCmd(workFlowDTO.getProcessDefinedId()));
        List<ActivityImpl> activities = processDefinitionEntity.getActivities();
        return null;
    }

    @Override
    public QueryTranDTO doGetProcessInfo(QueryTranDTO queryTranDTO) throws IOException {
        String countSql = "select count(*) from act_re_procdef t1 group by key_";
        String sourceSql = "SELECT" +
                "	t2.ID_ ID_," +
                "	t2.KEY_ KEY_," +
                "	t2.DEPLOYMENT_ID_," +
                "	t2.NAME_" +
                " FROM" +
                "	act_re_deployment t1" +
                " JOIN act_re_procdef t2 ON t1.ID_ = t2.DEPLOYMENT_ID_" +
                " GROUP BY" +
                "	t2.KEY_" +
                " order by t1.DEPLOY_TIME_ desc";
        queryTranDTO.setCountSql(countSql);
        queryTranDTO.setSourceSql(sourceSql);
        return this.customerQuery(queryTranDTO);
    }

    @Override
    public QueryTranDTO doGetProcessNodeInfo(QueryTranDTO queryTranDTO) throws IOException {

        //get searchFilter condition.
        List<SearchFilter> searchFilters = queryTranDTO.getSearchFilters();
        String processDefId = "";
        String processDefKey = "";

        //only handle the processDefinedId. other ignore.
        for (SearchFilter searchFilter : searchFilters) {
            if ("processDefinedId".equals(searchFilter.getFieldName())) {
                processDefId = searchFilter.getValue().toString();
            }
        }
        //get the processDefine by the processDefId
        ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefId);
        processDefKey = readOnlyProcessDefinition.getKey();
        List<PvmActivity> pvmActivities = (List<PvmActivity>) readOnlyProcessDefinition.getActivities();
        String sql = "";
        for (PvmActivity pvmActivity : pvmActivities) {

            if (WorkFlowUtil.WF_NODE_USERTASK.equals(pvmActivity.getProperty("type"))) {
                sql = sql + " select 'userTask' as type,  '" + pvmActivity.getProperty("name") + "' as name,'" + pvmActivity.getId() + "' as id,'" + processDefKey + "' as processKey from dual union";
            }
            for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
                sql = sql + " select 'sequenceFlow' as type,  '" + pvmTransition.getProperty("name") + "' as name,'" + pvmTransition.getId() + "' as id,'" + processDefKey + "' as processKey from dual union";
            }
        }
        sql = sql.substring(0, sql.lastIndexOf("union"));

        //estimate
        String countSql = "select count(*) from (" + sql + ") t1" +
                " left join tb_workflow_operater t2" +
                " on t1.id  = t2.operatorId and t2.workFlowTypeId = ''";

        //get the detail info
        String sourceSql = "select t1.type as type, t1.name as text,t1.id as operaterId,t2.requestUrl from (" + sql + ") t1" +
                " left join tb_workflow_operater t2" +
                " on t1.id  = t2.operatorId and t2.workFlowTypeId = '" + processDefId + "'";

        queryTranDTO.setCountSql(countSql);
        queryTranDTO.setSourceSql(sourceSql);
        //get data
        this.customerQuery(queryTranDTO);

        //get the workflow info in our system
        List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterManager.getWorkFlowOperaterByWorkFLowTypeId(processDefKey);
        Map<String, WorkFlowOperaterEntity> cacheMap = CollectionUtil.extractToMap(workFlowOperaterEntities, "operatorId");

        //enhance the data
        for (Map<String, Object> map : queryTranDTO.getCustomDatas()) {
            map.put("workFlowTypeId", processDefKey);
            if (cacheMap.keySet().contains(map.get("operaterId").toString())) {
                map.put("id", cacheMap.get(map.get("operaterId").toString()).getId());
                map.put("requestUrl", cacheMap.get(map.get("operaterId").toString()).getRequestUrl());
                map.put("createDataFun", cacheMap.get(map.get("operaterId").toString()).getCreateDataFun());
                map.put("callBackDataFun", cacheMap.get(map.get("operaterId").toString()).getCallBackDataFun());
                map.put("markType", cacheMap.get(map.get("operaterId").toString()).getMarkType());
                map.put("selectType", cacheMap.get(map.get("operaterId").toString()).getSelectType());
                map.put("roleCodes", cacheMap.get(map.get("operaterId").toString()).getRoleCodes());
                map.put("haveOperaterStr", cacheMap.get(map.get("operaterId").toString()).getHaveOperaterStr());
                map.put("phoneHaveOperaterStr", cacheMap.get(map.get("operaterId").toString()).getPhoneHaveOperaterStr());
                map.put("phoneMarkType", cacheMap.get(map.get("operaterId").toString()).getPhoneMarkType());
                map.put("isGiveUser", cacheMap.get(map.get("operaterId").toString()).getIsGiveUser());
                map.put("isShowDialog", cacheMap.get(map.get("operaterId").toString()).getIsShowDialog());
                map.put("isShowDate", cacheMap.get(map.get("operaterId").toString()).getIsShowDate());
                map.put("phoneActionType", cacheMap.get(map.get("operaterId").toString()).getPhoneActionType());
                map.put("countersign", cacheMap.get(map.get("operaterId").toString()).getCountersign());
                map.put("userType", cacheMap.get(map.get("operaterId").toString()).getUserType());
                map.put("defaultAdvice", cacheMap.get(map.get("operaterId").toString()).getDefaultAdvice());
                map.put("handleTime", cacheMap.get(map.get("operaterId").toString()).getHandleTime());
                cacheMap.remove(map.get("operaterId").toString());
            } else {
                map.put("id", null);
                map.put("requestUrl", null);
                map.put("createDataFun", null);
                map.put("callBackDataFun", null);
                map.put("markType", null);
                map.put("selectType", null);
                map.put("roleCodes", null);
                map.put("haveOperaterStr", null);
                map.put("phoneHaveOperaterStr", null);
                map.put("phoneMarkType", null);
                map.put("isGiveUser", null);
                map.put("isShowDialog", null);
                map.put("isShowDate", null);
                map.put("phoneActionType", null);
                map.put("countersign", null);
                map.put("userType", null);
                map.put("printControl", null);
                map.put("defaultAdvice", null);
                map.put("handleTime",null);
            }
        }

        //remove the deprecated operater
        for (WorkFlowOperaterEntity workFlowOperaterEntity : cacheMap.values()) {
            workFlowOperaterDAO.delete(workFlowOperaterEntity);
        }

        return queryTranDTO;
    }


    public void addToDoEvent(WorkFlowDTO workFlowDTO) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, UnsupportedEncodingException {
//		WorkFlowToDoConfigEntity workFlowToDoConfigEntity = workFlowToDoConfigDAO.findOne(DynamicSpecifications.bySearchFilter(new SearchFilter("workFlowTypeId",workFlowDTO.getWorkFlowTypeId()), WorkFlowToDoConfigEntity.class));
//		if(workFlowToDoConfigEntity == null) {
//			logger.error("WFEXCEPTION:the workflowtypeid todoconfig is null, please config the workflowtypeid in WorkFlowToDoConfigEntity.");
//			throw new RuntimeException("the workflowtypeid todoconfig is null, please config the workflowtypeid in WorkFlowToDoConfigEntity.");
//		}
//		
//		QueryTranDTO queryTranDTO = new QueryTranDTO();
//		QueryDTO queryDTO = new QueryDTO();
//		queryDTO.setBaseClass(workFlowToDoConfigEntity.getBaseClassName());
//		queryTranDTO.setQueryDTO(queryDTO);
//		SpringDataDAO<IEntity> springDataDAO = (SpringDataDAO<IEntity>)getObjectDAO(queryTranDTO);
//		Object object = null;
//		try {
//		    object = springDataDAO.findOne(Long.valueOf(workFlowDTO.getBusinessKey()));
//		} catch(Exception e) {
//		    logger.error("启动工作流之前请先设置workflowDTO的workFlowTypeId和businessType");
//		    throw new WorkFlowConfigException("启动工作流之前请先设置workflowDTO的workFlowTypeId和businessType");
//		} 
//		TodoDTO todoDTO = new TodoDTO();
//		UserEntity userEntity = userManager.getUserByUserCode(workFlowDTO.getAssignUserCode());
//		todoDTO.setUserId(userEntity.getId());
//		todoDTO.setAppId(Long.valueOf(workFlowToDoConfigEntity.getAppId()));
//		todoDTO.setTaskId(workFlowDTO.getTaskId());
//		todoDTO.setBusinessId(workFlowDTO.getBusinessKey());
//		todoDTO.setRoleCode(workFlowDTO.getCurrentRoleCode());
//		todoDTO.setTaskName(workFlowDTO.getUserTask());
//		todoDTO.setMailMessage(workFlowDTO.getMailMessage());
//		todoDTO.setMessageType(workFlowDTO.getMessageType());
//		String realReportType="";
//		if("draftSendDocApprovalV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    String nfwTypeCode = BeanUtils.getProperty(object, "nfwTitle").toString();
//		    if("0001".equals(nfwTypeCode)) {
//			todoDTO.setBusinessType("0001");
//			todoDTO.setBusinessTypeText("拟政府发文");
//		    }
//		    if("0002".equals(nfwTypeCode)) {
//			todoDTO.setBusinessType("0002");
//			todoDTO.setBusinessTypeText("拟政府发函");
//		    }
//		    if("0003".equals(nfwTypeCode)) {
//			todoDTO.setBusinessType("0003");
//			todoDTO.setBusinessTypeText("拟政府发电");
//		    }
//		    if("0004".equals(nfwTypeCode)) {
//			todoDTO.setBusinessType("0004");
//			todoDTO.setBusinessTypeText("拟办公厅发文");
//		    }
//		    if("0005".equals(nfwTypeCode)) {
//			todoDTO.setBusinessType("0005");
//			todoDTO.setBusinessTypeText("拟办公厅发函");
//		    }
//		}
//		
//		if("sendDocApprovalV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    String reportType = BeanUtils.getProperty(object, "reportType").toString();
//		    if("0001".equals(reportType)) {
//			realReportType = "0001";
//			todoDTO.setBusinessType("0006");
//			todoDTO.setBusinessTypeText("市政府发文");
//		    }	
//		    if("0002".equals(reportType)) {
//			realReportType = "0002";
//			todoDTO.setBusinessType("0007");
//			todoDTO.setBusinessTypeText("市政府发函");
//		    }	
//		    if("0003".equals(reportType)) {
//			todoDTO.setBusinessType("0008");
//			todoDTO.setBusinessTypeText("市政府发电");
//		    }	
//		    if("0005".equals(reportType)) {
//			realReportType = "0005";
//			todoDTO.setBusinessType("0009");
//			todoDTO.setBusinessTypeText("办公厅发文");
//		    }	
//		    if("0006".equals(reportType)) {
//			realReportType = "0006";
//			todoDTO.setBusinessType("0010");
//			todoDTO.setBusinessTypeText("办公厅发函");
//		    }
//		}
//		
//		if("sendTelegramApprovalV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//			todoDTO.setBusinessType("0008");
//			todoDTO.setBusinessTypeText("市政府发电");
//		}
//		
//		if("getDocFromHigherV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0011");
//		    todoDTO.setBusinessTypeText("上级来文");
//		}
//		
//		if("getDocFromLower".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0012");
//		    todoDTO.setBusinessTypeText("请示件");
//		}
//		
//		if("internalCheckReport".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0013");
//		    todoDTO.setBusinessTypeText("请示报告卡");
//		}
//		
//		if("getTelegramV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0014");
//		    todoDTO.setBusinessTypeText("来电");
//		}
//		
//		if("letterForBureaus".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0015");
//		    todoDTO.setBusinessTypeText("委办局函文");
//		}
//		if("meetContent".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0016");
//		    todoDTO.setBusinessTypeText("会议议题");
//		}
//		if("meetContentCW".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0027");
//		    todoDTO.setBusinessTypeText("常务会议议题");
//		}
//		if("meetContentBG".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0028");
//		    todoDTO.setBusinessTypeText("办公会议议题");
//		}
//		if("meetPlan".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0020");
//		    todoDTO.setBusinessTypeText("会议方案");
//		}
//		if("meetPlanSz".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0030");
//		    todoDTO.setBusinessTypeText("会议方案");
//		}
//		if("meetPlanFsz".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0031");
//		    todoDTO.setBusinessTypeText("会议方案");
//		}
//		if("meetTell".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0021");
//		    todoDTO.setBusinessTypeText("会议通知");
//		}
//		if("meetMark".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0023");
//		    todoDTO.setBusinessTypeText("会议纪要");
//		}
//		todoDTO.setSpeed(WorkFlowUtil.WF_URGENCY_4);
//		if("dynamicwork".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0017");
//		    todoDTO.setBusinessTypeText("动态期刊");
//		}
//		if("superviseSZ".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0018");
//		    todoDTO.setBusinessTypeText("督办通知");
//		}
//		if("superviseFSZ".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0019");
//		    todoDTO.setBusinessTypeText("督办通知");
//		}
//		if("superviseSZBranch".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0024");
//		    todoDTO.setBusinessTypeText("督办通知");
//		}
//		if("superviseSumList".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0022");
//		    todoDTO.setBusinessTypeText("督办单汇总");
//		}
//		
//		//0025为外出报备，没使用流程引擎，所以在这儿报备一下。
//		try {
//		    String urgency = BeanUtils.getProperty(object, "urgency");
//		    todoDTO.setSpeed(urgency);
//		} catch(Exception e) {
//		    logger.error("the businessType have not urgency");
//		}
//		try {
//		    	if(workFlowToDoConfigEntity.getTitleField().startsWith("#")) {
//		    	    todoDTO.setTitle(workFlowToDoConfigEntity.getTitleField().substring(1));
//		    	} else {
//				todoDTO.setTitle(BeanUtils.getProperty(object, workFlowToDoConfigEntity.getTitleField()));
//		    	}
//			Method method = object.getClass().getMethod("getCreateDate", null);
//			Date startDate = (Date)method.invoke(object, null);
//			todoDTO.setStartDate(startDate);
//			if("0001".equals(todoDTO.getSpeed())) {
//			    todoDTO.setTheDeadLineTime(new Date(startDate.getTime()+86400000));
//			}
//			if("0002".equals(todoDTO.getSpeed())) {
//			    todoDTO.setTheDeadLineTime(new Date(startDate.getTime()+259200000));
//			}
//			if("0003".equals(todoDTO.getSpeed())) {
//			    todoDTO.setTheDeadLineTime(new Date(startDate.getTime()+432000000));
//			}
//			if("0004".equals(todoDTO.getSpeed())) {
//			    todoDTO.setTheDeadLineTime(new Date(startDate.getTime()+604800000));
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//			String msg = "WFEXCEPTION:can not find the field:"+workFlowToDoConfigEntity.getTitleField()+" in the baseClass:" +workFlowToDoConfigEntity.getBaseClassName();
//			logger.error(msg);
//			throw new RuntimeException(msg);
//		}
//		
//		if("meetPlan".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0020");
//		    MeetPlanEntity meetPlanEntity = (MeetPlanEntity)object;
//		    if("0003".equals(meetPlanEntity.getTitleType())) {
//			try {
//			    todoDTO.setTitle(DateUtil.parseDateToString(meetPlanEntity.getPlanDate(),"yyyy-MM-dd HH:mm")+" 全体会");
//			}catch(Exception e) {
//			    logger.error(e.getMessage());
//			    throw new RuntimeException(e.getMessage());
//			}
//		    } else  {
//			if("0001".equals(meetPlanEntity.getTitleType())) {
//			    todoDTO.setTitle("第"+meetPlanEntity.getMeetCount()+"次常务会");
//			} else if("0002".equals(meetPlanEntity.getTitleType())) {
//			    todoDTO.setTitle("第"+meetPlanEntity.getMeetCount()+"次党组会");
//			} else if("0004".equals(meetPlanEntity.getTitleType())) {
//			    todoDTO.setTitle("第"+meetPlanEntity.getMeetCount()+"次市长办公会");
//			}
//			
//		    }
//		}
//		if("meetPlanSz".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setBusinessType("0020");
//		    MeetPlanEntity meetPlanEntity = (MeetPlanEntity)object;
//		    todoDTO.setTitle("第"+meetPlanEntity.getMeetCount()+"次市长办公会");
//		}
//		if("sendDocApprovalV1".equals(workFlowToDoConfigEntity.getWorkFlowTypeId())) {
//		    todoDTO.setUrl(workFlowToDoConfigEntity.getPcUrl()+"&id="+workFlowDTO.getBusinessKey()+"&taskId="+workFlowDTO.getTaskId()+"&type="+realReportType+"&mark="+todoDTO.getRoleCode());
//		} else {
//		    todoDTO.setUrl(workFlowToDoConfigEntity.getPcUrl()+"&id="+workFlowDTO.getBusinessKey()+"&taskId="+workFlowDTO.getTaskId()+"&mark="+todoDTO.getRoleCode());
//		}
//		
//		todoDTO.setMobileUrl(workFlowToDoConfigEntity.getPhoneUrl()+"&id="+workFlowDTO.getBusinessKey()+"&taskId="+workFlowDTO.getTaskId());
//		todoManager.addTodoEvents(todoDTO);
    }

    public void removeToDoEvent(WorkFlowDTO workFlowDTO) {
//		WorkFlowToDoConfigEntity workFlowToDoConfigEntity = workFlowToDoConfigDAO.findOne(DynamicSpecifications.bySearchFilter(new SearchFilter("workFlowTypeId",workFlowDTO.getWorkFlowTypeId()), WorkFlowToDoConfigEntity.class));
//		if(workFlowToDoConfigEntity == null) {
//			logger.error("WFEXCEPTION:the workflowtypeid todoconfig is null, please config the workflowtypeid in WorkFlowToDoConfigEntity.");
//			throw new RuntimeException("the workflowtypeid todoconfig is null, please config the workflowtypeid in WorkFlowToDoConfigEntity.");
//		}
//		UserEntity userEntity = UserUtil.getCurrentUser();
//		TodoDTO todoDTO = new TodoDTO();
//		todoDTO.setAppId(Long.valueOf(workFlowToDoConfigEntity.getAppId()));
//		todoDTO.setBusinessId(workFlowDTO.getBusinessKey());
//		todoDTO.setUserId(userEntity.getId());
//		todoDTO.setTaskId(workFlowDTO.getTaskId());
//		if(workFlowDTO.getIsConCurrent() == 0) {
//			todoManager.deleteTodoEventsByBusinessKey(todoDTO);
//		} else {
//		    	if(19 == userEntity.getId()) {
//		    	    todoDTO.setRoleCode("002");
//		    	    todoManager.findInfoByBusinessIdR(todoDTO);
//		    	} else {
//		    	    todoManager.deleteTodoEventsAndTaskId(todoDTO);
//		    	}
//			
//		}
    }

    private WorkFlowDTO setAssignUserName(WorkFlowDTO workFlowDTO) {
//		if(workFlowDTO.getAssignUserCode() != null) {
//			UserEntity userEntity = userManager.getUserByUserCode(workFlowDTO.getAssignUserCode());
//			if(workFlowDTO.getAssignUserName() == null) {
//				workFlowDTO.setAssignUserName(userEntity.getUserName());
//			} else {
//				workFlowDTO.setAssignUserName(workFlowDTO.getAssignUserName()+","+userEntity.getUserName());
//			}
//			
//		} 
        return workFlowDTO;
    }

    private WorkFlowDTO setAssignRoleName(WorkFlowDTO workFlowDTO) {
//		if(workFlowDTO.getRoleCodes() != null) {
//			RoleEntity roleEntity = roleManager.getRoleByRoleCode(workFlowDTO.getAssignRoleCode());
//			if(workFlowDTO.getAssignRoleName() == null) {
//				workFlowDTO.setAssignRoleName(roleEntity.getRoleName());
//			} else {
//				workFlowDTO.setAssignRoleName(workFlowDTO.getAssignRoleName()+","+roleEntity.getRoleName());
//			}
//		}
        return workFlowDTO;
    }

    private WorkFlowDTO setAssignRoleName1(WorkFlowDTO workFlowDTO) {
//		if(workFlowDTO.getUserCodes() != null) {
//			UserEntity userEntity = userManager.getUserByUserCode(workFlowDTO.getAssignRoleCode());
//			if(workFlowDTO.getAssignRoleName() == null) {
//				workFlowDTO.setAssignRoleName(userEntity.getUserName());
//			} else {
//				workFlowDTO.setAssignRoleName(workFlowDTO.getAssignRoleName()+","+userEntity.getUserName());
//			}
//		}
        return workFlowDTO;
    }

    private WorkFlowDTO setCandidateGroup(WorkFlowDTO workFlowDTO) {
        List<String> userCodes = new ArrayList<String>();
        if ((workFlowDTO.getRoleCodes()) != null && (workFlowDTO.getRoleCodes().length() != 0)) {
            userCodes = Arrays.asList(workFlowDTO.getUserCodes().split(","));
        }
        for (String userCode : userCodes) {
//			taskService.addCandidateGroup(workFlowDTO.getTaskId(), roleCode);
            taskService.addCandidateUser(workFlowDTO.getTaskId(), userCode);
        }
        return workFlowDTO;
    }

    private WorkFlowOperaterEntity getRoleCodesByOperaterId(WorkFlowDTO workFlowDTO) {

        if (workFlowDTO.getWorkFlowTypeId() == null || workFlowDTO.getOperaterId() == null) {
            return new WorkFlowOperaterEntity();
        }
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        SearchFilter searchFilter1 = new SearchFilter("workFlowTypeId", workFlowDTO.getWorkFlowTypeId());
        SearchFilter searchFilter2 = new SearchFilter("operatorId", workFlowDTO.getOperaterId());
        searchFilters.add(searchFilter1);
        searchFilters.add(searchFilter2);

        List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilters, WorkFlowOperaterEntity.class));
        if (workFlowOperaterEntities.size() > 1) {
            throw new RuntimeException("has two same id for one workFLowTypeId in WorkFlowOperaterEntity");
        }
        if (workFlowOperaterEntities.size() == 0) {
            return new WorkFlowOperaterEntity();
        } else {
            return workFlowOperaterEntities.get(0);
        }

    }

    public void cleanProcessDefineCache(String processDefineKey) {
        String cacheId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefineKey).latestVersion().singleResult().getId();
        ((ProcessEngineConfigurationImpl) processEngineConfiguration).getProcessDefinitionCache().remove(cacheId);
    }

    @Override
    public QueryTranDTO decorationData(QueryTranDTO queryTranDTO) {
        List<IEntity> iEntities = queryTranDTO.getPage().getContent();
        List<Map<String, Object>> customerDatas = Lists.newArrayList();
//		for(IEntity iEntity : iEntities) {
//			Map <String, Object> map = Maps.newHashMap(); 
//			TodoEntity todoEntity = (TodoEntity)iEntity;
//			map.put("appCode", todoEntity.getAppEntity().getAppCode());
//			map.put("todoTheme", todoEntity.getTodoTheme());
//			map.put("todoURL", todoEntity.getTodoURL());
//			map.put("completeDate", todoEntity.getTheDeadLineTime());
//			map.put("startDate", todoEntity.getStartDate());
//			if(todoEntity.getStartDate() != null) {
//        			if(new Date().getTime() - todoEntity.getStartDate().getTime() < 86400000) {
//        			    map.put("flag", 1);
//        			} else {
//        			    map.put("flag", 0);
//        			}
//			} else {
//			    map.put("flag", 0);
//			}
//			customerDatas.add(map);
//		}
        queryTranDTO.setCustomDatas(customerDatas);
        return queryTranDTO;
    }

    @Override
    public Map<String, Object> phoneGetApprovalUserInfo(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) {
        JsonObject jsonObject = new JsonObject();
        try {
            workFlowDTO.setOperaterSType("0002");
            workFlowDTO = getOpperatorBtns(workFlowDTO, httpServletRequest);
        } catch (Exception e) {
            throw new RuntimeException("get info error.");
        }
        int i = 0;

        List<WorkFlowOperaterDTO> workFlowOperaterDTOs = Lists.newArrayList();
        WorkFlowOperaterDTO workFlowOperaterDTOBack = null;
        WorkFlowOperaterDTO workFlowOperaterDTONext = null;
        for (WorkFlowOperaterDTO workFlowOperaterDTO : workFlowDTO.getWorkFlowOperaterDTOs()) {
            if ("文书科办理".equals(workFlowOperaterDTO.getText())) {
                workFlowOperaterDTO.setText("签批");
            } else if ("文书办办理".equals(workFlowOperaterDTO.getText())) {
                workFlowOperaterDTO.setText("签批");
            }

            if ("退回起草人".equals(workFlowOperaterDTO.getText())) {
                workFlowOperaterDTOBack = workFlowOperaterDTO;
            } else if ("退回".equals(workFlowOperaterDTO.getText())) {
                workFlowOperaterDTOBack = workFlowOperaterDTO;
            } else if ("下一步".equals(workFlowOperaterDTO.getText())) {
                workFlowOperaterDTONext = workFlowOperaterDTO;
            } else {
                workFlowOperaterDTOs.add(workFlowOperaterDTO);
            }
        }

        if (workFlowOperaterDTOBack != null) {
            workFlowOperaterDTOs.add(0, workFlowOperaterDTOBack);
        }
        if (workFlowOperaterDTONext != null) {
            workFlowOperaterDTOs.add(workFlowOperaterDTONext);
        }
        for (WorkFlowOperaterDTO workFlowOperaterDTO : workFlowOperaterDTOs) {
            i++;
            if ("0002".equals(workFlowOperaterDTO.getOperaterType())) {
                String[] roleCodeArr = workFlowOperaterDTO.getRoleCodes().split(",");
                List<String> roleCodes = Arrays.asList(roleCodeArr);

                Sort sort = new Sort("sort");

                List<WorkFlowGroupEntity> workFlowGroupEntities = workFlowGroupDAO.findAll(sort);
                List<RoleEntity> roleEntities = roleDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("roleCode", Operator.IN, roleCodes), RoleEntity.class), sort);

                Map<String, List<Map<String, String>>> userMap = Maps.newLinkedHashMap();
                for (WorkFlowGroupEntity workFlowGroupEntity : workFlowGroupEntities) {
                    List<Map<String, String>> userList = Lists.newArrayList();
                    for (RoleEntity roleEntity : roleEntities) {
                        if (workFlowGroupEntity.getRoleEntities().contains(roleEntity)) {
                            Map<String, String> map = Maps.newHashMap();
                            map.put("roleCode", roleEntity.getRoleCode());
                            map.put("userName", roleEntity.getRoleName());
                            map.put("action", workFlowOperaterDTO.getValue());
                            userList.add(map);
                        }
                    }
                    if (userList.size() > 0) {
                        userMap.put(workFlowGroupEntity.getGroupName(), userList);
                    }
                }

                jsonObject.setValue("button.btnNext.name", "下一步");
                jsonObject.setValue("button.btnNext.action", workFlowOperaterDTO.getValue());

                if ("0002".equals(workFlowOperaterDTO.getPhoneMarkType())) {
                    jsonObject.setValue("button.btnNext.mark", false);
                } else if ("0003".equals(workFlowOperaterDTO.getPhoneMarkType()) || "0005".equals(workFlowOperaterDTO.getPhoneMarkType())) {
                    jsonObject.setValue("button.btnNext.mark", workFlowOperaterDTO.getDefaultAdvice() == null ? "确认收到" : workFlowOperaterDTO.getDefaultAdvice());
                } else {
                    jsonObject.setValue("button.btnNext.mark", true);
                }
                jsonObject.setValue("button.btnNext.userInfo", userMap);
                jsonObject.setValue("button.btnNext.requestUrl", null);
                break;
            } else {
                String[] roleCodeArr = workFlowOperaterDTO.getRoleCodes().split(",");
                List<String> roleCodes = Arrays.asList(roleCodeArr);

                Sort sort = new Sort("sort");

                List<WorkFlowGroupEntity> workFlowGroupEntities = workFlowGroupDAO.findAll(sort);
                List<RoleEntity> roleEntities = roleDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("roleCode", Operator.IN, roleCodes), RoleEntity.class), sort);

                if ("0002".equals(workFlowOperaterDTO.getPhoneMarkType())) {
                    jsonObject.setValue("button.btn" + i + ".mark", false);
                } else if ("0003".equals(workFlowOperaterDTO.getPhoneMarkType()) || "0005".equals(workFlowOperaterDTO.getPhoneMarkType())) {
                    jsonObject.setValue("button.btn" + i + ".mark", workFlowOperaterDTO.getDefaultAdvice() == null ? "确认收到" : workFlowOperaterDTO.getDefaultAdvice());
                } else {
                    jsonObject.setValue("button.btn" + i + ".mark", true);
                }

                Map<String, List<Map<String, String>>> userMap = Maps.newLinkedHashMap();
                for (WorkFlowGroupEntity workFlowGroupEntity : workFlowGroupEntities) {
                    List<Map<String, String>> userList = Lists.newArrayList();
                    for (RoleEntity roleEntity : roleEntities) {
                        if (workFlowGroupEntity.getRoleEntities().contains(roleEntity)) {
                            Map<String, String> map = Maps.newHashMap();
                            map.put("roleCode", roleEntity.getRoleCode());
                            map.put("userName", roleEntity.getRoleName());
                            userList.add(map);
                        }
                    }
                    if (userList.size() > 0) {
                        userMap.put(workFlowGroupEntity.getGroupName(), userList);
                    }
                }
                jsonObject.setValue("button.btn" + i + ".name", workFlowOperaterDTO.getText());
                jsonObject.setValue("button.btn" + i + ".action", workFlowOperaterDTO.getValue());
                jsonObject.setValue("button.btn" + i + ".requestUrl", workFlowOperaterDTO.getRequestUrl());
                if (!"0002".equals(workFlowOperaterDTO.getSelectType())) {
                    jsonObject.setValue("button.btn" + i + ".userInfo", userMap);
                }
            }
        }
        jsonObject.setValue("userType", workFlowDTO.getUserType());
        jsonObject.setValue("userTask", workFlowDTO.getUserTask());
        return jsonObject.getData();
    }

    /* (non-Javadoc)
     * @see cn.clickmed.cmcp.workflow.manager.WorkFlowManager#initWorkFlowGroup(cn.clickmed.cmcp.workflow.dto.WorkFlowDTO)
     */
    @Override
    public List<TreeDTO> initWorkFlowGroup(WorkFlowDTO workFlowDTO) {
        List<TreeDTO> treeDTOs = Lists.newArrayList();
        String roleCodes = workFlowDTO.getRoleCodes();
        if ((roleCodes != null) && (roleCodes.length() <= 0)) {
            return treeDTOs;
        }
        List<String> roleCodeArr = Arrays.asList(roleCodes.split(","));
        List<Object> objects = workFlowOperaterDAO.findSelectInfoByRoleCodes(roleCodeArr);

        List<String> roleIds = Lists.newArrayList();
        for (Object tempInfo : objects) {
            Object[] nowInfoArr = (Object[]) tempInfo;
            TreeDTO treeDTOP = null;
            int index = roleIds.indexOf(nowInfoArr[0].toString());
            if (index == -1) {
                treeDTOP = new TreeDTO();
                treeDTOP.setName(nowInfoArr[2].toString());
                treeDTOP.setIsParent(true);
                treeDTOP.setId(nowInfoArr[0].toString());
                treeDTOP.setCode(nowInfoArr[1].toString());
                treeDTOP.setOpen(false);
                if ("0001".equals(workFlowDTO.getMark())) {
                    treeDTOP.setNocheck(true);
                }
                treeDTOs.add(treeDTOP);
            } else {
                treeDTOP = treeDTOs.get(index);
            }
            TreeDTO treeDTOC = new TreeDTO();
            treeDTOC.setName(nowInfoArr[5].toString());
            treeDTOC.setCode(nowInfoArr[4].toString());
            treeDTOC.setId(nowInfoArr[3].toString());
            treeDTOC.setIsParent(false);
            treeDTOP.getChildren().add(treeDTOC);
        }
        return treeDTOs;

    }

    /* (non-Javadoc)
     * @see cn.clickmed.cmcp.workflow.manager.WorkFlowManager#getMarkInfo(cn.clickmed.cmcp.workflow.dto.WorkFlowDTO)
     */
    @Override
    public Map<String, Object> getMarkInfo(WorkFlowDTO workFlowDTO) {

        Map<String, Object> result = Maps.newHashMap();
        String processInstanceId = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(workFlowDTO.getBusinessKey()).orderByProcessInstanceStartTime().desc().list().get(0).getId();
        String sql = "select t1.id_ taskId,t1.TASK_DEF_KEY_ taskKey from act_hi_taskinst t1 where t1.proc_inst_id_=" + processInstanceId + " order by t1.START_TIME_ desc";
        QueryDAO queryDAO = SpringUtil.getBeanByName("queryDAO");
        List<Map<String, Object>> datas = queryDAO.getMapObjects(sql);

        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        Map<String, String> markMaps = Maps.newHashMap();
        for (Comment comment : comments) {
            if ("0004".equals(comment.getType())) {
                markMaps.put(comment.getTaskId(), comment.getFullMessage());
            }
        }

        Map<String, List> map = Maps.newHashMap();
        for (Map<String, Object> tempMap : datas) {
            if (map.containsKey(tempMap.get("taskKey"))) {
                Map<String, String> map1 = Maps.newHashMap();
                map1.put("message", markMaps.get(tempMap.get("taskId")));
                map.get(tempMap.get("taskKey")).add(map1);
            } else {
                List list = Lists.newArrayList();
                Map<String, String> map1 = Maps.newHashMap();
                map1.put("message", markMaps.get(tempMap.get("taskId")));
                list.add(map1);
                map.put(tempMap.get("taskKey").toString(), list);
            }
        }
        result.put("markInfo", map);
        return result;
    }

    /* (non-Javadoc)
     * @see cn.clickmed.cmcp.workflow.manager.WorkFlowManager#getCurrentUserTask(cn.clickmed.cmcp.workflow.dto.WorkFlowDTO, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, Object> getCurrentUserTask(WorkFlowDTO workFlowDTO, HttpServletRequest httpServletRequest) {

//		String processDefinitionId = null;
//		ActTaskEntity task = null;
//		
//		//if taskId is not null, the workflow has not start.
//		workFlowDTO.setIsStartOpperator(false);
//		
//		ActTaskDAO actTaskDAO = SpringUtil.getBeanByName("actTaskDAO");
//		task = actTaskDAO.findOne(workFlowDTO.getTaskId());
//		processDefinitionId = task.getProcessDefineEntity().getId();
//		ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
//		workFlowDTO.setWorkFlowTypeId(readOnlyProcessDefinition.getKey());
//		
//		//获取指定工作流的操作项
//		SearchFilter searchFilter = new SearchFilter("workFlowTypeId",readOnlyProcessDefinition.getKey());
//		List <WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter, WorkFlowOperaterEntity.class));
//		Map <String, WorkFlowOperaterEntity> cacheMap = CollectionUtil.extractToMap(workFlowOperaterEntities, "operatorId");
//		PvmActivity pvmActivity = readOnlyProcessDefinition.findActivity(task.getExecutionEntity().getActId());
//		
        Map<String, Object> result = Maps.newHashMap();
//		result.put("userTask", pvmActivity.getId());
        return result;
    }

    @Override
    public TimeLineDTO getTimeLine(WorkFlowDTO workFlowDTO) throws ParseException {
        String processDefinitionId;
        HistoricProcessInstance processInstance = null;
        //根据workFlowTypeId 或者 processInstanceId找到对应的processDefinitionId。
        if (workFlowDTO.getProcessInstanceId() == null) {
            //processInstanceId为空表示未启动流程的timeline，此时workflowTypeId应该有值。
            if(workFlowDTO.getWorkFlowTypeId() == null) {
                ExceptionUtil.throwWorkFlowConfigException("获取时间线：未传入processInstanceId 或 workFlowTypeId");
            }
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workFlowDTO.getWorkFlowTypeId()).latestVersion().singleResult();
            if(processDefinition == null) {
                ExceptionUtil.throwWorkFlowConfigException("获取时间线：传入的workFlowTypeId不存在:"+processDefinition.getKey());
            }
            processDefinitionId = processDefinition.getId();

        } else {
            //processInstanceId有值，表示为已经启动的流程实例
            processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(workFlowDTO.getProcessInstanceId()).singleResult();
            if(processInstance == null) {
                ExceptionUtil.throwWorkFlowConfigException("获取时间线：传入的processInstanceId不存在");
            }
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //找到processDefinitionId对应的流程节点
        ReadOnlyProcessDefinition readOnlyProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        TimeLineDTO timeLineDTO = new TimeLineDTO();
        List <TimeLineNodeDTO> timeLineNodeDTOs = Lists.newArrayList();
        timeLineDTO.setTimeLineNodes(timeLineNodeDTOs);
        for(PvmActivity pvmActivity : readOnlyProcessDefinition.getActivities()) {
            TimeLineNodeDTO timeLineNodeDTO = new TimeLineNodeDTO();
            switch (pvmActivity.getProperty(WorkFlowUtil.WF_NODE_TYPE_KEY).toString()){
                case WorkFlowUtil.WF_NODE_STARTEVENT: {
                    //启动节点 放入list中第一个
                    timeLineNodeDTO.setNodeType(WorkFlowUtil.TIME_NODE_TYPES);
                    timeLineNodeDTO.setNodeId(pvmActivity.getId().toString());
                    timeLineDTO.getTimeLineNodes().add(0, timeLineNodeDTO);
                } break;
                case WorkFlowUtil.WF_NODE_ENDEVENT: {
                    //结束节点 放入list中最后一个
                    //TODO 此处假设结束节点总在最后一个
                    timeLineNodeDTO.setNodeType(WorkFlowUtil.TIME_NODE_TYPEE);
                    timeLineNodeDTO.setNodeId(pvmActivity.getId().toString());
                    timeLineDTO.getTimeLineNodes().add(timeLineNodeDTO);
                } break;
                case WorkFlowUtil.WF_NODE_USERTASK: {
                    timeLineNodeDTO.setNodeName(StringUtil.handleNUll(pvmActivity.getProperty(WorkFlowUtil.WF_NODE_NAME_KEY),null));
                    timeLineNodeDTO.setNodeId(pvmActivity.getId().toString());
                    timeLineDTO.getTimeLineNodes().add(timeLineNodeDTO);
                } break;
                default:{
                    ExceptionUtil.throwWorkFlowConfigException("获取时间线：任务节点类型不支持："+pvmActivity.getProperty(WorkFlowUtil.WF_NODE_TYPE_KEY).toString());
                }
            }
        }

        //找出当前的任务节点 未启动流程的话就是当前节点，启动流程的话查询当前任务节点
        if (workFlowDTO.getProcessInstanceId() == null) {
            timeLineDTO.setCompetingNodeId(timeLineDTO.getTimeLineNodes().get(1).getNodeId());
        } else {
            //时间点信息map
            Map<String,TimeLineNodeDTO> timeLineNodesMap = CollectionUtil.extractToMap(timeLineNodeDTOs,"nodeId");
            //节点备注信息，及节点一些额外信息
            List<Object> cacheTaskInfos = workFlowOperaterDAO.findTaskInfoByProcessInstanceId(workFlowDTO.getProcessInstanceId());

            for(Object oo : cacheTaskInfos) {
                Object [] o = (Object [])oo;
                //如果当前的任务节点信息为当前时间节点的数据，保存该数据
                if(timeLineNodesMap.keySet().contains(o[0].toString())) {
                    TimeLineNodeDTO timeLineNodeDTO = timeLineNodesMap.get(o[0].toString());
                    if(o[4] == null) {
                        timeLineDTO.setCompetingNodeId(o[0].toString());
                    } else {
                        Map <String,Object> taskInfo = Maps.newLinkedHashMap();
                        taskInfo.putAll(getNodeInfoByWorkFlowTypeAndTask(processInstance,o[0].toString(),o[6].toString()));

                        taskInfo.put("办理人：",o[1]);
                        taskInfo.put("任务开始时间：",o[2]==null?"":DateUtil.parseDateToString((Date)o[2],"yyyy-MM-dd HH:mm:ss"));
                        taskInfo.put("任务办理时间：",o[4]==null?"":DateUtil.parseDateToString((Date)o[4],"yyyy-MM-dd HH:mm:ss"));
                        taskInfo.put("办理信息：", o[5]==null?"":o[5]);
                        timeLineNodeDTO.getTaskInfos().add(taskInfo);
                    }
                }
            }
        }
        return timeLineDTO;
    }

    private Map <String,Object> getNodeInfoByWorkFlowTypeAndTask(HistoricProcessInstance processInstance, String taskKey ,String taskId) throws ParseException {
        Map <String,Object> result  = Maps.newLinkedHashMap();
        String processKey = processInstance.getProcessDefinitionId().substring(0,processInstance.getProcessDefinitionId().indexOf(":"));
        switch (processKey) {
            case "openService" : {
                switch (taskKey) {
                    case "usertask1": {
                        SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(processInstance.getBusinessKey());
                        result.put("开户人：",spmsWorkOrder.getSpmsUserName());
                        result.put("电话：",spmsWorkOrder.getSpmsUserMobile());
                        result.put("email：",spmsWorkOrder.getEmail());
                    }break;
                    //订户信息
                    case "usertask2": {
                        SearchFilter searchFilter = new SearchFilter("spmsWorkOrder.id", processInstance.getBusinessKey());
                        SpmsUserDAO spmsUserDAO = SpringUtil.getBeanByName("spmsUserDAO");
                        SpmsUser spmsUser = spmsUserDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilter, SpmsUser.class));
                        result.put("displaynone_wfuserId",spmsUser.getId());
                        result.put("姓名:",spmsUser.getFullname());
                        result.put("身份证:", spmsUser.getIdNumber()==null?"":spmsUser.getIdNumber());
                        result.put("用户类型:",DictUtil.getDictValue("spmsuser_type",spmsUser.getType().toString()));
//                        result.put("业务区域:",spmsUser.getBizArea().getName());
//                        result.put("用电区域:", spmsUser.getEleArea().getName());
                        result.put("住址:",spmsUser.getAddress());
                        result.put("电话:", spmsUser.getMobile());
                        result.put("邮箱:",spmsUser.getEmail());
//                        result.put("电表号:",spmsUser.getAmmeter());
                    }break;
                    case "usertask3": {
                        SearchFilter searchFilter = new SearchFilter("spmsWorkOrder.id", processInstance.getBusinessKey());
                        SpmsProductDAO spmsProductDAO = SpringUtil.getBeanByName("spmsProductDAO");
                        List<SpmsProduct> spmsProducts = spmsProductDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter, SpmsProduct.class));
                        //TODO 假设只有一个产品
                        result.put("displaynone_wfproductId",spmsProducts.get(0).getId());
                        result.put("displaynone_wfproductName",spmsProducts.get(0).getSpmsProductType().getNames());
                        result.put("产品类型:",spmsProducts.get(0).getSpmsProductType().getNames());
                        result.put("产品订购时间:",DateUtil.parseDateToString(spmsProducts.get(0).getSubscribeDate(),"yyyy-MM-dd"));
                    }break;
                    case "usertask4": {
//                        SearchFilter searchFilter = new SearchFilter("spmsWorkOrder.id", processInstance.getBusinessKey());
//                        SpmsUser spmsUser = spmsUserDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilter,SpmsUser.class));
//                        List<SpmsUserProductBinding> spmsUserProductBindings = spmsUserProductBindingDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("spmsUser.id",spmsUser.getId()),SpmsUserProductBinding.class));
//                        result.put("绑定网关:",spmsUser.getSpmsDevice().getMac());
////                        for(SpmsUserProductBinding spmsUserProductBinding : spmsUserProductBindings) {
////                        	if(spmsUserProductBinding.getDeviceType() == 2) {
////                        		result.put("空调:", spmsUserProductBinding.getSpmsDevice().getMac());
////                        	} else {
////                        		result.put("门窗传感:", spmsUserProductBinding.getSpmsDevice().getMac());
////                        	}
////                        }
//                        for(int i = 0;i< spmsUserProductBindings.size();i++) {
//                            if(spmsUserProductBindings.get(i).getDeviceType() == 2) {
//                                result.put("空调" + (i + 1) + ":", spmsUserProductBindings.get(i).getSpmsDevice().getMac());
//                            } else {
//                                result.put("门窗传感" + (i + 1) + ":", spmsUserProductBindings.get(i).getSpmsDevice().getMac());
//                            }
//                        }
////                        String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
////						List lst=queryDAO.getMapObjects(sql);
////						for(int i=0;i<lst.size();i++){
////							Map m=(Map) lst.get(i);
////							if(m.get("iKey").equals("mark")){
////								result.put("备注:", m.get("iValue").toString());
////							}
////						}
                    }
                    break;
                    case "usertask5": {
                        SearchFilter searchFilter = new SearchFilter("spmsWorkOrder.id", processInstance.getBusinessKey());
                        SpmsPhoneVisit spmsPhoneVisit = spmsPhoneVisitDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilter,SpmsPhoneVisit.class));
                        result.put("反馈信息:",DictUtil.getDictValue("phonevisit_infotype",spmsPhoneVisit.getInfoType().toString()));
//                        if(spmsPhoneVisit.getInfoType() == 4) {
//                            result.put("信息:",spmsPhoneVisit.getInfo());
//                        }
                        result.put("回访人:",spmsPhoneVisit.getCreateUser().getUserName());
                        result.put("回访时间:",DateUtil.parseDateToString(spmsPhoneVisit.getCreateDate(),"yyyy-MM-dd"));
                    }break;
                    default: {

                    }
                }
            }break;
            //业务变更 所有工单页表单项
            case "businessChange" : {
            	switch (taskKey) {
	            	case "usertask1":{
            		  SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(processInstance.getBusinessKey());
                        result.put("开户人：",spmsWorkOrder.getSpmsUserName());
                        result.put("电话：",spmsWorkOrder.getSpmsUserMobile());
                        result.put("email：",spmsWorkOrder.getEmail());
                        String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
						List lst=queryDAO.getMapObjects(sql);
						String productId="";
						String changeProductId="";
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("iKey").equals("orderProduct")){
								productId=m.get("iValue").toString();								
							}
							if(m.get("iKey").equals("changeProduct")){
								changeProductId=m.get("iValue").toString();								
							}
							
						}
						sql="select p.id,t.`names` pname from spms_product p,spms_product_type t where p.type_id=t.id and p.id = '"+productId+"'";
						lst=queryDAO.getMapObjects(sql);
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("id").equals(productId)){
								result.put("现有产品：",m.get("pname").toString());								
							}
						}
						sql = "select t.id,t.names pname from spms_product_type t where t.id = '" + changeProductId + "'";
						lst=queryDAO.getMapObjects(sql);
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("id").equals(changeProductId)){
								result.put("变更产品：",m.get("pname").toString());								
							}
						}
            		}break;
					case "usertask2":{
						String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
						List lst=queryDAO.getMapObjects(sql);
						String productId="";
						String changeProductId="";
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("iKey").equals("qfye")){
								result.put("欠费余额：",m.get("iValue"));
							}
							if(m.get("iKey").equals("jf")){
								if(m.get("iValue").equals("1")){
									result.put("是否缴费费：","是");
								}
								if(m.get("iValue").equals("2")){
									result.put("是否缴费费：","否");
								}
								if(m.get("iValue").equals("0")){
									result.put("是否缴费费：","");
								}
							}
							
						}
                        result.put("应缴费余额：","0元");
					}break;
					case "usertask3":{
						String pid=processInstance.getId();
						String sql="select * from tb_workflow_variable where processId='"+pid+"'";
						List lst=queryDAO.getMapObjects(sql);
						String productId="";
						String changeProductId="";
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("iKey").equals("orderProduct")){
								productId=m.get("iValue").toString();								
							}
							if(m.get("iKey").equals("changeproductId")){
								changeProductId=m.get("iValue").toString();								
							}
							
						}
						String str = "";
						if(null != productId && !"".equals(productId)){
							str = productId+"','"+changeProductId;
						}else{
							str = changeProductId;
						}
						sql="select p.id,t.`names` pname from spms_product p,spms_product_type t where p.type_id=t.id and p.id in('" + str + "')";
						lst=queryDAO.getMapObjects(sql);
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							if(m.get("id").equals(productId)){
								result.put("现有产品：",m.get("pname").toString());								
							}
							if(m.get("id").equals(changeProductId)){
								result.put("变更产品：",m.get("pname").toString());								
							}
						}
						if(null == changeProductId || "".equals(changeProductId)){
							sql="select t.`value` typename,d.mac from spms_user_product_binding b,spms_device d ,dict_device_type t "
									+ " where b.device_id=d.id and d.type=t.`code` and  b.product_id='"+productId+"'";
						}else{
							sql="select t.`value` typename,d.mac from spms_user_product_binding b,spms_device d ,dict_device_type t "
									+ " where b.device_id=d.id and d.type=t.`code` and  b.product_id='"+changeProductId+"'";
						}
						lst=queryDAO.getMapObjects(sql);
						String devices="";
						for(int i=0;i<lst.size();i++){
							Map m=(Map) lst.get(i);
							devices+=m.get("typename").toString()+":"+m.get("mac").toString()+";";
						}
						result.put("设备：", devices);
						
						sql="select * from tb_workflow_variable where taskId='"+taskId+"' and ikey = 'delLog'";
	            		lst=queryDAO.getMapObjects(sql);
	            		String delLog="";
	            		for(int i=0;i<lst.size();i++){
	            			Map m=(Map) lst.get(i);
	            			delLog = m.get("iValue").toString();
	            		}
	            		result.put("回收设备：", delLog);
	            		
					}break;
					case "usertask4":{
						List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
						for (JSZCEntity entity : list) {
							switch (entity.getiKey()) {
							case "infoType":
								result.put("回访结果", DictUtil.getDictValue("phonevisit_infotype", entity.getiValue()));
								break;
							default:
								break;
							}
						}
						break;
					}
					default:
						break;
				}
            }break;
            //业务退订 所有工单页表单项
            case "unsubscribe" : {
            	switch (taskKey) {
            	case "usertask1":{
            		SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(processInstance.getBusinessKey());
            		result.put("开户人：",spmsWorkOrder.getSpmsUserName());
            		result.put("电话：",spmsWorkOrder.getSpmsUserMobile());
            		result.put("email：",spmsWorkOrder.getEmail());
            		String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
            		List lst=queryDAO.getMapObjects(sql);
            		String productId="";
            		String changeProductId="";
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			if(m.get("iKey").equals("orderProduct")){
            				productId=m.get("iValue").toString();								
            			}
            		}
            		sql="select p.id,t.`names` pname from spms_product p,spms_product_type t where p.type_id=t.id and p.id in('"+productId+"','"+changeProductId+"')";
            		lst=queryDAO.getMapObjects(sql);
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			if(m.get("id").equals(productId)){
            				result.put("退订产品：",m.get("pname").toString());								
            			}
            		}
            		List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
            		for (JSZCEntity entity : list) {
            			switch (entity.getiKey()) {
            			case "tdReason":
            				result.put("退订原因：", DictUtil.getDictValue("unsubscribe_tdreason", entity.getiValue()));
            				break;
            			default:
            				break;
            			}
            		}
            		break;
            	}
            	case "usertask2":{
            		String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
					List lst=queryDAO.getMapObjects(sql);
					String productId="";
					String changeProductId="";
					for(int i=0;i<lst.size();i++){
						Map m=(Map) lst.get(i);
						if(m.get("iKey").equals("qfye")){
							result.put("欠费余额：",m.get("iValue"));
						}
						if(m.get("iKey").equals("jf")){
							if(m.get("iValue").equals("1")){
								result.put("是否缴费费：","是");
							}
							if(m.get("iValue").equals("2")){
								result.put("是否缴费费：","否");
							}
							if(m.get("iValue").equals("0")){
								result.put("是否缴费费：","");
							}
						}
						
					}
                    result.put("应缴费余额：","0元");
            	}break;
            	case "usertask3":{
            		//tring pid=processInstance.getSuperProcessInstanceId();
            		String sql="select * from tb_workflow_variable where taskId='"+taskId+"'";
            		List lst=queryDAO.getMapObjects(sql);
            		String productId="";
            		String changeProductId="";
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			if(m.get("iKey").equals("orderProduct")){
            				productId=m.get("iValue").toString();								
            			}
            		}
            		sql="select p.id,t.`names` pname from spms_product p,spms_product_type t where p.type_id=t.id and p.id in('"+productId+"','"+changeProductId+"')";
            		lst=queryDAO.getMapObjects(sql);
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			if(m.get("id").equals(productId)){
            				result.put("退订产品：",m.get("pname").toString());								
            			}
            		}
            		sql="select t.`value` typename,d.mac from spms_user_product_binding b,spms_device d ,dict_device_type t "
            				+ " where b.device_id=d.id and d.type=t.`code` and  b.product_id='"+productId+"'";
            		lst=queryDAO.getMapObjects(sql);
            		String devices="";
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			devices+=m.get("typename").toString()+":"+m.get("mac").toString()+";";
            		}
            		result.put("设备：", devices);
            		sql="select * from tb_workflow_variable where taskId='"+taskId+"' and ikey = 'delLog'";
            		lst=queryDAO.getMapObjects(sql);
            		String delLog="";
            		for(int i=0;i<lst.size();i++){
            			Map m=(Map) lst.get(i);
            			delLog = m.get("iValue").toString();
            		}
            		result.put("回收设备：", delLog);
            	}break;
            	case "usertask4":{
            		List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
            		for (JSZCEntity entity : list) {
            			switch (entity.getiKey()) {
            			case "infoType":
            				result.put("回访结果：", DictUtil.getDictValue("phonevisit_infotype", entity.getiValue()));
            				break;
            			default:
            				break;
            			}
            		}
            		break;
            	}
            	default:
            		break;
            	}
            }break;
            //技术支持 所有工单页表单项
            case "jszc" : {
            	switch (taskKey) {
	            	case "usertask1":{
	            		List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
	            		for (JSZCEntity entity : list) {
	            			switch (entity.getiKey()) {
	            			case "bxCause":
	            				result.put("报修项：", DictUtil.getDictValue("jszc_bxCause", entity.getiValue()));
	            				break;
	            			default:
	            				break;
	            			}
	            		}
	            		break;
	            	}
					case "usertask2":{
						List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
						for (JSZCEntity entity : list) {
							switch (entity.getiKey()) {
							case "failureCause":
								result.put("故障原因：", DictUtil.getDictValue("jszc_failurecause", entity.getiValue()));
								break;
							case "isSolve":
								result.put("是否解决：", DictUtil.getDictValue("jszc_isSolve", entity.getiValue()));
								break;
							default:
								break;
							}
						}
						break;
					}
					case "usertask3":{
						List<JSZCEntity> list = jszcDao.getTaskVariable(taskId);
						for (JSZCEntity entity : list) {
							switch (entity.getiKey()) {
							case "infoType":
								result.put("回访结果：", DictUtil.getDictValue("phonevisit_infotype", entity.getiValue()));
								break;
							default:
								break;
							}
						}
						break;
					}
					default:
						break;
				}
            }break;
            default:{

            }
        }
        return result;
    }
}
