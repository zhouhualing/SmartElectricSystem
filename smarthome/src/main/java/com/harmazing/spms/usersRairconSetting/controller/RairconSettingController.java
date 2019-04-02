package com.harmazing.spms.usersRairconSetting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.usersRairconSetting.dto.ClocksettingDTO;
import com.harmazing.spms.usersRairconSetting.dto.RairconSettingDTO;
import com.harmazing.spms.usersRairconSetting.dto.deviceCurveDTO;
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
@RequestMapping("/rairconSet")
public class RairconSettingController {
	@Autowired
	private RairconSettingManager rairconSettingManager;
	/**
	 * 加载用户设备空调曲线列表
	 */
	@RequestMapping("/QueryCurve")
	@ResponseBody
	public List<RairconSettingDTO> QueryCurve(@RequestBody Map<String,Object> deviceId) {
		//List<RairconSetting> l = null;
		/*if(deviceId.get("userId")!=null){
			//最低温:minTemps,最高温:maxTemps
			l = rairconSettingManager.QueryRairconCurveForUser( deviceId.get("userId").toString() );
		}else if(deviceId.get("deviceId")!=null){
			if( "all".equals( deviceId.get("deviceId")+"" )){
				//加载所有曲线图
				l = rairconSettingManager.QueryRairconCurveAll();
			}else{
				l = rairconSettingManager.QueryRairconCurve( deviceId.get("deviceId").toString() );
			}
			l = rairconSettingManager.QueryRairconCurveAll();
		}*/
		//通过用户查询曲线
		List<RairconSetting> l = rairconSettingManager.QueryRairconCurveForUser(sendMessage.getSpmsUserId());
		List<RairconSettingDTO> ll = new ArrayList<RairconSettingDTO>(); 
		for (RairconSetting rairconSetting : l) {
			RairconSettingDTO RairconSettingDTO = new RairconSettingDTO();
			if( "all".equals( deviceId.get("deviceId")+"" )){
				BeanUtils.copyProperties( rairconSetting , RairconSettingDTO );
				ll.add(RairconSettingDTO);
				continue;
			}
			if(deviceId.get("minTemps")!=null && 
					!"".equals(deviceId.get("minTemps")+"") && 
					deviceId.get("maxTemps")!=null && 
					!"".equals(deviceId.get("maxTemps")+"")){
				if(rairconSettingManager.tempJudge(   rairconSetting.getId() , deviceId.get("minTemps")+"" , deviceId.get("maxTemps")+""   )   ){
					BeanUtils.copyProperties( rairconSetting , RairconSettingDTO );
					ll.add(RairconSettingDTO);
				}
			}
			/*
				UserDTO UserDTO = new UserDTO();   
				BeanUtils.copyProperties(rairconSetting.getSpmsUser().getUser(), UserDTO);
				RairconSettingDTO.setUser(UserDTO);
				SpmsDeviceDTO SpmsDeviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(rairconSetting.getSpmsDevice(), SpmsDeviceDTO);
				RairconSettingDTO.setSpmsDevice(SpmsDeviceDTO);
			*/
		}
		return ll;
	}
	/**
	 * 加载单独空调曲线数量
	 */
	@RequestMapping("/QueryCurveNum")
	@ResponseBody
	public long QueryCurveNum(@RequestBody Map<String,Object> m){
		/*if( "all".equals(deviceId.get("deviceId")+"") ){
			return rairconSettingManager.QueryRairconCurveAllNum();
		}//minTemps,maxTemps*/	
		long l = 0 ;
		if(m.get("deviceId")!=null){
			l = rairconSettingManager.QueryRairconCurveNumByUserId(m.get("minTemps")+"",m.get("maxTemps")+"");
		}
		return l;
	}
	/**
	 * 加载单独空调曲线数量
	 */
	@RequestMapping("/QueryCurveNumForCurveDevice")
	@ResponseBody
	public long QueryCurveNumForCurveDevice(@RequestBody Map<String,Object> m){
		/*if( "all".equals(deviceId.get("deviceId")+"") ){
			return rairconSettingManager.QueryRairconCurveAllNum();
		}//minTemps,maxTemps*/	
		long l = 0 ;
		if(m.get("deviceId")!=null){
			l = rairconSettingManager.QueryCurveNumForCurveDevice(m.get("deviceId")+"",m.get("minTemps")+"",m.get("maxTemps")+"");
		}
		return l;
	}
	/**
	 * 修改空调设置  天数重复设置
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/updateCurveRepeat")
	@ResponseBody
	public String updateCurveRepeat(@RequestBody Map<String,Object> m){
		
		rairconSettingManager.updateCurveRepeat(m.get("curveId").toString(),m.get("deviceId").toString(),(Map)m.get("weeks"));
		
		/*----覆盖原有设置------*/
		
