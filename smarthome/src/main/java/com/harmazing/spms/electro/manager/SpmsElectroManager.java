package com.harmazing.spms.electro.manager;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.area.dto.AreaTreeDTO;
import com.harmazing.spms.common.manager.IManager;

public interface SpmsElectroManager extends IManager{
	public List<AreaTreeDTO> findAllAreaLoadStatus();
	
	public List<Map<String,Object>> findAllAreaCurrentElectro();
	
	public List<AreaTreeDTO> findAllArea();
	
	public List<AreaTreeDTO> getChildNodes(String parentId,List<AreaTreeDTO> nodes);
	
	public List<AreaTreeDTO> findAllAreaLoadStatus(List<AreaTreeDTO> areas);
	
	public List<List<Long>> getAllPowerByArea(String areaId) throws ParseException;
	
	public List<List<Long>>getAllReactivePowerByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllPowerFactorByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllApparentPowerByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllEpByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllReactiveEnergyByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllReactiveDemandByArea(String areaId) throws ParseException;
	
	public List<List<Long>> getAllActiveDemandByArea(String areaId) throws ParseException;
	
	public List<Map<String,Integer>> statisOnlineStatusByArea(String areaId);
	
	public List<Map<String,Integer>> statisEleLevelByArea(String areaId);
	
	public Integer getAreaMaxLoad(String areaId);
	
	public List<List<Long>> latestPowerByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestReactivePowerByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestPowerFactorByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestApparentPowerByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestEpByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestReactiveEnergyByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestReactiveDemandByArea(String areaId,long endTime) throws ParseException;
	
	public List<List<Long>> latestActiveDemandByArea(String areaId,long endTime) throws ParseException;
	
	public void adjustElectro(String areaId,int type, int scale) throws Exception ;
	
	
	//取得调控策略的详细信息
	public Map<String,Object> getRegulationInfo(String areaId);
	
	public Map<String,Object> regulationElectroForArea(String areaId , Map<String,Object> info) throws Exception;
}
