package com.harmazing.spms.system.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.system.dao.SystemConfigDao;
import com.harmazing.spms.system.dto.SystemConfigDto;
import com.harmazing.spms.system.entity.SystemConfig;
import com.harmazing.spms.system.manager.SystemConfigManager;

@Service("systemConfigManager")
public class SystemConfigManagerImpl implements SystemConfigManager{
	@Autowired
	private SystemConfigDao systemConfigDao;
	
	@Override
	public SystemConfigDto getSystemConfig(SystemConfigDto systemConfigDto) {
		SystemConfig sc = systemConfigDao.findOne(systemConfigDto.getConfigName());
		if(null != sc){
			BeanUtils.copyProperties(sc, systemConfigDto);
		}
		return systemConfigDto;
	}

	@Transactional
	@Override
	public SystemConfigDto saveSystemConfig(SystemConfigDto systemConfigDto) throws Exception {
		SystemConfig sc = new SystemConfig();
		BeanUtils.copyProperties(systemConfigDto, sc);
		systemConfigDao.save(sc);
		if(sc.getConfigName().equals("logTime")){
			Map m=new HashMap();
			m.put("commandType",2);//设备到网关
			m.put("interval",sc.getConfigValue());
			m.put("messageType","SERVICEUPDATE");
			CommandUtil.asyncSendMessage(m);
		}else if(sc.getConfigName().equals("logTimeD")){
			Map m=new HashMap();
			m.put("commandType",1);//网关到netty
			m.put("interval",sc.getConfigValue());
			m.put("messageType","SERVICEUPDATE");
			CommandUtil.asyncSendMessage(m);
		}
		return systemConfigDto;
	}

}
