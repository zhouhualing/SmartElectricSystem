package com.harmazing.spms.base.manager;

import java.io.IOException;

import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.common.manager.IManager;

/**
 * 查询列表的manager.
 * @author Zhaocaipeng
 * since 2013-9-17
 */
public interface QueryManager extends IManager {
	
	/**
	 * 获取数据集
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 */
	public QueryTranDTO executeSearch(QueryTranDTO queryTranDTO);
	
	/**
	 * 删除对应byid
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 */
	public QueryTranDTO doDeleteObjectById(QueryTranDTO queryTranDTO);
	
	/**
	 * do customer query
	 * @param queryTranDTO
	 * @return QueryTranDTO
	 * @throws IOException
	 */
	public QueryTranDTO customerQuery(QueryTranDTO queryTranDTO) throws IOException;
}
