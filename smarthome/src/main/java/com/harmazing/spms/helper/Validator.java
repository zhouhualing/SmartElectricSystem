package com.harmazing.spms.helper;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.device.dao.SpmsAirConditionDAO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.user.dao.SpmsUserDeviceDAO;
import com.harmazing.spms.user.entity.SpmsUserDevice;

public class Validator {
	
	private static Validator inst = new Validator();
	
	private Validator() {}
	
	public static Validator Instance(){
		return inst;
	}
	
	public Map<String, Object> checkUserExist(String userId , boolean ignoreStatus){
		UserDAO userDAO = (UserDAO)SpringUtil.getBeanByName("userDAO");
		UserEntity ue = userDAO.findOne(userId);
		if (ue == null){
			ue = userDAO.getByMobile(userId);
		}		
		if(ue != null){
			if(ignoreStatus){
				return SetResult(true,ue);
			}			    
			else{			
				if(ue.getStatus() != null &&  ue.getStatus() != 0){
					return SetResult();
				}else{
					return SetResult(true,ue);
				}
			}
		}
		return SetResult();
	}
	
	public Map<String, Object> checkUserExist(String userId){
	    return checkUserExist(userId,false);
	}
	
	public Map<String, Object> checkUserDeviceExist(String userId,String devId,Integer isPrimary){
		SpmsUserDeviceDAO spmsUserDeviceDAO = (SpmsUserDeviceDAO)SpringUtil.getBeanByName("spmsUserDeviceDAO");
		Map<String, Object> map = null;
		if(checkUserExist(userId) == null)
			return SetResult();
		SpmsUserDevice ud = spmsUserDeviceDAO.findByUserIdAndDeviceId(userId, devId,isPrimary);
		if (ud == null) {
			ud = spmsUserDeviceDAO.findByMM(userId, devId, isPrimary);
			if(ud != null){
				map = SetResult(true,ud);
				map.put("idType","mobile");
				return map;
			}			
		}else {
			map = SetResult(true,ud);
			map.put("idType","id");
			return map;
		}

		return SetResult();
	}
	
	public Map<String, Object> checkUserDeviceExist(String devId,Integer isPrimary){
		SpmsUserDeviceDAO spmsUserDeviceDAO = (SpmsUserDeviceDAO)SpringUtil.getBeanByName("spmsUserDeviceDAO");
		Map<String, Object> map = null;
		List<SpmsUserDevice> uds = spmsUserDeviceDAO.findByDeviceId(devId,isPrimary);
		if (uds == null || uds.isEmpty()) {
			uds = spmsUserDeviceDAO.findByDeviceMac(devId, isPrimary);
			if(uds != null && !uds.isEmpty()){
				map = SetResult(true,uds.get(0));
				map.put("idType","mobile");
				return map;
			}			
		}else {
			map = SetResult(true,uds.get(0));
			map.put("idType","id");
			return map;
		}

		return SetResult();
	}
	
	public Map<String, Object> checkUserDeviceExist(String userId,String devId){
		SpmsUserDeviceDAO spmsUserDeviceDAO = (SpmsUserDeviceDAO)SpringUtil.getBeanByName("spmsUserDeviceDAO");
		Map<String, Object> map = null;
		if(checkUserExist(userId) == null)
			return SetResult();
		SpmsUserDevice ud = spmsUserDeviceDAO.findByUserIdAndDeviceId(userId, devId);
		if (ud == null) {
			ud = spmsUserDeviceDAO.findByMM(userId, devId);
			if(ud != null){
				map = SetResult(true,ud);
				map.put("idType","mobile");
				return map;
			}			
		}else {
			map = SetResult(true,ud);
			map.put("idType","id");
			return map;
		}

		return SetResult();
	}
	
	public boolean checkAc(String acId){
		SpmsAirConditionDAO spmsAcDAO = (SpmsAirConditionDAO)SpringUtil.getBeanByName("spmsAirConditionDAO");
		SpmsAirCondition ac = spmsAcDAO.getByMac(acId);
		if (ac == null || ac.getGateway() == null || StringUtil.isNUllOrEmpty(ac.getGateway().getMac())) {			
			return false;
		}
		return true;
	}
	
	public boolean checkDeviceDTO(SpmsDeviceDTO devDTO){
		if(devDTO==null || StringUtil.isNUllOrEmpty(devDTO.getMac()) || devDTO.getType() == null)
			return false;
	    return true;
	}
	
	
	private  Map<String, Object> SetResult(boolean bTrue,Object result) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("success",bTrue);
		map.put("result",result);
		return map;
	}
	
	private  Map<String, Object> SetResult() {
        return SetResult(false,null);
	}
}
