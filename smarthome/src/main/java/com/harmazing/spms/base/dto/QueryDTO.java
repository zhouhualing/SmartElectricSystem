package com.harmazing.spms.base.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.harmazing.spms.common.dto.IDTO;

@XmlType
public class QueryDTO implements IDTO {
	
	/**
	 * the queryId of xml file.
	 */
	private String queryId;
	
	/**
	 * baseclass
	 */
	private String baseClass;
	
	/**
	 * the columns of query
	 */
	private List<ColumnDTO> column;
	
	/**
	 * the filter
	 */
	private String filter;
	
	/**
	 * the custom query's manager
	 */
	private String queryManager;
	
	/**
	 * the custom query's method
	 */
	private String queryMethod;
	
	/**
	 * only one field
	 * format[fileld#desc/asc]
	 */
	private String orderBy;
	
	/**
	 * cutomer decoration data
	 */
	private String customerDecoration;
	
	private Boolean useEntity = false;

	@XmlAttribute
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@XmlElement
	public List<ColumnDTO> getColumn() {
		return column;
	}

	public void setColumn(List<ColumnDTO> column) {
		this.column = column;
	}

	@XmlAttribute
	public String getBaseClass() {
		return baseClass;
	}

	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}

	@XmlAttribute
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	@XmlAttribute
	public String getQueryManager() {
		return queryManager;
	}

	@XmlAttribute
	public Boolean getUseEntity() {
	    return useEntity;
	}

	public void setUseEntity(Boolean useEntity) {
	    this.useEntity = useEntity;
	}

	@XmlAttribute
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@XmlAttribute
	public String getQueryMethod() {
		return queryMethod;
	}

	public void setQueryManager(String queryManager) {
		this.queryManager = queryManager;
	}

	public void setQueryMethod(String queryMethod) {
		this.queryMethod = queryMethod;
	}

	@XmlAttribute
	public String getCustomerDecoration() {
		return customerDecoration;
	}

	public void setCustomerDecoration(String customerDecoration) {
		this.customerDecoration = customerDecoration;
	}
	
}
