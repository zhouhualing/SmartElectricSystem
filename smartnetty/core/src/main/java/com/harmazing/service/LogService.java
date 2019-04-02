package com.harmazing.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by ming on 14-9-10.
 */
public interface LogService {
	
	/**
	 * 批量添加空调日志
	 * @param logs
	 */
	public void appendAirconLogs(List<Map<String,String>> logs);


    /**
     * 添加空调日志
     * @param timestamp
     * @param airconId
     * @param currentTemperature
     * @param currentPower
     */
    //public void appendAirconLog(Timestamp timestamp, String airconId, Integer currentTemperature, Integer currentPower, Long accumulatePower);

    /**
     * 添加空调日志
     * @param timestamp
     * @param airconId
     * @param currentTemperature
     * @param currentPower
     * @param mode
     * @param speed
     * @param direction
     */
   // public void appendAirconLog(Timestamp timestamp, String airconId, Integer currentTemperature, Integer targetTemperature,
   //                             Integer currentPower, Long accumulatePower, Integer mode, Integer speed, Integer direction);

    /**
     * 批量添加门窗日志
     * @param logs
     */
    public void appendWinDoorLogs(List<Map<String,String>> logs);
    
    /**
     * 添加门窗日志
     * @param timestamp
     * @param on
     */
    public void appendWinDoorLog(Timestamp timestamp, String deviceId, boolean on);
    
    /**
     * 批量插入网关日志
     * @param logs
     */
    public void appendGatewayLogs(List<Map<String,String>> logs);

    /**
     * 插入网关日志
     * @param timestamp
     * @param deviceId
     * @param on
     */
    public void appendGatewayStatus(Timestamp timestamp, String deviceId, String userId, Integer on);

}
