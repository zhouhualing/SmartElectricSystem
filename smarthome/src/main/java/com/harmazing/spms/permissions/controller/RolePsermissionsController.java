package com.harmazing.spms.permissions.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SearchFilter.Operator;
import com.harmazing.spms.permissions.dto.PermissionsDTO;
import com.harmazing.spms.permissions.dto.RolePermissionsDTO;
import com.harmazing.spms.permissions.manager.RolePermissionsManagerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/rolePermissions")
public class RolePsermissionsController {
	@Autowired
	private RolePermissionsManagerImpl rolePermissionsManager;
	/**
	 * 按ID取得某角色的信息
	 * @param rolePermissionsDTO
	 * @return
	 */
	@RequestMapping("/getInfo")
	@ResponseBody
	public RolePermissionsDTO getUserInfo(@RequestBody RolePermissionsDTO rolePermissionsDTO) {
		return rolePermissionsManager.getRolePermissionsById(rolePermissionsDTO);
	}
	
	/**
	 * 取得所有的菜单权限
	 * @return
	 */
	@RequestMapping("/getAllPermissions")
	@ResponseBody
	public List<PermissionsDTO> getAllPermissions(){
		return rolePermissionsManager.getAllPermissions();
	}
	
	/**
	 * 保存角色信息
	 * @param rolePermissionsDTO
	 * @return
	 */
	@RequestMapping("/doSave")
	@ResponseBody
	public RolePermissionsDTO doSave(@RequestBody RolePermissionsDTO rolePermissionsDTO){
		return rolePermissionsManager.doSave(rolePermissionsDTO);
	}
	
	/**
	 * 保存角色的菜单权限
	 * @param info
	 * @return
	 */
	@RequestMapping("/doSavePermissions")
	@ResponseBody
	public Map<String,Object> doSavePermissions(@RequestBody Map<String,Object> info){
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			result =  rolePermissionsManager.doSavePermissions(info);
			result.put("success", true);
			result.put("msg","角色菜单配置成功");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("msg","角色菜单配置错误");
			return result;
		}
		
	}
	
	
	/**
	 * 删除角色信息
	 * @param rolePermissionsDTO
	 * @return
	 */
	@RequestMapping("/doDelete")
	@ResponseBody
	public Map<String,Object> doDelete(@RequestBody Map<String,Object> info){
		return rolePermissionsManager.deleteRole((String)info.get("id"));
	}
	
	/**
	 * 批量删除角色
	 * @param ids
	 * @return
	 */
	@RequestMapping("/doDeletes")
	@ResponseBody
	public List<String> doDelete(@RequestBody List <String> ids){
		rolePermissionsManager.deleteRoles(ids);
		return ids;
	}
}
