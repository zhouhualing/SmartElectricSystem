package com.harmazing.spms.base.util;

import java.util.ArrayList;
import java.util.List;

import com.harmazing.spms.common.dto.IDTO;

/**
 * bean util.
 * @author Zhaocaipeng
 * since 2013-9-16
 */
public class BeanUtils {

	/**
	 * copy all properties.
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		List <String> ignoreColumns = new ArrayList<String>();
		if(source instanceof IDTO) {
			ignoreColumns.add("id");
			ignoreColumns.add("version");
			ignoreColumns.add("createUser");
			ignoreColumns.add("createDate");
			ignoreColumns.add("lastModifyUser");
			ignoreColumns.add("lastModifyDate");
			
		} 
		copyProperties(source, target, ignoreColumns);
	}
	
	/**
	 * copy properties.
	 * @param source
	 * @param target
	 * @param exclusion  the properties of soruce which no copy.
	 */
	public static void copyProperties(Object source, Object target, List <String> exclusion) {
		org.springframework.beans.BeanUtils.copyProperties(source, target, exclusion.toArray(new String [] {}));
	}

}
