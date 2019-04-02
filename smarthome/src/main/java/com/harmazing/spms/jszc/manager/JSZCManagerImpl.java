package com.harmazing.spms.jszc.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.jszc.dao.JSZCDao;
import com.harmazing.spms.jszc.dto.JSZCDTO;
import com.harmazing.spms.jszc.entity.JSZCEntity;
import com.harmazing.spms.jszc.entity.JSZCEntityId;
import com.harmazing.spms.jszc.manager.JSZCManager;

@Service("jszcService")
public class JSZCManagerImpl implements JSZCManager{
	@Autowired
	private JSZCDao jszcDao;
	
	@Autowired
	private QueryDAO queryDAO;
	
	public List<Object[]> listFailureCause(){
		return jszcDao.listFailureCause();
	}

	@Override
	@Transactional
	public void doSave(List<JSZCDTO> jszcdto) {
		for (JSZCDTO dto : jszcdto) {
			JSZCEntity entity = new JSZCEntity();
			BeanUtils.copyProperties(dto, entity);
			jszcDao.save(entity);
		}
	}

	@Override
	public List<Map<String, Object>> listVariablesByProcId(String procId) {
		return queryDAO.getMapObjects("select taskId,iKey,iValue,processId from tb_workflow_variable t where t.processId = '" + procId + "'");
	}

	@Override
	public Map<String, Object> isQR(Map<String, Object> map) {
		List<JSZCEntity> list = jszcDao.getTaskVariable(map.get("taskId").toString());
		for (JSZCEntity jszcEntity : list) {
			if(null != jszcEntity.getiKey() && "qr".equals(jszcEntity.getiKey())){
				map.put(jszcEntity.getiKey(), true);
				continue;
			}
			map.put(jszcEntity.getiKey(), jszcEntity.getiValue());
		}
		return map;
	}

	@Override
	public Map<String, Object> QR(Map<String, Object> map) {
		JSZCEntity entity = new JSZCEntity();
		entity.setTaskId(map.get("taskId").toString());
		entity.setiKey("qr");
		entity.setiValue("1");
		jszcDao.save(entity);
		return map;
	}

	@Override
	public Map<String, Object> unQR(Map<String, Object> map) {
		JSZCEntityId id = new JSZCEntityId();
		id.setTaskId(map.get("taskId").toString());
		id.setiKey("qr");
		JSZCEntity entity = jszcDao.findOne(id);
		jszcDao.delete(entity);
		return map;
	}
}
