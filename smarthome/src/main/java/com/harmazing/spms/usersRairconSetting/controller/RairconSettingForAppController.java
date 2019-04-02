package com.harmazing.spms.usersRairconSetting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.usersRairconSetting.dto.ClocksettingDTO;
import com.harmazing.spms.usersRairconSetting.dto.RairconSettingDTO;
import com.harmazing.spms.usersRairconSetting.entity.Clocksetting;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;
import com.harmazing.spms.usersRairconSetting.entity.deviceCurve;
import com.harmazing.spms.usersRairconSetting.manager.RairconSettingManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 空调定时器设置功能
 */
@Controller
@RequestMapping("/app")
public class RairconSettingForAppController {
	@Autowired
	private RairconSettingManager rairconSettingManager;
	/**
	 * 加载用户设备空调曲线列表
	 */
	@RequestMapping("/QueryCurve")
	@ResponseBody
	public List<RairconSettingDTO> QueryCurve(@RequestBody Map<String,Object> m) {
		String userId = m.get("userId") != null ? m.get("userId") +"" :"";
		String deviceId = m.get("deviceId") != null ? m.get("deviceId") +"" :"";
		List<RairconSetting> l = null;
		if(!"".equals(userId)){
			l = rairconSettingManager.QueryRairconCurveForUser(userId);
		}
		if(!"".equals(deviceId)){
			l = rairconSettingManager.QueryRairconCurveForDeviceId(deviceId,userId);//+温度限制判断
		}
		List<RairconSettingDTO> ll = new ArrayList<RairconSettingDTO>(); 
		for (RairconSetting rairconSetting : l) {
			RairconSettingDTO RairconSettingDTO = new RairconSettingDTO();
			BeanUtils.copyProperties(rairconSetting, RairconSettingDTO);
			if(rairconSetting.getSpmsUser()!=null){
				UserDTO UserDTO = new UserDTO();
				BeanUtils.copyProperties(rairconSetting.getSpmsUser().getUser(), UserDTO);
				RairconSettingDTO.setUser(UserDTO);
			}
			if(rairconSetting.getSpmsDevice()!=null){
				SpmsDeviceDTO SpmsDeviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(rairconSetting.getSpmsDevice(), SpmsDeviceDTO);
				RairconSettingDTO.setSpmsDevice(SpmsDeviceDTO);
			}
			ll.add(RairconSettingDTO);
		}
		return ll;
	}
	/**
	 * 加载单独空调曲线数量
	 */
	@RequestMapping("/QueryCurveNum")
	@ResponseBody
	public long QueryCurveNum(@RequestBody Map<String,Object> m){
		long l = 0;
		if(m.get("deviceId")!=null && !"".equals(m.get("deviceId")+""))
			return  rairconSettingManager.QueryRairconCurveNum(m.get("deviceId")+"");
		if(m.get("userId")!=null && !"".equals(m.get("userId")+""))
			return  rairconSettingManager.getRairconCurveCountForUser(m.get("userId")+"");
		return l;
	}
	/**
	 * 修改空调设置  天数重复设置
	 */
	@RequestMapping("/updateCurveRepeat")
	@ResponseBody
	public Map<String,Object> updateCurveRepeat(@RequestBody Map<String,Object> m){
		rairconSettingManager.updateCurveRepeat(m.get("curveId").toString(),m.get("deviceId").toString(),(Map<?, ?>)m.get("weeks"));
		return sendMessage.reusltMap(true);
	}
	/**
	 * 加载单独曲线设置节点数据
	 */	
	@RequestMapping("/QueryCurveSetting")
	@ResponseBody
	public List<ClocksettingDTO> QueryCurveRaircon(@RequestBody Map<String,Object> m) {
		List<Clocksetting> l = rairconSettingManager.QueryRairconCurveSetting(m.get("Curveid").toString());
		List<ClocksettingDTO> ll = new ArrayList<ClocksettingDTO>();
		for (Clocksetting clocksetting : l) {
			ClocksettingDTO clocksettingDTO = new ClocksettingDTO();
			BeanUtils.copyProperties(clocksetting, clocksettingDTO);
			if(clocksetting.getRairconSetting()!=null){
				RairconSettingDTO rairconSettingDTO = new RairconSettingDTO();
				BeanUtils.copyProperties(clocksetting.getRairconSetting(), rairconSettingDTO);
				clocksettingDTO.setRairconSettingDTO(rairconSettingDTO);
			}else{
				clocksettingDTO.setRairconSettingDTO(null);
			}
			if(clocksetting.getSpmsDevice()!=null){
				SpmsDeviceDTO SpmsDeviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(clocksetting.getSpmsDevice(), SpmsDeviceDTO);
				clocksettingDTO.setSpmsDevice(SpmsDeviceDTO);
			}else{
				clocksettingDTO.setSpmsDevice(null);
			}
			ll.add(clocksettingDTO);
		}
		return ll;
	}
	
