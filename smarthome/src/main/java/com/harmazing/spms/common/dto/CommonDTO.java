package com.harmazing.spms.common.dto;

import java.util.Map;

/**
 * common DTO.
 * @author Zhaocaipeng
 * since 2013-9-15
 */
public abstract class CommonDTO implements IDTO {
	
	/**
	 * id
	 */
	private String id;

	/**
	 * 创建人姓名
	 */
	private String createUserName;

	/**
	 * 创建人手机
	 */
	private String createUserMobile;

    private Map<String,Object> message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserMobile() {
		return createUserMobile;
	}

	public void setCreateUserMobile(String createUserMobile) {
		this.createUserMobile = createUserMobile;
	}

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
