package com.harmazing.spms.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
@SuppressWarnings({"unchecked","rawtypes"})
public class CollectionUtil {
	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * 
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 */
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw new RuntimeException("提取属性失败");
		}

		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
			    	Object o = PropertyUtils.getProperty(obj, propertyName);
				list.add( o==null?"":o);
			}
		} catch (Exception e) {
			throw new RuntimeException("提取属性失败");
		}

		return list;
	}
	
	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 和当前对象组合成Map.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @return {key:obj}的map
	 */
	public static Map extractToMap(final Collection collection, final String propertyName) {
		return extractToMap(collection,propertyName,false);
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 和当前对象组合成Map.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param isMap 是否为map.
	 * @return {key:obj}的map
	 */
	public static Map extractToMap(final Collection collection, final String propertyName,Boolean isMap) {
		Map map = new HashMap(collection.size());
		if(isMap) {
			for(Object tempObject : collection) {
				Map tempMap = (Map)tempObject;
				map.put(tempMap.get(propertyName),tempMap);
			}
			return map;
		} else {
			try {
				for (Object obj : collection) {
					map.put(PropertyUtils.getProperty(obj, propertyName),obj);
				}
			} catch (Exception e) {
				throw new RuntimeException("提取属性失败");
			}

			return map;
		}
	}
	
	public static Boolean isBlank(Collection <?> collection) {
		if((collection!=null)&&collection.size() >0) {
			return false;
		} else {
			return true;
		}
	}
}
