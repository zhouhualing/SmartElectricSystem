package com.harmazing.spms.base.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.base.manager.DictManager;
import com.harmazing.spms.base.util.DictUtil;

/**
 * DictManagerImpl.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Service
public class DictManagerImpl implements DictManager {
	
	/* (non-Javadoc)
	 * @see cn.clickmed.cmcp.manager.dict.DictManager#getDicts(java.util.List)
	 */
	public Map <String, List<DictDTO>> getDicts(List <String> tableNames) {
		Map <String, List<DictDTO>> dictMaps = new HashMap<String, List<DictDTO>>();
		for(String tableName : tableNames) {
			dictMaps.put(tableName, DictUtil.getDictValues(tableName)) ;
		}
		return dictMaps;
	}
}
