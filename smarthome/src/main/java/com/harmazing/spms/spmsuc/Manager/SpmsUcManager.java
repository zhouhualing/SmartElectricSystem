package com.harmazing.spms.spmsuc.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.MongoUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.device.dao.*;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.entity.SpmsDeviceData;
import com.harmazing.spms.device.entity.SpmsGateway;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.spmsuc.Manager.SpmsUcManager;
import com.harmazing.spms.spmsuc.dto.SpmsUserDataDTO;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.manager.SpmsUserManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class SpmsUcManager implements IManager{
	@Autowired
	private SpmsUserDAO spmsUserDAO;
	@Autowired
	private SpmsAirConditionDAO spmsAcDAO;
	@Autowired
	private SpmsPirDAO spmsPirDAO;
	@Autowired
	private SpmsWinDoorDAO spmsWinDoorDAO;	
	@Autowired
	SpmsGatewayDAO spmsGatewayDAO;
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private SpmsUserManager spmsUserManager;
	@Autowired
	private SpmsDeviceManager spmsDeviceManager;

	/**
	 * 获取用户的所有设备数据
	 * @throws ParseException 
	 */
	
	public List<SpmsUserDataDTO> findUserData(SpmsUser spmsUser) throws ParseException {
		//List<SpmsUserDataDTO> result = Lists.newArrayList();
		List<SpmsUserDataDTO> result = new ArrayList<SpmsUserDataDTO>();
		//System.out.println("List<SpmsUserProductBinding> binding = spmsUserProductBindingDAO.getListByUserId(spmsUser.getId());：当前系统时间：---------"+sendMessage.getTime());
//		List<SpmsUserProductBinding> binding = spmsUserProductBindingDAO.getListByUserId(spmsUser.getId());
		//System.out.println("List<SpmsUserProductBinding> binding = spmsUserProductBindingDAO.getListByUserId(spmsUser.getId());：当前系统时间：---------"+sendMessage.getTime());
		//如果该用户没有绑定网关，则就没有对应的设备。
//		if(spmsUser.getSpmsDevice() == null || spmsUser.getSpmsDevice().getId() == null){
//			return result;
//		}
		//Integer gwStatus = (spmsDeviceDAO.findOne(spmsUser.getSpmsDevice().getId())).getOperStatus(); //取得网关的状态
		Integer gwStatus =0;
//		if(spmsUser.getSpmsDevice().getOperStatus()!=null){
//			gwStatus=spmsUser.getSpmsDevice().getOperStatus(); //取得网关的状态
//		}
//		if(  binding != null  &&  binding.size() > 0  ){
//			for(SpmsUserProductBinding bind : binding){
//				final SpmsDevice device = bind.getSpmsDevice();
//				if( device == null || device.getId() == null ){
//					continue;
//				}
//				//final SpmsDeviceData spmsDeviceData = loadDeviceNewest(bind.getSpmsDevice().getId(), null);
//				final int deviceTypeId = device.getType();
//				final String customname = bind.getCustomName();
//				//final Integer isdisplay = bind.getDisplayHomepage();// 是否在主页显示
//				if (  bind.getDisplayHomepage() == 1 ) {
//					if ( SpmsUserDataDTO.AC_TYPE.intValue() == deviceTypeId ) {
//						SpmsUserDataDTO userData = new SpmsUserDataDTO();
//						userData.setDeviceType(SpmsUserDataDTO.AC_TYPE);
//						userData.setDeviceId(device.getId());
//						userData.setSn(device.getSn());
//						userData.setMac(device.getMac());
//						userData.setStatus(gwStatus.intValue() == 1?device.getOperStatus():gwStatus);//增加设备状态信息
//						//Map<String,Object> ruleInfo = getDeviceRlueInfo(device.getId());//服务的配置信息
//						SpmsProductType SpmsProductType = bind.getSpmsProduct().getSpmsProductType();
//						/*userData.setMaxTemp((Integer)ruleInfo.get("maxTemp"));
//						userData.setMinTemp((Integer)ruleInfo.get("minTemp"));*/
//						userData.setMaxTemp(SpmsProductType.getZhiReMax());//制热最大
//						userData.setMinTemp(SpmsProductType.getZhiLengMix());//制冷最低
//						//(Integer)ruleInfo.get("allowHeat")
//						userData.setAllowHeat(SpmsProductType.getConfigurationInformation());
//						if (customname == null || customname.equals("")) {
//							userData.setDeviceName(device.getSn());
//						} else {
//							userData.setDeviceName(customname);
//						}
//						userData.setOnoffStatus(device.getOnOff());
//						userData.setRt(device.getTemp());
//						userData.setAct(device.getAcTemp());
//						userData.setMode(device.getMode());
//						userData.setSpeed(device.getSpeed());
//						/*System.out.println("原生sql：当前系统时间：---------"+sendMessage.getTime());
//						String sql = "select start_time,power from spms_ac_status where device_id = '"+ device.getId()
//								+ "' ORDER BY start_time asc";
//						List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
//						System.out.println("原生sql：当前系统时间：---------"+sendMessage.getTime());
//						//如果该用户没有绑定网关，则就没有对应的设备。
//						List<Object[]> datas = Lists.newArrayList();
//						for (Map<String,Object> map : d) {
//							int power = ((BigInteger) map.get("power")).intValue();
//							long time =  ((Date)map.get("start_time")).getTime(); 
//							Object[] data = new Object[] {time, power };
//							datas.add(data);
//						}
//						for(int i =0 ; i < d.size() ; i++){
//							long time = 0;
//							int power =0;
//							Map<String,Object> m = d.get(i);
//							power = ((BigInteger) m.get("power")).intValue();
//							//time = DateUtil.parseStringToDate(m.get("start_time").toString()).getTime();
//							time = ((Date)m.get("start_time")).getTime();
//							Object[] data = new Object[] {time, power };
//							datas.add(data);
//						}
//						userData.setDatas(datas);*/							
//						result.add(userData);
//						} else if (SpmsUserDataDTO.WIN_TYPE.intValue() == deviceTypeId || SpmsUserDataDTO.DOOR_TYPE.intValue() == deviceTypeId){
//							SpmsUserDataDTO userData = new SpmsUserDataDTO();
//							if (SpmsUserDataDTO.DOOR_TYPE.equals(deviceTypeId)) {
//								userData.setDeviceType(SpmsUserDataDTO.DOOR_TYPE);
//							} else if (SpmsUserDataDTO.WIN_TYPE.equals(deviceTypeId)) {
//								userData.setDeviceType(SpmsUserDataDTO.WIN_TYPE);
//							}
//							userData.setDeviceId(device.getId());
//							userData.setStatus(gwStatus.intValue() == 1?device.getOperStatus():gwStatus);//增加设备状态信息
//							userData.setSn(device.getSn());
//							userData.setMac(device.getMac());
//							if (customname == null || customname.equals("")) {
//								userData.setDeviceName(device.getSn());
//							} else {
//								userData.setDeviceName(customname);
//							}
//							userData.setOnoffStatus(device.getOnOff());
//							userData.setRemain(device.getRemain());
//							/*String sql = "select operate_time,operate_type from spms_win_door_status where device_id = '"
//									+ device.getId()
//									+ "' ORDER BY operate_time asc";
//							List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
//							List<Object[]> datas = Lists.newArrayList();
//							for(int i =0 ; i < d.size() ; i++){
//								long time = 0;
//								int power =0;
//								Map<String,Object> m = d.get(i);
//								power = ((Number) m.get("operate_type")).intValue();
//								time = ((Date)m.get("operate_time")).getTime();
//								Object[] data = new Object[] {
//										time, power };
//								datas.add(data);
//							}
//							for ( Map<String,Object> m : d ) {
//								int power = ((Number) m.get("operate_type")).intValue();
//								long time = ((Date)m.get("operate_time")).getTime();
//								Object[] data = new Object[] {time, power };
//								datas.add(data);
//							}
//							userData.setDatas(datas);	*/						
//							result.add(userData);
//					}
//				}
//			}
//		}
		return result;
	}
	
	public SpmsDeviceData loadDeviceNewest(String deviceId) {
		
		return loadDeviceNewest(deviceId,EnumTypesConsts.DeviceType.Dev_Type_All);
	}
	
	public SpmsDeviceData loadDeviceNewest(String deviceId,Integer devType) {
		//String sql = "select * from spms_device where id="+deviceId;
		SpmsDeviceDTO spmsDevice = spmsDeviceManager.getDeviceDTOByMac(deviceId, devType);
		if(spmsDevice == null)
			spmsDevice = spmsDeviceManager.getDeviceDTOById(deviceId, devType);
		SpmsDeviceData data = new SpmsDeviceData();
		data.setAcTemp(spmsDevice.getAcTemp());
		data.setTemp(spmsDevice.getTemp());
		data.setDeviceId(spmsDevice.getId());
		data.setMode(spmsDevice.getMode());
		data.setOnOff(spmsDevice.getOnOff());
		data.setSpeed(spmsDevice.getSpeed());
		
		//data.status=>device.operStatus
		data.setStatus(spmsDevice.getOperStatus());
		data.setType(spmsDevice.getType());
		data.setRemain(spmsDevice.getRemain());
		data.setMac(spmsDevice.getMac());
		data.setFloorTemp(spmsDevice.getFloorTemp());
		data.setUpperTemp(spmsDevice.getUpperTemp());
		return data;
	}

	
	public String getWeatherInform(String cityName) {
		// 百度天气API
				String baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location="+cityName+"&output=json&ak=SwMwinxey9LkWAAdtRVnkWXw";
				StringBuffer strBuf;

				try {
					// 通过浏览器直接访问http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ
					// 5slgyqGDENN7Sy7pw29IUvrZ 是我自己申请的一个AK(许可码)，如果访问不了，可以自己去申请一个新的ak
					// 百度ak申请地址：http://lbsyun.baidu.com/apiconsole/key
					// 要访问的地址URL，通过URLEncoder.encode()函数对于中文进行转码
					baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location="
							+ URLEncoder.encode(cityName, "utf-8")
							+ "&output=json&ak=SwMwinxey9LkWAAdtRVnkWXw";
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				strBuf = new StringBuffer();

				try {
					URL url = new URL(baiduUrl);
					URLConnection conn = url.openConnection();
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							conn.getInputStream(), "utf-8"));// 转码。
					String line = null;
					while ((line = reader.readLine()) != null)
						strBuf.append(line + " ");
					reader.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return strBuf.toString();
	}

	
	public Integer getGwJoinStatus(String gwid) {
		SpmsGateway sd = spmsGatewayDAO.findOne(gwid);
		if(sd == null || sd.getId() == null){
			sd = spmsGatewayDAO.findGwByMAC(gwid);
			if(sd == null || sd.getId() == null){
				return null;
			}
		}
		return sd.getOperStatus();
	}

	
	public Map<String, Object> changeCustomName(String newname, String deviceid) {
		Map<String, Object> result = Maps.newHashMap();
//		List<SpmsUserProductBinding> temp = spmsUserProductBindingDAO.findByDevice(deviceid);
//		if(temp == null || temp.size() ==0){
//			return null;
//		}
//		SpmsUserProductBinding surb = temp.get(0);
//		if(surb!=null && surb.getId()!=null){
//			surb.setCustomName(newname);
//			if(null == surb.getVersion()){
//				surb.setVersion(1L);
//			}
//			spmsUserProductBindingDAO.save(surb);
//			result.put("success", true);
//		}else{
//			result.put("success", false);
//			result.put("msg", "没有找到相应设备。");
//		}
		return result;
	}

	
	public Map<String, Object> acOffCommand(String gwId, String deviceId) {
		 Map<String,Object> result = Maps.newHashMap();
		 Integer gwStatus = getGwJoinStatus(gwId);
		 if(gwStatus.intValue() != 1){
	            result.put("status",gwStatus);
	            result.put("success",false);
	            result.put("msg","网关连接失败");
	            return  result;
	        }
		 SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId);
		 int status = spmsDeviceData.getStatus();
		 if(status == 0){
	        	result.put("status",status);
	            result.put("success",false);
	            result.put("msg","空调连接异常");
	            return  result;
	        }
		 Map onCommand = CommandUtil.buildCommandMessageMap(CommandUtil.Scope.DEVICE, gwId, spmsDeviceData.getMac(), CommandUtil.CommandType.OFF, null, null);
		try {
	            //发送关闭空调的命令
	            int onStatus = CommandUtil.syncSendMessage(onCommand);
	            //成功
	            if(onStatus == 1) {
	                //获取空调当前最新状态
	                if(getSensorInfo(gwId.toString(),spmsDeviceData.getMac())){
	                	spmsDeviceData = loadDeviceNewest(deviceId);
	                    //status = spmsDevice.getOperStatus();
	                    int onOff = spmsDeviceData.getOnOff();
	                    int temp = spmsDeviceData.getTemp();
						int maxTemp = spmsDeviceData.getUpperTemp();
						int minTemp = spmsDeviceData.getFloorTemp();
						int allowHeat = 1; //TODO
	                    result.put("maxTemp",maxTemp);
	                    result.put("minTemp",minTemp);
	                    result.put("allowHeat",allowHeat);
	                    result.put("success", true);
	                    result.put("status",status);
	                    //result.put("onOff",onOff);
	                    result.put("onOff",0);
	                    result.put("temp",temp);
	                    result.put("deviceId", deviceId);
	                }else {
	                    result.put("status",status);
	                    result.put("success",false);
	                    result.put("msg","服务器端出错");
	                }
	            }
	            //异常
	            else if(onStatus == -1){
	            	result.put("status",status);
	                result.put("success",false);
	                result.put("msg","服务器端出错");
	            }
	            //超时
	            else if(onStatus == -2){
	            	result.put("status",status);
	                result.put("success",false);
	                result.put("msg","连接超时");
	            }
	        } catch (SocketTimeoutException e) {
	        	result.put("status",status);
	            result.put("success",false);
	            result.put("msg","连接超时");
	        } catch (Exception e) {
	        	result.put("status",status);
	            result.put("success",false);
	            result.put("msg","服务器端出错");
	        }

		return result;
	}
	
	 private boolean getSensorInfo(String gwId,String deviceId) {
	        boolean flag = true;

	        Map command = Maps.newHashMap();
	        try {
	            command = CommandUtil.buildGetSensorInfoMessageMap(CommandUtil.Scope.DEVICE, gwId, deviceId);
	            int status = CommandUtil.syncSendMessage(command);
	            if(status == 1){
	                flag = true;
	            }else{
	                flag = false;
	            }
	        } catch (Exception e) {
	            flag = false;
	        }
	        return flag;
	    }

	
	public Map<String, Object> acOnCommand(String gwId, String deviceId) {
		Map<String,Object> result = Maps.newHashMap();
		Integer gwStatus = getGwJoinStatus(gwId);
        if(gwStatus.intValue() != 1){
            result.put("status",gwStatus);
            result.put("success",false);
            result.put("msg","网关连接失败");
            return  result;
        }
        
        //获取空调最新的运行状态，如果为离线则不能发送开关命令，直接返回连接异常
        SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId);
        //int status = spmsDevice.getOperStatus();
        int status = spmsDeviceData.getStatus();
        if(status == 0){
        	result.put("status",status);
            result.put("success",false);
            result.put("msg","空调连接异常");
            return  result;
        }

        //生成空调打开的控制命令
        Map command = CommandUtil.buildCommandMessageMap(CommandUtil.Scope.DEVICE, gwId, spmsDeviceData.getMac(), CommandUtil.CommandType.ON, "", 1);
        try {
            //发送打开空调的命令
            int onStatus = CommandUtil.syncSendMessage(command);
            if(onStatus == 1) {
                //成功
                if(getSensorInfo(gwId.toString(),spmsDeviceData.getMac())){
                    //获取当前设备的最新状态
                	spmsDeviceData = loadDeviceNewest(deviceId);
                    //status = spmsDevice.getOperStatus();
                    int onOff = spmsDeviceData.getOnOff();
					int maxTemp = spmsDeviceData.getUpperTemp();
					int minTemp = spmsDeviceData.getFloorTemp();
					int allowHeat = 1; //TODO
                    if(spmsDeviceData.getType() == 2){
                    	int temp = spmsDeviceData.getTemp();
                        int acTemp = spmsDeviceData.getAcTemp();
                        int mode = spmsDeviceData.getMode();
                        int speed = spmsDeviceData.getSpeed();
                        result.put("maxTemp",maxTemp);
                        result.put("minTemp",minTemp);
                        result.put("allowHeat",allowHeat);
                        result.put("temp",temp);
                        result.put("acTemp",acTemp);
                        result.put("mode",mode);
                        result.put("speed",speed);
                    }
                    result.put("success", true);
                    result.put("status",status);
                    //result.put("onOff",onOff);
                    result.put("onOff",1);
                    result.put("deviceId", deviceId);
                }else {
                    result.put("status",status);
                    result.put("success",false);
                    result.put("msg","服务器端出错");
                }
            }
            //异常
            else if(onStatus == -1){
            	result.put("status",status);
                result.put("success",false);
                result.put("msg","服务器端出错");
            }
            //超时
            else if(onStatus == -2){
            	result.put("status",status);
                result.put("success",false);
                result.put("msg","连接超时");
            }
        } catch (SocketTimeoutException e) {
        	result.put("status",status);
            result.put("success",false);
            result.put("msg","连接超时");
        } catch (Exception e) {
        	e.printStackTrace();
        	result.put("status",status);
            result.put("success",false);
            result.put("msg","服务器端出错");
        }

        return result;
	}

	
	public Map<String, Object> changeAcStatus(String gwId, String deviceId, String commandType, int value) {
		Map<String, Object> result = Maps.newHashMap();

		Integer gwStatus = getGwJoinStatus(gwId);
		if (gwStatus.intValue() != 1) {
			result.put("success", false);
			result.put("msg", "网关连接失败");
			return result;
		}

		Map command = Maps.newHashMap();
		SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId);
		int status = spmsDeviceData.getStatus();
		if (status == 0) {
			result.put("success", false);
			result.put("msg", "空调连接异常");
			return result;
		}
		try {
			command = CommandUtil.buildCommandMessageMap(CommandUtil.Scope.DEVICE, gwId, spmsDeviceData.getMac(),
					commandType, null, value);
			int onStatus = CommandUtil.syncSendMessage(command);
			if (onStatus == 1) {
				if (getSensorInfo(gwId.toString(), spmsDeviceData.getMac())) {
					// if(getSensorInfo(gwId,deviceId)){
					// 获取当前设备的最新状态
					spmsDeviceData = loadDeviceNewest(deviceId);
					// status = spmsDevice.getOperStatus();
					int onOff = spmsDeviceData.getOnOff();
					int temp = spmsDeviceData.getTemp();
					int acTemp = spmsDeviceData.getAcTemp();
					int mode = spmsDeviceData.getMode();
					int speed = spmsDeviceData.getSpeed();
					int maxTemp = spmsDeviceData.getUpperTemp();
					int minTemp = spmsDeviceData.getFloorTemp();
					int allowHeat = 1; //TODO
					result.put("success", true);
					result.put("status", status);
					if (commandType.equals(CommandUtil.CommandType.ON)) {
						result.put("onOff", 1);
					} else if (commandType.equals(CommandUtil.CommandType.OFF)) {
						result.put("onOff", 0);
					}
					
					result.put("maxTemp", maxTemp);
					result.put("minTemp", minTemp);
					result.put("allowHeat", allowHeat);
					result.put("temp", temp);
					result.put("acTemp", acTemp);
					result.put("mode", mode);
					result.put("speed", speed);
					result.put("deviceId", deviceId);
				} else {
					result.put("status", status);
					result.put("success", false);
					result.put("msg", "连接超时");
				}
			} else {
				result.put("status", onStatus);
				result.put("success", false);
				result.put("msg", "设备不在线");
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "连接超时");
			e.printStackTrace();
		}
		return result;
	}
	
	/*刷新全部设备信息*/
	
	public Map<String, Object> getDevicesCurrentStatus( Map<String, Object> info) {
		Map<String,Object> result = Maps.newHashMap();
		String gwId = info.get("gwid").toString();
		List<String> deviceIds = (List<String>) info.get("devices");
		Map<String,String> last = (Map<String, String>) info.get("last");
		
		Integer gwStatus = getGwJoinStatus(gwId);
		Map<String,Object> gws = Maps.newHashMap();
    	gws.put("type", 1);
    	gws.put("gwStatus", gwStatus);
    	result.put("gw",gws);
    	
    	List<Object> acs = Lists.newArrayList();
    	List<Object> sensors = Lists.newArrayList();
    	for(int i = 0 ; i < deviceIds.size() ; i ++){
    		 Map<String,Object> data = Maps.newHashMap();
    		 try{
    			 SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceIds.get(i));
    			 if(spmsDeviceData.getType() == 2){
 					int maxTemp = spmsDeviceData.getUpperTemp();
 					int minTemp = spmsDeviceData.getFloorTemp();
 					int allowHeat = 1; //TODO
    				 data.put("maxTemp",maxTemp);
    				 data.put("minTemp",minTemp);
                 	 data.put("allowHeat",allowHeat);
                 	int status =0;
                 	try{
                 		status = spmsDeviceData.getStatus();
                 	}catch(Exception e){
                    	 
                     }
                 	int onOff=0;
                 	try{
                      onOff = spmsDeviceData.getOnOff();
                	}catch(Exception e){
                   	 
                    }
                 	int temp=0;
                	try{
                      temp = spmsDeviceData.getTemp();
                	}catch(Exception e){
                      	 
                    }
                	int acTemp=-1;
                	try{
                      acTemp = spmsDeviceData.getAcTemp();
                	}catch(Exception e){
                      	 
                    }
                	int mode=-1;
                	try{
                      mode = spmsDeviceData.getMode();
                	}catch(Exception e){
                      	 
                    }
                	int speed=-1;
                	try{
                		speed = spmsDeviceData.getSpeed();
                	}catch(Exception e){
                      	 
                    }
                     data.put("success", true);
                     data.put("status",gwStatus.intValue()==1?status:gwStatus);//0离线，1在线，2异常
                     data.put("onOff",onOff);
                     data.put("temp",temp);
                     data.put("acTemp",acTemp);
                     data.put("mode",mode);
                     data.put("speed",speed);
                     data.put("deviceId", spmsDeviceData.getDeviceId());
                     data.put("type", spmsDeviceData.getType());
                     if(last == null || last.isEmpty() || last.get(spmsDeviceData.getDeviceId()) == null){
                    	data.put("newpower", new Object[]{});
                    	acs.add(data);
                     	continue;
                     }
                     Map<String,String> lasttime = last;
                     String id = String.valueOf(spmsDeviceData.getDeviceId());
                     String time = String.valueOf(lasttime.get(id));
                     if(null != info.get("type") && "1".equals(((Map)info.get("type")).get(id))){
                    	 data.put("newpower",getDevcieNewpowerByTime(id,time));
                     }else{
                    	 data.put("newpower",getDevcieNewpowerByTime1(id,time));
                     }
                     acs.add(data);
    			 } else if(spmsDeviceData.getType() == 3 || spmsDeviceData.getType() == 4) {
    				 int status = spmsDeviceData.getStatus();
                     int onOff = spmsDeviceData.getOnOff();
                     int remain = spmsDeviceData.getRemain() !=null ? spmsDeviceData.getRemain() : 0;
                     data.put("success", true);
                     data.put("status",gwStatus.intValue()==1?status:gwStatus);//0离线，1在线，2异常
                     data.put("onOff", onOff);
                     data.put("remain", remain);
                     data.put("deviceId", spmsDeviceData.getDeviceId());
                     data.put("type", spmsDeviceData.getType());
                     
                     Object[] newstatus  = getWinDoorOnOff(spmsDeviceData.getDeviceId());
                     if(newstatus == null){
                     	data.put("newtime",null);
                     	data.put("newopenclose",null);
                     }else{
                     	data.put("newtime",newstatus[0]);
                     	 data.put("newopenclose",newstatus[1]);
                     }
                     sensors.add(data);
    			 }
    		 }catch (Exception e) {
                 data.put("success", false);
                 data.put("deviceId",deviceIds.get(i));
                 data.put("msg","更新设备信息异常");
                 e.printStackTrace();
             }
             //result.add(data);
    	}
    	result.put("sensor", sensors);
    	result.put("ac", acs);
    	return result;
	}

	
	public List<Object[]> getDevcieNewpowerByTime(String deviceid, String lasttime) throws ParseException {
		if(lasttime == null || lasttime.equals("")){
			lasttime = Long.toString((new java.util.Date()).getTime());
		}
		List<Object[]> result = Lists.newArrayList();
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(lasttime)));
		
		String sql = "select start_time as time, power from spms_ac_status where device_id='"+deviceid+"' and DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') > '"+date+"' order by start_time asc ";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((BigInteger) m.get("power")).intValue();
			Long time = ((Date)m.get("time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}
	
	public List<Object[]> getDevcieNewpowerByTime1(String deviceid, String lasttime) throws ParseException {
		if(lasttime == null || lasttime.equals("")){
			lasttime = Long.toString((new java.util.Date()).getTime());
		}
		List<Object[]> result = Lists.newArrayList();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date last = new Date(Long.parseLong(lasttime));
		//String dataformat = DateUtil.parseDateToString(last, "yyyy-MM-dd hh:mm:ss");
		String dataformat = sdf1.format(last);
		String sql = "select start_time as time, accumulatePower from spms_ac_status where device_id='"+deviceid+"' and DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') > '"+dataformat+"' order by start_time asc ";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((BigInteger) m.get("accumulatePower")).intValue();
			Long time = ((Date)m.get("time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}

	
	public Object[] getWinDoorOnOff(String deviceid) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String sql = "select operate_time as time, operate_type from spms_win_door_status where device_id="+deviceid+" and DATE_FORMAT(operate_time,'%Y-%m-%d') > '"+date+"'  order by operate_time desc ";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for (int i = 0; i < list.size(); i++) {
			Map<String ,Object> m = list.get(i);
			Integer openclose =  (Integer) m.get("operate_type");
			/*Long time = DateUtil.parseStringToDate(m.get("time").toString()).getTime();*/
			Long time = DateUtil.getMills(m.get("time").toString(),"yyyy-MM-dd HH:mm:ss");
			result.add(new Object[] { time, openclose });
		}
		if( result != null  && result.size() != 0 ){
			return result.get(0);
		}
		return null;
	}
	
	
	public SpmsDeviceBase getSpmsDevice(String gwid) {
		return spmsGatewayDAO.findOne(gwid);
	}
/**
 * 取得用电信息（设备）
 * @throws ParseException 
 */
	
	public Map<String, Object> getDeviceExpend(Map<String, Object> m) throws ParseException {
		// TODO Auto-generated method stub
		Map<String , Object> result = Maps.newHashMap();
		String deviceId = m.get("deviceId")+"";
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		if( m.get("deviceType") != null && "0".equals(m.get("deviceType")+"") ){
			String sql = "select operate_time,operate_type from spms_win_door_status where device_id = '"
					+ deviceId
					+ "'  and DATE_FORMAT(operate_time,'%Y-%m-%d') >= '"+date+"' ORDER BY operate_time asc";
			List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
			List<Object[]> datas = Lists.newArrayList();
			for ( Map<String,Object> map : d ) {
				int power = ((Number) map.get("operate_type")).intValue();
				long time = ((Date)map.get("operate_time")).getTime();
				Object[] data = new Object[] { time, power };
				datas.add(data);
			}
			result.put("datas",datas);	
		}else{
			String sql = "select start_time,power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' and DATE_FORMAT(start_time,'%Y-%m-%d') >= '"+date+"' ORDER BY start_time asc";
			List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
			//如果该用户没有绑定网关，则就没有对应的设备。
			List<Object[]> datas = new ArrayList<Object[]>();
			for (Map<String,Object> map : d) {
				int power = ((BigInteger) map.get("power")).intValue();
				long time =  ((Date)map.get("start_time")).getTime(); 
				Object[] data = new Object[] { time, power };
				datas.add(data);
			}
			result.put("datas",datas);	
		}
		return result;
	}
	
	public Map<String, Object> getDeviceExpend1(Map<String, Object> m) throws ParseException {
		// TODO Auto-generated method stub
		Map<String , Object> result = Maps.newHashMap();
		String deviceId = m.get("deviceId")+"";
		
		//统计类型
		String rbtn = m.get("rbtn").toString();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String formatStr = "";
		
		if( m.get("deviceType") != null && "0".equals(m.get("deviceType")+"") ){
			String sql = "select operate_time,operate_type from spms_win_door_status where device_id = '"
					+ deviceId
					+ "' ORDER BY operate_time asc";
			List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
			List<Object[]> datas = Lists.newArrayList();
			for ( Map<String,Object> map : d ) {
				int power = ((Number) map.get("operate_type")).intValue();
				long time = ((Date)map.get("operate_time")).getTime();
				Object[] data = new Object[] { time, power };
				datas.add(data);
			}
			result.put("datas",datas);	
		}else{
			//根据统计类型 查询不同表
			String sql = "";
			if(rbtn.equals("1")){
				formatStr = "yyyy-MM-dd HH:mm:ss";
				sql = "select start_time as date ,truncate(accumulatePower/1000,3) as power from spms_ac_status where device_id = '"+ deviceId+ "' " +
						"AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"+date+"' ORDER BY start_time asc";
				
			}else if(rbtn.equals("2")){
				formatStr = "yyyy-MM-dd HH";
				sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,truncate(max(accumulatePower)/1000,3) as power from spms_ac_status where device_id = '"+ deviceId+ "' " +
						"AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"+date+"' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
			}else if(rbtn.equals("3")){
				date = date.substring(0,7);
				formatStr = "yyyy-MM-dd";
				sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_month where device_id = '" + deviceId + "' and substr(date,1,7) ='" + date + "' order by date";
			}else if(rbtn.equals("4")){
				date = date.substring(0,4);
				formatStr = "yyyy-MM";
				sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_year where device_id = '" + deviceId + "' and substr(date,1,4) ='" + date + "' order by date";
			}
			
			
			List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
			//如果该用户没有绑定网关，则就没有对应的设备。
			List<Object[]> datas = new ArrayList<Object[]>();
			for (Map<String,Object> map : d) {
				Double power = ( Double.parseDouble(map.get("power").toString()));
				long time =  new SimpleDateFormat(formatStr).parse(map.get("date").toString()).getTime(); 
				Object[] data = new Object[] { time, power };
				datas.add(data);
			}
			result.put("datas",datas);	
			
		}
		return result;
	}
	
}
