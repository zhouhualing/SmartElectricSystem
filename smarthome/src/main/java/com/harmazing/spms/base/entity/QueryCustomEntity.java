package com.harmazing.spms.base.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.GenericEntity;


@Entity
@Table(name="tb_query_queryCustom")
public class QueryCustomEntity extends GenericEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * queryId
	 * *query*xml中配置的queryId
	 */
	@Column(name="queryId")
	private String queryId;
	
	/**
	 * 显示的列，按顺序排列
	 */
	@Column(name="columnNames", length=4000)
	private String columnNames;
	
	/**
	 * 所属用户的自定义
	 */
	@ManyToOne(cascade=CascadeType.REFRESH, fetch= FetchType.LAZY)
	@JoinColumn(foreignKey=@ForeignKey(name="none"))
	private UserEntity userEntity;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQueryId() {
		return queryId;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
}
