package com.harmazing.spms.base.manager;


import java.util.List;

import com.harmazing.spms.base.dto.AttachmentInfoDTO;
import com.harmazing.spms.base.entity.AttachmentEntity;
import com.harmazing.spms.common.manager.IManager;

/**
 * attachment manager.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public interface AttachmentManager extends IManager {
	
	/**
	 * save attachment
	 * @param attachmentEntity
	 */
	public void doSaveAttachment(AttachmentEntity attachmentEntity);
	
	public AttachmentEntity findByAttachmentEntity(String fileId);
	
	public void doAddAttachmentToBusiness(String businessType,String businessId, List <String> attachmentIds);
	
	public void setAttachmentInfoDTO(String businessType,String businessId, List <AttachmentInfoDTO> attachmentInfoDTOs);
}
