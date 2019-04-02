///**
// * 
// */
//package com.harmazing.spms.product.controller;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.harmazing.spms.base.dto.QueryTranDTO;
//import com.harmazing.spms.device.dto.SpmsDeviceDTO;
//import com.harmazing.spms.product.entity.SpmsProduct;
//import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
//
//import com.google.common.collect.Maps;
//import com.harmazing.spms.product.dto.SpmsProductDTO;
//import com.harmazing.spms.product.manager.SpmsProductManager;
//
//
//
///**
// * describe:
// * @author TanFan 
// * 产品控制器类
// * since 2015年1月15日
// */
//@Controller
//@RequestMapping("/spmsProduct")
//public class SpmsProductController {
//    
//    @Autowired
//    //注入产品服务类
//    private SpmsProductManager spmsProductManager;
//    
//    @RequestMapping("/doSave")
//    @ResponseBody
//    //产品保存方法
//    public SpmsProductDTO doSaveSpmsProduct(@RequestBody SpmsProductDTO spmsProductDTO) {
//    	return spmsProductManager.doSave(spmsProductDTO);
//    }
//    @RequestMapping("/doDLSave")
//    @ResponseBody
//    //产品修改方法
//    public SpmsProductDTO doSaveSpmsProductDL(@RequestBody SpmsProductDTO spmsProductDTO) {
//    	return spmsProductManager.doSaveDL(spmsProductDTO);
//    }
//    
//    @RequestMapping("/getInfo")
//    @ResponseBody
//    //根据ID获取指定产品
//    public SpmsProductDTO doQuerySpmsProductList(@RequestBody 	Map <String,String> dto) {
//    	return spmsProductManager.doQuery(dto.get("id"));
//    }
//    
//    @RequestMapping("/getProductDeviceList")
//    @ResponseBody
//    public List<SpmsDeviceDTO> getProductDeviceList(@RequestBody Map <String,String> info){
//    	return spmsProductManager.getProductDeviceList(info.get("id"));
//    }
//    @RequestMapping("/delete")
//    @ResponseBody
//    //根据ID物理删除产品
//    public Boolean doDeleteSpmsProduct(@RequestBody Map <String,String> dto) {
//    	return spmsProductManager.doDelete(dto.get("id"));
//    }
//    
//    @RequestMapping("/deleteAll")
//    @ResponseBody
//    //根据产品ID数组 批量删除产品
//    public Boolean doDeleteAllSpmsProduct(@RequestBody int[] data1) {
//    	return spmsProductManager.doDeleteAll(data1);
//    
//   }
//    
//    @RequestMapping("/getAllProducttype")
//    @ResponseBody
//    public Map<String,Object> getAllProducttype(){
//    	return spmsProductManager.getAllProducttype();
//    }
//    
//    //产品统计-销售数量与金额
//    @RequestMapping("/getCountAmount")
//    @ResponseBody
//    public Map<String,Object> getCountAmount(@RequestBody Map <String,Object> info){
//	   Map<String,Object> result = Maps.newHashMap();
//	   String typeid = (String) info.get("type");
//	   String start = (String) info.get("start");
//	   String end = (String) info.get("end");
//	   
//	   result = spmsProductManager.getCountAmount(typeid, start, end);
//	   
//	   return result;
//	   
//    }
//  //产品统计-成本与收益
//    @RequestMapping("/getCostEarnings")
//    @ResponseBody
//    public Map<String,Object> getCostEarnings(@RequestBody Map <String,Object> info) throws ParseException{
//    	Map<String,Object> result = Maps.newHashMap();
//    	String typeid = (String) info.get("type");
//    	String start = (String) info.get("start");
//    	String end = (String) info.get("end");
//    	result = spmsProductManager.getCostEarnings(typeid, start, end);
//    	return result;
//    }
//    
//    //产品统计-收益历史同期
//    @RequestMapping("/getOldEarnings")
//    @ResponseBody
//    public Map<String,Object> getOldEarnings(@RequestBody Map <String,Object> info) throws ParseException{
//    	Map<String,Object> result = Maps.newHashMap();
//    	String typeid = (String) info.get("type");
//    	String start = (String) info.get("start");
//    	String end = (String) info.get("end");
//    	result = spmsProductManager.getOldEarnings(typeid, start, end);
//    	return result;
//    }
//
//    //根据工单信息获取产品信息
//    @RequestMapping("/getProductInfoByWorkOrder")
//    @ResponseBody
//    public Map<String, Object> getProductInfoByWorkOrder(@RequestBody Map <String,Object> info){
//        return spmsProductManager.getProductInfoByWorkOrder(info);
//    }
//
//    //给订户绑定网关
//    //给产品绑定指定设备
//    @RequestMapping("/bindDeviceToProduct")
//    @ResponseBody
//    public SpmsProductDTO bindDeviceToProduct(@RequestBody SpmsProductDTO spmsProductDTO){
//		try {
//			return spmsProductManager.bindDeviceToProduct(spmsProductDTO);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return spmsProductDTO;
//		}
//    }
//
//    //验证绑定的网关与设备
//    @RequestMapping("/checkGWandDevice")
//    @ResponseBody
//    public Map<String ,Object> checkGWandDevice(@RequestBody SpmsProductDTO spmsProductDTO){
//    	return spmsProductManager.checkGWandDevice(spmsProductDTO);
//    }
//    
//    @RequestMapping("/getProductList")
//	public List<SpmsProduct> getProductList(@RequestBody QueryTranDTO queryTranDTO) {
////		queryManager.executeSearch(queryTranDTO);
////		MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
////		ModelAndView modelAndView = new ModelAndView(mappingJackson2JsonView);
////		modelAndView.addObject("data", queryTranDTO);
////		return modelAndView;
//    	// TODO
//    	return null;
//	}
//}
