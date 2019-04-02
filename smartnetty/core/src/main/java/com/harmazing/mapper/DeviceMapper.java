package com.harmazing.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;

/**
 * Created by ming on 14-9-2.
 */
public interface DeviceMapper {

    final static String TB_NAME = "spms_device";
        
    
    
    
    /*@Update("UPDATE " + TB_NAME + " set " +
	"operStatus = #{operationStatus}, " +
	"sessionId = #{session}," +
	"onOff = #{onOff}, " +
	"temp = #{temperature}, " +
	"acTemp = #{acTemperature}, " +
	"power = #{power}, " +
	"speed = #{speed}, " +
	"direction = #{direction}, " +
	"startTime = #{startTime}, " +
    "mode = #{mode}, " +
    "remain = #{remain}, " +
    "accumulatePower = #{accumulatePower}, " +
    "server = #{server} " +
    "WHERE id = #{id}")*/
    /**
     * 根据Mac地址和序列号获取GateWay信息
     * @param mac
     * @param sn
     * @return
     */
    @Select("SELECT * FROM " + TB_NAME +" WHERE mac = #{mac} AND sn = #{sn} AND type= #{deviceType}")
    @ResultMap("DeviceResultMap")
    public Device getDeviceByMacAndSN(@Param("mac") String mac, @Param("sn") String sn, @Param("deviceType") String deviceType);
    
    
    /**
     * 根据MAC地址获取GateWay信息
     * @param mac
     * @param sn
     * @return
     */    
    //@Select("SELECT DISTINCT surb.user_id as userid,sd.id,sd.mac,sd.type_code as type FROM spms_user_rule_binding surb,spms_device sd WHERE surb.gw_id = sd.id AND sd.mac=#{mac}")
    @SelectProvider(type=DeviceSqlProvider.class , method = "getGatewayByMacAndSN")
    public Map getGatewayByMacAndSN(@Param("mac")String mac,@Param("sn") String sn);
    
    /**
     * 获取网关绑定的设备信息
     * @param mac
     * @param sn
     * @return
     */
    /*@Select("SELECT sd.id,sd.mac,sd.type_code as type,sr.cooler_start as coolerStart,sr.cooler_end as coolerEnd,sr.heater_start as heaterStart,sr.heater_end as heaterEnd,sr.sensor_enabled as sensorEnabled,sr.mode FROM spms_user_product_binding surb,spms_device sd,spms_rule sr WHERE surb.device_id = sd.id AND surb.rule_id = sr.id AND surb.user_id=#{userid}")*/
    //@Select("SELECT sd.id,sd.mac,sd.type as type FROM spms_user_product_binding supb,spms_device sd WHERE supb.device_id = sd.id  AND supb.user_id=#{userid}")
    @Select("SELECT sd.id,sd.mac,sd.type as type,spt.zhiLengMix as coolerStart," +
    		"spt.zhiLengMax as coolerEnd,spt.zhiReMix as heaterStart,spt.zhiReMax " +
    		"as heaterEnd,spt.sensorEnabled as sensorEnabled,spt.configurationInformation " +
    		"as mode FROM spms_user_product_binding supb,spms_device sd,spms_product_type spt " +
    		"WHERE supb.device_id = sd.id  AND supb.producttype_id = spt.id " +
    		"AND supb.user_id=#{userid}")
    public List<Map> getGatewayBindDevices(@Param("userid")String userid);

    /**
     * 获取所有绑定了的GateWay
     * @return
     */
    @Select("SELECT"+
            " 	t1.gwId,"+
            " 	t4.mac,"+
            " 	t4.sn,"+
            " 	t1.user_id as userId,"+
            " 	t4.`status`,"+
            " 	t4.disabel AS disable,"+
            " 	t2.id deviceId,"+
            " 	t2.mac deviceMac,"+
            " 	t2.`type` as deviceType,"+
            " 	t3.zhiLengMix AS coolerStart,"+
            " 	t3.zhiLengMax AS coolerEnd,"+
            " 	t3.zhiReMix AS heaterStart,"+
            " 	t3.zhiReMax AS heaterEnd,"+
            " 	t3.sensorEnabled AS sensorEnabled,"+
            " 	t3.configurationInformation AS mode,"+
            "   t5.biz_area_id AS bizArea,"+
            "   t5.ele_area_id AS eleArea"+
            " FROM"+
            " 	spms_user_product_binding t1"+
            " JOIN spms_device t2 ON t1.device_id = t2.id"+
            " JOIN spms_product_type t3 ON t1.producttype_id = t3.id"+
            " JOIN spms_device t4 ON t1.gwId = t4.id"+
            " JOIN spms_user t5 on t1.user_id=t5.id"+
            " ORDER BY"+
            " 	t1.gwId")
    @ResultMap("AllGWResultMap")
    public List<Map> getAllBindingGateWay();
    
