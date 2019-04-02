package com.harmazing.spms.permissions.dto;

import java.util.List;

import com.harmazing.spms.common.dto.CommonDTO;

public class PermissionsDTO extends CommonDTO {
	/* 菜单名称 */
	private String name;
	/* 菜单编码 */
	private String code;
	/* 菜单类型，2=顶层，3=子菜单 */
	private int type;
	/* 父菜单ID */
	private String parentId;
	/* 排序 */
	private int sort;
	/* 是否有权限，1=有，0=没有 */
	private int isRight;
	/* 子菜单列表 */
	private List<PermissionsDTO> children;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getIsRight() {
		return isRight;
	}

	public void setIsRight(int isRight) {
		this.isRight = isRight;
	}

	public List<PermissionsDTO> getChildren() {
		return children;
	}

	public void setChildren(List<PermissionsDTO> children) {
		this.children = children;
	}

}