package com.harmazing.spms.area.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.harmazing.spms.common.entity.CommonEntity;
/**
 * 
 * @author yyx
 *since 2015年1月4日
 */
@Entity
@Table(name="spms_area")
public class Area extends CommonEntity {
	/**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private Area parent; // 父级编号
	/**
	 * path parent1,parent2,parent3
	 */
	@Column(name = "parentIds")
	@Length(min = 1, max = 255)
	private String parentIds; // 所有父级编号
	@Length(min = 0, max = 100) 
	private String code; // 区域编码
	@Length(min = 1, max = 100)
	private String name; // 区域名称
	/**
	 * 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	 * dict_area_type
	 */
	@Length(min = 1, max = 1)
	private String type; // 区域类型
	/**
	 * 区域分类（1：服务区域；2：电力区域）
	 * dict_area_classification
	 */
	@Length(min = 1, max = 1)
	private String classification;// 区域分类
	/**
	 * 控电策略
	 * dict_area_policy
	 */
	private Integer policy;//控电策略
	/**
	 * 列表排序
	 */
	private Integer sort;
	
	/**
	 * 备注信息
	 */
	@Length(min = 0, max = 200)
	private String remarks;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Area> childList = Lists.newArrayList(); // 拥有子区域列表
	public Area getParent() {
		return parent;
	}
	public void setParent(Area parent) {
		this.parent = parent;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public Integer getPolicy() {
		return policy;
	}
	public void setPolicy(Integer policy) {
		this.policy = policy;
	}
	public List<Area> getChildList() {
		return childList;
	}
	public void setChildList(List<Area> childList) {
		this.childList = childList;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null ) {
			return false;
		}
		if(obj instanceof  Area) {
			if(this.getId().equals(((Area)obj).getId())) {
				return true;
			}
		} else {
			return false;
		}
		return super.equals(obj);
	}
}
