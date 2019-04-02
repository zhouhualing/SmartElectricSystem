package com.harmazing.spms.area.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class AreaTreeDTO {
	/**
	 * parent
	 */
	private AreaTreeDTO parent;
	
	/**
	 * parentId
	 */
	private String parentId;
	/**
	 * maxload
	 */
	
	/**
	 * status
	 */
	private Integer status;
	private Long maxload;
	/**
	 * classification
	 */
	private String classification;
	/**
	 * ID
	 */
	private String id;
	/**
	 * is parent
	 */
	private Boolean isParent = false;
	
	/**
	 * checked
	 */
	private Boolean checked =false;
	
	/**
	 * singlecheck
	 */
	private Boolean singleCheck = false;
	
	/**
	 * name
	 */
	private String name;
	
	/**
	 * realname
	 */
	private String realName;
	
	/**
	 * code
	 */
	private String code;
	
	/**
	 * nodeType
	 */
	private String type;
	
	/*
	 * 
	 */
	private Boolean nocheck;
	
	/**
	 * open
	 */
	private Boolean open = false;
	
	/**
	 * sort
	 */
	private Integer sort;
	
	/**
	 * parentIds
	 */
	private String parentIds;

	/**
	 * children
	 */
	private List<AreaTreeDTO> children = Lists.newArrayList();
	
	/**
	 * treeType 0 表示一般tree  1表示带有设备数的tree
	 */
	private Integer treeType = 0;

	
	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public Boolean getChecked() {
		return checked;
	}

	public Integer getTreeType() {
	    return treeType;
	}

	public void setTreeType(Integer treeType) {
	    this.treeType = treeType;
	}

	public String getParentIds() {
	    return parentIds;
	}

	public void setParentIds(String parentIds) {
	    this.parentIds = parentIds;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getSingleCheck() {
		return singleCheck;
	}

	public void setSingleCheck(Boolean singleCheck) {
		this.singleCheck = singleCheck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<AreaTreeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<AreaTreeDTO> children) {
		this.children = children;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public AreaTreeDTO getParent() {
		return parent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParent(AreaTreeDTO parent) {
		this.parent = parent;
	}

	public Long getMaxload() {
		return maxload;
	}

	public void setMaxload(Long maxload) {
		this.maxload = maxload;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
