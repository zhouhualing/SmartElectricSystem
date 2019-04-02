package com.harmazing.spms.base.manager;

import com.harmazing.spms.base.dto.QueryCustomDTO;
import com.harmazing.spms.common.manager.IManager;

/**
 * query custom mannager.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public interface QueryCustomManager extends IManager {
	
	/**
	 * save queryCustom
	 * @param queryCustomDTO
	 * @return QueryCustomDTO
	 */
	public QueryCustomDTO doSaveQueryCustomModify(QueryCustomDTO queryCustomDTO);
}
