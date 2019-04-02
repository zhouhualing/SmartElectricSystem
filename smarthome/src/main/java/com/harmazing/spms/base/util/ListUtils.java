package com.harmazing.spms.base.util;

import java.util.List;

public class ListUtils {
	/**
	 * 判断list是否为空
	 * @param list
	 * @return
	 */
	public static boolean isNotBlank(List list){
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
}