    /**
     * 获取某个用户的网关和设备信息
     * @return
     */
    @Select("SELECT"+
            " 	t1.gwId,"+
            " 	t4.mac,"+
            " 	t4.sn,"+
            " 	t1.user_id as userId,"+
            " 	t4.`status`,"+
            " 	t4.disabel AS disable,"+
            " 	t2.id deviceId,"+
            " 	t2.mac deviceMac,"+
            " 	t2.`type` as deviceType,"+
            " 	t3.zhiLengMix AS coolerStart,"+
            " 	t3.zhiLengMax AS coolerEnd,"+
            " 	t3.zhiReMix AS heaterStart,"+
            " 	t3.zhiReMax AS heaterEnd,"+
            " 	t3.sensorEnabled AS sensorEnabled,"+
            " 	t3.configurationInformation AS mode,"+
            "   t5.biz_area_id AS bizArea,"+
            "   t5.ele_area_id AS eleArea"+
            " FROM"+
            " spms_user_product_binding t1"+
            " JOIN spms_device t2 ON t1.device_id = t2.id"+
            " JOIN spms_product_type t3 ON t1.producttype_id = t3.id"+
            " JOIN spms_device t4 ON t1.gwId = t4.id"+
            " JOIN spms_user t5 on t1.user_id=t5.id"+
            " where t1.user_id=#{uid}"
            )
    @ResultMap("AllGWResultMap")
    public List<Map> getBindingGateWayByUser(@Param("uid") String uid);
    
    /**
     * 根据mac和sn获取与用户绑定的网关信息
     * 因为客户那只知道mac地址,改为按网关获取
     */
    
    @Select("SELECT"+
            " 	t1.gwId,"+
            " 	t4.mac,"+
            " 	t4.sn,"+
            " 	t1.user_id as userId,"+
            " 	t4.`status`,"+
            " 	t4.disabel AS disable,"+
            " 	t2.id deviceId,"+
            " 	t2.mac deviceMac,"+
            " 	t2.`type` as deviceType,"+
            " 	t3.zhiLengMix AS coolerStart,"+
            " 	t3.zhiLengMax AS coolerEnd,"+
            " 	t3.zhiReMix AS heaterStart,"+
            " 	t3.zhiReMax AS heaterEnd,"+
            " 	t3.sensorEnabled AS sensorEnabled,"+
            " 	t3.configurationInformation AS mode,"+
            "   t5.biz_area_id AS bizArea,"+
            "   t5.ele_area_id AS eleArea"+
            " FROM"+
            " spms_user_product_binding t1"+
            " JOIN spms_device t2 ON t1.device_id = t2.id"+
            " JOIN spms_product_type t3 ON t1.producttype_id = t3.id"+
            " JOIN spms_user t5 on t1.user_id=t5.id"+
            " ,spms_device t4 "+
            " where t1.gwId = t4.id and t4.mac=#{mac} "
//            + "and t4.sn=#{sn}"
            )
    @ResultMap("AllGWResultMap")
    public List<Map> getBindingGateWayByMacAndSn(@Param("mac")String mac,@Param("sn")String sn);

    /**
     * 根据id获取GateWay信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM " + TB_NAME +" WHERE id = #{id}")
    @ResultMap("DeviceResultMap")
    public Device getDeviceById(@Param("id") String id);


    @Update("UPDATE " + TB_NAME + " set " +
    		"vender = #{vender}, " +
            "model = #{model}, " +
            "hardware = #{hardware}, " +
            "software = #{software}, " +
            "cursoft = #{currentSoftware}, " +
            "operStatus = #{operationStatus}, " +
            "sessionId = #{session}, " +
            "onOff = #{onOff}, " +
            "temp = #{temperature}, " +
            "acTemp = #{acTemperature}, " +
            "power = #{power}, " +
            "speed = #{speed}, " +
            "direction = #{direction}, " +
            "startTime = #{startTime}, " +
            "mode = #{mode}, " +
            "remain = #{remain}, " +
            "server = #{server}, " +
            "accumulatePower = #{accumulatePower} " +
            "WHERE id = #{id}")
    public void updateDevice(
            @Param("vender") String vender,
            @Param("model") String model,
            @Param("hardware")  String hardware,
            @Param("software") String software,
            @Param("currentSoftware") String currentSoftware,
            @Param("operationStatus") Integer operationStatus,
            @Param("session") String session,
            @Param("onOff") Integer onOff,
            @Param("temperature") Integer temperature,
            @Param("acTemperature") Integer acTemperature,
            @Param("power") Integer power,
            @Param("speed") Integer speed,
            @Param("direction") Integer direction,
            @Param("startTime") Timestamp startTime,
            @Param("mode") Integer mode,
            @Param("remain") Integer remain,
            @Param("server") String server,
            @Param("accumulatePower") Long accumulatePower,
            @Param("id") String id
    );
    
    /*@Update("UPDATE " + TB_NAME + " set " +
    		"operStatus = #{operationStatus}, " +
    		"sessionId = #{session}," +
    		"onOff = #{onOff}, " +
    		"temp = #{temperature}, " +
    		"acTemp = #{acTemperature}, " +
    		"power = #{power}, " +
    		"speed = #{speed}, " +
    		"direction = #{direction}, " +
    		"startTime = #{startTime}, " +
            "mode = #{mode}, " +
            "remain = #{remain}, " +
            "accumulatePower = #{accumulatePower}, " +
            "server = #{server} " +
            "WHERE id = #{id}")*/
    @SelectProvider(type = DeviceSqlProvider.class, method = "updateByDevice")
    public void updateByDevice(
    		@Param("operationStatus") Integer operationStatus,
    		@Param("session") String session,
    		@Param("onOff") Integer onOff,
    		@Param("temperature") Integer temperature,
    		@Param("acTemperature") Integer acTemperature,
    		@Param("power") Integer power,
    		@Param("speed") Integer speed,
    		@Param("direction") Integer direction,
    		@Param("startTime") Timestamp startTime,
            @Param("mode") String mode,
            @Param("remain") Integer remain,
            @Param("accumulatePower") Long accumulatePower,
            @Param("server") String server,
            @Param("id") String id
    );
    /**
     * 批量更新设备信息
     * @param list
     */
    /*@UpdateProvider(type = DeviceSqlProvider.class, method = "batchUpdateDevices")
    public void batchUpdateDevices(List<Map<String, Object>> list);*/
    