	/**
	 * 添加曲线
	 *//*
	@RequestMapping("/addCurve")
	public void addCurve(@RequestBody RairconSetting rairconSetting ) {
		rairconSettingManager.SaveRairconCurve(rairconSetting);
	}*/
	/**
	 * 删除曲线
	 */
	
	@RequestMapping("/delCurve")
	@ResponseBody
	public String delCurve(HttpServletRequest request) {
		try {
			rairconSettingManager.delRairconCurve(request.getParameter("curveid")+"",request.getParameter("userId")+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/**
	 * 添加空调运行计划
	 * 添加RairconSetting表
	 */
	@RequestMapping("/addCurveSetting")
	@ResponseBody
	public String addSettingCurve(@RequestBody Clocksetting clocksetting,String userId) {
		try {
			rairconSettingManager.addSettingCurve(clocksetting,userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/**
	 * 更新曲线表节点数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateClock")
	@ResponseBody
	public Map<String,Object> updateClock(@RequestBody Map<String, Object> m){
		Map<String, Object> mp = new HashMap<String, Object>();
		if(  m.get("clockSetting")!=null && !"".equals( m.get("clockSetting")+"" )  ){
			mp = (Map<String,Object>)m.get("clockSetting");
			boolean r = rairconSettingManager.updateClock(mp.get("id")+"",m.get("userId")+"" , mp);
			return sendMessage.reusltMap(r);
		}else{
			return sendMessage.reusltMap(false);
		}
	}
	/**
	 * 批量添加空调运行计划
	 * 添加ting表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCurvesClocks")
	@ResponseBody
	public Map<String,Object> addCurveSettings(@RequestBody Map<String, Object> m) {
		RairconSetting rairconSetting = new RairconSetting();
		Map<String, Object> map = (  Map<String, Object>  ) m.get("clockSetting");
		  String clocking = (map.get("clocking")+"").split(":")[0].length()==1 ? "0"+map.get("clocking")+"" : map.get("clocking")+""  ;
		  clocking = clocking.split(":")[0] +":"+ (clocking.split(":")[1].length()==1 ? "0"+clocking.split(":")[1] : clocking.split(":")[1]);
		if(m.get("rairconSetting")!=null && !"".equals(m.get("rairconSetting")+"")){//为已有曲线添加点
			rairconSetting = rairconSettingManager.QueryCurveById(m.get("rairconSetting")+"");
			Clocksetting c = new Clocksetting();
			c.setClocking(   clocking  );
			c.setMode(   Integer.parseInt(map.get("mode")+"")   );
			c.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
			c.setTemp(   Integer.parseInt(map.get("temp")+"")   );
			System.out.println(map.get("windspeed")+"");
			c.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
			c.setStartend(  Integer.parseInt(map.get("startend")!=null?map.get("startend")+"":"0")  );
			c.setRairconSetting(rairconSetting);
			rairconSettingManager.addSettingCurves(m.get("deviceId")+"",c,m.get("userId")+"");
			return sendMessage.reusltMap(true);
		}
		rairconSetting = rairconSettingManager.addCurveTforP(m.get("userId")+"",m.get("deviceId")+"");//添加曲线表
		//先添加曲线表  再添加节点表
		Clocksetting c = new Clocksetting();
		c.setClocking(  clocking  );
		c.setMode(   Integer.parseInt(map.get("mode")+"")   );
		c.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
		c.setTemp(   Integer.parseInt(map.get("temp")+"")   );
		c.setSpmsUser(rairconSettingManager.querySpmsUserById(m.get("userId")+""));
		c.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
		c.setStartend(  Integer.parseInt(map.get("startend")!=null?map.get("startend")+"":"0")  );
		c.setRairconSetting(rairconSetting);
		rairconSettingManager.addSettingCurves(m.get("deviceId")+"",c,m.get("userId")+"");
		return sendMessage.reusltMap(true);
	}
	
	/**
	 * 删除空调运行计划
	 * 删除RairconSetting表
	 */
	@RequestMapping("/delCurveSetting")
	@ResponseBody
	public String delSettingCurve(HttpServletRequest request) {
		try {
			rairconSettingManager.delSettingCurveForApp(request.getParameter("clocksettingid").toString(),request.getParameter("userId")+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/*-----------------------------------↓----重复设置先关----↓---------------------------------------------------*/
	/**
	 * 保存重复设置
	 */
	@RequestMapping("/saveRepeatPopup")
	@ResponseBody 
	public Map<String,Object> saveRepeatPopup(HttpServletRequest request){
		String week[] = (request.getParameter("week")+"").split(",");
		Map<String,Object> mm = new HashMap<String,Object>();
		mm.put("monday", week[0]);
		mm.put("tuesday", week[1]);
		mm.put("wednesday", week[2]);
		mm.put("thursday", week[3]);
		mm.put("friday", week[4]);
		mm.put("saturday", week[5]);
		mm.put("sunday", week[6]);
		rairconSettingManager.saveRepeatPopup(request.getParameter("curveId")+"",mm,request.getParameter("deviceId")+"");
		//覆盖掉原有的设置
		rairconSettingManager.CoverCurveRepeat(  request.getParameter("deviceId")+"" , request.getParameter("curveId")+"" , mm );
		return sendMessage.reusltMap(true);
	}
	/**
	 * 查询定时重复情况
	 */
	@RequestMapping("/queryCurveRepeat")
	@ResponseBody
	public Map<String,Object> queryCurveRepeat(HttpServletRequest request){
		//deviceId  设备Id  week  周一至周日   curveid 曲线ID
		//查询相关数据然后用week一一对比，有数据能查到，说明有重复的，查不到说明没有重复的。
		//先去查询曲线表看看有重复的没
		if(rairconSettingManager.queryCurveRepeatForApp(  request.getParameter("deviceId")+"" , request.getParameter("curveid")+"" , request )){
			return sendMessage.reusltMap(false);
		//再去查查定时相关数据看看时间上有冲突没
		}else if(rairconSettingManager.queryClockRepeatForApp(  request.getParameter("deviceId")+"" ,request.getParameter("clocking")+"", request )){
			return sendMessage.reusltMap(false);
		}else{//以上都没有重复的  返回true
			return sendMessage.reusltMap(true);
		}
	}
	/*-----------------------------------↓--定时设置相关--↓----------------------------------------------------------*/
	/**
	 * ------加载空调定时内容-----
	 * */
	@RequestMapping("/QueryTimingSet")
	@ResponseBody
	public List<ClocksettingDTO> QueryTimingSet(HttpServletRequest request){
		String deviceId = request.getParameter("deviceId")!=null ? request.getParameter("deviceId")+"":"";
		List<Clocksetting> clocksettingList = rairconSettingManager.QueryTimingSet(deviceId);
		List<ClocksettingDTO> rl = new ArrayList<ClocksettingDTO>();
		for (Clocksetting clocksetting : clocksettingList) {
			ClocksettingDTO clocksettingDTO = new ClocksettingDTO();
			BeanUtils.copyProperties(clocksetting, clocksettingDTO);
			if(clocksetting.getRairconSetting()!=null){
				RairconSettingDTO rairconSettingDTO = new RairconSettingDTO();
				BeanUtils.copyProperties(clocksetting.getRairconSetting(), rairconSettingDTO);
				clocksettingDTO.setRairconSettingDTO(rairconSettingDTO);
			}else{
				clocksettingDTO.setRairconSettingDTO(null);
			}
			if(clocksetting.getSpmsDevice()!=null){
				SpmsDeviceDTO SpmsDeviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(clocksetting.getSpmsDevice(), SpmsDeviceDTO);
				clocksettingDTO.setSpmsDevice(SpmsDeviceDTO);
			}else{
				clocksettingDTO.setSpmsDevice(null);
			}
			rl.add(clocksettingDTO);
		}
		return rl;
	}
	/**
	 * 更新定时器设置
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/UpdateShowTimingSet")
	@ResponseBody
	public Map<String,Object> UpdateShowTimingSet(@RequestBody Map<String,Object> m){
		SpmsAirCondition spmsDevice = new SpmsAirCondition();
		Clocksetting clocksetting = new Clocksetting();
		if(m.get("deviceId") != null){//根据设备ID查找设备
			spmsDevice = rairconSettingManager.queryDevice(m.get("deviceId")+"");
		}
		if(m.get("clockSetting") != null){
			clocksetting = rairconSettingManager.updateclockSet((Map<String,Object>) m.get("clockSetting"), spmsDevice,m.get("userId")+"");
			//TODO 重复覆盖
			Map<String,Object> mm = (Map<String,Object>)m.get("clockSetting");
			rairconSettingManager.updateClockForRepeat( clocksetting ,m.get("deviceId")+"" , clocksetting.getId() , mm);
		}
		return sendMessage.reusltMap(true);
	}
	
	
	
	
	/**
	 * 加载曲线重复设置内容
	 */	
	@RequestMapping("/queryCurveRepeatData")
	@ResponseBody
	public Map<String,Object> queryCurveRepeatData(HttpServletRequest request){
		List<deviceCurve> deviceCurveList = rairconSettingManager.queryDeviceCurveForCurveId(    request.getParameter("curveId")+"", request.getParameter("deviceId")+""   );
		deviceCurve deviceCurve = new deviceCurve();
		boolean[] rm = new boolean[7];
		if( deviceCurveList != null && deviceCurveList.size() != 0 ){
			deviceCurve = deviceCurveList.get(0);
			rm[0] = (deviceCurve.getMonday()!=null&& deviceCurve.getMonday()!=0) ;
			rm[1] = (deviceCurve.getTuesday()!=null && deviceCurve.getTuesday()!=0);
			rm[2] = (deviceCurve.getWednesday()!=null&& deviceCurve.getWednesday()!=0);
			rm[3] = (deviceCurve.getThursday()!=null && deviceCurve.getThursday()!=0);
			rm[4] = (deviceCurve.getFriday()!=null && deviceCurve.getFriday()!=0);
			rm[5] = (deviceCurve.getSaturday()!=null && deviceCurve.getSaturday()!=0);
			rm[6] = (deviceCurve.getSunday()!=null&& deviceCurve.getSunday()!=0);
		}else{
			for (int i = 0; i < rm.length; i++) {
				rm[i] = false;
			}
		}
		Map<String,Object> rmM = new HashMap<String,Object>();
		rmM.put("result", rm);
		return rmM;
	}
}		