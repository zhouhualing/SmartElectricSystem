/**
 * 
 */
package com.harmazing.spms.base.dto;

import java.util.List;

import com.google.common.collect.Lists;
import com.harmazing.spms.common.dto.CommonDTO;

public class TreeDTO extends CommonDTO {
	
        /**
         * pId
         */
    	private String pId;
    
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
	 * children
	 */
	private List<TreeDTO> children = Lists.newArrayList();

	public Boolean getIsParent() {
		return isParent;
	}
	
	public List<TreeDTO> getChildren() {
	    return children;
	}

	public Integer getSort() {
	    return sort;
	}

	public void setSort(Integer sort) {
	    this.sort = sort;
	}

	public void setChildren(List<TreeDTO> children) {
	    this.children = children;
	}

	public Boolean getOpen() {
	    return open;
	}

	public void setOpen(Boolean open) {
	    this.open = open;
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
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

	public Boolean getSingleCheck() {
		return singleCheck;
	}

	public void setSingleCheck(Boolean singleCheck) {
		this.singleCheck = singleCheck;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	/* (non-Javadoc)
	* @see java.lang.Object#equals(java.lang.Object)
	*/
	@Override
	public boolean equals(Object obj) {
	    if((obj == null) || (obj.getClass() != TreeDTO.class)) {
		return false;
	    } else {
		if(this.getId() == ((TreeDTO)obj).getId()) {
		    return true;
		}
	    }
	    return false;
	}

}
