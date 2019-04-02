package com.harmazing.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-9.
 */
public interface ServiceMapper {
    public final static String TB_NAME_USER_SERVICE = "spms_user_rule";
    public final static String TB_NAME_USER_SERVICE_DEVICE = "spms_user_rule_binding";
    //public final static String TB_NAME_SERVICE = "spms_rule";

    /**
     * 根据用户Id获取服务Id列表
     * @param userId
     * @return
     */
    @Select("SELECT rule_id FROM " + TB_NAME_USER_SERVICE + " WHERE user_id = #{userId}")
    @ResultType(String.class)
    public List<String> selectAirconServiceIdByUserId (@Param("userId") String userId);

    /**
     * 根据服务Id获取设备id列表
     * @param serviceId
     * @return
     */
    @Select("SELECT device_id FROM " + TB_NAME_USER_SERVICE_DEVICE + " WHERE user_id = #{userId} AND rule_id = #{serviceId}")
    @ResultType(String.class)
    public List<String> selectDeviceIdByAirconServiceId(@Param("userId") String userId, @Param("serviceId") String serviceId);

    /**
     * 根据服务Id获取服务信息
     * @param id
     * @return
     */
    //@Select("SELECT id, service_id, rule_name, mode, cooler_start, cooler_end, heater_start, heater_end FROM " +
            //" " + TB_NAME_SERVICE + " " +
            //"WHERE del_flag=0 AND id=#{id}")
    @Select("SELECT sp.id, spt.area_id as service_id, spt.names as rule_name, " +
    		"spt.configurationInformation as mode, spt.zhiLengMix as cooler_start , " +
    		"spt.zhiLengMax as cooler_end,spt.zhiReMix as heater_start,spt.zhiReMax " +
    		"as heater_end FROM  spms_product sp, spms_product_type spt  " +
    		"WHERE sp.type_id = spt.id AND sp.id=#{id}")
    @ResultMap("ServiceResultMap")
    public Map selectAirconServiceById(@Param("id") String id);

    /**
     * 根据Ids获取Service
     * @param ids
     * @return
     */
    /*@Select("<script>" +
            "SELECT id, service_id, rule_name, mode, cooler_start, cooler_end, heater_start, heater_end FROM " +
            " " + TB_NAME_SERVICE + " " +
            "WHERE del_flag=0 " +
            " AND " +
            "id IN" +
            "<foreach item='item' index='index' collection='ids' " +
            "open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")*/
    @Select("<script>" +
    		"SELECT sp.id, spt.area_id as service_id, spt.names as rule_name, " +
    		"spt.configurationInformation as mode, spt.zhiLengMix as cooler_start , " +
    		"spt.zhiLengMax as cooler_end,spt.zhiReMix as heater_start,spt.zhiReMax " +
    		"as heater_end FROM  spms_product sp, spms_product_type spt  " +
    		"WHERE sp.type_id = spt.id  AND sp.id IN" +
            "<foreach item='item' index='index' collection='ids' " +
            "open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    @ResultMap("ServiceResultMap")
    public List<Map> selectAirconServiceByIds(@Param("ids") String[] ids);
}
