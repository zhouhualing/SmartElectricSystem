package com.harmazing.spms.desktop.manager;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.desktop.dto.SpmsAccointBillDTO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;

public interface SpmsAccountBillManager extends IManager {
	
	public List<SpmsAccountBill> findAllByUser(String userId);
	
	public SpmsAccointBillDTO getInfo(String id);
	/**
	 * 生成账单
	 * @return
	 */
	public boolean initAccountBill();
	/**
	 * 定时任务生成账单
	 * @return
	 */
	public boolean executeAccountBill();
	
	/**
	 * 发送订单邮件
	 * @return
	 */
	public int sendAccountEmail(String spmsAccountEmailId);

	/**
	 * 创建表格数据
	 * @param id
	 * @param map 
	 */
	public Map<String, Object> createChart1(String id, Map<String, Object> map);

	/**
	 * 创建表格数据
	 * @param id
	 * @param map 
	 */
	public Map<String, Object> createChart2(String id, Map<String, Object> map);
}
