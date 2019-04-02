package com.harmazing.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.harmazing.entity.Device;
import com.harmazing.mapper.DeviceExceptionMapper;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.service.DeviceExceptionService;

public class DeviceExceptionServiceImpl extends ServiceImpl implements DeviceExceptionService {

	@Override
	public void saveorupdateException(String id,String mac, String exceptionCode) {
		SqlSession session = sqlSessionFactory.openSession(false);
		try{
			DeviceMapper dm=session.getMapper(DeviceMapper.class);
			Device device=dm.getDeviceByMac(mac);
			String deviceId="";
			if(device!=null){
				deviceId=device.getId();
			}
			DeviceExceptionMapper exception=session.getMapper(DeviceExceptionMapper.class);
//			if(exception.getAcException(deviceId)!=null){
//				exception.updateAcException(deviceId, exceptionCode);
//			}else{
				exception.setAcException(id,deviceId,exceptionCode);
//			}
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			session.close();
		}
	}

}
