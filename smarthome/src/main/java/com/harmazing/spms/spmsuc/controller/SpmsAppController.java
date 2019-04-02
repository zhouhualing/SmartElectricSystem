package com.harmazing.spms.spmsuc.controller;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.harmazing.spms.base.manager.UserManager;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.JsonObject;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.spmsuc.Manager.SpmsAppManager;
import com.harmazing.spms.user.dto.SpmsUserDTO;
import com.harmazing.spms.user.manager.SpmsUserManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.JDomXml;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.entity.UserEntity;



@Controller
@RequestMapping("/app")
public class SpmsAppController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private SpmsUserManager spmsUserManager;
	
	@Autowired
	private SpmsAppManager spmsAppManager;
	@Autowired
	private QueryDAO queryDao;
	/**
     * 手机用户登录请求
     * @param {username:XXX,password:XXX}
     * @return          以JSON格式返回登录结果
     *                  成功:{
     *                      "flag":1,
     *                      "userId":"用户ID",
     *                      "name":"用户名",
     *                      "mobile":"手机号",
     *                      "type":类型(1试用 2商用),
     *                      "bizArea":"业务区域",
     *                      "eleArea":"用电区域",
     *                      "address":"地址",
     *                      "email":"邮件",
     *                      "ammeter":"电表号",
     *                      "gwId":"网关ID"
     *                  }
     *                  用户名错误:{"flag":0,"msg":"用户名错误"}
     *                  密码错误:{"flag":0,"msg":"密码错误"}
     */
	@RequestMapping(value = "login")
    @ResponseBody
	public Map<String,Object> login(@RequestParam(value="username") String username,@RequestParam(value="password") String password,HttpServletRequest request, HttpServletResponse response){
		String userCode = username;
		String passWord = password;
		Map<String, Object> result = Maps.newHashMap();
		result.put("flag", 0);
		UserEntity ue = userManager.getByUserCode(userCode);
		if (ue != null) {
			if (passWord.equals(ue.getPassword())) {
				SpmsUserDTO spmsUser = spmsUserManager.getByMobile(ue.getUserCode());
				result.put("flag", 1);
				result.put("userId", String.valueOf(spmsUser.getId()));
				result.put("name", spmsUser.getFullname());
				result.put("mobile", spmsUser.getMobile());
				result.put("type", spmsUser.getType());
				result.put("address", spmsUser.getAddress());
				result.put("email", spmsUser.getEmail());
				result.put("status", ue.getStatus());
				// remove gateway info

				// if(spmsUser.getSpmsDevice()!=null){
				// result.put("gwId",String.valueOf(spmsUser.getSpmsDevice()));
				// }else{
				// result.put("gwId","");
				// }
			} else {
				result.put("msg", "密码错误");
			}
		} else {
			result.put("msg", "帐号错误");
		}
		return result;
	}
	
	/**
     * 获取用户的所有设备与设备最新状态
     * @param {userId:XXX}    用户ID
     * @return      设备数据
     *              {
     *                  "id":设备ID,
                        "gwId":网关ID,
                        "name":设备名称,
                        "type":设备类型(2空调，3门，4窗)，
                        "state":设备状态(0关，1开，2异常)，
                        "temp":室内温度,
                        "acTemp":空调温度,
                        "mode":空调模式,
                        "remain":电池电量,
                        
                        "maxTemp":温度上限,
                        "minTemp":温度下限,
                        "allowHeat":1有制热模式、0没有制热模式
                        "errorCode":异常代码
                        “errorDetail”:异常描述
                        "clock":定时器应用情况
     *              }
     */
	@RequestMapping(value = "index")
    @ResponseBody
	public List<Map<String,Object>> index(@RequestParam(value="userId") String userId,HttpServletRequest request, HttpServletResponse response){
		return spmsAppManager.getDeviceStatusByUser(userId);
	}
	
	/**
     * 获取设备当前状态
     * @param deviceId      设备ID
     * @param gwId          网关ID
     * @param request
     * @param response
     * @return      设备最新数据
     *              空调：
     *              {
     *                  "success":true/false(请求成功或失败),
                        "status":0离线，1在线，2异常,
                        "onOff":0关，1开,
                        "temp":室内温度,
                        "acTemp":空调温度,
                        "mode":空调模式,
                        "speed":空调风速,
                        "deviceId":设备ID,
                        "type":设备类型,
                        
                        "maxTemp":温度上限,
                        "minTemp":温度下限,
                        "allowHeat":1有制热模式、0没有制热模式
     *              }
     *
     *
     *              门窗：
     *              {
     *                  "success":true/false(请求成功或失败),
                        "status":0离线，1在线，2异常,
                        "onOff":0关，1开,
                        "remain":电池电量,
                        "deviceId":设备ID,
                        "type":设备类型
     *              }
     *
     *              请求数据失败:
     *              {
     *                  "success":true/false(请求成功或失败),
                        "deviceId":设备ID
     *              }
     *
     */
	@RequestMapping(value = "deviceData")
    @ResponseBody
    public List<Map<String,Object>> deviceData(@RequestParam(value="deviceId") String[] deviceId,@RequestParam(value="gwId") String gwId,HttpServletRequest request, HttpServletResponse response){
		return spmsAppManager.getDevicesCurrentStatusByApp(gwId, deviceId);
	}
	 /**
     * 根据空调ID获取空调用电记录
     * @param deviceId 空调ID
     * @param request
     * @param response
     * @return  空调用电记录
     *          [
     *              [时间，功率]
     *          ]
     */
	@RequestMapping(value = "acEleRecord")
    @ResponseBody
    public List<Object[]> acEleRecord(@RequestParam(value="deviceId") String deviceId,@RequestParam(value="type")String type,@RequestParam(value="rbtn")String rbtn,HttpServletRequest request,HttpServletResponse response) throws ParseException{
    	if(null != type && "1".equals(type)){
    		return spmsAppManager.aceEleRecord(deviceId);
    	}else{
    		return spmsAppManager.aceEleRecord1(deviceId,rbtn);
    	}
    }
    /**
     * 根据空调的ID和最后时间来获取最新的用电记录
     * @param deviceId 空调ID
     * @param endTime 最后时间的时间戳
     * @param request
     * @param response
     * @return 空调用电记录
     * 			[
     *              [时间，功率]
     *          ]
     */
    @RequestMapping(value = "acNewEleRecord")
    @ResponseBody
    public List<Object[]> acNewElRecord (@RequestParam(value="deviceId")String deviceId ,@RequestParam(value="rbtn") String rbtn,@RequestParam(value="endTime")String endTime,@RequestParam(value="type")String type,HttpServletRequest request,HttpServletResponse response) throws ParseException{
    	
    	String n = ( "null".equals(endTime) || endTime == null || "".equals(endTime)) ? "0":endTime;
    	
    	if(null != type && "1".equals(type)){
    		return  spmsAppManager.acNewElRecord(deviceId, Long.valueOf(n));
    	}else{
    		return  spmsAppManager.acNewElRecord1(deviceId, Long.valueOf(n),rbtn);
    	}
    }
    
    
    /**
     * 根据传感器ID获取传感器开关记录
     * @param deviceId 传感器ID
     * @param request
     * @param response
     * @return  传感器开关记录
     *          [
     *              [时间，开关状态(0关，1开)]
     *          ]
     */
    @RequestMapping(value = "getSensorOnOffState")
    @ResponseBody
    public List<Object[]> getSensorOnOffState(@RequestParam(value="deviceId") String deviceId,HttpServletRequest request,HttpServletResponse response) throws ParseException{
    	return spmsAppManager.getSensorOnOffState(deviceId);
    }
    /**
     * 根据传感器ID获取传感器最新开关信息
     * @param deviceId 传感器ID
     * @param request
     * @param response
     * @return  传感器开关记录
     *      
     *              [时间，开关状态(0关，1开)]
     *    
     */
    @RequestMapping(value = "getNewSensorOnOffState")
    @ResponseBody
    public Object[] getNewSensorOnOffState(@RequestParam(value="deviceId") String deviceId,HttpServletRequest request,HttpServletResponse response) throws ParseException{
    	return spmsAppManager.getNewSensorOnOffState(deviceId);
    }
    
    /**
     * 改变空调状态
     * @param gwId      	网关ID
     * @param deviceId  	空调ID
     * @param commandType   调整状态
	     * 						ON = 开;
	        					OFF = 关;
	        					MODE_SET = 模式;
	        					FAN_SET = 风速;
	        					TEMP_SET = 温度;
     * @param value     	最新状态
     * @return				是否调整成功
     * 					成功:
     * 					{
     * 						"success":true,
                         	"status":运行状态(0异常，1正常),
                         	"onOff":开关状态(0关，1开),
                         	"temp":室温,
                         	"acTemp":空调温度,
                         	"mode":模式,
                         	"speed":风速,
                         	"deviceId":设备ID
     * 					}
     * 					失败:
     * 					{
     * 						"success":false,
                         	"msg":"服务器端出错"
     * 					}
     */
    @RequestMapping(value = "changeAcState")
    @ResponseBody
    public Map<String,Object> changeAcState(@RequestParam(value="deviceId") String deviceId,@RequestParam(value="gwId") String gwId,@RequestParam(value="commandType") String commandType,@RequestParam(value="value") String value,HttpServletRequest request,HttpServletResponse response){
    	return spmsAppManager.changeAcStatus(gwId,deviceId,commandType,Integer.valueOf(value));
    }
    
    /**
     * 更改设备名称
     * @param deviceId
     * @param newName 新的设备名
     * @return 反馈状态
     */
