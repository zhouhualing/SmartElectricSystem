 package com.harmazing.spms.base.dao;

import java.util.List;

import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.common.dao.IDAO;

/**
 * dict dao.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public interface DictDAO extends IDAO {
	
	/**
	 * find dict list by dict tableName
	 * @param dict's tableName
	 * @return List<DictDTO>
	 */
	public List<DictDTO> loadDict(String tableName);
}
