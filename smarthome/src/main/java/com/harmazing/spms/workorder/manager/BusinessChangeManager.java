package com.harmazing.spms.workorder.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.harmazing.spms.workorder.dto.SpmsChangeProductDTO;

/**
 * 业务变更manager
 * @author wang.bing
 *
 */
public interface BusinessChangeManager {
	/**
	 * 获取用户欠款
	 * @param para
	 * @return
	 */
	public Double getUserBanlance(@RequestBody Map<String,Object> para);
	
	/**
	 * 获取用户产品
	 * @param para
	 * @return
	 */
	public List getUserProduct(@RequestBody Map<String,Object> para);
	
	/**
	 * 获取产品列表
	 * @param request
	 * @return
	 */
	public List getDevices(HttpServletRequest request);
	
	/**
	 * 获取用户详细信息
	 * @param para
	 * @return
	 */
	public Map<String,Object> getUserDetail(@RequestBody Map<String,Object> para);
	
	/**
	 * 获取用户设备信息
	 * @param para
	 * @return
	 */
	public List getUserDevices(@RequestBody Map<String,Object> para);
	
	/**
	 * 保存更改的设备
	 * @param productDto
	 * @return
	 */
	public SpmsChangeProductDTO saveChangeDevices(@RequestBody SpmsChangeProductDTO productDto);
	
	/**
	 * 退订
	 * @param productDto
	 * @return
	 */
	public SpmsChangeProductDTO saveTdDevices(@RequestBody SpmsChangeProductDTO productDto);
	
	/**
	 * 检查设备
	 * @param para
	 * @return
	 */
	public String checkDevice(@RequestBody Map<String,Object> para);
	
	/**
	 * 批量检查设备
	 * @param para
	 * @return
	 */
	public List checkDevices(@RequestBody Map<String,Object> para);

	/**
	 * 验证设备的绑定数量
	 * @param productDto
	 * @return
	 */
	public Map<String, Object> countUserDevices(SpmsChangeProductDTO productDto);

	public Map<String,Object> getUserByWorkOrder(Map<String, Object> para);

	public Map<String, Object> listStorage(Map<String, Object> para);
}
