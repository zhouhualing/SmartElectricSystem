package com.harmazing.spms.electro.manager;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;

public interface SpmsResultDataManager extends IManager{
	/**
	 * 根据区域取出其历史的有功功率 
	 * @param areaId 区域id
	 * @return
	 */
	public List<List<Object>> getAllPowerByArea(String areaId ,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取出其历史的无功功率
	 * @param areaId
	 * @return
	 */
	public List<List<Object>>getAllReactivePowerByArea(String areaId,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取出其历史的功率因数
	 * @param areaId
	 * @return
	 */
	public List<List<Object>> getAllPowerFactorByArea(String areaId,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取出其历史的视在功率
	 * @param areaId
	 * @return
	 */
	public List<List<Object>> getAllApparentPowerByArea(String areaId,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取其历史的有功电能
	 * @param areaId
	 * @param rbtn 统计时间类型
	 * @return
	 */
	public List<List<Object>>  getAllEpByArea(String areaId,boolean isUpdate, String rbtn) throws ParseException;
	/**
	 * 根据区域取其历史的无功电能
	 * @param areaId
	 * @param rbtn 统计时间类型
	 * @return
	 */
	public List<List<Object>>  getAllReactiveEnergyByArea(String areaId,boolean isUpdate, String rbtn) throws ParseException;
	/**
	 * 根据区域取其历史的无功需求
	 * @param areaId
	 * @return
	 */
	public List<List<Object>> getAllReactiveDemandByArea(String areaId,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取其历史的有功需求
	 * @param areaId
	 * @return
	 */
	public List<List<Object>> getAllActiveDemandByArea(String areaId,boolean isUpdate) throws ParseException;
	/**
	 * 根据区域取不同时刻的在线设备数
	 * @param areaId
	 * @return
	 */
	public List<List<Object>> getAllDeviceNumByArea(String areaId,boolean isUpdate) throws ParseException;

	/**
	 * 根据区域取不同时刻的在线设备数
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getAllDataByArea(String areaId,boolean isUpdate, String rbtn) throws ParseException;
}
