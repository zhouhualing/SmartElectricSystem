package com.harmazing.spms.log.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.dto.OrgDTO;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.log.dao.SpmsLogDAO;
import com.harmazing.spms.log.dto.SpmsLogDTO;
import com.harmazing.spms.log.entity.SpmsLog;
import com.harmazing.spms.log.manager.SpmsLogManager;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;


@Service("SpmsLogManager")
public class SpmsLogManagerImpl implements SpmsLogManager{
	@Autowired
	SpmsLogDAO  spmsLogDAO;
	
	@Autowired
	UserDAO  userDAO;
	@Override
	public SpmsLogDTO doSave(SpmsLogDTO c) {
		SpmsLog spmsLog =null;
		if(c.getId()!=null){
			spmsLog=spmsLogDAO.findOne(c.getId());
		}else{
			spmsLog=new SpmsLog();
		}
		
		BeanUtils.copyProperties(c, spmsLog);
		UserEntity user=userDAO.findOne(c.getSpmsUserDTO().getId());
		spmsLog.setCreateBy(user);
		spmsLogDAO.save(spmsLog);
		return null;
	}
	@Override
	public SpmsLogDTO getDetail(String id) {
		// TODO Auto-generated method stub
		SpmsLog spmsLog =null;
		SpmsLogDTO spmsLogDTO =new SpmsLogDTO();
		if(id !=null){
			spmsLog=spmsLogDAO.findOne(id);
			
			spmsLogDTO.setRemoteAddr(spmsLog.getRemoteAddr());
			spmsLogDTO.setException(spmsLog.getException());
			spmsLogDTO.setMethod(spmsLog.getMethod());
			spmsLogDTO.setRequestUri(spmsLog.getRequestUri());
			spmsLogDTO.setParams(spmsLog.getParams());
			spmsLogDTO.setType(spmsLog.getType());
		    spmsLogDTO.setCreateDate(spmsLog.getCreateDate());
			spmsLogDTO.setId(id);
			spmsLogDTO.setUserAgent(spmsLog.getUserAgent());
			UserDTO userDTO = new UserDTO();
			OrgDTO orgDTO =new OrgDTO();
		/*	BeanUtils.copyProperties(spmsLog.getCreateBy().getOrgEntity(), orgDTO);
			BeanUtils.copyProperties(spmsLog.getCreateBy(), userDTO);
			userDTO.setOrgDTO(orgDTO);
			spmsLogDTO.setCreateBy(userDTO);*/
			return spmsLogDTO;
		}
		else{
			
			return null;
		}
		
		
	
	
	}
}
