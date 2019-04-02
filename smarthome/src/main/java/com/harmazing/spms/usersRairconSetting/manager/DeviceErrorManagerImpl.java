package com.harmazing.spms.usersRairconSetting.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.usersRairconSetting.dto.DeviceErrorDTO;
import com.harmazing.spms.usersRairconSetting.entity.DeviceErrorRecord;
import com.harmazing.spms.usersRairconSetting.manager.DeviceErrorManager;

@Service("deviceErrorManager")
public class DeviceErrorManagerImpl implements DeviceErrorManager{

	@Autowired
	public QueryDAO queryDao;
	
	public DeviceErrorDTO queryDeviceError(String deviceId){
		String sql="select * from ("
				+ "select s.deviceId,s.errorCode,d.errorDetail from spms_device_error s left join dict_device_error d "
				+ "on s.errorCode=d.errorCode where s.deviceId='"+deviceId+"' ORDER BY s.createDate desc LIMIT 0,1"
				+ " ) t where t.errorCode!='E0'";
		List<Map<String,Object>> lst=queryDao.getMapObjects(sql);
		DeviceErrorDTO dto=null;
		if(lst!=null && lst.size()>0){
			Map m=lst.get(0);
			dto=new DeviceErrorDTO();
			dto.setErrorCode((String) m.get("errorCode"));
			dto.setErrorExplain((String)m.get("errorDetail"));
			dto.setDeviceId(deviceId);
		}
		return dto;
	}
}
