package com.harmazing.spms.area.dto;

import java.util.List;

import com.google.common.collect.Lists;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.dto.CommonDTO;

public class AreaDTO extends CommonDTO {
	private static final long serialVersionUID = 1L;
	private AreaDTO parent; // 父级编号
	/**
	 * path parent1,parent2,parent3
	 */
	private String parentIds; // 所有父级编号
	private String code; // 区域编码
	private String name; // 区域名称
	private Integer sort; // 排序
	/**
	 * 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	 * dict_area_type
	 */
	private String type; // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	/**
	 * 区域分类（1：服务区域；2：电力区域）
	 * dict_area_classification
	 */
	private String classification;// 区域分类（1：服务区域；2：电力区域）
	private Integer policy;//控电策略A-E
	private List<Area> childList = Lists.newArrayList(); // 拥有子区域列表
	private String remarks;
	public AreaDTO getParent() {
		return parent;
	}
	public void setParent(AreaDTO parent) {
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
	
	
}