		rairconSettingManager.CoverCurveRepeat(m.get("deviceId").toString(),m.get("curveId").toString(),(Map)m.get("weeks"));
		
		return "1";
	}
	
	/**
	 * 加载单独曲线设置节点数据
	 */	
	@RequestMapping("/QueryCurveSetting")
	@ResponseBody
	public List<ClocksettingDTO> QueryCurveRaircon(@RequestBody Map<String,Object> Curveid) {
		List<Clocksetting> l = rairconSettingManager.QueryRairconCurveSetting(Curveid.get("Curveid").toString());
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
	 * 加载重复日期信息设备曲线相关
	 */	
	@RequestMapping("/QueryCurveSettingRepeat")
	@ResponseBody
	public List<deviceCurveDTO> QueryCurveSettingRepeat(@RequestBody Map<String,Object> m){
		List<deviceCurveDTO> rl = new ArrayList<deviceCurveDTO>();
		List<deviceCurve> l = rairconSettingManager.queryDeviceCurveForCurveId(m.get("CurveId")+"",m.get("deviceId")+"");
		for (deviceCurve deviceCurve : l) {
			deviceCurveDTO deviceCurveDTO = new deviceCurveDTO();
			BeanUtils.copyProperties(deviceCurve, deviceCurveDTO);
			rl.add(deviceCurveDTO);
		}
		return rl;
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
	public String delCurve(@RequestBody Map<String,Object> curveid ) {
		try {
			rairconSettingManager.delRairconCurve(curveid.get("curveid").toString(),"");
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
	public String addSettingCurve(@RequestBody Clocksetting clocksetting) {
		try {
			rairconSettingManager.addSettingCurve(clocksetting,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/**
	 * 批量添加空调运行计划
	 * 添加ting表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCurvesClocks")
	@ResponseBody
	public Map<String,Object> addCurveSettings(@RequestBody Map<String, Object> m) {
		List<Map<String, Object>> list = (List<  Map<String, Object>  >) m.get("Clocksetting");
		//try {
		//rairconSettingManager.addSettingCurves(clocksetting);
		//判断是否为新添加的一组  若不是则查询对应的RairconSetting表  根据rairconSetting参数
		RairconSetting rairconSetting = new RairconSetting();
		System.out.println(m.get("rairconSetting")+"");
		
		if(m.get("rairconSetting")!=null && !"".equals(m.get("rairconSetting")+"")){
			rairconSetting = rairconSettingManager.QueryRairconSetting(m.get("rairconSetting")+"");
			//删除原有的
			rairconSettingManager.delClock(m.get("rairconSetting")+"");
			//添加新的
			for (Map<String, Object> map : list) {
				 String clocking = (map.get("clocking")+"").split(":")[0].length()==1 ? "0"+map.get("clocking")+"" : map.get("clocking")+""  ;
				  clocking = clocking.split(":")[0] +":"+ (clocking.split(":")[1].length()==1 ? "0"+clocking.split(":")[1] : clocking.split(":")[1]);
				Clocksetting c = new Clocksetting();
				c.setClocking(  clocking );
				c.setMode(   Integer.parseInt(map.get("mode")+"")   );
				c.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
				c.setTemp(   Integer.parseInt(map.get("temp")+"")   );
				c.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
				c.setStartend(  Integer.parseInt( map.get("startend")+"" )  );
				/*
					c.setMonday( Integer.parseInt(map.get("monday")+"") );//星期一
					c.setTuesday( Integer.parseInt(map.get("tuesday")+"") );//星期二
					c.setWednesday( Integer.parseInt(map.get("wednesday")+"") );//星期三
					c.setThursday( Integer.parseInt(map.get("thursday")+"") );//星期四
					c.setFriday( Integer.parseInt(map.get("friday")+"") );//星期五
					c.setSaturday( Integer.parseInt(map.get("saturday")+"") );//星期六
					c.setSunday( Integer.parseInt(map.get("sunday")+"") );//星期日
				*/				
				c.setRairconSetting(rairconSetting);
				rairconSettingManager.addSettingCurves(m.get("deviceId")+"",c,"");
			}
		}else{
			rairconSetting = rairconSettingManager.addCurveT(m.get("deviceId")+"");
			//先添加曲线表  再添加时钟设置表
			for (Map<String, Object> map : list) {
				Clocksetting c = new Clocksetting();
				 String clocking = (map.get("clocking")+"").split(":")[0].length()==1 ? "0"+map.get("clocking")+"" : map.get("clocking")+""  ;
				  clocking = clocking.split(":")[0] +":"+ (clocking.split(":")[1].length()==1 ? "0"+clocking.split(":")[1] : clocking.split(":")[1]);
				c.setClocking(  clocking  );
				c.setMode(   Integer.parseInt(map.get("mode")+"")   );
				c.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
				c.setTemp(  Integer.parseInt( map.get("temp")+"" )  );
				c.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
				c.setStartend(  Integer.parseInt(map.get("startend")+"")  );
				c.setRairconSetting(rairconSetting);
				rairconSettingManager.addSettingCurves(m.get("deviceId")+"",c,"");
			}
		}	
		return sendMessage.reusltMap(true);
	}
	/**
	 * 删除定时
	 * 删除clocksetting表
	 */
	@RequestMapping("/delClocksetting")
	@ResponseBody
	public String delClocksetting(@RequestBody Map<String,Object> clocksettingid) {
		try {
			rairconSettingManager.delSettingCurveSetting(clocksettingid.get("clocksettingid").toString(),"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/**
	 * 删除空调运行计划
	 * 删除RairconSetting表
	 */
	@RequestMapping("/delCurveSetting")
	@ResponseBody
	public String delSettingCurve(@RequestBody Map<String,Object> clocksettingid) {
		try {
			rairconSettingManager.delSettingCurve(clocksettingid.get("clocksettingid").toString(),"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		}
		return "1";
	}
	/*-----------------------------------↓----重复设置相关----↓---------------------------------------------------*/
	/**
	 * 保存重复设置
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/saveRepeatPopup")
	@ResponseBody 
	public Map<String,Object> saveRepeatPopup(@RequestBody Map<String,Object> m){
		Map mm =(Map) m.get("week");
		rairconSettingManager.saveRepeatPopup(m.get("curveid")+"",mm , m.get("deviceId")+"");
		//覆盖掉原有的设置
		rairconSettingManager.CoverCurveRepeat(  m.get("deviceId")+"" , m.get("curveid")+"" , mm );
		return null;
	}
	/**
	 * 查询定时重复情况
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/queryCurveRepeat")
	@ResponseBody
	public Map<String,Object> queryCurveRepeat(@RequestBody Map<String,Object> m){
		//deviceId  设备Id  week  周一至周日   curveid 曲线ID
		//查询相关数据然后用week一一对比，有数据能查到，说明有重复的，查不到说明没有重复的。
		//先去查询曲线表看看有重复的没
		Map mm = (Map)m.get("weeks");
		if(rairconSettingManager.queryCurveRepeat2(  m.get("deviceId")+"" , m.get("curveId")+"" , mm )){
			return sendMessage.reusltMap(false);
		//再去查查定时相关数据看看时间上有冲突没
		}/*else if(rairconSettingManager.queryClockRepeat(  m.get("deviceId")+"" ,m.get("clocking")+"" ,mm )){
			rm.put("result", false);
			return rm;
		}*/else{//以上都没有重复的  返回true
			return sendMessage.reusltMap(true);
		}
	}
	/**
	 * 查询定时重复情况
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping("/queryCurveRepeatToTimingSet")
	@ResponseBody
	public Map<String,Object> queryCurveRepeatToTimingSet(@RequestBody Map<String,Object> m){
		Map mm = (Map)m.get("week");
		if(rairconSettingManager.queryClockRepeat(  m.get("deviceId")+"" ,  m.get("clocking")+"",m.get("clocksettingId")+"", mm )){
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
	public List<ClocksettingDTO> QueryTimingSet(@RequestBody Map<String,Object> m){
		List<Clocksetting> clocksettingList = rairconSettingManager.QueryTimingSet(m.get("deviceId")+"");
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
	 * 加载单独空调曲线数量
	 */
	@RequestMapping("/QueryClockNum")
	@ResponseBody
	public long QueryClockNum(@RequestBody Map<String,Object> deviceId){
		if( "all".equals(deviceId.get("deviceId")+"") ){
			return rairconSettingManager.QueryRairconClockAllNum();
		}
		long l = rairconSettingManager.QueryRairconClockNum(deviceId.get("deviceId").toString());
		return l;
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
		if(m.get("spmsDevice") != null){//根据设备ID查找设备
			spmsDevice = rairconSettingManager.queryDevice(m.get("spmsDevice")+"");
		}
		if(m.get("Clocksetting") != null){
			clocksetting = rairconSettingManager.updateclockSet((Map) m.get("Clocksetting"), spmsDevice,"");
		}
		//TODO 重复覆盖
		Map mm = (Map)m.get("week");
		rairconSettingManager.updateClockForRepeat( clocksetting ,m.get("spmsDevice")+"" , m.get("clocking")+"" , mm);
		return sendMessage.reusltMap(true);
	}
}		