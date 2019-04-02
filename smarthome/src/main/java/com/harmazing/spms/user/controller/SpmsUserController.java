package com.harmazing.spms.user.controller;

import java.io.IOException;
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
import org.apache.poi.ss.usermodel.Cell;
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
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.product.dto.SpmsProductDTO;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.user.dto.SpmsUserBindDTO;
import com.harmazing.spms.user.dto.SpmsUserDTO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.manager.SpmsUserManager;

@Controller
@RequestMapping("/spmsUser")
public class SpmsUserController {
	@Autowired
	private SpmsUserManager spmsUserManager;

	@RequestMapping("/doSave")
	@ResponseBody
	public SpmsUserDTO doSaveSpmsDevice(SpmsUserDTO spmsUserDTO) {
		return spmsUserManager.doSave(spmsUserDTO);
	}
	@RequestMapping("/ValidationMobile")
	@ResponseBody
	public Map<String,Object> ValidationMobile(@RequestBody Map<String, Object> info){
		String userid = (String) info.get("userid").toString();
		String mobile = (String) info.get("mobile").toString();
		if(StringUtil.isNUll(mobile)){
			Map<String,Object> result = Maps.newHashMap();
			result.put("success", false);
			result.put("msg", "手机号不能为空");
			return result;
		}
		return spmsUserManager.ValidationMobile(userid, mobile);
	}
	@RequestMapping("/ValidationMobileAndEmail")
	@ResponseBody
	public Map<String,Object> ValidationMobileAndEmail(@RequestBody Map<String, Object> info){
		String userid = (String) info.get("userid").toString();
		String mobile = (String) info.get("mobile").toString();
		String email = (String) info.get("email").toString();
		if(StringUtil.isNUll(mobile)){
			Map<String,Object> result = Maps.newHashMap();
			result.put("success", false);
			result.put("msg", "手机号不能为空");
			return result;
		}
//		if(StringUtil.isNUll(email)){
//			Map<String,Object> result = Maps.newHashMap();
//			result.put("success", false);
//			result.put("msg", "邮箱不能为空");
//			return result;
//		}
		return spmsUserManager.ValidationMobileAndEmail(userid, mobile, email);
	}

	@RequestMapping("/getInfo")
	@ResponseBody
	public SpmsUserDTO getUserInfo(@RequestBody SpmsUserDTO spmsUserDTO) {
		return spmsUserManager.getSpmsUser(spmsUserDTO);
	}

	@RequestMapping("/getByMobile")
	@ResponseBody
	public Map<String,Object> getByMobile(@RequestBody SpmsUserDTO spmsUserDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		SpmsUser su = spmsUserManager.getOnlyByMobile(spmsUserDTO.getMobile());
		map.put("user", su);
//		boolean flag = su.getSpmsUserProductBindings() == null;
//		if(!flag){
//			flag = su.getSpmsUserProductBindings().size() == 0;
//		}
//		map.put("hasProduct", !flag);
		return map;
	}

	@RequestMapping("/hasGwByMac")
	@ResponseBody
	public Map<String, Object> hasGwByMac(String gwmac,String userId) {
//		return spmsUserManager.hasGwByMac(gwmac,userId);
		return null;
	}

	@RequestMapping("/bindGetWay")
	@ResponseBody
	public Map<String, Object> bindGetWay(@RequestBody Map<String, Object> info) {
//		return spmsUserManager.bindGetWay(info.get("userid").toString(), info.get("gwid").toString());
		return null;
	}

	@RequestMapping("/doDels")
	@ResponseBody
	public String doDels(@RequestBody Map<String, String> info) {
		return spmsUserManager.deleteByIds(info.get("ids")) + "";
	}
	@RequestMapping("/doDel")
	@ResponseBody
	public String doDel(@RequestBody Map<String, String> info) {
		return spmsUserManager.deleteById(info.get("ids")) + "";
	}
	@RequestMapping("/getAllProductTypeInfo")
	@ResponseBody
	public List<SpmsProductTypeDTO> getAllProductTypeInfo(){
		return spmsUserManager.getAllProductTypeInfo();
	}
	
	@RequestMapping("/addDeviceToProduct")
	@ResponseBody
	public Map<String,Object> addDeviceToProduct(String devicemac,String devicetype,String productId){
//		return spmsUserManager.addDeviceToProduct(devicemac, devicetype, productId);
		return null;
	}
	
	@RequestMapping("/bindProduct")
	@ResponseBody
	public Map<String,Object> bindProduct(@RequestBody Map<String, Object> info) throws ParseException{
		//参数收到了，然后用传来的参数拼一个产品出来， 返回值是现在的产品，然后在页面上遍历出来。
		//之前的产品先遍历进div里
		Date date = DateUtil.parseStringToDate(info.get("subscribeDate").toString());
		List<String> deviceMacs =  (List<String>) info.get("devicemacs");
//		return spmsUserManager.addUserProductBind(info.get("userid").toString(), ((String) info.get("producttypeid")).toString(), date, deviceMacs);
		return null;
	}

