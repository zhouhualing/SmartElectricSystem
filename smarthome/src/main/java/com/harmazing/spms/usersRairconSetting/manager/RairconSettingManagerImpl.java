package com.harmazing.spms.usersRairconSetting.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.device.dao.SpmsAirConditionDAO;
import com.harmazing.spms.device.dao.*;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.device.entity.SpmsWinDoor;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.usersRairconSetting.dao.SpmsRairconCurveDAO;
import com.harmazing.spms.usersRairconSetting.dao.SpmsRairconCurveSettingDAO;
import com.harmazing.spms.usersRairconSetting.dao.deviceCurveDAO;

import com.harmazing.spms.usersRairconSetting.entity.Clocksetting;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;
import com.harmazing.spms.usersRairconSetting.entity.deviceCurve;

import com.harmazing.spms.usersRairconSetting.manager.RairconSettingManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("RairconSettingManager")
public class RairconSettingManagerImpl implements RairconSettingManager {
	@Autowired
	private SpmsRairconCurveDAO RairconCurveDAO;
	@Autowired
	private deviceCurveDAO deviceCurveDAO;
	@Autowired
	private SpmsRairconCurveSettingDAO RairconCurveSettingDAO;
	@Autowired
	private SpmsAirConditionDAO spmsAcDAO;
	@Autowired
	private SpmsWinDoorDAO spmsWdDAO;
	@Autowired
	private SpmsPirDAO spmsPirDAO;
	@Autowired
	private SpmsUserDAO spmsUserDAO;
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	/**
	 * 加载用户设备空调曲线列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List QueryRairconCurve(String deviceId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.getSpmsRairconCurveByDeviceId(deviceId);
	}
	/**
	 * 加载用户设备空调曲线列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List QueryRairconCurveForUser(String userId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.getSpmsRairconCurveByUserId(userId);
	}
	/**
	 * 加载用户设备空调曲线列表
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List QueryRairconCurveForUserForApp(String userId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.getSpmsRairconCurveByUserIdForApp(userId);
	}
	/*
	 * 添加
	 */
	@Override
	public void SaveRairconCurve(RairconSetting rairconSetting,String userId) {
		// TODO Auto-generated method stub
		RairconCurveDAO.save(rairconSetting);
		if(rairconSetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(rairconSetting.getSpmsDevice().getId(),userId);
	}
	/*
	 * 删除
	 */
	@Transactional(readOnly = false)
	@Override
	public void delRairconCurve(	String curveid	,	String userId	) {
		// TODO Auto-generated method stub
		RairconSetting rairconSetting = (RairconSetting)RairconCurveDAO.findOne(curveid);
		if( rairconSetting.getSpmsDevice() != null ){
			sendMessage.sendCurveMessage(	rairconSetting.getSpmsDevice().getId(),userId	);
		}
		RairconCurveDAO.delete(	rairconSetting	);
	}
	/**
	 * 加载曲线设置空调编辑列表
	 */		
	@Override
	public List<Clocksetting> QueryRairconCurveSetting(String Curveid) {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.getSpmsRairconCurveSettingByCurveid(Curveid);
	}
	/*
	 * 添加
	 */
	@Transactional(readOnly = false)
	@Override
	public void addSettingCurve(Clocksetting clocksetting,String userId) {
		// TODO Auto-generated method stub
//		clocksetting.getRairconSetting().setUser(UserUtil.getCurrentUser());
		clocksetting.setRairconSetting(RairconCurveDAO.findOne(clocksetting.getRairconSetting().getId()));
		RairconCurveSettingDAO.save(clocksetting);
		if(clocksetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(clocksetting.getSpmsDevice().getId(),userId);
	}
	/*
	 * 删除
	 */
	@Transactional(readOnly = false)
	@Override
	public void delSettingCurve(String clocksettingid,String userId) {
		// TODO Auto-generated method stub
		RairconSetting clocksetting = (RairconSetting)RairconCurveDAO.findOne(clocksettingid);
		if(clocksetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(clocksetting.getSpmsDevice().getId(),userId);
		RairconCurveDAO.delete(clocksetting);
	}
	
	/*
	 * 删除定时	
	 */
	@Transactional(readOnly = false)
	@Override
	public void delSettingCurveSetting(String clocksettingid,String userId) {
		// TODO Auto-generated method stub
		Clocksetting clocksetting = (Clocksetting)RairconCurveSettingDAO.findOne(clocksettingid);
		if(clocksetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(clocksetting.getSpmsDevice().getId(),userId);
		RairconCurveSettingDAO.delete(clocksetting);
	}
	
	/**
	 * 删除曲线节点（手机端）
	 */
	@Override
	public void delSettingCurveForApp(String clocksettingid,String userId) {
		// TODO Auto-generated method stub
		Clocksetting clocksetting = (Clocksetting)RairconCurveSettingDAO.findOne(clocksettingid);
		RairconSetting rairconSetting = (RairconSetting)RairconCurveDAO.findOne(clocksettingid);
		if(clocksetting!=null){
			if(clocksetting.getRairconSetting()!=null){
				if(clocksetting.getRairconSetting().getClocksettingList().size()==1){
					RairconCurveDAO.delete(clocksetting.getRairconSetting());
				}else{
					RairconCurveSettingDAO.delete(clocksetting);
				}
			}else{
				RairconCurveSettingDAO.delete(clocksetting);
			}
			if(clocksetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(clocksetting.getSpmsDevice().getId(),userId);
		}else if(rairconSetting!=null){
			RairconCurveDAO.delete(rairconSetting);
			if(rairconSetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(rairconSetting.getSpmsDevice().getId(),userId);
		}
		
	}
	@Override
	public long QueryRairconCurveNum(String deviceId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.getRairconCurveCount(deviceId);
	}
	
	@Override
	public long getRairconCurveCountForUser(String userId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.getRairconCurveCountForUser(userId);
	}
	
	//批量添加空调计划
	@Transactional(readOnly = false)
	@Override
	public void addSettingCurves(String deviceId,Clocksetting clocksetting,String userId) {
		// TODO Auto-generated method stub
		RairconCurveSettingDAO.save(clocksetting);
		sendMessage.sendCurveMessage(deviceId,userId);
	}
	/**
	 * 添加曲线表
	 */
	@Transactional(readOnly = false)
	@Override
	public RairconSetting addCurveT(String deviceId){
		RairconSetting rairconSetting = new RairconSetting();
		rairconSetting.setUser( UserUtil.getCurrentUser() );
		
		if( deviceId!=null && !"".equals(deviceId) )
		rairconSetting.setSpmsDevice( spmsAcDAO.findOne(deviceId) );
		
		String mobile = UserUtil.getCurrentUser().getMobile();
		if( mobile != null ){
			rairconSetting.setSpmsUser( spmsUserDAO.getByMobile( mobile ) );
		}
		rairconSetting.setStartend(1);//默认为开启   TODO
		RairconCurveDAO.save( rairconSetting );
		sendMessage.sendCurveMessage( deviceId , rairconSetting.getSpmsUser().getId() );
		return rairconSetting;
	}
	/**
	 * 查询RairconSetting表  根据ID
	 */
	@Override
	public RairconSetting QueryRairconSetting(String rairconSettingid) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.findOne(rairconSettingid);
	}
	@Transactional(readOnly = false)
	@Override
	public void delClock(String rairconSettingid) {
		// TODO Auto-generated method stub
		RairconCurveSettingDAO.delClock(rairconSettingid);
	}
	/**
	 * 修改空调设置  天数重复设置(星期)(舒睡曲线)
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	@Override
	public void updateCurveRepeat(String curveId, String deviceId,Map weekMap) {
		// TODO Auto-generated method stub
		String monday = weekMap.get("monday")+"";//星期一
		String tuesday = weekMap.get("tuesday")+"";//星期二
		String wednesday = weekMap.get("wednesday")+"";//星期三
		String thursday = weekMap.get("thursday")+"";//星期四
		String friday = weekMap.get("friday")+"";//星期五
		String saturday = weekMap.get("saturday")+"";//星期六
		String sunday = weekMap.get("sunday")+"";//星期日
		//将重复的制0
		List<deviceCurve> l = deviceCurveDAO.queryDeviceCurveForCurveId(curveId,deviceId);
		deviceCurve deviceCurve = new deviceCurve();
		if( l!=null && l.size()!=0 ){//修改旧记录
			deviceCurve = l.get(0);
			deviceCurve.setMonday(Integer.parseInt(monday));
			deviceCurve.setTuesday(Integer.parseInt(tuesday));
			deviceCurve.setWednesday(Integer.parseInt(wednesday));
			deviceCurve.setThursday(Integer.parseInt(thursday));
			deviceCurve.setFriday(Integer.parseInt(friday));
			deviceCurve.setSaturday(Integer.parseInt(saturday));
			deviceCurve.setSunday(Integer.parseInt(sunday));
			deviceCurveDAO.save(deviceCurve);
		}else{//添加新的记录
			deviceCurve.setMonday(Integer.parseInt(monday));
			deviceCurve.setTuesday(Integer.parseInt(tuesday));
			deviceCurve.setWednesday(Integer.parseInt(wednesday));
			deviceCurve.setThursday(Integer.parseInt(thursday));
			deviceCurve.setFriday(Integer.parseInt(friday));
			deviceCurve.setSaturday(Integer.parseInt(saturday));
			deviceCurve.setSunday(Integer.parseInt(sunday));
			deviceCurve.setSpmsDevice(spmsAcDAO.findOne(deviceId));
			deviceCurve.setRairconSetting(RairconCurveDAO.findOne(curveId));
			deviceCurveDAO.save(deviceCurve);
		}
		sendMessage.sendCurveMessage(deviceId,"");
	}
	//-----------------------------------↓--定时设置相关--↓----------------------------------------------------------
	/**
	 * 加载空调定时设置信息
	 */
	@Override
	public List<Clocksetting> QueryTimingSet(String deviceid) {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.QueryTimingSet(deviceid);
	}
	/**
	 * 根据设备ID返回相应对象
	 */
	@Override
	public SpmsAirCondition queryDevice(String deviceId) {
		// TODO Auto-generated method stub
		return spmsAcDAO.findOne(deviceId);
	}
	/**
	 * 根据ID查询定时表
	 */
	@Override
	public Clocksetting queryClockSetById(String id) {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.findOne(id);
	}
	/**
	 * 更新定时设置
	 */
	@Transactional
	@Override
	public Clocksetting updateclockSet(Map<String, Object> map,SpmsAirCondition spmsDevice,String userId) {
		// TODO Auto-generated method stub
		String clocking = (map.get("clocking")+"").split(":")[0].length()==1 ? "0"+map.get("clocking")+"" : map.get("clocking")+""  ;
		clocking = clocking.split(":")[0] +":"+ (clocking.split(":")[1].length()==1 ? "0"+clocking.split(":")[1] : clocking.split(":")[1]);
		if(map.get("id") != null && !"0".equals(map.get("id")+"") && !"".equals(map.get("id")+"")){//修改
//			updateclockSet(map,spmsDevice);
			Clocksetting cd = RairconCurveSettingDAO.findOne(map.get("id").toString());
			Clocksetting cc = new Clocksetting();
			BeanUtils.copyProperties(cd, cc);
			cc.setClocking(   clocking   );
			cc.setMode(   Integer.parseInt(map.get("mode")+"")   );
			cc.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
			cc.setTemp(  Integer.parseInt( map.get("temp")+"" )  );
			cc.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
			cc.setStartend(Integer.parseInt( map.get("startend")!=null?map.get("startend")+"":"0"));
			cc.setAlone(1);
			//cc.setSpmsDevice(spmsDevice);
			cc.setMonday(   Integer.parseInt(map.get("monday")+"")   );
			cc.setTuesday(   Integer.parseInt(map.get("tuesday")+"")   );
			cc.setWednesday(   Integer.parseInt(map.get("wednesday")+"")   );
			cc.setThursday(   Integer.parseInt(map.get("thursday")+"")   );
			cc.setFriday(   Integer.parseInt(map.get("friday")+"")   );
			cc.setSaturday(   Integer.parseInt(map.get("saturday")+"")   );
			cc.setSunday(   Integer.parseInt(map.get("sunday")+"")   );
			RairconCurveSettingDAO.save(cc);
			if(cc.getSpmsDevice() != null) sendMessage.sendCurveMessage(cc.getSpmsDevice().getId(),userId);
			return cc;
		}else{//添加
			Clocksetting c = new Clocksetting();
			c.setClocking(   clocking   );
			c.setMode(   Integer.parseInt(map.get("mode")+"")   );
			c.setOn_off(   Integer.parseInt(map.get("on_off")+"")   );
			c.setTemp(  Integer.parseInt( map.get("temp")+"" )  );
			c.setWindspeed(   Integer.parseInt(map.get("windspeed")+"")   );
			c.setStartend(Integer.parseInt( map.get("startend")!=null?map.get("startend")+"":"0"));
			c.setAlone(1);
			c.setSpmsDevice(spmsDevice);
			c.setMonday(   Integer.parseInt(map.get("monday")+"")   );
			c.setTuesday(   Integer.parseInt(map.get("tuesday")+"")   );
			c.setWednesday(   Integer.parseInt(map.get("wednesday")+"")   );
			c.setThursday(   Integer.parseInt(map.get("thursday")+"")   );
			c.setFriday(   Integer.parseInt(map.get("friday")+"")   );
			c.setSaturday(   Integer.parseInt(map.get("saturday")+"")   );
			c.setSunday(   Integer.parseInt(map.get("sunday")+"")   );
			RairconCurveSettingDAO.save(c);
			if(c.getSpmsDevice() != null) sendMessage.sendCurveMessage(c.getSpmsDevice().getId(),userId);
			return c;
		}
			
	}
	@Override
	public List<Clocksetting> QueryCurveClock(String curveId,String alone) {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.queryClocksByCurveId(curveId,alone);
	}
	
	/*
	 * 曲线重复判断
	 */
	@Override
	public boolean queryCurveRepeat2(String deviceId, String curveid, Map mm) {
		List<deviceCurve> l  = deviceCurveDAO.querydeviceCurveForDeviceId(deviceId,
				Integer.parseInt(mm.get("monday") + ""),
				Integer.parseInt(mm.get("tuesday") + ""),
				Integer.parseInt(mm.get("wednesday") + ""),
				Integer.parseInt(mm.get("thursday") + ""),
				Integer.parseInt(mm.get("friday") + ""),
				Integer.parseInt(mm.get("saturday") + ""),
				Integer.parseInt(mm.get("sunday") + ""),
				curveid
				);
		if( l != null && l.size() != 0 )return true;
		return false;
	}
	/*
	 * 定时重复相关判断
	 */
	@Override
	public boolean queryClockRepeat(String deviceId,String clocking,String clocksettingId, Map mm) {
		List<Clocksetting> l  = RairconCurveSettingDAO.queryCurveRepeat(deviceId,
				Integer.parseInt(mm.get("monday") + ""),
				Integer.parseInt(mm.get("tuesday") + ""),
				Integer.parseInt(mm.get("wednesday") + ""),
				Integer.parseInt(mm.get("thursday") + ""),
				Integer.parseInt(mm.get("friday") + ""),
				Integer.parseInt(mm.get("saturday") + ""),
				Integer.parseInt(mm.get("sunday") + ""),
				clocking,
				clocksettingId
				);
		if( l != null && l.size() != 0 )return true;
		return false;
	}
	/**
	 * 保存重复设置
	 */
	@Transactional
	@Override
	public boolean saveRepeatPopup(String curveid, Map mm,String deviceId) {
		deviceCurve deviceCurve = new deviceCurve();
		List<deviceCurve> deviceCurveList =  deviceCurveDAO.queryDeviceCurveForCurveId(curveid, deviceId);
		if(	 deviceCurveList != null  &&  deviceCurveList.size() > 0	){
			deviceCurve = deviceCurveList.get(0);
		}
		deviceCurve.setRairconSetting(RairconCurveDAO.findOne(curveid));
		deviceCurve.setSpmsDevice(spmsAcDAO.findOne(deviceId));
		deviceCurve.setMonday(Integer.parseInt(mm.get("monday")+""));
		deviceCurve.setTuesday(Integer.parseInt(mm.get("tuesday")+""));
		deviceCurve.setWednesday(Integer.parseInt(mm.get("wednesday")+""));
		deviceCurve.setThursday(Integer.parseInt(mm.get("thursday")+""));
		deviceCurve.setFriday(Integer.parseInt(mm.get("friday")+""));
		deviceCurve.setSaturday(Integer.parseInt(mm.get("saturday")+""));
		deviceCurve.setSunday(Integer.parseInt(mm.get("sunday")+""));
		deviceCurveDAO.save(deviceCurve);
		return true;
		// TODO Auto-generated method stub
	}
	/*
	 * 更新定时器设置
	 */
	@Override
	public void updateclockSetForApp(HttpServletRequest request,SpmsAirCondition spmsDevice,String userId) {
		// TODO Auto-generated method stub
		if(request.getParameter("id") != null && !"0".equals(request.getParameter("id")+"") && !"".equals(request.getParameter("id")+"")){//修改
//			updateclockSet(map,spmsDevice);
			Clocksetting cd = RairconCurveSettingDAO.findOne(request.getParameter("id").toString());
			Clocksetting cc = new Clocksetting();
			BeanUtils.copyProperties(cd, cc);
			cc.setClocking(   request.getParameter("clocking")+""   );
			cc.setMode(   Integer.parseInt(request.getParameter("mode")+"")   );
			cc.setOn_off(   Integer.parseInt(request.getParameter("on_off")+"")   );
			cc.setTemp(   Integer.parseInt( request.getParameter("temp")+"" )  );
			cc.setWindspeed(   Integer.parseInt(request.getParameter("windspeed")+"")   );
			cc.setStartend(Integer.parseInt( request.getParameter("startend")+""));
			cc.setAlone(1);
			//cc.setSpmsDevice(spmsDevice);
			cc.setMonday(   Integer.parseInt(request.getParameter("monday")+"")   );
			cc.setTuesday(   Integer.parseInt(request.getParameter("tuesday")+"")   );
			cc.setWednesday(   Integer.parseInt(request.getParameter("wednesday")+"")   );
			cc.setThursday(   Integer.parseInt(request.getParameter("thursday")+"")   );
			cc.setFriday(   Integer.parseInt(request.getParameter("friday")+"")   );
			cc.setSaturday(   Integer.parseInt(request.getParameter("saturday")+"")   );
			cc.setSunday(   Integer.parseInt(request.getParameter("sunday")+"")   );
			RairconCurveSettingDAO.save(cc);
			if(cc.getSpmsDevice() != null) sendMessage.sendCurveMessage(cc.getSpmsDevice().getId(),userId);	
		}else{//添加
			Clocksetting c = new Clocksetting();
			c.setClocking(   request.getParameter("clocking")+""   );
			c.setMode(   Integer.parseInt(request.getParameter("mode")+"")   );
			c.setOn_off(   Integer.parseInt(request.getParameter("on_off")+"")   );
			c.setTemp(  Integer.parseInt(  request.getParameter("temp")+"" )  );
			c.setWindspeed(   Integer.parseInt(request.getParameter("windspeed")+"")   );
			c.setStartend(Integer.parseInt( request.getParameter("startend")+""));
			c.setAlone(1);
			c.setSpmsDevice(spmsDevice);
			c.setMonday(   Integer.parseInt(request.getParameter("monday")+"")   );
			c.setTuesday(   Integer.parseInt(request.getParameter("tuesday")+"")   );
			c.setWednesday(   Integer.parseInt(request.getParameter("wednesday")+"")   );
			c.setThursday(   Integer.parseInt(request.getParameter("thursday")+"")   );
			c.setFriday(   Integer.parseInt(request.getParameter("friday")+"")   );
			c.setSaturday(   Integer.parseInt(request.getParameter("saturday")+"")   );
			c.setSunday(   Integer.parseInt(request.getParameter("sunday")+"")   );
			RairconCurveSettingDAO.save(c);
			if(c.getSpmsDevice() != null) sendMessage.sendCurveMessage(c.getSpmsDevice().getId(),userId);	
		}
	}
	@Override
	public boolean queryCurveRepeatForApp(String deviceId, String curveid,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<RairconSetting> l  = RairconCurveDAO.queryCurveRepeat(deviceId,
				Integer.parseInt(request.getParameter("monday") + ""),
				Integer.parseInt(request.getParameter("tuesday") + ""),
				Integer.parseInt(request.getParameter("wednesday") + ""),
				Integer.parseInt(request.getParameter("thursday") + ""),
				Integer.parseInt(request.getParameter("friday") + ""),
				Integer.parseInt(request.getParameter("saturday") + ""),
				Integer.parseInt(request.getParameter("sunday") + ""),
				curveid
				);
		if( l != null && l.size() != 0 )return true;
		return false;
	}
	@Override
	public boolean queryClockRepeatForApp(String deviceId,String clocking, HttpServletRequest request) {
		List<Clocksetting> l  = RairconCurveSettingDAO.queryCurveRepeat(deviceId,
				Integer.parseInt(request.getParameter("monday") + ""),
				Integer.parseInt(request.getParameter("tuesday") + ""),
				Integer.parseInt(request.getParameter("wednesday") + ""),
				Integer.parseInt(request.getParameter("thursday") + ""),
				Integer.parseInt(request.getParameter("friday") + ""),
				Integer.parseInt(request.getParameter("saturday") + ""),
				Integer.parseInt(request.getParameter("sunday") + ""),
				clocking
				);
		if( l != null && l.size() != 0 )return true;
		return false;
	}
	@Override
	public void saveRepeatPopupForApp(String curveid, HttpServletRequest request) {
		// TODO Auto-generated method stub
		RairconSetting RairconSetting = RairconCurveDAO.findOne(curveid);
		RairconSetting.setMonday(Integer.parseInt(request.getParameter("monday")+""));
		RairconSetting.setTuesday(Integer.parseInt(request.getParameter("tuesday")+""));
		RairconSetting.setWednesday(Integer.parseInt(request.getParameter("wednesday")+""));
		RairconSetting.setWednesday(Integer.parseInt(request.getParameter("thursday")+""));
		RairconSetting.setFriday(Integer.parseInt(request.getParameter("friday")+""));
		RairconSetting.setSaturday(Integer.parseInt(request.getParameter("saturday")+""));
		RairconSetting.setSaturday(Integer.parseInt(request.getParameter("sunday")+""));
		RairconCurveDAO.save(RairconSetting);
	}
	/**
	 * 更新曲线表节点数据(手机端)
	 */
	@Transactional
	@Override
	public boolean updateClock(String clockId,String userId, Map<String, Object> m) {
		// TODO Auto-generated method stub
		Clocksetting clocksetting = (Clocksetting)RairconCurveSettingDAO.findOne(clockId);
		RairconSetting rairconSetting = RairconCurveDAO.findOne(clocksetting.getRairconSetting().getId());
		SpmsAirCondition spmsDevice = new SpmsAirCondition();
		if(clocksetting.getSpmsDevice()!=null){
			spmsDevice = spmsAcDAO.findOne( clocksetting.getSpmsDevice().getId() );
			clocksetting.setSpmsDevice(spmsDevice) ;//设备表
		}
		clocksetting.setRairconSetting(rairconSetting);//曲线表
		clocksetting.setTemp(Integer.parseInt(m.get("temp")+"")) ;//温度
		clocksetting.setClocking(m.get("clocking")+"");//设置时间
		clocksetting.setOn_off(Integer.parseInt(m.get("on_off")+"")) ;//开关(1：开启；0：关闭)
		clocksetting.setWindspeed(Integer.parseInt(m.get("windspeed")+"")) ;//风速 (0,1,2......6)d
		clocksetting.setMode(Integer.parseInt(m.get("mode")+"")) ;//空调模式(制冷:1，制热:2，送风:3，除湿:4，自动:0)
		clocksetting.setStartend(Integer.parseInt(m.get("startend")+""));//表示当前定时设置启用还是未启用……
		clocksetting.setSpmsUser(spmsUserDAO.findOne(userId));
		clocksetting.setAlone(0);//表示当前定时设置是否为独立的……
		RairconCurveSettingDAO.save(clocksetting);
		if(clocksetting.getSpmsDevice() != null) sendMessage.sendCurveMessage(clocksetting.getSpmsDevice().getId(),userId);	
		return false;
	}
	@Override
	public RairconSetting QueryCurveById(String curveId) {
		// TODO Auto-generated method stub
		return RairconCurveDAO.findOne(curveId);
	}
	/**
	 * 加载所有曲线图
	 */
	@Override
	public List<RairconSetting> QueryRairconCurveAll() {
		// TODO Auto-generated method stub
		String mobile=UserUtil.getCurrentUser().getMobile();
		String userId = "";
		if(mobile != null){
			SpmsUser user=spmsUserDAO.getByMobile(mobile);
			userId = user.getId();
		}
		return RairconCurveDAO.findAllByUser(userId);
	}
	@Override
	public long QueryRairconCurveAllNum() {
		// TODO Auto-generated method stub
		String mobile=UserUtil.getCurrentUser().getMobile();
		String userId = "";
		if(mobile != null){
			SpmsUser user=spmsUserDAO.getByMobile(mobile);
			userId = user.getId();
		}
		return RairconCurveDAO.countForUser(userId);
	}
	@Override
	public SpmsUser querySpmsUserById(String userId) {
		// TODO Auto-generated method stub
		return spmsUserDAO.findOne(userId);
	}
	/**
	 * 覆盖曲线重复设置
	 */
	@Override
	public void CoverCurveRepeat(String deviceId, String curveId, Map mm) {
		// TODO Auto-generated method stub
		int monday = Integer.parseInt(mm.get("monday") + "");
		int tuesday = Integer.parseInt(mm.get("tuesday") + "");
		int wednesday = Integer.parseInt(mm.get("wednesday") + "");
		int thursday = Integer.parseInt(mm.get("thursday") + "");
		int sunday = Integer.parseInt(mm.get("sunday") + "");
		int friday = Integer.parseInt(mm.get("friday") + "");
		int saturday = Integer.parseInt(mm.get("saturday") + "");
		List<deviceCurve> l  = deviceCurveDAO.querydeviceCurveForDeviceId(deviceId,
				monday,
				tuesday,
				wednesday,
				thursday,
				friday,
				saturday,
				sunday,
				curveId
				);
		for (deviceCurve deviceCurve : l) {
			deviceCurve.setMonday( monday == 1 ? 0 : deviceCurve.getMonday() != null ? deviceCurve.getMonday() : 0);
			deviceCurve.setTuesday( tuesday == 1 ? 0 : deviceCurve.getTuesday() != null ? deviceCurve.getTuesday() : 0);
			deviceCurve.setWednesday( wednesday == 1 ? 0 : deviceCurve.getWednesday() != null ? deviceCurve.getWednesday() : 0);
			deviceCurve.setThursday( thursday == 1 ? 0 : deviceCurve.getThursday() != null ? deviceCurve.getThursday() : 0);
			deviceCurve.setFriday( friday == 1 ? 0 : deviceCurve.getFriday() != null ? deviceCurve.getFriday() : 0);
			deviceCurve.setSaturday( saturday == 1 ? 0 : deviceCurve.getSaturday() != null ? deviceCurve.getSaturday() : 0);
			deviceCurve.setSunday( sunday == 1 ? 0 : deviceCurve.getSunday() != null ? deviceCurve.getSunday() : 0);
			deviceCurveDAO.save(deviceCurve);
		}
	}
	/**
	 * 覆盖--定时重复设置
	 */
	@Transactional
	@Override
	public void updateClockForRepeat(Clocksetting c , String deviceId, String clocking ,Map mm) {
		// TODO Auto-generated method stub
		int monday = Integer.parseInt(mm.get("monday") + "");
		int tuesday = Integer.parseInt(mm.get("tuesday") + "");
		int wednesday = Integer.parseInt(mm.get("wednesday") + "");
		int thursday = Integer.parseInt(mm.get("thursday") + "");
		int sunday = Integer.parseInt(mm.get("sunday") + "");
		int friday = Integer.parseInt(mm.get("friday") + "");
		int saturday = Integer.parseInt(mm.get("saturday") + "");
		List<Clocksetting> l  = RairconCurveSettingDAO.queryCurveRepeat(deviceId,
				monday,
				tuesday,
				wednesday,
				thursday,
				friday,
				saturday,
				sunday,	
				c.getClocking(),
				c.getId()
				);
		for (Clocksetting clocksetting : l) {
			clocksetting.setMonday( monday == 1 ? 0 : clocksetting.getMonday() != null ? clocksetting.getMonday() : 0);
			clocksetting.setTuesday( tuesday == 1 ? 0 : clocksetting.getTuesday() != null ? clocksetting.getTuesday() : 0);
			clocksetting.setWednesday( wednesday == 1 ? 0 : clocksetting.getWednesday() != null ? clocksetting.getWednesday() : 0);
			clocksetting.setThursday( thursday == 1 ? 0 : clocksetting.getThursday() != null ? clocksetting.getThursday() : 0);
			clocksetting.setFriday( friday == 1 ? 0 : clocksetting.getFriday() != null ? clocksetting.getFriday() : 0);
			clocksetting.setSaturday( saturday == 1 ? 0 : clocksetting.getSaturday() != null ? clocksetting.getSaturday() : 0);
			clocksetting.setSunday( sunday == 1 ? 0 : clocksetting.getSunday() != null ? clocksetting.getSunday() : 0);
			RairconCurveSettingDAO.save(clocksetting);
			//删除重复设置为空的
			boolean cr = false;
			if( clocksetting.getMonday() != null && clocksetting.getMonday() != 0 ) cr = true;
			if( clocksetting.getTuesday() != null && clocksetting.getTuesday() != 0 ) cr = true;
			if( clocksetting.getWednesday() != null && clocksetting.getWednesday() != 0 ) cr = true;
			if( clocksetting.getThursday() != null && clocksetting.getThursday() != 0 ) cr = true;
			if( clocksetting.getFriday() != null && clocksetting.getFriday() != 0 ) cr = true;
			if( clocksetting.getSaturday() != null && clocksetting.getSaturday() != 0 ) cr = true;
			if( clocksetting.getSunday() != null && clocksetting.getSunday() != 0 ) cr = true;
			if( ! cr ){
				RairconCurveSettingDAO.delete(clocksetting);
			}
		}
	}
	@Override
	public long QueryRairconClockNum(String deviceId) {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.getRairconClockCount(deviceId);
	}
	@Override
	public long QueryRairconClockAllNum() {
		// TODO Auto-generated method stub
		return RairconCurveSettingDAO.count();
	}
	/**
	 * 温度判断读取曲线节点
	 */
	@Override
	public boolean tempJudge(String curveId, String minTemps, String maxTemps) {
		// TODO Auto-generated method stub
		long r1 = RairconCurveSettingDAO.queryClocksCountByCurveId(  curveId,Integer.parseInt(minTemps),Integer.parseInt(maxTemps)  );
		long r2 = RairconCurveSettingDAO.queryClocksCountByCurveIdAll(curveId);
		return r1 == r2;
	}
	/**
	 * 获取用户曲线数量
	 */
	@Override
	public long QueryRairconCurveNumByUserId(String minTemps,String maxTemps) {
		// TODO Auto-generated method stub
		List<RairconSetting> RairconSettingList = RairconCurveDAO.getSpmsRairconCurveByUserId(sendMessage.getSpmsUserId());
		long i= 0;
		for (RairconSetting rairconSetting : RairconSettingList) {
			if(tempJudge(rairconSetting.getId(), minTemps, maxTemps)){
				i++;
			}
		}
		return i;
	}
	/**
	 * 
	 */
	@Override
	public RairconSetting addCurveTforP(String userId,String deviceId) {
		RairconSetting rairconSetting = new RairconSetting();
		rairconSetting.setUser( UserUtil.getCurrentUser() );
		if( deviceId!=null && !"".equals(deviceId) )
		rairconSetting.setSpmsDevice( spmsAcDAO.findOne(deviceId) );
		
		/*if( deviceId!=null && !"".equals(deviceId) )
		rairconSetting.setSpmsDevice( spmsDeviceDAO.findOne(deviceId) );*/
		
		/*String mobile = UserUtil.getCurrentUser().getMobile();
		if( mobile != null ){
			rairconSetting.setSpmsUser( spmsUserDAO.findOne(userId) );
		}*/
		rairconSetting.setSpmsUser( spmsUserDAO.findOne(userId) );
		RairconCurveDAO.save( rairconSetting );
		if(deviceId!=null)
		sendMessage.sendCurveMessage( deviceId , rairconSetting.getSpmsUser().getId() );
		return rairconSetting;
	}
	@Override
	public List<deviceCurve> queryDeviceCurveForCurveId(String curveId,String deviceId) {
		// TODO Auto-generated method stub
		return deviceCurveDAO.queryDeviceCurveForCurveId(curveId,deviceId);
	}
	/**
	 * 查询舒适曲线（手机端）
	 */
	@Override
	public List<RairconSetting> QueryRairconCurveForDeviceId(String deviceId,String userId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> rl = queryDAO.getMapObjects("select id from spms_raircon_setting where id in ( select ta.id from (SELECT "+
			"t1.id,count(1) count "+
			"FROM "+
				"spms_raircon_setting t1, "+
				"rairconcurve_clocksetting t5, "+
				"spms_user_product_binding t3, "+
				"spms_product_type t4 "+
			"WHERE "+
			"t3.producttype_id = t4.id "+
			"AND t1.id = t5.raircon_Setting_id "+
			"AND ( ( t5.Temp >= t4.zhiLengMix AND t5.Temp <= t4.zhiReMax ) or ( t5.on_off = 0 )  )"+
			"AND t3.device_id = '"+deviceId+"'  "+
			"and t1.spms_userId = '"+userId+"'  "+
			" group by t1.id ) ta, "+
			"(select t6.id,COUNT(t7.id) count from spms_raircon_setting t6,rairconcurve_clocksetting t7 where t6.id = t7.raircon_Setting_id group by t6.id) tb "+
			"where ta.id = tb.id and ta.count = tb.count  ) order by createDate desc");
		List<RairconSetting> rrl = new ArrayList<RairconSetting>();
		for (Map<String, Object> map : rl) {
			RairconSetting rairconSetting = new RairconSetting();
			rairconSetting.setId(map.get("id")+"");
			rrl.add(rairconSetting);
		}
		return rrl;
	}
	/**
	 * 查询设备应用的舒适曲线数量
	 */
	@Override
	public long QueryCurveNumForCurveDevice(String deviceId, String minTemp, String maxTemp) {
		// TODO Auto-generated method stub
		return deviceCurveDAO.queryByDeviceId(deviceId,minTemp,maxTemp);
	}
}
