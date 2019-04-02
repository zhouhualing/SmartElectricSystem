package com.harmazing.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.harmazing.entity.Device;

/**
 * Created by ming on 14-9-15.
 */
public interface UserService {

	public String getTbUserUserIdByUserCode(String user_code);
    /**
     * 根据用电区域Id获取用户Id列表
     * @param id
     * @return
     */
    public List<String> getUserIdByEleAreaId(String id);

    /**
     * 根据服务区域获取用户Id列表
     * @param id
     * @return
     */
    public List<String> getUserIdByBizAreaId(String id);

    String getUserIdByGatewayId(String gatewayId);
    
    public String findEleAreaById(String userId);
    
    /**
     * 获取运营状态的网关信息
     */
    public Device getUserGW(String mac,String sn);
    
    /**
     * 
     * @param userCode
     * @return
     */
    public String findSpmsUserIdByUserCode(String userCode);
    
    /**
     * 
     * @param device_id
     * @param device_type
     * @param is_primary
     * @param user_id
     * @param version
     */
    public void insertUserAndDeviceBinding(String device_id,  int device_type, int is_primary, String user_id, int version );
}
