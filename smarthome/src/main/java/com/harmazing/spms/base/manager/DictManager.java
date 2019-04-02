package com.harmazing.spms.base.manager;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.common.manager.IManager;

/**
 * dict manager.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public interface DictManager extends IManager{
	
	/**
	 * get dictDtos
	 * @param tableNames
	 * @return Map <String, List<DictDTO>>
	 */
	public Map <String, List<DictDTO>> getDicts(List <String> tableNames);
}
