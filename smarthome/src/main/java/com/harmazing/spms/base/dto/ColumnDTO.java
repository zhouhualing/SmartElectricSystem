package com.harmazing.spms.base.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.common.dto.IDTO;

@XmlType
public class ColumnDTO implements IDTO{
	
	/**
	 * allowSearch
	 */
	private String allowSearch = "false";
	
	/**
	 * format date
	 */
	private String dateFormat = "yyyy-MM-dd";
	
	/**
	 * dict table name
	 */
	private String dict;
	
	/**
	 * display
	 */
	private String display = "true";
	
	/**
	 * column show name
	 */
	private String header = "";

	/**
	 * the filed name of baseentity
	 */
	private String key;
	
	/**
	 * operator
	 */
	private SearchFilter.Operator operator = SearchFilter.Operator.EQ;
	
	/**
	 * column type
	 */
	private String type = "java.lang.String";
	
	/**
	 * width
	 */
	private String width = null;
	
	/**
	 * filter
	 */
	private String andFilter = null; 
	
	private String orFilter = null;
	
	/**
	 * align
	 */
	private String align = "center";
	
	/**
	 * maxLength
	 */
	private Long maxLength = 10L;
	
	private String handleFormat;
	
	/**
	 * js row click
	 */
	private String rowClick="true";
	
	@XmlAttribute
	public String getAllowSearch() {
		return allowSearch;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	@XmlAttribute
	public String getHandleFormat() {
		return handleFormat;
	}

	public void setHandleFormat(String handleFormat) {
		this.handleFormat = handleFormat;
	}

	@XmlAttribute
	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	@XmlAttribute
	public String getDict() {
		return dict;
	}

	public String getDisplay() {
		return display;
	}

	@XmlAttribute
	public String getHeader() {
		return header;
	}
	
	@XmlAttribute
	public String getOrFilter() {
		return orFilter;
	}

	public void setOrFilter(String orFilter) {
		this.orFilter = orFilter;
	}

	@XmlAttribute
	public String getKey() {
		return key;
	}

	@XmlAttribute
	public SearchFilter.Operator getOperator() {
		return operator;
	}
	
	@XmlAttribute
	public String getType() {
		return type;
	}

	public String getWidth() {
		return width;
	}

	public void setAllowSearch(String allowSearch) {
		this.allowSearch = allowSearch;
	}
	
	public String getAlign() {
		return align;
	}

	@XmlAttribute
	public void setAlign(String align) {
		this.align = align;
	}

	@XmlAttribute
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	@XmlAttribute
	public void setDisplay(String display) {
		this.display = display;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getAndFilter() {
		return andFilter;
	}

	@XmlAttribute
	public void setAndFilter(String andFilter) {
		this.andFilter = andFilter;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setOperator(SearchFilter.Operator operator) {
		this.operator = operator;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public void setWidth(String width) {
		this.width = width;
	}

	public String getRowClick() {
	    return rowClick;
	}

	@XmlAttribute
	public void setRowClick(String rowClick) {
	    this.rowClick = rowClick;
	}
	
	
	
}
