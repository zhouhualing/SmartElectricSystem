package com.harmazing.spms.main.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.harmazing.spms.main.manager.SpmsStatisticsManager;

@Controller
@RequestMapping("/main")
public class SpmsMainController {
	@Autowired
	private SpmsStatisticsManager spmsStatisticsManager;
	
	
	/**
	 * 获取首页展示数据
	 * @return Map<String,List<Map<String,Object>>>
	 * @throws ParseException 
	 */
	@RequestMapping("/index")
    @ResponseBody
	public Map<String,List<Map<String,Object>>> index() throws ParseException{
		Map<String,List<Map<String,Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		/**
		 * 1、userCountByArea用户数量按照所属区域进行统计(柱状图)
		 */
		List<Map<String,Object>> userCountByArea = Lists.newArrayList();
		userCountByArea = spmsStatisticsManager.userCountByArea(1);
		
		/**
		 * 2、userCountByRule用户服务类统计(用户数量按照服务的类别)
		 */
		List<Map<String,Object>> userCountByRule = Lists.newArrayList();
		userCountByRule = spmsStatisticsManager.userCountByRule();
		
		/**
		 * 3、userCountByOnlineStatus用户数量根据在线状态统计(offline/online/exception)
		 */
		List<Map<String,Object>> userCountByOnlineStatus = Lists.newArrayList();
		userCountByOnlineStatus = spmsStatisticsManager.userCountByOnlineStatus();
		
		/**
		 * 4、userIncrementByDate用户开通/退订数量增量根据时间的曲线
		 */
		List<Map<String,Object>> userIncrementByDate = Lists.newArrayList();
		userIncrementByDate = spmsStatisticsManager.userIncrementByDate();
		
		/**
		 * 5、deviceByModel设备库存统计(按型号/按仓库位置（各类别设备）)
		 */
		List<Map<String,Object>> deviceByModel = Lists.newArrayList();
		deviceByModel = spmsStatisticsManager.deviceByModel();
		
		/**
		 * 6、deviceByOptStatus设备运营状态统计(设备数目按照 - 库存/运营/售后维修/报废 （个类别设备）)
		 */
		List<Map<String,Object>> deviceByOptStatus = Lists.newArrayList();
		deviceByOptStatus = spmsStatisticsManager.deviceByOptStatus();
		
		
		/**
		 * 7  add to result 
		 */
		result.put("userCountByArea", userCountByArea);
		result.put("userCountByRule", userCountByRule);
		result.put("userCountByOnlineStatus", userCountByOnlineStatus);
		result.put("userIncrementByDate", userIncrementByDate);
		result.put("deviceByModel", deviceByModel);
		result.put("deviceByOptStatus", deviceByOptStatus);
		
		
		/**
		 * 8 、 用户产品期限统计（已到期，1月内，1-3月内，3月以上,半年以上）
		 */
		List<Map<String,Object>> userCountByProductRemaining = Lists.newArrayList();
		userCountByProductRemaining = spmsStatisticsManager.userCountByProductRemaining();
		result.put("userCountByProductRemaining", userCountByProductRemaining);
		
		/**
		 * 软件版本统计
		 */
		List<Map<String,Object>> SoftwareVersionStatisticalList = new ArrayList<Map<String,Object>>();
		Map<String,Object> SoftwareVersionStatistical = new HashMap<String,Object>();
		SoftwareVersionStatistical = spmsStatisticsManager.getSoftwareVersionStatisticalData();
		SoftwareVersionStatisticalList.add(SoftwareVersionStatistical);
		result.put("SoftwareVersionStatistical", SoftwareVersionStatisticalList);
		
		return result;
		
	}
}
