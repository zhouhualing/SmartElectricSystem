package com.harmazing.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-10.
 */
public interface LogMapper extends ServiceMapper {

    public final static String AC_STATUS_TB_NAME = "spms_ac_status";
    public final static String WIN_DOOR_RUN_LOG_TB_NAME = "spms_win_door_status";
    public final static String GATEWAY_STATUS_TB_NAME = "spms_gw_status";

    @Insert("INSERT INTO " +
            " " + AC_STATUS_TB_NAME + " " +
            "(`id`, `device_id`, `on_off`, `temp`,`humidity`, `power`, `accumulatePower`, `start_time`,`reactivePower`," +
            "`reactiveEnergy`,`apparentPower`,`voltage`,`current`,`frequency`,`powerFactor`,`demandTime`," +
            "`period`,`activeDemand`,`reactiveDemand`) VALUES " +
            "(#{id}, #{deviceId}, #{onOff}, #{currentTemperature}, #{humidity}, #{power}, #{accumulatePower}, #{currentTime},#{reactivePower}," +
            "#{reactiveEnergy},#{apparentPower},#{voltage},#{current},#{frequency},#{powerFactor},#{startTime}," +
            "#{period},#{activeDemand},#{reactiveDemand})")
    public void insertAirconShortLog(
            @Param("id") String id,
            @Param("deviceId") String deviceId,
            @Param("onOff") Integer onOff,
            @Param("currentTemperature") Integer currentTemperature,
            @Param("power") Integer power,
            @Param("accumulatePower") Long accumulatePower,
            @Param("humidity") Long humidity,
            @Param("currentTime") Timestamp currentTime,
            @Param("reactivePower") Integer reactivePower,
            @Param("reactiveEnergy") Integer reactiveEnergy,
            @Param("apparentPower") Integer apparentPower,
            @Param("voltage") Integer voltage,
            @Param("current") Integer current,
            @Param("frequency") Integer frequency,
            @Param("powerFactor") Integer powerFactor,
            @Param("startTime") Timestamp startTime,
            @Param("period") Integer period,
            @Param("activeDemand") Integer activeDemand,
            @Param("reactiveDemand") Integer reactiveDemand
    );

    @Insert("INSERT INTO " +
            " " + AC_STATUS_TB_NAME + " " +
            "(`id`, `device_id`, `on_off`, `temp`, `ac_temp`, `humidity`, `power`, `accumulatePower`, `model`, `speed`, `direction`, `start_time`) VALUES " +
            "(#{id}, #{deviceId}, #{onOff}, #{currentTemperature}, #{targetTemperature}, #{power}, #{accumulatePower}, #{mode}, #{speed}, #{direction}, #{currentTime})")
    public void insertAirconLongLog(
            @Param("id") String id,
            @Param("deviceId") String deviceId,
            @Param("onOff") Integer onOff,
            @Param("currentTemperature") Integer currentTemperature,
            @Param("targetTemperature") Integer targetTemperature,
            @Param("humidity") Integer humidity,
            @Param("power") Integer power,
            @Param("accumulatePower") Long accumulatePower,
            @Param("mode") Integer mode,
            @Param("speed") Integer speed,
            @Param("direction") Integer direction,
            @Param("currentTime") Timestamp currentTime
    );


    @Insert("INSERT INTO " +
            " " + WIN_DOOR_RUN_LOG_TB_NAME + " " +
            "(`id`, `device_id`, `operate_type`, `operate_time`) VALUES " +
            "(#{id}, #{deviceId}, #{on}, #{currentTime})")
    public void insertWinDoorLog(
            @Param("id") String id,
            @Param("deviceId") String deviceId,
            @Param("on") Integer on,
            @Param("currentTime") Timestamp currentTime
    );

    @Insert("INSERT INTO " +
            " " + GATEWAY_STATUS_TB_NAME + " " +
            "(`id`, `device_id`, `status`, `user_id`, `create_time`) VALUES " +
            "(#{id}, #{deviceId}, #{status}, #{userId}, #{timestamp})")
    public void insertGatewayLog(
            @Param("id") String id,
            @Param("deviceId") String deviceId,
            @Param("status") Integer status,
            @Param("userId") String userId,
            @Param("timestamp") Timestamp timestamp
    );
    /**
     * 批量插入gwlog
     */
    
    public void batchInsertGWLog(List<Map<String, Object>> list);
    /**
     * 批量插入aclog
     */
    @InsertProvider(type=LogSqlProvider.class,method="batchInsertACLog")
    public void batchInsertACLog(List<Map<String, Object>> list);
    /**
     * 批量插入门窗日志
     */
    public void batchInsertWDLog(List<Map<String, Object>> list);
}
