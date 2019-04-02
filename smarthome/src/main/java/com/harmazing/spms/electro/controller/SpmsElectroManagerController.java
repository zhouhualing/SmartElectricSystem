package com.harmazing.spms.electro.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.area.dto.AreaTreeDTO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.area.manager.AreaManager;
import com.harmazing.spms.electro.manager.SpmsElectroManager;
import com.harmazing.spms.electro.manager.SpmsResultDataManager;

@Controller
@RequestMapping("/electro")
public class SpmsElectroManagerController {
	
	@Autowired
	private SpmsElectroManager spmsElectroManager;
	@Autowired
	private SpmsResultDataManager spmsResultDataManager;
	@Autowired
	private AreaManager areaManager;

	@RequestMapping("/areaTree")
	@ResponseBody
	public List<AreaTreeDTO> areaTree(){
		List<AreaTreeDTO> result = Lists.newArrayList();
		
		result = spmsElectroManager.findAllAreaLoadStatus();
				
		return result;
	}
	
	/*定时获取最新的区域列表*/
	@RequestMapping(value = "/updateAreaTree")
	@ResponseBody
	public Map<String,Object> updateAreaTree() {
		List<AreaTreeDTO> tree = spmsElectroManager.findAllAreaLoadStatus();
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data",tree );
		return result;
	}
	
	/*@RequestMapping(value = "/data")
	@ResponseBody
	public Map<String,Object> data(@RequestBody  Map<String,Object> info) throws ParseException{
		Map<String,Object> result = Maps.newHashMap();
		String areaId = info.get("areaId").toString();
		Area area = areaManager.findById(areaId);

		AreaDTO ad = new AreaDTO();
		//AreaDTO adP = new AreaDTO();
		//adP.setId(area.getParent().getId());
		//adP.setName(area.getParent().getName());
		ad.setId(area.getId());
		ad.setClassification(area.getClassification());
		ad.setCode(area.getCode());
		ad.setName(area.getName());
		//ad.setParent(adP);
		ad.setParentIds(area.getParentIds());
		ad.setType(area.getType());
		ad.setPolicy(area.getPolicy());
		List<List<Long>> power = spmsElectroManager.getAllPowerByArea(areaId);//有功功率
		List<List<Long>> reactivePower = spmsElectroManager.getAllReactivePowerByArea(areaId);//无功功率
		List<List<Long>> powerFactor = spmsElectroManager.getAllPowerFactorByArea(areaId);//功率因数
		List<List<Long>> apparentPower = spmsElectroManager.getAllApparentPowerByArea(areaId);//视在功率
		List<List<Long>> ep = spmsElectroManager.getAllEpByArea(areaId);//有功电能
		List<List<Long>> reactiveEnergy = spmsElectroManager.getAllReactiveEnergyByArea(areaId);//无功电能
		List<List<Long>> reactiveDemand = spmsElectroManager.getAllReactiveDemandByArea(areaId);//无功需求
		List<List<Long>> activeDemand = spmsElectroManager.getAllActiveDemandByArea(areaId);//有功需求
		List<Map<String,Integer>> statusData = spmsElectroManager.statisOnlineStatusByArea(areaId);
		List<Map<String,Integer>> eleLevelData = spmsElectroManager.statisEleLevelByArea(areaId);
		Integer maxLoad = spmsElectroManager.getAreaMaxLoad(areaId);
		
		result.put("area", ad);
		result.put("statusData", statusData);
		result.put("eleLevelData", eleLevelData);
		result.put("maxLoad", maxLoad);
		
		result.put("powers", power);
		result.put("reactivePowers", reactivePower);
		result.put("powerFactors", powerFactor);
		result.put("apparentPowers", apparentPower);
		result.put("eps", ep);
		result.put("reactiveEnergys", reactiveEnergy);
		result.put("activeDemands", activeDemand);
		result.put("reactiveDemands", reactiveDemand);
		return result;
	}*/
	
	
	@RequestMapping(value = "/data")
	@ResponseBody
	public Map<String,Object> data(@RequestBody  Map<String,Object> info) throws ParseException{
		Map<String,Object> result = Maps.newHashMap();
		if(null == info.get("areaId") || "".equals(info.get("areaId"))){
			return result;
		}
		String areaId = info.get("areaId").toString();
		String rbtn = info.get("rbtn").toString();
		
		Area area = areaManager.findById(areaId);
		if(area==null){
			return result;
		}
		AreaDTO ad = new AreaDTO();
		//AreaDTO adP = new AreaDTO();
		//adP.setId(area.getParent().getId());
		//adP.setName(area.getParent().getName());
		ad.setId(area.getId());
		ad.setClassification(area.getClassification());
		ad.setCode(area.getCode());
		ad.setName(area.getName());
		//ad.setParent(adP);
		ad.setParentIds(area.getParentIds());
		ad.setType(area.getType());
		ad.setPolicy(area.getPolicy());
//		List<List<Long>> power = spmsResultDataManager.getAllPowerByArea(areaId ,false);//有功功率
//		List<List<Long>> reactivePower = spmsResultDataManager.getAllReactivePowerByArea(areaId ,false);//无功功率
//		List<List<Long>> powerFactor = spmsResultDataManager.getAllPowerFactorByArea(areaId ,false);//功率因数
//		List<List<Long>> apparentPower = spmsResultDataManager.getAllApparentPowerByArea(areaId ,false);//视在功率
//		List<List<Long>>  ep = spmsResultDataManager.getAllEpByArea(areaId ,false, rbtn);//有功电能
//		List<List<Long>>  reactiveEnergy = spmsResultDataManager.getAllReactiveEnergyByArea(areaId ,false, rbtn);//无功电能
//		List<List<Long>> reactiveDemand = spmsResultDataManager.getAllReactiveDemandByArea(areaId ,false);//无功需求
//		List<List<Long>> activeDemand = spmsResultDataManager.getAllActiveDemandByArea(areaId ,false);//有功需求
//		List<List<Long>> deviceNum = spmsResultDataManager.getAllDeviceNumByArea(areaId ,false);//在线设备数
		List<Map<String,Integer>> statusData = spmsElectroManager.statisOnlineStatusByArea(areaId);
		List<Map<String,Integer>> eleLevelData = spmsElectroManager.statisEleLevelByArea(areaId);
		Integer maxLoad = spmsElectroManager.getAreaMaxLoad(areaId);
		
		result = spmsResultDataManager.getAllDataByArea(areaId, false, rbtn);
		
		result.put("area", ad);
		result.put("statusData", statusData);
		result.put("eleLevelData", eleLevelData);
		result.put("maxLoad", maxLoad);
		
//		result.put("powers", power);
//		result.put("reactivePowers", reactivePower);
//		result.put("powerFactors", powerFactor);
//		result.put("apparentPowers", apparentPower);
//		result.put("eps", ep);
//		result.put("reactiveEnergys", reactiveEnergy);
//		result.put("activeDemands", activeDemand);
//		result.put("reactiveDemands", reactiveDemand);
//		result.put("deviceNum", deviceNum);
		return result;
	}
	