    @UpdateProvider(type = DeviceSqlProvider.class, method = "batchUpdateAc")
    public void batchUpdateAc(List<Map<String, Object>> list);
    /*@UpdateProvider(type = DeviceSqlProvider.class, method = "batchUpdateDevices")
    public void batchUpdateDevices(List<Map<String, Object>> list);*/
    
    @Insert("INSERT INTO " + TB_NAME +
            " (id, vender, model, hardware, software, cursoft, status, operStatus, sessionId,mac,sn,storage,type) " +
            "VALUES (#{id}, #{vender}, #{model}, #{hardware}, #{software}, #{currentSoftware}, #{status}, #{operationStatus}, #{session},#{mac},#{sn},#{storage},#{type})")
    public void insertDevice(
            @Param("id") String id,
            @Param("vender") String vender,
            @Param("model") String model,
            @Param("hardware") String hardware,
            @Param("software") String software,
            @Param("currentSoftware") String currentSoftware,
            @Param("status") int status,
            @Param("operationStatus") int operationStatus,
            @Param("session") String session,
            @Param("mac") String mac,
            @Param("storage") String storage,
            @Param("sn") String sn ,
            @Param("type") String type
    );
    
   

    @Select("SELECT * FROM " + TB_NAME +
            "WHERE parent_id = #{parentId}")
    @ResultMap("DeviceResultMap")
    public List<Device> getDeviceByParentId(@Param("parentId") String parentId);

    @Select("Select * FROM " + TB_NAME +
            "WHERE parent_id = #{parentId} AND type_code = #{deviceType}")
    @ResultMap("DeviceResultMap")
    public List<Device> getDeviceByParentIdAndDeviceType(@Param("parentId") String parentId, @Param("deviceType") String deviceType);

    @Select("select d.* from spms_device d,spms_user u where d.id=u.gw_id and u.id= #{userId} ")
    @ResultMap("DeviceResultMap")
    public List<Device> getDeviceByUserId(@Param("userId") String userId);

    @Update("UPDATE " + TB_NAME + " SET sessionId=NULL, operStatus=0, server=NULL WHERE server=#{server}")
    public void clearDeviceSessionByServer(@Param("server") String server);
    
    @Select("select su.ele_area_id from spms_user_product_binding supb,spms_user su where su.gw_id=supb.gwId and supb.device_id =#{deviceId}")
    @ResultType(String.class)
    public String findEleAreaByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 根据mac获取设备信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM " + TB_NAME +" WHERE mac= #{mac}")
    @ResultMap("DeviceResultMap")
    public Device getDeviceByMac(@Param("mac") String mac);
    
    /**
     * 获取所有session
     * 
     * @return
     */
    @Select("SELECT sessionId FROM " + TB_NAME +" WHERE server= #{server} and sessionId is not null")
    @ResultMap("DeviceResultMap")
    public List<String> getSessions(@Param("server") String server);
    /**
     * 根据区域获取所有session
     * 
     * @return
     */
    @Select("select sessionId from spms_device d,spms_user_product_binding b,spms_user u"
    		+ " where d.type=1 and d.operStatus=1 and d.server=#{server}"
    		+ "	and d.id=b.gwId and b.user_id=u.id and u.ele_area_id=#{areaId}")
   
    public List<String> getSessionsByArea(@Param("server") String server,@Param("areaId") String areaId);
    /**
     * 获取所有在线设备
     */
    @Select("select id from spms_device d where d.onOff!=1 and type=1 and status=2")
    public List<String> getGwState();
}