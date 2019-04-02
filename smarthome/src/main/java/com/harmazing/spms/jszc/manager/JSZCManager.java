package com.harmazing.spms.jszc.manager;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.jszc.dto.JSZCDTO;

public interface JSZCManager extends IManager{
	/**
	 * 获取故障原因字典
	 * @return
	 */
	public List<Object[]> listFailureCause();

	/**
	 * 存储技术支持信息
	 * @param list
	 */
	public void doSave(List<JSZCDTO> list);

	/**
	 * 根据ProcId查找所有变量
	 * @param object
	 * @return
	 */
	public List<Map<String, Object>> listVariablesByProcId(String procId);

	public Map<String, Object> isQR(Map<String, Object> map);

	public Map<String, Object> QR(Map<String, Object> map);

	public Map<String, Object> unQR(Map<String, Object> map);
}
