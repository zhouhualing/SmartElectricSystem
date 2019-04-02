package com.harmazing.spms.workflow.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.workflow.dao.WorkFlowOperaterDAO;
import com.harmazing.spms.workflow.dto.WorkFlowOperaterDTO;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;
import com.harmazing.spms.workflow.manager.WorkFlowManager;
import com.harmazing.spms.workflow.manager.WorkFlowOperaterManager;

@Service("workFlowOperaterManager")
public class WorkFlowOperaterManagerImpl implements WorkFlowOperaterManager {

	@Autowired
	private WorkFlowOperaterDAO workFlowOperaterDAO;
	
	
	@Override
	public List<WorkFlowOperaterEntity> getWorkFlowOperaterByWorkFLowTypeId(String workFlowTypeId) {
		SearchFilter searchFilter = new SearchFilter("workFlowTypeId", workFlowTypeId);
		List<WorkFlowOperaterEntity> workFlowOperaterEntities = getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilter, WorkFlowOperaterEntity.class));
		return workFlowOperaterEntities;
	}

	@Override
	@Transactional
	public WorkFlowOperaterEntity doSaveOperater(WorkFlowOperaterDTO workFlowOperaterDTO) {
		WorkFlowOperaterEntity workFlowOperaterEntity = new WorkFlowOperaterEntity();
		Integer oldValue=0;
		if((workFlowOperaterDTO.getId() != null)&&(workFlowOperaterDTO.getId().length() > 0)) {
			workFlowOperaterEntity = workFlowOperaterDAO.findOne(workFlowOperaterDTO.getId());
		}
		workFlowOperaterEntity.setText(workFlowOperaterDTO.getText());
		workFlowOperaterEntity.setWorkFlowTypeId(workFlowOperaterDTO.getWorkFlowTypeId());
		workFlowOperaterEntity.setCreateDataFun(workFlowOperaterDTO.getCreateDataFun());
		workFlowOperaterEntity.setCallBackDataFun(workFlowOperaterDTO.getCallBackDataFun());
		workFlowOperaterEntity.setMarkType(workFlowOperaterDTO.getMarkType());
		workFlowOperaterEntity.setSelectType(workFlowOperaterDTO.getSelectType());
		workFlowOperaterEntity.setOperatorId(workFlowOperaterDTO.getOperaterId());
		workFlowOperaterEntity.setRoleCodes(workFlowOperaterDTO.getRoleCodes());
		workFlowOperaterEntity.setRequestUrl(workFlowOperaterDTO.getRequestUrl());
		workFlowOperaterEntity.setIsGiveUser(workFlowOperaterDTO.getIsGiveUser());
		workFlowOperaterEntity.setIsShowDialog(workFlowOperaterDTO.getIsShowDialog());
		workFlowOperaterEntity.setIsShowDate(workFlowOperaterDTO.getIsShowDate());
		workFlowOperaterEntity.setCountersign(workFlowOperaterDTO.getCountersign());
		workFlowOperaterEntity.setType(workFlowOperaterDTO.getType());
		workFlowOperaterEntity.setHaveOperaterStr(workFlowOperaterDTO.getHaveOperaterStr());
		workFlowOperaterEntity.setPhoneHaveOperaterStr(workFlowOperaterDTO.getPhoneHaveOperaterStr());
		workFlowOperaterEntity.setPhoneMarkType(workFlowOperaterDTO.getPhoneMarkType());
		workFlowOperaterEntity.setPhoneActionType(workFlowOperaterDTO.getPhoneActionType());
		workFlowOperaterEntity.setUserType(workFlowOperaterDTO.getUserType());
		workFlowOperaterEntity.setDefaultAdvice(workFlowOperaterDTO.getDefaultAdvice());
		workFlowOperaterEntity.setHandleTime(workFlowOperaterDTO.getHandleTime());
		if("userTask".equals(workFlowOperaterDTO.getType())) {
			WorkFlowManager workFlowManager = SpringUtil.getBeanByName("workFlowManager");
			workFlowManager.cleanProcessDefineCache(workFlowOperaterDTO.getWorkFlowTypeId());
		}
		workFlowOperaterDAO.save(workFlowOperaterEntity);
		return workFlowOperaterEntity;
	}

	@Override
	public List<WorkFlowOperaterEntity> getWorkFlowOperaterBySearchFilter(Specification<WorkFlowOperaterEntity> specification) {
		List<WorkFlowOperaterEntity> workFlowOperaterEntities = workFlowOperaterDAO.findAll(specification);
		return workFlowOperaterEntities;
	}

}
