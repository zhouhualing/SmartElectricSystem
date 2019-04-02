package com.harmazing.spms.base.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.AttachmentDAO;
import com.harmazing.spms.base.dto.AttachmentInfoDTO;
import com.harmazing.spms.base.entity.AttachmentEntity;
import com.harmazing.spms.base.manager.AttachmentManager;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;

/**
 * AttachmentManagerImpl. 
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Service
public class AttachmentManagerImpl implements AttachmentManager{

	@Autowired
	private AttachmentDAO attachmentDAO;
	
	@Override
	public void doSaveAttachment(AttachmentEntity attachmentEntity) {
		attachmentDAO.save(attachmentEntity);
	}

	@Override
	public AttachmentEntity findByAttachmentEntity(String fileId) {
		
		return attachmentDAO.findOne(fileId);
	}

	@Override
	public void doAddAttachmentToBusiness(String businessType, String businessId, List <String> attachmentIds) {
		SearchFilter searchFilter1 = new SearchFilter("businessId",businessId);
		SearchFilter searchFilter2 = new SearchFilter("businessType",businessType);
		List <SearchFilter> searchFilters = new ArrayList<SearchFilter>();
		searchFilters.add(searchFilter1);
		searchFilters.add(searchFilter2);
		List <AttachmentEntity> attachmentEntitiesOld = attachmentDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilters, AttachmentEntity.class));
		for(AttachmentEntity attachmentEntity : attachmentEntitiesOld) {
			attachmentEntity.setBusinessId(null);
			attachmentEntity.setBusinessType(null);
			attachmentDAO.save(attachmentEntity);
		}
		if(attachmentIds.size() >0) {
			SearchFilter searchFilter = new SearchFilter();
			searchFilter.setFieldName("id");
			searchFilter.setOperator(searchFilter.operator.IN);
			searchFilter.setValue(attachmentIds);
			List <AttachmentEntity> attachmentEntities = attachmentDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter, AttachmentEntity.class));
			for(AttachmentEntity attachmentEntity : attachmentEntities) {
				attachmentEntity.setBusinessType(businessType);
				attachmentEntity.setBusinessId(businessId);
				attachmentDAO.save(attachmentEntity);
			}
		}
		
	}

	@Override
	public void setAttachmentInfoDTO(String businessType, String businessId, List<AttachmentInfoDTO> attachmentInfoDTOs) {
		
		List <SearchFilter> searchFilters = new ArrayList<SearchFilter>();
		SearchFilter searchFilter1 = new SearchFilter("businessType",businessType);
		SearchFilter searchFilter2 = new SearchFilter("businessId",businessId);
		searchFilters.add(searchFilter1);
		searchFilters.add(searchFilter2);
		List <AttachmentEntity> attachmentEntities =  attachmentDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilters, AttachmentEntity.class));
		for(AttachmentEntity attachmentEntity : attachmentEntities) {
			AttachmentInfoDTO attachmentInfoDTO = new AttachmentInfoDTO();
			attachmentInfoDTO.setId(attachmentEntity.getId());
			attachmentInfoDTO.setName(attachmentEntity.getFileName());
			attachmentInfoDTO.setUrl("/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
			attachmentInfoDTO.setSize(attachmentEntity.getFileSize());
			attachmentInfoDTO.setType(attachmentEntity.getFilePostfix());
			attachmentInfoDTO.setThumbnailUrl(attachmentEntity.getFilePath()+"/test");
			attachmentInfoDTO.setPdfUrl("/cmcp/static/pdf/"+attachmentEntity.getId()+"."+attachmentEntity.getFilePostfix());
			attachmentInfoDTO.setType(attachmentEntity.getFilePostfix());
			attachmentInfoDTO.setDeleUrl("url2");
			attachmentInfoDTOs.add(attachmentInfoDTO);
		}
		
	}


}
