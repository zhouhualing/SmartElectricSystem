package com.harmazing.spms.base.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.QueryCustomDAO;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.dto.QueryCustomDTO;
import com.harmazing.spms.base.entity.QueryCustomEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.QueryCustomManager;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.common.log.ILog;

@Service("queryCustomManager")
public class QueryCustomManagerImpl implements QueryCustomManager, ILog {

	@Autowired
	private QueryCustomDAO queryCustomDAO;
	
	@Override
	public QueryCustomDTO doSaveQueryCustomModify(QueryCustomDTO queryCustomDTO) {
		QueryCustomEntity queryCustomEntity = (QueryCustomEntity)queryCustomDAO.findByQueryIDANDUserCode(queryCustomDTO.getQueryId(), UserUtil.getCurrentUserCode());
		if(queryCustomEntity == null) {
			UserDAO userDAO = SpringUtil.getBeanByName("userDAO");
			UserEntity userEntityDB = userDAO.findByUserCode(UserUtil.getCurrentUserCode());
			queryCustomEntity = new QueryCustomEntity();
			queryCustomEntity.setQueryId(queryCustomDTO.getQueryId());
			queryCustomEntity.setUserEntity(userEntityDB);
		} 
		
		queryCustomEntity.setColumnNames(queryCustomDTO.getColumnNames());
		queryCustomDAO.save(queryCustomEntity);
		return queryCustomDTO;
	}

	public QueryCustomDAO getQueryCustomDAO() {
		return queryCustomDAO;
	}

	public void setQueryCustomDAO(QueryCustomDAO queryCustomDAO) {
		this.queryCustomDAO = queryCustomDAO;
	}
	
	

}