	/*@RequestMapping(value = "/updateData")
	@ResponseBody
	public Map<String,Object> updateData(@RequestBody  Map<String,Object> info) throws ParseException{
		Map<String,Object> result = Maps.newHashMap();
		String areaId = info.get("areaId").toString();
		long endTime = Long.parseLong(info.get("endTime").toString());
		
		Area area = areaManager.findById(areaId);
		AreaDTO ad = new AreaDTO();
		//AreaDTO adP = new AreaDTO();
		//adP.setId(area.getParent().getId());
		//adP.setName(area.getParent().getName());
		ad.setId(area.getId());
		ad.setClassification(area.getClassification());
		ad.setCode(area.getCode());
		ad.setName(area.getName());
		//ad.setParent(adP);
		ad.setParentIds(area.getParentIds());
		ad.setType(area.getType());
		ad.setPolicy(area.getPolicy());
		
		Integer maxLoad = spmsElectroManager.getAreaMaxLoad(areaId);
		List<Map<String,Integer>> statusData = spmsElectroManager.statisOnlineStatusByArea(areaId);
		List<Map<String,Integer>> eleLevelData = spmsElectroManager.statisEleLevelByArea(areaId);
		
		List<List<Long>> power = spmsElectroManager.latestPowerByArea(areaId, endTime);//有功功率
		List<List<Long>> reactivePower = spmsElectroManager.latestReactivePowerByArea(areaId, endTime);//无功功率
		List<List<Long>> powerFactor = spmsElectroManager.latestPowerFactorByArea(areaId, endTime);//功率因数
		List<List<Long>> apparentPower = spmsElectroManager.latestApparentPowerByArea(areaId, endTime);//视在功率
		List<List<Long>> ep = spmsElectroManager.latestEpByArea(areaId, endTime);//有功电能
		List<List<Long>> reactiveEnergy = spmsElectroManager.latestReactiveEnergyByArea(areaId, endTime);//无功电能
		List<List<Long>> reactiveDemand = spmsElectroManager.latestReactiveDemandByArea(areaId, endTime);//无功需求
		List<List<Long>> activeDemand = spmsElectroManager.latestActiveDemandByArea(areaId, endTime);//有功需求
		
		result.put("area", area);
		result.put("maxLoad", maxLoad);
		result.put("statusData", statusData);
		result.put("eleLevelData", eleLevelData);
		
		result.put("newpowers", power);
		result.put("newreactivePowers", reactivePower);
		result.put("newpowerFactors", powerFactor);
		result.put("newapparentPowers", apparentPower);
		result.put("neweps", ep);
		result.put("newreactiveEnergys", reactiveEnergy);
		result.put("newactiveDemands", reactiveDemand);
		result.put("newreactiveDemands", activeDemand);
		
		
		return result;
	}*/
	@RequestMapping(value = "/updateData")
	@ResponseBody
	public Map<String,Object> updateData(@RequestBody  Map<String,Object> info) throws ParseException{
		Map<String,Object> result = Maps.newHashMap();
		if(null == info.get("areaId") || "".equals(info.get("areaId"))){
			return result;
		}
		String areaId = info.get("areaId").toString();
		String rbtn = info.get("rbtn").toString();
		//long endTime = Long.parseLong(info.get("endTime").toString());
		
		Area area = areaManager.findById(areaId);
		AreaDTO ad = new AreaDTO();
		//AreaDTO adP = new AreaDTO();
		//adP.setId(area.getParent().getId());
		//adP.setName(area.getParent().getName());
		ad.setId(area.getId());
		ad.setClassification(area.getClassification());
		ad.setCode(area.getCode());
		ad.setName(area.getName());
		//ad.setParent(adP);
		ad.setParentIds(area.getParentIds());
		ad.setType(area.getType());
		ad.setPolicy(area.getPolicy());
		
		Integer maxLoad = spmsElectroManager.getAreaMaxLoad(areaId);
		List<Map<String,Integer>> statusData = spmsElectroManager.statisOnlineStatusByArea(areaId);
		List<Map<String,Integer>> eleLevelData = spmsElectroManager.statisEleLevelByArea(areaId);
		
//		List<List<Long>> power = spmsResultDataManager.getAllPowerByArea(areaId ,false);//有功功率
//		List<List<Long>> reactivePower = spmsResultDataManager.getAllReactivePowerByArea(areaId ,false);//无功功率
//		List<List<Long>> powerFactor = spmsResultDataManager.getAllPowerFactorByArea(areaId ,false);//功率因数
//		List<List<Long>> apparentPower = spmsResultDataManager.getAllApparentPowerByArea(areaId ,false);//视在功率
//		List<List<Long>>  ep = spmsResultDataManager.getAllEpByArea(areaId ,false, rbtn);//有功电能
//		List<List<Long>>  reactiveEnergy = spmsResultDataManager.getAllReactiveEnergyByArea(areaId ,false, rbtn);//无功电能
//		List<List<Long>> reactiveDemand = spmsResultDataManager.getAllReactiveDemandByArea(areaId ,false);//无功需求
//		List<List<Long>> activeDemand = spmsResultDataManager.getAllActiveDemandByArea(areaId ,false);//有功需求
//		List<List<Long>> deviceNum = spmsResultDataManager.getAllDeviceNumByArea(areaId ,false);//在线设备数
		
		result = spmsResultDataManager.getAllDataByArea(areaId, true, rbtn);
		
		result.put("area", area);
		result.put("maxLoad", maxLoad);
		result.put("statusData", statusData);
		result.put("eleLevelData", eleLevelData);
		
//		result.put("newpowers", power);
//		result.put("newreactivePowers", reactivePower);
//		result.put("newpowerFactors", powerFactor);
//		result.put("newapparentPowers", apparentPower);
//		result.put("neweps", ep);
//		result.put("newreactiveEnergys", reactiveEnergy);
//		result.put("newactiveDemands", reactiveDemand);
//		result.put("newreactiveDemands", activeDemand);
//		result.put("newdeviceNum", deviceNum);
		return result;
	}
	
