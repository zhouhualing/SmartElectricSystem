package com.harmazing.service;

import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-5.
 */
public interface ServiceService {

    /**
     * 获取对应用户的Service
     * @param userId
     * @return
     */
    public List<Map> getUserAirconService(String userId);

    /**
     * 根据用户Id获取服务Id
     * @param userId
     * @return
     */
    public List<String> getAirconServiceIdByUserId(String userId);

    /**
     * 根据ServiceId获取DeviceId
     * @param serviceId 服务Id
     * @return
     */
    public List<String> getDeviceIdByAirconServiceId(String userId, String serviceId);


    /**
     * 根据SeviceId 获取服务 Map
     * @param serviceId
     * @return
     */
    public Map getAirconServiceByServiceId(String userId, String serviceId);


    /**
     * 根据SeviceId 获取服务 Map
     * @param ids
     * @return
     */
    public List<Map> getAirconServiceByServiceIds(String userId, String[] ids);
}
