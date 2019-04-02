package com.harmazing.spms.base.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.harmazing.spms.common.dto.IDTO;

@XmlRootElement(name="querys")
public class QuerysDTO implements IDTO {
	
	/**
	 * query
	 */
	private List <QueryDTO> query;

	@XmlElement
	public List<QueryDTO> getQuery() {
		return query;
	}

	public void setQuery(List<QueryDTO> query) {
		this.query = query;
	}

	


}
