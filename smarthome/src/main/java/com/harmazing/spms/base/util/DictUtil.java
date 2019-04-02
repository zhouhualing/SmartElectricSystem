package com.harmazing.spms.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dao.DictDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.DictDTO;

/**
 * dict Util.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class DictUtil {
	
	public static Map <String,List<DictDTO>> dictCache = new HashMap <String,List<DictDTO>>();
	
	public static List<DictDTO> getDictValues(String tableName) {
		if("dynamic_category".equals(tableName)){
			//政府工作动态单独处理，始终去数据库读取。
			String sql = "select * from dict_dynamic_category where status = '0001'";
		    QueryDAO queryDAO = SpringUtil.getBeanByName("queryDAO");
		    List<Map <String, Object>> datas = queryDAO.getMapObjects(sql);
		    List<DictDTO> newDictDTOs = new ArrayList<DictDTO>();
		    if(datas!=null && !datas.isEmpty()){
		    	for(Map <String, Object> map : datas){
		    		DictDTO dictDTO = new DictDTO();
		    		dictDTO.setCode(map.get("CODE").toString());
		    		dictDTO.setiOrder(map.get("IORDER").toString());
		    		dictDTO.setValue(map.get("VALUE").toString());
		    		newDictDTOs.add(dictDTO);
		    	}
		    }
			return newDictDTOs;
		}else{
			if(!dictCache.containsKey("dict_"+tableName)) {
				DictDAO dictDAO = SpringUtil.getBeanByName("dictDAO");
				dictCache.put("dict_"+tableName,dictDAO.loadDict("dict_"+tableName));	
			} 
			return dictCache.get("dict_"+tableName);
		}
		
	}
	
	public static String getDictValue(String tableName, String key) {
		List<DictDTO> dictDTOs = getDictValues(tableName);
		return (String)CollectionUtil.extractToMap(dictDTOs, "code","value").get(key);
	}
}