	@RequestMapping("/bindProductWF")
	@ResponseBody
	public SpmsProductDTO bindProduct(@RequestBody SpmsProductDTO spmsProductDTO) throws ParseException{
		try {
			return spmsUserManager.addUserProductBindWF(spmsProductDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return spmsProductDTO;
		}
	}
	
	@RequestMapping("/getUserProductBind")
	@ResponseBody
	public List<SpmsUserBindDTO> getUserProductBind(@RequestBody Map<String, Object> info) throws ParseException{
//		return spmsUserManager.getUserProductBind(info.get("id").toString());
		return null;
	}
	@RequestMapping("/cancelDeviceBind")
	@ResponseBody
	public String cancelDeviceBind(String mac){
//		return spmsUserManager.cancelDeviceBind(mac)+"";
		return null;
	}
	@RequestMapping("/reBindDevice")
	@ResponseBody
	public Map<String,Object> reBindDevice(String oldMac,String newMac,String productId){
//		return spmsUserManager.reBindDevice(oldMac,newMac,productId);
		return null;
	}

	@RequestMapping("/validDevice")
	@ResponseBody
	public Map<String,Object> validDevice(@RequestBody Map<String, Object> info){
		return spmsUserManager.validDevice(info);
	}
	@RequestMapping("/reBindDevice1")
	@ResponseBody
	public Map<String,Object> reBindDevice1(String oldMac,String newMac,String dtype,String productId){
//		return spmsUserManager.reBindDevice1(oldMac,newMac,dtype,productId);
		return null;
	}

	@RequestMapping("/validateUser")
	@ResponseBody
	public Map<String,Object> validateUser(@RequestBody Map<String, Object> map){
		return spmsUserManager.validateUser(map.get("mobile").toString());
	}
	@RequestMapping("/import")
	public void spmsUserImport(@RequestParam MultipartFile[] myfiles, HttpServletRequest request , HttpServletResponse response) throws IOException{
		//设置response消息头
		response.setContentType("text/html"); 
		String re = new String();
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				response.getWriter().write(
						"{'success':false,'msg':'导入时，必须选择正确的数据文件'}");
				response.getWriter().flush();
				return;
			}
			try {
				Workbook wb = null;
				if (myfile.getOriginalFilename().endsWith("xls")) {
					wb = new HSSFWorkbook(myfile.getInputStream());// 解析xls格式
				} else if (myfile.getOriginalFilename().endsWith("xlsx")) {
					wb = new XSSFWorkbook(myfile.getInputStream());// 解析xlsx格式
				}
				List<SpmsUserDTO> list = new ArrayList<SpmsUserDTO>();
				Sheet sheet = wb.getSheetAt(0);
				int rows = sheet.getLastRowNum();
				for (int i = 1; i <= rows; i++) {
					Row row = sheet.getRow(i);
					try {
						// 如果是空行
						if (row.getCell(0).getNumericCellValue() == 0.0) {
							break;
						}
					} catch (Exception e) {
						break;
					}
					
					SpmsUserDTO spmsUserDTO=new SpmsUserDTO();
					if(row.getCell(1)!=null){
						row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
						spmsUserDTO.setFullname(row.getCell(1).getStringCellValue());
					}
					if(row.getCell(2)!=null){
						row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
						spmsUserDTO.setIdNumber(row.getCell(2).getStringCellValue());
					}
					if(row.getCell(3)!=null){
						row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
						spmsUserDTO.setAddress(row.getCell(3).getStringCellValue());
					}
					if(row.getCell(4)!=null){
						row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
						spmsUserDTO.setMobile(row.getCell(4).getStringCellValue());
					}
					if(row.getCell(5)!=null){
						row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
						spmsUserDTO.setEmail(row.getCell(5).getStringCellValue());
					}
//					if(row.getCell(6)!=null){
//						row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
//						spmsUserDTO.setAmmeter(row.getCell(6).getStringCellValue());
//					}
//					if(row.getCell(7)!=null){
//						row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
//						if("1".equals(row.getCell(7).getStringCellValue())||"2".equals(row.getCell(7).getStringCellValue())){
//							spmsUserDTO.setType(Integer.parseInt(row.getCell(7)
//									.getStringCellValue()));
//						}else {
//							spmsUserDTO.setType(0);
//						}
//					}
//					if(row.getCell(8)!=null){
//						row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
//						spmsUserDTO.setBizAreaName(row.getCell(8).getStringCellValue());
//					}
//					if(row.getCell(9)!=null){
//						row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
//						spmsUserDTO.setEleAreaName(row.getCell(9).getStringCellValue());
//					}
					list.add(spmsUserDTO);
				}
				re = spmsUserManager.saveAll(list);
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write(
						"{'success':false,'msg':'导入文件解析失败,请检查数据'}");
				response.getWriter().flush();
				return;
			}
		}
		String msg = "导入成功!";
		if (re.length() > 0) {
			msg = msg + "但用户<br>" + re;
		}
		response.getWriter().write("{'success':true,'msg':'" + msg + "'}");
		response.getWriter().flush();
	}
}
