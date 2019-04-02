package com.harmazing.spms.product.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.product.manager.SpmsProductTypeManager;

/**
 * describe:
 * @author TanFan
 * 这是产品类型控制器类
 * since 2015年1月15日
 * @param <SpmsProductType>
 */
@Controller
@RequestMapping("/spmsProductType")
public class SpmsProductTypeController {
	
	 	@Autowired
	 	//注入产品类型服务类
	    private SpmsProductTypeManager spmsProductTypeManager;
	 	
	 	
	 	 @RequestMapping("/doSave")
	     @ResponseBody
	     //映射保存产品类型方法
	     public SpmsProductTypeDTO doSaveSpmsProductType(SpmsProductTypeDTO spmsProductTypeDTO) {
	     	return spmsProductTypeManager.doSave(spmsProductTypeDTO);
	     }
	 	 
	 	 @RequestMapping("/doUpdate")
	     @ResponseBody
	     //映射修改产品类型方法
	     public SpmsProductTypeDTO doUpdateSpmsProductType(@RequestBody SpmsProductTypeDTO spmsProductTypeDTO) {
	     	return spmsProductTypeManager.doUpdate(spmsProductTypeDTO);
	     }
	 	 @RequestMapping("/doQuery")
	     @ResponseBody
	     //映射根据ID查询产品类型方法
	     public SpmsProductTypeDTO doQuerySpmsProductType(@RequestBody Map <String,String> dto) {
	     	return spmsProductTypeManager.doQuery(dto.get("id"));
	     }
	 	 
	 	 @RequestMapping("/doDelete")
	     @ResponseBody
	     //映射根据ID物理删除产品类型方法
	     public Map<String, Object> doDeleteSpmsProductType(@RequestBody Map <String,String> dto) {
	     	
	 		 return spmsProductTypeManager.doDelete(dto.get("id"));
	     }
	 	 
	     @RequestMapping("/deleteAll")
	     @ResponseBody
	     //映射根据产品类型ID数组批量物理删除产品类型方法
	     public Boolean  doDeleteAllSpmsProductType(@RequestBody int[] data1) {
	     	return spmsProductTypeManager.doDeleteAll(data1);
	     
	     }
	     //映射根据产品类型ID数组逻辑删除产品类型方法
	     @RequestMapping("/deleteProductAll")
	     @ResponseBody
	     public Map<String, Object>  doDeleteProductTypeAll(@RequestBody List <String> ids) {
	    	 return  spmsProductTypeManager.deleteProductAll(ids);
	     }
	   
	     //映射获取所有产品类型方法
	 	 @RequestMapping("/getAll")
	 	 @ResponseBody
	 	 public List<SpmsProductTypeDTO> getAll(){
	 		 return spmsProductTypeManager.getAll();
	 	 }	     
}
