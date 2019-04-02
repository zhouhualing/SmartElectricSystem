package com.harmazing.spms.base.dao;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.dao.IDAO;

/**
 * queryDAO.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public interface QueryDAO extends IDAO {

	/**
	 * find objects by nativesql.
	 * @param nativeSql
	 * @return List <Object>
	 */
	public List <Object> getObjects(String nativeSql);
	
	public List <Map<String,Object>> getMapObjects(String nativeSql);
	
	public Integer doExecuteSql(String nativeSql);
	
}
