package com.harmazing.spms.spmsuc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.UserManager;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.spmsuc.Manager.SpmsUcManager;
import com.harmazing.spms.spmsuc.dto.SpmsUserDataDTO;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dto.SpmsUserDTO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.manager.SpmsUserManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;



/**
 * 用户PC端controller
 * 
 * @author yyx
 * 
 */
@Controller
@RequestMapping("/spmsuc")
public class SpmsUcController {
	@Autowired
	private SpmsUserManager spmsUserManager;
	@Autowired
	private SpmsUcManager spmsUcManager;
	@Autowired
	private UserManager userManager;
	
	/**
	 * 取得当前用户的所有设备数据
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/getIndexData")
	@ResponseBody
	public Map<String, Object> getIndexData() throws ParseException {
		Map<String , Object> result = Maps.newHashMap();
		SpmsUserDAO userDao=SpringUtil.getBeanByName("spmsUserDAO");
		String mobile=UserUtil.getCurrentUser().getMobile();
		SpmsUser spmsuser = null;
		if(mobile != null){
			spmsuser=userDao.getByMobile(mobile);
		}else{
			return sendMessage.reusltMap(false);
		}
		SpmsUserDTO spmsUserDTO=new SpmsUserDTO();
		if(spmsuser!=null){
			BeanUtils.copyProperties(spmsuser, spmsUserDTO);
			spmsUserDTO.setTypeText(spmsuser.getType()==1 ? "试用":"商用"  );
		}
		//System.out.println("查询之前：当前系统时间---------"+sendMessage.getTime());
		List<SpmsUserDataDTO> datas = spmsUcManager.findUserData(spmsuser); //所有设备数据
		//System.out.println("查询之后：当前系统时间：---------"+sendMessage.getTime());
		//String weatherInform = spmsUcManager.getWeatherInform(spmsuser.getBizAreaName()); //当前的用户所在市的温度
		//Integer gwStatus = spmsUcManager.getGwJoinStatus(spmsuser.getSpmsDevice()); //用户绑定网关的状态
		List<SpmsDeviceDTO> deviceDTOs = spmsUserManager.getUserDevices(mobile, EnumTypesConsts.DeviceType.Dev_Type_All);

		//TODO
		//Business should be changed here
		if(deviceDTOs!=null && deviceDTOs.size()>0){
			result.put("gwStatus", deviceDTOs.get(0).getOperStatus());
			result.put("gwId", deviceDTOs.get(0).getId());
			result.put("isDevice", 1);
		}else{
			result.put("isDevice", 0);
		}
		result.put("datas", datas);
		result.put("spmsuser", spmsUserDTO);
		result.put("weatherInform", null);
		return result;
	}
	/**
	 * 取得用电信息
	 * @throws ParseException 
	 */
	@RequestMapping("/getDeviceExpend")
	@ResponseBody
	public  Map<String, Object> getDeviceExpend(@RequestBody Map<String,Object> m) throws ParseException{
		Map<String , Object> result = new HashMap<String , Object>();
		if(null != m.get("type") && "1".equals(m.get("type"))){
			result = spmsUcManager.getDeviceExpend(m);
		}else{
			result = spmsUcManager.getDeviceExpend1(m);
		}
		return result;
	} 
	
	/**
	 * 取得用户的个人信息
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "userInfo")
	@ResponseBody
	public SpmsUserDTO userInfo(@RequestBody Map<String,Object> info){
		return spmsUserManager.getByUserid(info.get("userid").toString());
	}
	
	/**
	 * 修改设备的自定义名称
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "changeCustomName")
	@ResponseBody
	public Map<String, Object> changeCustomName(@RequestBody Map<String,Object> info){
		String newname = (String) info.get("newname");

		return spmsUcManager.changeCustomName(newname, info.get("deviceid").toString());
	}

	
	/**
	 * 关闭空调
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "deviceClose")
	@ResponseBody
	public Map<String, Object> deviceClose(@RequestBody Map<String,Object> info){
		Map<String, Object> result = new HashMap<String, Object>();
		result = spmsUcManager.acOffCommand(info.get("gwid").toString(), info.get("deviceid").toString());
		return result;
	}
	
	/**
	 * 开空调
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "deviceOpen")
	@ResponseBody
	public Map<String, Object> deviceOpen(@RequestBody Map<String,Object> info){
		Map<String, Object> result = spmsUcManager.acOnCommand(info.get("gwid").toString(), info.get("deviceid").toString());
		return result;
	}
	
	/**
	 * 设置温度
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "setAcTemp")
	@ResponseBody
	public  Map<String, Object> setAcTemp(@RequestBody Map<String,Object> info){
		int newtemp = (int) info.get("newtemp");
		int oldtemp = (int) info.get("oldtemp");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result = spmsUcManager.changeAcStatus(info.get("gwid").toString(),info.get("deviceid").toString(), CommandUtil.CommandType.TEMP_SET,newtemp);
		return result;
	}
	
	
	/**
	 * 设置风速
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "setAcFs")
	@ResponseBody
	public  Map<String, Object> setAcFs(@RequestBody Map<String,Object> info){
		int newfs = (int) info.get("newfs");
		int oldfs = (int) info.get("oldfs");
		if(newfs == 7){
        	newfs = 0;
        }
		Map<String, Object> result = new HashMap<String, Object>();
		result= spmsUcManager.changeAcStatus(info.get("gwid").toString(),info.get("deviceid").toString(), CommandUtil.CommandType.FAN_SET,newfs);
		return result;
	}
	/**
	 * 修改空调模式
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "setAcMs")
	@ResponseBody
	public  Map<String, Object> setAcMs(@RequestBody Map<String,Object> info){
		int newms = (int) info.get("newms");
		//int oldms = (int) info.get("oldms");
		String gwid = (String) info.get("gwid");
		String deviceid = (String) info.get("deviceid");
		Map<String, Object> result = new HashMap<String, Object>();
	    result = spmsUcManager.changeAcStatus(gwid,deviceid, CommandUtil.CommandType.MODE_SET,newms);
	    return result;
	}
	
	/**
	 * 更新所有设备数据
	 * @param {{gwid:XXX,gwmac,lasttime:XXX},{....},{....}}
	 * @return
	 */
	@RequestMapping(value = "getDevicesCurrentStatus")
    @ResponseBody
	public Map<String,Object> getDevicesCurrentStatus(@RequestBody Map<String,Object> info){
		Map<String,Object> result = Maps.newHashMap();
		result = spmsUcManager.getDevicesCurrentStatus(info);
		return result;
	}
	
	
	@RequestMapping(value = "modifyPwd")
	@ResponseBody
	public Map<String, Object> modifyPwd(@RequestBody Map<String,Object> info){
		Map<String, Object> result = new HashMap<String, Object>();
		UserEntity ue = UserUtil.getCurrentUser();
		String oldpwd = (String)info.get("oldpwd");
		String newpwd = (String)info.get("newpwd");
		if (!newpwd.equals("") && newpwd !=null && !oldpwd.equals("") && oldpwd !=null){
			if (oldpwd.equals(ue.getPassword())){
				ue.setPassword(newpwd);
    			userManager.saveChange(ue);
    			result.put("success", true);
				result.put("msg", "修改密码成功");
			}else{
				result.put("success", false);
				result.put("msg", "修改密码失败，旧密码错误");
			}
		}
		return result;
	}
}
