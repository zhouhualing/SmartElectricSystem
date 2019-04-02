package com.harmazing.spms.base.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * the impl of queryDAO.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Repository("queryDAO")
@SuppressWarnings("unchecked")
public class QueryDAOImpl implements QueryDAO {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	/* (non-Javadoc)
	 * @see cn.clickmed.cmcp.dao.query.QueryDAO#getObjects(java.lang.String)
	 */
	public List <Object> getObjects(String nativeSql) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery(nativeSql);
		List <Object> results =  query.getResultList();
		entityManager.close();
		return results;
	}
	
	public List <Map<String,Object>> getMapObjects(String nativeSql) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery(nativeSql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List <Map<String,Object>> results = query.getResultList();
		entityManager.close();
		return results;
	}

	@Override
	public Integer doExecuteSql(String nativeSql) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		int  value=0; 
		try{
			Query query = entityManager.createNativeQuery(nativeSql);
			value =  query.executeUpdate();
			entityManager.getTransaction().commit();
		}finally{
			entityManager.close();
		}
		return value;
	}


}
