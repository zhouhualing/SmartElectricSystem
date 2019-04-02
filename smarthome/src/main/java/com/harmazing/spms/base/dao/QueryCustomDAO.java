package com.harmazing.spms.base.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.QueryCustomEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

/**
 * queryCustomDAO.
 * @author Zhaocaipeng
 * since 2013-10-15
 */
@Repository 
public interface QueryCustomDAO extends SpringDataDAO<QueryCustomEntity> {
	
	/**
	 * find querycustom by queryId and Usercode
	 * @param queryId
	 * @param userCode
	 * @return QueryCustomEntity
	 */
	@Query("select t from QueryCustomEntity t where t.queryId = :queryId and t.userEntity.userCode = :userCode")
	public QueryCustomEntity findByQueryIDANDUserCode(@Param("queryId")String queryId, @Param("userCode")String userCode);

}