//    @RequestMapping(value = "changeDeviceName")
//    @ResponseBody
//    public Map<String ,Object> changeDeviceName(@RequestParam(value="deviceid") String deviceId,@RequestParam(value="newName") String newName,HttpServletRequest request,HttpServletResponse response){
//    	return spmsAppManager.changeDeviceName(deviceId,newName);
//    }
    
    /**
     * 更改用户密码
     * @param oldpwd 旧的密码
     * @param newpwd 新的密码
     * @return  反馈状态
     */
    @RequestMapping(value = "changeUserPwd")
    @ResponseBody
    public Map<String,Object> changeUserPwd(@RequestParam(value="username") String username,@RequestParam(value="oldpwd")String oldpwd, @RequestParam(value="newpwd")String newpwd,HttpServletRequest request,HttpServletResponse response){
    	String userCode = username;
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	UserEntity ue = userManager.getByUserCode(userCode);
    	if(!StringUtil.isNUll(oldpwd) && !StringUtil.isNUll(newpwd)){
    		if(oldpwd.equals(ue.getPassword())){
    			ue.setPassword(newpwd);
    			userManager.saveChange(ue);
    			result.put("success", true);
				result.put("msg", "修改密码成功");
    		}else{
				result.put("success", false);
				result.put("msg", "修改密码失败，旧密码错误");
			}
    	}else{
    		result.put("success", false);
			result.put("msg", "参数不全");
    	}
    	
    	return result;
    }
    /**
     * 添加设备
     */
    @RequestMapping(value = "addDevice")
    @ResponseBody
    public String addDevice(String usrId,String deviceMac){
    	String res="ok";
//    	try{
	    	//String sql="select id ,type from spms_device where mac='"+deviceMac+"' and status=1";
	    	String sql="select id ,type,status,storage from spms_device where mac='"+deviceMac+"' or sn='"+deviceMac+"'";
	    	List lst=queryDao.getMapObjects(sql);
	    	
	    	if(lst!=null && lst.size()>0 && ((Map)lst.get(0)).get("id")!=null){
	    		Map m=(Map) lst.get(0);
	    		String deviceId=(String) m.get("id");
	    		String typeId=m.get("type").toString();
	//    		if(typeId.equals("1")){
	//    			res="系统不支持此类设备";
	//    			return res;
	//    		}
	    		
	    		Integer  status=(Integer) m.get("status");
	    		Integer  storage=(Integer) m.get("storage");
	    		
	    		if(status!=1 || storage ==5){
	    			res="该设备已绑定用户,不能添加";
	    			return res;
	    		}
	    		
	    		sql="select gw_id gwid from spms_user where id='"+usrId+"'";
	    		String gwId="";
	    		List ll=queryDao.getMapObjects(sql);
	    		if(ll!=null && ll.size()>0){
	    			Map map=(Map) ll.get(0);
	    			gwId=(String) map.get("gwid");
	    		}
	    		if(typeId.equals("1")){
	    			if(gwId!=null && gwId!=""){
	        			res="该用户已经绑定网关，不能再进行网关绑定";
	        			return res;
	        		} else{
	        			sql="update spms_user set gw_id='"+deviceId+"' where user_id ='"+usrId+"'";
	        			queryDao.doExecuteSql(sql);
	        		}  
	    		}else{
	    		
	    		
		    		if(gwId==null || gwId==""){
		    			res="该用户未绑定网关不能添加设备";
		    			return res;
		    		}   
		    		//sql="select product_id, producttype_id from spms_user_product_binding where user_id='"+usrId+"'";
		    		sql="select id, type_id from spms_product where status ='1' and  user_id ='"+usrId+"'";
		    		ll=queryDao.getMapObjects(sql);
		    		String productId="";
		    		String productTypeId="";
		    		if(ll!=null && ll.size()>0){
						int count = 0;
						int num=0;
						Map map = (Map) ll.get(0);
						productId = (String) map.get("id");
						productTypeId = (String) map.get("type_id");
						sql = "select count(device_id) num from spms_user_product_binding where user_id='"
								+ usrId
								+ "'  and deviceType='"
								+ typeId
								+ "' and product_id='" + productId + "'";
						List cl = queryDao.getMapObjects(sql);
						if (cl != null && cl.size() > 0) {
							Map map1 = (Map) cl.get(0);
							num = ((BigInteger) map1.get("num")).intValue();
						}
						sql = "select chuangGanCount,kongTiaoCount from spms_product_type where id='"
								+ productTypeId + "'";
						List queryList = queryDao.getMapObjects(sql);
						if (queryList != null && queryList.size() > 0) {
							Map pmap = (Map) queryList.get(0);
							if (typeId.equals("2")) {
								count = (Integer) pmap.get("kongTiaoCount");
							}
							if (typeId.equals("3")) {
								count = (Integer) pmap.get("chuangGanCount");
							}
						}
						if (count - num < 1) {
							res = "已达到最大绑定设备数";
							return res;
						}
						
		    		} else{
		    			res="该用户未订购产品不能添加设备";
		    			return res;
		    		}   		
		    		
		    		
	    			//获取当前时间
	    			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	    			String currentDate=df.format(new Date());
	    			
	    			sql="insert into spms_user_product_binding "
							+ "(id,createDate,deviceType,displayHomepage,gwId,device_id,product_id,producttype_id,user_id,device_type,version)"
							+ "values ("
							+ "'"+UUID.randomUUID().toString()+"',"
							+ "'"+currentDate+"',"
							+ "'"+typeId+"',"
							+ "'"+1+"',"
							+ "'"+gwId+"',"
							+ "'"+deviceId+"',"
							+ "'"+ productId+"',"
							+ "'"+productTypeId+"',"
							+ "'"+usrId+"',"
							+ "'"+typeId+"',"
							+ "0"
							+ ")";
	    			
	    			queryDao.doExecuteSql(sql);
	    			sql = "update spms_device set status=2,storage=5 where id='"+deviceId+"'";
	    			queryDao.doExecuteSql(sql);
	    			
	    			/***发送服务更新命令*****/
	    			
	    			Map mes=new HashMap();
	    			mes.put("userId",usrId);
	    			mes.put("commandType",0);
	    			mes.put("messageType","SERVICEUPDATE");
	    			CommandUtil.asyncSendMessage(mes);
		    		
	    		}
	    	}else{
	    		res="系统不存在此设备";
	    	}
//    	}catch(Exception e){
//    		res="系统出错";
//    		e.printStackTrace();
//    	}
    	return res;    	
    }
    /**
     * 打开网络
     */
    @RequestMapping(value = "openNetWork")
    @ResponseBody
    public boolean openNetWork(String usrId){    	
    	boolean suc=true;
    	try{
	    	Map message=new HashMap();
			message.put("userId", usrId);
			message.put("messageType", "GATEWAYCONTROL");
			CommandUtil.asyncSendMessage(message);
    	}catch(Exception e){
    		suc=false;
    		e.printStackTrace();
    	}
    	return suc;
    }
	/**
	 * 获取天气
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "getWeather")
    @ResponseBody
    public Map<String,Object> getWeather(HttpServletRequest request){
    	String ip = request.getRemoteHost();//"114.111.167.209";//
    	Map<String,Object> rm = new HashMap<String,Object>();
    	try {
    		//根据IP获取城市名称 在根据城市名称获取城市编号
    		String cs = sendMessage.doGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="+ip);
			//System.out.println(cs);
			JSONObject jsonObject = JSONObject.fromObject(cs);
			Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);  
			//String city = mapJson.get("city")+"";
			//http://www.weather.com.cn/data/cityinfo/101010100.html
			//根据城市编号获取城市天气信息
			String s = sendMessage.doGet("http://www.weather.com.cn/data/cityinfo/"+sendMessage.getCityCode(mapJson.get("city")+"")+".html");
			JSONObject jsonObject2 = JSONObject.fromObject(s);
			rm = (Map<String,Object>)jsonObject2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			rm.put("result", "error");
			e.printStackTrace();
			return rm;
		}//获取天气
		return rm;
    }
}
