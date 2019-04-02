package com.harmazing.spms.base.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.AttachmentEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

/**
 * attachment DAO. 
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Repository("attachmentDAO")
public interface AttachmentDAO extends SpringDataDAO<AttachmentEntity>{
	
   
}