	@RequestMapping(value = "/adjustElectro")
	@ResponseBody
	public Map<String,Object> adjustElectro(@RequestBody  Map<String,Object> info){
		 Map<String,Object> result = Maps.newHashMap();
		 String areaId = info.get("areaId").toString();
		 int type = Integer.parseInt(info.get("type").toString());
		 int scale = Integer.parseInt(info.get("scale").toString());
		 
		 try{
			 spmsElectroManager.adjustElectro(areaId,type,Math.abs(scale));
			 result.put("success",true);
		 }catch(Exception e){
			 e.printStackTrace();
	         result.put("success",false);
	         result.put("msg","系统错误,调整失败");
		 }
		 
		 return result;
	}
	@RequestMapping(value = "/regulationInfo")
	@ResponseBody
	public Map<String,Object> regulationInfo(@RequestBody  Map<String,Object> info){
		String areaId = info.get("areaId").toString();
		
		return spmsElectroManager.getRegulationInfo(areaId);
	}
	
	@RequestMapping(value = "/regulationElectroForArea")
	@ResponseBody
	public Map<String,Object> regulationElectroForArea(@RequestBody  Map<String,Object> info){
		Map<String,Object> result = Maps.newHashMap();
		String areaId = info.get("areaId").toString();
		//Map<String,Object> parameter = (Map<String, Object>) info.get("parameter");
		int l = (int) info.get("low");
		int m  = (int) info.get("medium");
		int h = (int) info.get("high");
		Map<String,Object> t = Maps.newHashMap();
		t.put("low", l);
		t.put("medium", m);
		t.put("high", h);
		try{
			result = spmsElectroManager.regulationElectroForArea(areaId, t);
			result.put("success", true);
			result.put("msg", "区域用电调控成功");
		}catch(Exception e){
			result.put("success", false);
			result.put("msg", "区域用电调控失败");
		}
		return result;
	}
}
