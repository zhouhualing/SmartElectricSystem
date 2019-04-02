/**
 * 
 */
package com.harmazing.spms.device.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年12月31日
 */
@Controller
@RequestMapping("/spmsDevice")
public class SpmsDeviceController {
    
    @Autowired
    private SpmsDeviceManager spmsDeviceManager;
    @RequestMapping("/getInfo")
    @ResponseBody
    public SpmsDeviceDTO getUserInfo(@RequestBody SpmsDeviceDTO spmsDeviceDTO) {
    	return spmsDeviceManager.getSpmsDevice(spmsDeviceDTO);
    }
    
    /**
     * 获取新的Zigbee数据
     * @param spmsDeviceDTO
     * @return
     */
    @RequestMapping("/getRefreshZigbee")
    @ResponseBody
    public Map<String,Object> getRefreshZigbee(@RequestBody Map<String,Object> m) {
    	return spmsDeviceManager.getRefreshZigbee(m.get("type")+"",m.get("deviceId")+"");
    }
    /**
     * 发消息，Zigbee数据
     * @param spmsDeviceDTO
     * @return
     */
    @RequestMapping("/sendmMessageForZigbee")
    @ResponseBody
    public Map<String,Object> sendmMessageForZigbee(@RequestBody Map<String,Object> m) {
//    	String userId = "0";
//		String mobile=UserUtil.getCurrentUser().getMobilePhone();
//		SpmsUserDAO userDao=SpringUtil.getBeanByName("spmsUserDAO");
//		if(mobile != null){
//			SpmsUser user=userDao.getByMobile(mobile);
//			userId = user.getId();
//		}
    	String userId=spmsDeviceManager.getUserIdByDevice((String)m.get("type"),(String)m.get("deviceId"));
		Map<String,Object> rm = new HashMap<String,Object>();
		rm.put("scope", "USER");
		rm.put("id", userId);
		rm.put("messageType","GETGWINFO");
		try {
			CommandUtil.asyncSendMessage(rm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendMessage.reusltMap(true);
    }
    @RequestMapping("/doSave")
    @ResponseBody
    public SpmsDeviceDTO doSaveSpmsDevice(@RequestBody SpmsDeviceDTO spmsDeviceDTO) {
    	return spmsDeviceManager.doSave(spmsDeviceDTO);
    }
    
    @RequestMapping("/doEditBaseInfo")
    @ResponseBody
    public SpmsDeviceDTO doEditBaseInfo(SpmsDeviceDTO spmsDeviceDTO){
    	return spmsDeviceManager.doEditBaseInfo(spmsDeviceDTO);
    }
    
    @RequestMapping("/doEditOpreInfo")
    @ResponseBody
    public SpmsDeviceDTO doEditOpreInfo(SpmsDeviceDTO spmsDeviceDTO){
    	return spmsDeviceManager.doEditOpreInfo(spmsDeviceDTO);
    }
    
    
    @RequestMapping("/doDel")
    @ResponseBody
    public String doDel(String ids) {
    	return spmsDeviceManager.deleteByIds(ids)+"";
    }
    /**
     * 用户端门窗传感用电信息统计图所需数据
     * @param info
     * @return
     * @throws ParseException
     */
    @RequestMapping("/getDeviceOperInfo")
    @ResponseBody
    public Map<String,Object> getDeviceOperInfo(@RequestBody Map<String,Object> info) throws ParseException{
    	Map<String,Object> result = Maps.newHashMap();
    	String type = (String)info.get("type");
    	String deviceid = (String)info.get("id");
    	if(type.equals("1")){
    		List<Object[]> datas = spmsDeviceManager.deviceData(deviceid,type);
    		result.put("datas", datas);
    	}else if(type.equals("2")){
    		List<Map<String,Object>> l = spmsDeviceManager.findSpmsAcStatusForDeviceId(deviceid);
    		
    		List<Object[]> powers = new ArrayList<Object[]>();//spmsDeviceManager.findAllPowerById(deviceid);
    		List<Object[]> reactivePowers = new ArrayList<Object[]>();//spmsDeviceManager.findAllReactivePowerById(deviceid);
    		List<Object[]> powerFactors = new ArrayList<Object[]>();//spmsDeviceManager.findAllPowerFactorById(deviceid);
    		List<Object[]> apparentPowers = new ArrayList<Object[]>();//spmsDeviceManager.findAllApparentPowerById(deviceid);
    		List<Object[]> eP = spmsDeviceManager.findAllEpById(info);
    		List<Object[]> reactiveEnergys = spmsDeviceManager.findAllReactiveEnergyById(info);
    		List<Object[]> activeDemands = new ArrayList<Object[]>();//spmsDeviceManager.findAllActiveDemandById(deviceid);
    		List<Object[]> reactiveDemands = new ArrayList<Object[]>();//spmsDeviceManager.findAllReactiveDemandById(deviceid);
    		List<Object[]> currents = new ArrayList<Object[]>();//spmsDeviceManager.findAllCurrentById(deviceid);
    		List<Object[]> voltages = new ArrayList<Object[]>();//spmsDeviceManager.findAllVoltageById(deviceid);
    		List<Object[]> frequencys = new ArrayList<Object[]>();//spmsDeviceManager.findAllFrequencyById(deviceid);
    		
    		for (Map<String, Object> map : l) {
    			
    			Long time = ((Date)map.get("start_time")).getTime();
    			
    			float power = ((BigInteger) map.get("power")).intValue();
    			powers.add(new Object[] { time, power });
    			
    			float reactivePower = ((Integer) map.get("reactivePower")).intValue();
    			reactivePowers.add(new Object[] { time, reactivePower });
    			
    			float powerFactor = ((Integer) map.get("powerFactor")).intValue();
    			powerFactors.add(new Object[] { time, powerFactor });
    			
    			float apparentPower = ((Integer) map.get("apparentPower")).intValue();
    			apparentPowers.add(new Object[] { time, apparentPower });
    			
//    			float eps = ((Number) map.get("accumulatePower")).intValue();
//    			eP.add(new Object[] { time, eps });
//    			
//    			float reactiveEnergy = ((Integer) map.get("reactiveEnergy")).intValue();
//    			reactiveEnergys.add(new Object[] { time, reactiveEnergy });
    			
    			float activeDemand = ((Integer) map.get("activeDemand")).intValue();
    			activeDemands.add(new Object[] { time, activeDemand });
    			
    			float reactiveDemand = ((Integer) map.get("reactiveDemand")).intValue();
    			reactiveDemands.add(new Object[] { time, reactiveDemand });
    			
    			float current = ((Integer) map.get("current")).intValue();
    			currents.add(new Object[] { time, current });
    			
    			float voltage = ((Integer) map.get("voltage")).intValue();
    			voltages.add(new Object[] { time, voltage });
    			
    			float frequency = ((Integer) map.get("frequency")).intValue();
    			frequencys.add(new Object[] { time, frequency });
			}
    		
    		result.put("power", powers);
    		result.put("reactivePower", reactivePowers);
    		result.put("powerFactor", powerFactors);
    		result.put("apparentPower", apparentPowers);
    		result.put("eP", eP);
    		result.put("reactiveEnergy", reactiveEnergys);
    		result.put("activeDemand", activeDemands);
    		result.put("reactiveDemand", reactiveDemands);
    		result.put("current", currents);
    		result.put("voltage", voltages);
    		result.put("frequency", frequencys);
    	}else if(type.equals("3")){
    		List<Object[]> datas = spmsDeviceManager.deviceData(deviceid,type);
    		result.put("datas", datas);
    	}
    	return result;
    }
    
    @RequestMapping("/getDeviceRunInfo")
    @ResponseBody
    public Map<String,Object> getDeviceRunInfo(@RequestBody Map<String,Object> info){
    	Map<String,Object> result = Maps.newHashMap();
    	
    	String type = (String)info.get("type");
    	String deviceid = (String)info.get("id");
    	result = spmsDeviceManager.getDevcieRuninfo(deviceid, type);
    	
    	return result;
    }
    
    @RequestMapping("/getDeviceConfigInfo")
    @ResponseBody
    public Map<String,Object> getDeviceConfigInfo(@RequestBody Map<String,Object> info){
    	String deviceid = (String)info.get("id");
    	return spmsDeviceManager.getDeviceConfigInfo(deviceid);
    }
    @RequestMapping("/getStorageAll")
    @ResponseBody
    public List<Map<String, Object>> getStorageAll(){
    	return spmsDeviceManager.getStorageAll();
    }
    

	@RequestMapping("/import")
	public void deviceImport(@RequestParam MultipartFile[] myfiles, HttpServletRequest request , HttpServletResponse response) throws IOException{
		//设置response消息头
		response.setContentType("text/html"); 
		String type = request.getParameter("type");
		String re = new String();
		if (null == type || "".equals(type) || "null".equals(type)) {
			response.getWriter().write("{'success':false,'msg':'请以正常方式登陆'}");
			response.getWriter().flush();
			return;
		}
		
		for(MultipartFile myfile : myfiles){  
			 if(myfile.isEmpty()){
					response.getWriter().write("{'success':false,'msg':'导入时，必须选择正确的数据文件'}");
					response.getWriter().flush();
				 return;
			 }
			 try{
				 Workbook wb = null;
				 if(myfile.getOriginalFilename().endsWith("xls")){
		                wb = new HSSFWorkbook(myfile.getInputStream());//解析xls格式  
		         }else if(myfile.getOriginalFilename().endsWith("xlsx")){  
		                wb = new XSSFWorkbook(myfile.getInputStream());//解析xlsx格式  
		         }
//				 List<SpmsDevice> list = new ArrayList<SpmsDevice>();
//				 Sheet sheet = wb.getSheetAt(0);
//				 int rows = sheet.getLastRowNum();
//				 for (int i = 1; i <= rows; i++) {
//					Row row = sheet.getRow(i);
//					try{
//						//如果是空行
//						if(row.getCell(0).getNumericCellValue() == 0.0){
//							break;
//						}
//					}catch(Exception e){
//						break;
//					}
//					SpmsDevice sd = new SpmsDevice();
//					sd.setCreateDate(new Date(System.currentTimeMillis()));
//					sd.setVersion(1l);
//					sd.setCursoft(row.getCell(6)==null?"":row.getCell(6).getStringCellValue());
//					sd.setSoftware(row.getCell(6)==null?"":row.getCell(6).getStringCellValue());
//					sd.setDisabel(0);
//					sd.setHardware(row.getCell(5)==null?"":row.getCell(5).getStringCellValue());
//					sd.setMac(row.getCell(4)==null?"":row.getCell(4).getStringCellValue());
//					//sd.setModel(row.getCell(5)==null?"":row.getCell(5).getStringCellValue().substring(0, row.getCell(5).getStringCellValue().indexOf("_")));//?
//					sd.setOnOff(0);//开关状态 开启1 关闭0
//					sd.setOperStatus(0);//离线0 在线1 异常 2
//					sd.setSn(row.getCell(3)==null?"":row.getCell(3).getStringCellValue());
//					sd.setStartTime(row.getCell(7)==null?null:row.getCell(7).getDateCellValue());
//					sd.setStatus(1);//库存1 正常运营2 维修3 报废4
//					sd.setStorage(1);//仓库一1 仓库二2 仓库三3 仓库四4 已出库5
//					sd.setType(Integer.parseInt(type));
//					sd.setVender(row.getCell(8) == null?"":row.getCell(8).getStringCellValue());//?
//					list.add(sd);
//				 }
//				 re = spmsDeviceManager.saveAll(list);
			 }catch(Exception e){
				 e.printStackTrace();
				 response.getWriter().write("{'success':false,'msg':'导入文件解析失败,请检查数据'}");
				 response.getWriter().flush();
				 return;
			 }
		}
		String msg = "导入成功!";
		if(re.length() > 0){
			msg = msg + "但<br>" + re;
		}
		response.getWriter().write("{'success':true,'msg':'" + msg + "'}");
		response.getWriter().flush();
	}
	
	@RequestMapping("/validMacAndSn")
	@ResponseBody
	public Map<String,Object> findByMacAndSn(@RequestBody Map<String,Object> map){
		Map<String,Object> result = new HashMap<String, Object>();
//		List<SpmsDevice> list = spmsDeviceManager.findByMacAndSn(map.get("mac").toString(),map.get("sn").toString());
//		result.put("result", list==null || list.size() == 0);
		return result;
		
	}
}
