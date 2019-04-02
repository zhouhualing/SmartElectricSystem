package com.harmazing.spms.device.manager;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.device.dto.SpmsWarningSettingDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.area.dao.AreaDAO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.device.dao.SpmsWarningSettingDAO;
import com.harmazing.spms.device.dto.SpmsWarningSettingDTO;
import com.harmazing.spms.device.entity.SpmsWarningSetting;
import com.harmazing.spms.device.manager.SpmsWarningSettingManager;

/**
 * describe:
 * 
 * @author Zhaocaipeng since 2015年1月10日
 */
@Service("spmsWarningSettingManager")
public class SpmsWarningSettingManager implements IManager {

	@Autowired
	private SpmsWarningSettingDAO spmsWarningSettingDAO;
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private AreaDAO areaDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.harmazing.spms.device.manager.SpmsWarningSettingManager#getAreaSetInfo
	 * (com.harmazing.spms.device.dto.SpmsWarningSettingDTO)
	 */
	public SpmsWarningSettingDTO getAreaSetInfo(
			SpmsWarningSettingDTO spmsWarningSettingDTO) {
		String sql = "SELECT a.name,w.area_id,w.id,w.maxLoad,w.avgLoad,w.runDuration,w.gwJoinNum,w.acJoinNum,w.acAvgTemp,w.level"
				+ " from spms_area a LEFT JOIN  spms_warning_setting w on a.id= w.area_id "
				+ "where a.id ='" + spmsWarningSettingDTO.getAreaId() + "'";
		List<Map<String, Object>> list = queryDAO.getMapObjects(sql);
		if (list != null && list.size()>0) {
			Map<String, Object> m =list.get(0);
			spmsWarningSettingDTO.setName(m.get("name").toString());
			if(m.get("id")!=null){
			spmsWarningSettingDTO.setId(m.get("id").toString());
			spmsWarningSettingDTO.setMaxLoad((Integer) m.get("maxLoad"));
			spmsWarningSettingDTO.setAreaId(m.get("area_id").toString());
			spmsWarningSettingDTO.setAvgLoad((Integer) m.get("avgLoad"));
			spmsWarningSettingDTO.setRunDuration((Integer) m.get("runDuration"));
			spmsWarningSettingDTO.setAcAvgTemp((Float) m.get("acAvgTemp"));
			spmsWarningSettingDTO.setGwJoinNum((Integer) m.get("gwJoinNum"));
			spmsWarningSettingDTO.setAcJoinNum((Integer) m.get("acJoinNum"));
			spmsWarningSettingDTO.setLevel((Integer) m.get("level"));
			}
		}
		
		return spmsWarningSettingDTO;
	}

	public SpmsWarningSettingDTO doSaveAreaSet(
			SpmsWarningSettingDTO spmsWarningSettingDTO) {
		SpmsWarningSetting	spmsWarningSetting=spmsWarningSettingDAO.findOne(spmsWarningSettingDTO.getId());
		if(spmsWarningSetting==null){
		spmsWarningSetting=new SpmsWarningSetting();
		}
		BeanUtils.copyProperties(spmsWarningSettingDTO, spmsWarningSetting);
		Area area = areaDAO.findOne(spmsWarningSettingDTO.getAreaId());
		if (area != null) {
			spmsWarningSetting.setArea(area);
		}
		spmsWarningSettingDAO.save(spmsWarningSetting);
	
		return spmsWarningSettingDTO;
	}

	public boolean deleteAreaSet(SpmsWarningSettingDTO spmsWarningSettingDTO) {
		if (StringUtils.isNotBlank(spmsWarningSettingDTO.getId())) {
			SpmsWarningSetting spmsWarningSetting = spmsWarningSettingDAO.findOne(spmsWarningSettingDTO.getId());
			//SpmsWarningSetting spmsWarningSetting = spmsWarningSettingDAO.FindOneObject(spmsWarningSettingDTO.getId());
			if (spmsWarningSetting != null) {
				spmsWarningSettingDAO.delete(spmsWarningSetting);
				return true;
			}
		}
		return false;
		
	}
}
