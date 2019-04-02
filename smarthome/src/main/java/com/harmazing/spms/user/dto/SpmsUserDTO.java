package com.harmazing.spms.user.dto;

import java.util.List;
import com.harmazing.spms.base.dto.WorkFlowCommonDTO;

/**
 * 用户管理Entity
 *
 * @author 吴列鹏
 * @version 1.0
 */
public class SpmsUserDTO extends WorkFlowCommonDTO {
    /* 帐户ID */
    private String user;
    /* 用户类型 */
    private Integer type;
    /* 用户类型text */
    private String typeText;
    /* 姓名 */
    private String fullname;
    /* 住址 */
    private String address;
    /* 电话 */
    private String mobile;
    /* 邮箱 */
    private String email;
    /* 绑定设备Id列表*/
    private List<String> spmsDevices;
    /* 身份证 */
    private String idNumber;
    /*状态*/
    private Integer status;
    private String userId;
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

    /*如果是0则说明是更新基本信息，如果是1说明是更新网关*/
    private Integer updateFlag;

    public SpmsUserDTO() {
        super();
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSpmsDevices() {
        return spmsDevices;
    }

    public void setSpmsDevices(List<String> spmsDevices) {
        this.spmsDevices = spmsDevices;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Integer updateFlag) {
        this.updateFlag = updateFlag;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}