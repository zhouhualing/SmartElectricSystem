package com.harmazing.spms.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * search filter. 
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class SearchFilter {

	public enum Operator {
		EQ,NEQ,LIKE, GT, LT, GTE, LTE,CLIKE,CLICKIN,CLIKEIN,ISNULL,ISNOTNULL,IN,NOTIN,ORIN,BETWDATE,OR,BETWEEN

	}

	public String fieldName;
	public Object value;
	public Operator operator = Operator.EQ;
	public List <SearchFilter> orSearchFilters = new ArrayList<SearchFilter>();
	public List <SearchFilter> andSearchFilters = new ArrayList<SearchFilter>();
	public Boolean isAnd = true;
	public Boolean isLie = false;
	
	public SearchFilter() {
		
	}

    public void appendOrSearchFilter(SearchFilter searchFilter) {
        this.getOrSearchFilters().add(searchFilter);
    }

    public void appendAndSearchFilter(SearchFilter searchFilter) {
        this.getAndSearchFilters().add(searchFilter);
    }

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}
	
	public SearchFilter(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		return value;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Boolean getIsAnd() {
		return isAnd;
	}

	public void setIsAnd(Boolean isAnd) {
		this.isAnd = isAnd;
	}

	public Boolean getIsLie() {
	    return isLie;
	}

	public void setIsLie(Boolean isLie) {
	    this.isLie = isLie;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public List<SearchFilter> getOrSearchFilters() {
		return orSearchFilters;
	}

	public List<SearchFilter> getAndSearchFilters() {
		return andSearchFilters;
	}

	public void setAndSearchFilters(List<SearchFilter> andSearchFilters) {
		this.andSearchFilters = andSearchFilters;
	}

	public void setOrSearchFilters(List<SearchFilter> orSearchFilters) {
		this.orSearchFilters = orSearchFilters;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank((String) value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}
	
}
