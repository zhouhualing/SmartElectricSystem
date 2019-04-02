package com.harmazing.spms.spmsuc.Manager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.manager.QueryManager;
import com.harmazing.spms.base.util.JackJsonUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.helper.DeviceWraperDTO;
import com.harmazing.spms.spmsuc.dto.DeviceStatusDTO;

@Service
public class SpmsAppQueryManager implements IManager {
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private QueryManager queryManager;

	public List<Map<String,Object>> getWdStatus(DeviceStatusDTO statusDTO) {
		List<Object[]> result = Lists.newArrayList();
		Timestamp last = null;
		String date = null;
		if (statusDTO.getStartTime() != null) {
			last = new Timestamp(statusDTO.getStartTime());
			date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		}

		String sql = null;
		int startIndex = statusDTO.getStartNumber() - 1 >= 0 ? statusDTO.getStartNumber() - 1 : 0;
		int count = statusDTO.getEndNumber() - statusDTO.getStartNumber() + 1 >= 0
				? statusDTO.getEndNumber() - statusDTO.getStartNumber() + 1 : 0;
		if (!StringUtil.isNUllOrEmpty(date)) {
			sql = String.format(
					"select * from spms_win_door_status where mac='%s' and operate_time >= '%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(), date, startIndex, count);
		} else {
			sql = String.format(
					"select * from spms_win_door_status where mac='%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(), startIndex, count);
		}
		List<Map<String, Object>> list = queryDAO.getMapObjects(sql);

		return list;
	}
	
	public List<Map<String,Object>> getPmPlugStatus(DeviceStatusDTO statusDTO) {
		List<Object[]> result = Lists.newArrayList();
		Timestamp last = null;
		String date = null;
		if (statusDTO.getStartTime() != null) {
			last = new Timestamp(statusDTO.getStartTime());
			date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		}

		String sql = null;
		
		if(!StringUtil.isNUllOrEmpty(date)){
			sql = String.format("select * from spms_onoff_pm_plug_status where mac='%s' and operate_time >= '%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),date,statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}else {
			sql = String.format("select * from spms_onoff_pm_plug_status where mac='%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}
		 List<Map<String,Object>> list = queryDAO.getMapObjects(sql);

		return list;
	}
	
	public List<Map<String,Object>> getHtSensorStatus(DeviceStatusDTO statusDTO) {
		List<Object[]> result = Lists.newArrayList();
		Timestamp last = null;
		String date = null;
		if (statusDTO.getStartTime() != null) {
			last = new Timestamp(statusDTO.getStartTime());
			date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		}

		String sql = null;
		
		if(!StringUtil.isNUllOrEmpty(date)){
			sql = String.format("select * from spms_ht_sensor_status where mac='%s' and operate_time >= '%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),date,statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}else {
			sql = String.format("select * from spms_ht_sensor_status where mac='%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}
		 List<Map<String,Object>> list = queryDAO.getMapObjects(sql);

		return list;
	}	
	public List<Map<String,Object>> getPirStatus(DeviceStatusDTO statusDTO) {
		List<Object[]> result = Lists.newArrayList();
		Timestamp last = null;
		String date = null;
		if (statusDTO.getStartTime() != null) {
			last = new Timestamp(statusDTO.getStartTime());
			date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		}

		String sql = null;
		
		if(!StringUtil.isNUllOrEmpty(date)){
			sql = String.format("select * from spms_pir_status where mac='%s' and operate_time >= '%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),date,statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}else {
			sql = String.format("select * from spms_pir_status where mac='%s' order by operate_time desc limit %d,%d",
					statusDTO.getMac(),statusDTO.getStartNumber()-1,statusDTO.getEndNumber()-statusDTO.getStartNumber()+1);
		}
		 List<Map<String,Object>> list = queryDAO.getMapObjects(sql);

		return list;
	}

}
