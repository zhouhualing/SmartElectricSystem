package com.harmazing.spms.workorder.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.workorder.dto.SpmsChangeProductDTO;
import com.harmazing.spms.workorder.manager.BusinessChangeManager;


@Controller
public class BusinessChangeController {
	@Autowired
	private BusinessChangeManager businessChangeManager; 
	
	@RequestMapping("/businessChange/getUserBanlance")
    @ResponseBody
	public Double getUserBanlance(@RequestBody Map<String,Object> para){
		return businessChangeManager.getUserBanlance(para);
	}
	
	@RequestMapping("/businessChange/getUserProduct")
    @ResponseBody
	public List getUserProduct(@RequestBody Map<String,Object> para){
		return businessChangeManager.getUserProduct(para);
	}
	
	@RequestMapping("/businessChange/getProducts")
    @ResponseBody
	public List getDevices(HttpServletRequest request){		
		return businessChangeManager.getDevices(request);
	}
	
	@RequestMapping("/businessChange/getUserDetails")
    @ResponseBody
	public Map<String,Object> getUserDetail(@RequestBody Map<String,Object> para){		
		return businessChangeManager.getUserDetail(para);
	}
	
	@RequestMapping("/businessChange/getUserDevices")
    @ResponseBody
	public List getUserDevices(@RequestBody Map<String,Object> para){		
		return businessChangeManager.getUserDevices(para);
	}
	
	@RequestMapping("/businessChange/saveChangeDevices")
    @ResponseBody
	public SpmsChangeProductDTO saveChangeDevices(@RequestBody SpmsChangeProductDTO productDto){
		return businessChangeManager.saveChangeDevices(productDto);
	}

	/**
	 * 验证绑定数量
	 * @param productDto
	 * @return
	 */
	@RequestMapping("/businessChange/countUserDevices")
	@ResponseBody
	public Map<String, Object> countUserDevices(@RequestBody SpmsChangeProductDTO productDto){
		return businessChangeManager.countUserDevices(productDto);
	}

	/**
	 * 退订
	 * @param productDto
	 * @return
	 */
	@RequestMapping("/businessChange/saveTdDevices")
	@ResponseBody
	public SpmsChangeProductDTO saveTdDevices(@RequestBody SpmsChangeProductDTO productDto){
		return businessChangeManager.saveTdDevices(productDto);
	}
	
	
	@RequestMapping("/businessChange/checkDevice")
    @ResponseBody
	public String checkDevice(@RequestBody Map<String,Object> para){
		return businessChangeManager.checkDevice(para);
	}
	@RequestMapping("/businessChange/checkDevices")
    @ResponseBody
	public List checkDevices(@RequestBody Map<String,Object> para){
		return businessChangeManager.checkDevices(para);
	}
	
	@RequestMapping("/businessChange/getUserByWorkOrder")
	@ResponseBody
	public Map<String,Object> getUserByWorkOrder(@RequestBody Map<String,Object> para){
		return businessChangeManager.getUserByWorkOrder(para);
	}
	
	@RequestMapping("/businessChange/listStorage")
	@ResponseBody
	public Map<String,Object> listStorage(@RequestBody Map<String,Object> para){
		return businessChangeManager.listStorage(para);
	}
}
