package com.harmazing.spms.user.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.UserIdGenerator;
import com.harmazing.spms.base.util.SearchFilter.Operator;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.MD5EncryptUtil;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.common.entity.IEntity;
import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.device.dao.SpmsAirConditionDAO;
import com.harmazing.spms.device.dao.SpmsCentralAcDAO;
import com.harmazing.spms.device.dao.SpmsGatewayDAO;
import com.harmazing.spms.device.dao.SpmsPirDAO;
import com.harmazing.spms.device.dao.SpmsPlugAcDAO;
import com.harmazing.spms.device.dao.SpmsWinDoorDAO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsCentralAc;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.entity.SpmsGateway;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.entity.SpmsPlugAc;
import com.harmazing.spms.device.entity.SpmsWinDoor;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.DeviceDAOAccessor;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.helper.UserDeviceRespWraperDTO;
import com.harmazing.spms.helper.UserDeviceWraperDTO;
import com.harmazing.spms.helper.UserWraperDTO;
import com.harmazing.spms.product.dao.SpmsProductDAO;
import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
import com.harmazing.spms.product.dto.SpmsProductDTO;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.dao.SpmsUdGroupDAO;
import com.harmazing.spms.user.dao.SpmsUserAsyncDAO;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserDeviceDAO;
import com.harmazing.spms.user.dto.SpmsUserDTO;
import com.harmazing.spms.user.dto.SpmsUserDeviceDTO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.entity.SpmsUserAsyncEntity;
import com.harmazing.spms.user.entity.SpmsUserDevice;
import com.harmazing.spms.user.manager.SpmsUserManager;
import com.harmazing.spms.workorder.dao.SpmsWorkOrderDAO;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import com.harmazing.spms.workorder.manager.SpmsWorkOrderManager;

@Service("spmsUserManager")
public class SpmsUserManager implements IManager {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SpmsUserManager.class);
	
	@Autowired 
	private SpmsUserDAO spmsUserDAO;
	@Autowired
	private UserDAO userDAO; 
	@Autowired
	private SpmsDeviceManager spmsDeviceManager;
	@Autowired
	private SpmsGatewayDAO spmsGwDAO;
	@Autowired
	private SpmsAirConditionDAO spmsAcDAO;
	@Autowired
	private SpmsPlugAcDAO spmsPlugAcDAO;	
	@Autowired
	private SpmsCentralAcDAO spmsCentralAcDAO;
	
	@Autowired
	private SpmsWinDoorDAO spmsWdDAO;
	@Autowired
	private SpmsPirDAO spmsPirDAO;	
	@Autowired
	private SpmsPlugAcDAO spmsPlugDAO;
	@Autowired
	private SpmsProductTypeDAO spmsProductTypeDAO;
	@Autowired
	private SpmsProductDAO spmsProductDAO;
	@Autowired
	private SpmsWorkOrderManager spmsWorkOrderManager;
	
	@Autowired
	private SpmsWorkOrderDAO spmsWorkOrderDAO;

	@Autowired
	private SpmsUserDeviceDAO spmsUserDeviceDAO;
	
	@Autowired
	private SpmsUdGroupDAO spmsUdGroupDAO;
	@Autowired
	private SpmsUserAsyncDAO spmsUserAsyncDAO;
	
	
	/**
	 * add user
	 */	
	@Transactional
	public UserWraperDTO addUser( UserWraperDTO userDTO){
		
		//mobile and password could not be empty.
		String mobile = userDTO.getUser().getMobile();
		String password = userDTO.getUser().getPassword();		
		
		userDTO.setUserResult(ErrorCodeConsts.Success);
		if(mobile.trim().isEmpty() || password.trim().isEmpty()){
			userDTO.setUserResult(ErrorCodeConsts.UserNamePassEmpty);
		}		

		if(userDAO.getByMobile(mobile) != null ||
				spmsUserDAO.getByMobile(mobile) != null){
			userDTO.setStatus(ErrorCodeConsts.UserAlreadyExist);
			userDTO.setDescription(ErrorCodeConsts.getValue(ErrorCodeConsts.UserAlreadyExist));
			//this.updateUserFieldName(mobile, "password", MD5EncryptUtil.MD5(password));
			this.updateUserFieldName(mobile, "password", password);
			return userDTO;
		}	
		
		UserEntity tbUser = new UserEntity();
		SpmsUser spmsUser = new SpmsUser();
		
		BeanUtils.copyProperties(userDTO.getUser(), tbUser);
		
		tbUser.setUserCode(UserIdGenerator.getNext());
		tbUser.setStatus(0);
		//tbUser.setPassword(MD5EncryptUtil.MD5(tbUser.getPassword()));
		tbUser.setPassword(tbUser.getPassword());
		if(tbUser.getUserType()==null){
			tbUser.setUserType(1); // general user
		}
		
		UserEntity userPersist = userDAO.save(tbUser);
		
		spmsUser.setMobile(mobile); //TODO ?		
		spmsUser.setUser(userPersist);
		spmsUserDAO.save(spmsUser);
		
		BeanUtils.copyProperties(tbUser, userDTO);
		
		return userDTO;
	}
	
	@Transactional
	public UserDeviceWraperDTO addUserDeviceById(String userId, String devId,Integer isPrimary,Integer devType)
	{
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		UserEntity ue = userDAO.findOne(userId);
		if (ue == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
			return userWraperDTO;
		}
		Object device = null;
		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);

		try {
			Method m = Class.forName(DeviceDAOAccessor.getInstance().getClsName(devType)).getMethod("findOne" , String.class);
			device = m.invoke(dao, devId);
		} catch (Exception e) {
			userWraperDTO.setUserResult(ErrorCodeConsts.MethodNotExist);
			e.printStackTrace();
			return userWraperDTO;
		}
		if (device == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return userWraperDTO;
		}
		return addUserDevice(ue,(SpmsDeviceBase)device,isPrimary,devType);
	}
	
	public UserDeviceWraperDTO addUserDeviceByMM(String mobile, String mac, Integer isPrimary, Integer devType){
		return addUserDevice(mobile,mac,isPrimary,devType);
	}
	
	@Transactional
	public UserDeviceWraperDTO addUserDevice(String mobile, String mac, Integer isPrimary, Integer devType) {
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		UserEntity ue = userDAO.getByMobile(mobile);
		if (ue == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
			return userWraperDTO;
		}
		Object device = null;
		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);

		try {
			Method m = Class.forName(DeviceDAOAccessor.getInstance().getClsName(devType)).getMethod("findByMAC",
					String.class);
			device = m.invoke(dao, mac);
		} catch (Exception e) {
			userWraperDTO.setUserResult(ErrorCodeConsts.MethodNotExist);
			e.printStackTrace();
			return userWraperDTO;
		}
		if (device == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return userWraperDTO;
		}

		return addUserDevice(ue, (SpmsDeviceBase) device, isPrimary, devType);
	}
	
	@Transactional
	private UserDeviceWraperDTO addUserDevice(UserEntity ue, SpmsDeviceBase device, Integer isPrimary, Integer devType) {

		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();


		
		//check existing EnumTypesConsts.User_Primary
		if (isPrimary == EnumTypesConsts.UserDeviceType.User_Primary) {
			spmsUserDeviceDAO.deletebyDevId(device.getId());
		}
		else{
			if(spmsUserDeviceDAO.findByUserIdAndDeviceId(ue.getId(), device.getId()) != null){
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDevicePairAlreadyExist);
				return userWraperDTO;
			}
		}

		SpmsUserDevice ud = new SpmsUserDevice();
		ud.setIsPrimary(isPrimary);
		ud.setDeviceType(devType);
		ud.setDeviceId(device.getId());
		ud.setUserId(ue.getId());
		ud.setMobile(ue.getMobile());
		ud.setMac(device.getMac());		
		
		//save to DB
		spmsUserDeviceDAO.save(ud);	
		BeanUtils.copyProperties(ud,userWraperDTO.getUserDevice());
		LOGGER.info("addUserDeviceById success");
		return userWraperDTO;
	}
	
	
	
	
	//user_id :uuid, device_id:uuid or mobile,mac
	@Transactional
	public UserDeviceWraperDTO deleteUserDevice(String user_id, String device_id) {
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		
		SpmsUserDevice ud = spmsUserDeviceDAO.findByUserIdAndDeviceId(user_id, device_id);
		if(ud==null){
			ud = spmsUserDeviceDAO.findByMM(user_id, device_id);
			if(ud == null){
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
				return userWraperDTO;
			}
		}
		//spmsUserDeviceDAO.delete(ud);
		spmsUserDeviceDAO.deletebyId(ud.getId());
		BeanUtils.copyProperties(ud,userWraperDTO.getUserDevice());
		return userWraperDTO;
	}
	
	@Transactional
	public void deleteUserDeviceById(String id) {
		spmsUserDeviceDAO.deletebyId(id);
	}
	
	@Transactional
	public UserWraperDTO resetUserDevice(String dev_id,String dev_mac) {
		UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();		
		SpmsDeviceDTO deviceDTO = null; //spmsDeviceManager.getDeviceDTOByMac(dev_mac);
		String devId = null;
		if(StringUtil.isNUllOrEmpty(dev_id) &&
				StringUtil.isNUllOrEmpty(dev_mac))
			throw new InvalidParameterException("Both dev_id and dev_mac are null.");
		
		if(!StringUtil.isNUllOrEmpty(dev_id)){
			devId = dev_id;
		}else{
			deviceDTO = spmsDeviceManager.getDeviceDTOByMac(dev_mac);
			devId = deviceDTO.getId();
		}

	

		if (!StringUtil.isNUllOrEmpty(devId)) {
			List<SpmsUserDevice> uds =  spmsUserDeviceDAO.findByDeviceId(devId);
			if (uds != null && uds.size() > 0) {
				for (SpmsUserDevice ud : uds) {
					//deleteUdGroupByUdId(ud.getId());
					spmsUserDeviceDAO.delete(ud);
				}
			}
		}
		return userWraperDTO;
	}
	
	@Transactional
	public UserWraperDTO resetUserDevice(String dev_id) {
		UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();
		spmsUserDeviceDAO.deletebyDevId(dev_id);
		return userWraperDTO;
	}
	
	@Transactional
	public UserWraperDTO updateUserImage(String mobile, Byte[] image) {
		UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();

		SpmsUserAsyncEntity uae = spmsUserAsyncDAO.getByMobile(mobile);
		if(uae == null){
			uae = new SpmsUserAsyncEntity();
			uae.setMobile(mobile);
		}
		uae.setImage(image);
		
		spmsUserAsyncDAO.save(uae);
		//BeanUtils.copyProperties(uae, userWraperDTO.getUser());
		return userWraperDTO;	
	}
	
	@Transactional
	public UserWraperDTO getUserImage(String mobile) {
		UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();
		//reduce blob occupying memory
		//SpmsUserAsyncEntity uae = spmsUserAsyncDAO.getByMobile(mobile);
		userWraperDTO.getUser().setImage(spmsUserAsyncDAO.getImageByMobile(mobile));
		return userWraperDTO;	
	}

	@Transactional
	public UserWraperDTO updateUserFieldName(String user_id, String fieldName, String newValue) {
		UserWraperDTO userWraperDTO = new UserWraperDTO();
		UserDTO userDTO = new UserDTO();
		userWraperDTO.setUser(userDTO);
		
		userWraperDTO.getUser().setMobile(user_id);
		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		UserEntity ue = userDAO.getByMobile(user_id);

		if(ue==null){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
			return userWraperDTO;
		}
		byte[] items = fieldName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		String methodName = "set" + new String(items);

		try {
			Method m = UserEntity.class.getMethod(methodName, String.class);
			m.invoke(ue, newValue);
			userDAO.save(ue);
			
			//update DTO 
			m = userWraperDTO.getUser().getClass().getMethod(methodName, String.class);
			m.invoke(userWraperDTO.getUser(), newValue);
			return userWraperDTO;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		userWraperDTO.setUserResult(ErrorCodeConsts.UnknowError);
		return userWraperDTO;
	}
	
	@Transactional
	public SpmsUserDTO doSave(SpmsUserDTO spmsUserDTO) {
		SpmsUser spmsUser = null;
		
		if(StringUtils.isNotBlank(spmsUserDTO.getUserId())){
			spmsUser = spmsUserDAO.findOne(spmsUserDTO.getUserId());
			/*只更新基本设备信息*/
			BeanUtils.copyProperties(spmsUserDTO, spmsUser);
			/*保存区域*/
//			if(StringUtils.isNotBlank(spmsUserDTO.getEleAreaId().toString())){
//				spmsUser.setEleArea(areaDAO.findOne(spmsUserDTO.getEleAreaId()));
//			}
//			if(StringUtils.isNotBlank(spmsUserDTO.getBizAreaId().toString())){
//				spmsUser.setBizArea(areaDAO.findOne(spmsUserDTO.getBizAreaId()));
//			}
			/* 修改订户登录信息 */
			spmsUser.getUser().setMobile(spmsUser.getMobile());
			spmsUser.getUser().setUserCode(spmsUser.getMobile());
			spmsUser.getUser().setUserName(spmsUser.getFullname());
			spmsUser.getUser().setEmail(spmsUser.getEmail());
			
			/* 修改订户时为订户的工单进行信息同步 */
			if(null != spmsUser.getIdNumber() && !"".equals(spmsUser.getIdNumber())){
				List<SpmsWorkOrder> list = spmsWorkOrderDAO.findByIdNumber(spmsUser.getIdNumber());
				for (SpmsWorkOrder spmsWorkOrder : list) {
					spmsWorkOrder.setSpmsUserName(spmsUser.getFullname());
					spmsWorkOrder.setSpmsUserMobile(spmsUser.getMobile());
					spmsWorkOrder.setEmail(spmsUser.getEmail());
					spmsWorkOrderDAO.save(spmsWorkOrder);
				}
			}
			
			spmsUserDAO.save(spmsUser);
			BeanUtils.copyProperties(spmsUserDTO, spmsUser);
		}else if(StringUtils.isNotBlank(spmsUserDTO.getId())) {
			/*id不为空，说明订户基本信息已添加，需要绑定网关*/
			if(spmsUserDTO.getUpdateFlag()==0){
				spmsUser = spmsUserDAO.findOne(spmsUserDTO.getId());
				/*只更新基本设备信息*/
				BeanUtils.copyProperties(spmsUserDTO, spmsUser);
				/*保存区域*/
//				if(StringUtils.isNotBlank(spmsUserDTO.getEleAreaId().toString())){
//					spmsUser.setEleArea(areaDAO.findOne(spmsUserDTO.getEleAreaId()));
//				}
//				if(StringUtils.isNotBlank(spmsUserDTO.getBizAreaId().toString())){
//					spmsUser.setBizArea(areaDAO.findOne(spmsUserDTO.getBizAreaId()));
//				}
				/* 修改订户登录信息 */
				spmsUser.getUser().setMobile(spmsUser.getMobile());
				spmsUser.getUser().setUserCode(spmsUser.getMobile());
				spmsUser.getUser().setUserName(spmsUser.getFullname());
				spmsUser.getUser().setEmail(spmsUser.getEmail());
				
				/* 修改订户时为订户的工单进行信息同步 */
				if(null != spmsUser.getIdNumber() && !"".equals(spmsUser.getIdNumber())){
					List<SpmsWorkOrder> list = spmsWorkOrderDAO.findByIdNumber(spmsUser.getIdNumber());
					for (SpmsWorkOrder spmsWorkOrder : list) {
						spmsWorkOrder.setSpmsUserName(spmsUser.getFullname());
						spmsWorkOrder.setSpmsUserMobile(spmsUser.getMobile());
						spmsWorkOrder.setEmail(spmsUser.getEmail());
						spmsWorkOrderDAO.save(spmsWorkOrder);
					}
				}
				
				spmsUserDAO.save(spmsUser);
				BeanUtils.copyProperties(spmsUserDTO, spmsUser);
			}else{
				/*进行网关的更新*/
				/*设备不为空就进行设备的保存*/
				//TODO
//				if(StringUtils.isNotBlank(spmsUserDTO.getMac())){
//					spmsUser.setSpmsDevice(spmsDeviceManager.findDeviceByMac(spmsUserDTO.getMac()));
//				}
			}
			
		} else{
			//说明此时只是订户基本信息的添加
			/*先进行账号的生成*/
			UserEntity ue = new UserEntity();
			ue.setUserCode(spmsUserDTO.getMobile());
			ue.setPassword(UserEntity.DEFAULT_PWD);
			ue.setUserName(spmsUserDTO.getFullname());
			ue.setMobile(spmsUserDTO.getMobile());
			ue.setEmail(spmsUserDTO.getEmail());
			ue.setUserType(2);//订户
			UserEntity udPersist = userDAO.save(ue);
			/*订户的生成*/
			spmsUser = new SpmsUser();
			spmsUser.setUser(udPersist);
			BeanUtils.copyProperties(spmsUserDTO, spmsUser);
			/*保存区域*/
//			spmsUser.setEleArea(areaDAO.findOne(spmsUserDTO.getEleAreaId()));
//			spmsUser.setBizArea(areaDAO.findOne(spmsUserDTO.getBizAreaId()));
			spmsUser.setUser(ue);
			spmsUserDAO.save(spmsUser);
			spmsUserDTO.setId(spmsUser.getId());
		}

		//add spmsworkorder reference
		if(spmsUserDTO.getSpmsWorkOrderId() != null) {
			spmsUser.setSpmsWorkOrder(spmsWorkOrderManager.findBySpe(DynamicSpecifications.bySearchFilter(new SearchFilter("id",spmsUserDTO.getSpmsWorkOrderId()), SpmsWorkOrder.class)).get(0));
		}
		//spmsUserDAO.save(spmsUser);
		//spmsUserDTO.setId(spmsUser.getId());
		return spmsUserDTO;
	}
	

	
	public SpmsUserDTO getSpmsUser(SpmsUserDTO spmsUserDTO) {
		SpmsUser spmsUser = spmsUserDAO.findOne(spmsUserDTO.getId());
		BeanUtils.copyProperties(spmsUser, spmsUserDTO);
//		if(spmsUser.getBizArea() != null) {
//		    spmsUserDTO.setBizAreaId(spmsUser.getBizArea().getId());
//		    spmsUserDTO.setBizAreaName(spmsUser.getBizArea().getName());
//		}
//		if(spmsUser.getEleArea() != null) {
//		    spmsUserDTO.setEleAreaId(spmsUser.getEleArea().getId());
//		    spmsUserDTO.setEleAreaName(spmsUser.getEleArea().getName());
//		}
		//spmsUserDTO.setTypeText(DictUtil.getDictValue("spmsuser_type", spmsUser.getType().toString()));
//		if(spmsUser.getSpmsDevice()!=null && spmsUser.getSpmsDevice().getId()!=null){
//			spmsUserDTO.setSpmsDevice(spmsUser.getSpmsDevice().getId());
//			spmsUserDTO.setMac(spmsUser.getSpmsDevice().getMac());
//		}
		
		if(	spmsUser.getUser()!=null && spmsUser.getUser().getStatus()!=null){
			spmsUserDTO.setStatus(spmsUser.getUser().getStatus());
		}
		spmsUserDTO.setTypeText(DictUtil.getDictValue("spmsuser_type", spmsUser.getType().toString()));
		return spmsUserDTO;
	}

	
	@Transactional(readOnly = false)
	public boolean deleteByIds(String ids) {
		/*if(StringUtils.isNotBlank(ids)){
    		if(ids.indexOf(',')>0){
    			String[] idArra = ids.split(",");
    			for(String id : idArra){
    				List<SpmsProduct> list1 = spmsProductDAO.getSpmsProductByUserid(id);
    				for(int i = 0 ; i < list1.size() ;i++){
    					List<SpmsUserProductBinding> list2 = spmsUserProductBindingDAO.getUserBinding(id, list1.get(i).getId());
    					for(int j = 0 ; j < list2.size() ;j ++){
    						SpmsDevice sd = list2.get(j).getSpmsDevice();
    						sd.setStatus(1);
    						sd.setStorage(2);
    						//sd.setOperStatus(null);
    						spmsDeviceDAO.save(sd);
    						spmsUserProductBindingDAO.delete(list2.get(j));
    					}
    					spmsProductDAO.delete(list1.get(i));
    				}
    				SpmsDevice gw = spmsUserDAO.findOne(id).getSpmsDevice();
    				gw.setStatus(1);
    				gw.setStorage(2);
    				gw.setOperStatus(0);
					spmsDeviceDAO.save(gw);
    				spmsUserDAO.doDeleteById(id);
    			}
    			return true;
    		}else{
    			List<SpmsProduct> list1 = spmsProductDAO.getSpmsProductByUserid(ids);
    			for(int i = 0 ; i < list1.size() ;i++){
					List<SpmsUserProductBinding> list2 = spmsUserProductBindingDAO.getUserBinding(ids, list1.get(i).getId());
					for(int j = 0 ; j < list2.size() ;j ++){
						SpmsDevice sd = list2.get(j).getSpmsDevice();
						sd.setStatus(1);
						sd.setStorage(2);
						//sd.setOperStatus(null);
						spmsDeviceDAO.save(sd);
						spmsUserProductBindingDAO.delete(list2.get(j));
					}
					spmsProductDAO.delete(list1.get(i));
				}
    			if(spmsUserDAO.findOne(ids).getSpmsDevice()!=null){
    				SpmsDevice gw = spmsUserDAO.findOne(ids).getSpmsDevice();
    				gw.setStatus(1);
    				gw.setStorage(2);
    				gw.setOperStatus(0);
    				spmsDeviceDAO.save(gw);
    			}
    			spmsUserDAO.doDeleteById(ids);
    			return true;
    		}
    	}else{
    		return false;
    	}*/
//		假删除
		if(StringUtils.isNotBlank(ids)){
			if(ids.indexOf(',')>0){
				String[] idArra = ids.split(",");
				for (String id : idArra) {
					SpmsUser user = spmsUserDAO.findOne(id);
					UserEntity ue = user.getUser();
					if (ue != null) {
						ue.setStatus(2);
						userDAO.save(ue);
					}
				}
				return true;
			}else{
				SpmsUser user = spmsUserDAO.findOne(ids);
				UserEntity ue = user.getUser();
				if (ue != null) {
					ue.setStatus(2);
					userDAO.save(ue);
					return true;
				}
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public boolean deleteById(String id){
		if(StringUtils.isNotBlank(id)){
				SpmsUser user = spmsUserDAO.findOne(id);
				UserEntity ue = user.getUser();
				if (ue != null) {
					if (ue.getStatus() != null && ue.getStatus() == 0) {
						ue.setStatus(2);
					}else{
						ue.setStatus(0);
					}
					userDAO.save(ue);
					return true;
				}
			}
		return false;
	}
	
	// delete user group
	@Transactional
	public void deleteUdGroupByGroupId(String groupId) {
		spmsUdGroupDAO.deletebyGroupId(groupId);
	}

	@Transactional
	public void deleteUdGroupByUdId(String udId) {
		spmsUdGroupDAO.deletebyUdId(udId);
	}
	
	public UserWraperDTO getWraperByMobile(String mobile) {
		
		UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();
		UserEntity su = userDAO.getByMobile(mobile);
		if(su!=null)
			BeanUtils.copyProperties(su, userWraperDTO.getUser());
		return userWraperDTO;
	}
	
	public SpmsUserDTO getByMobile(String mobile) {
		SpmsUser su = spmsUserDAO.getByMobile(mobile);
		SpmsUserDTO suDTO = new SpmsUserDTO();
		if(su!=null){		
			BeanUtils.copyProperties(su, suDTO);
			suDTO.setTypeText(DictUtil.getDictValue("spmsuser_type", su.getType().toString()));
		}
		return suDTO;
	}
	
	public SpmsUser getOnlyByMobile(String mobile) {
		SpmsUser su = spmsUserDAO.getByMobile(mobile);
		return su;
	}
	
	public SpmsUserDTO getByUserid(String userid) {
		SpmsUser su = spmsUserDAO.findOne(userid);
		SpmsUserDTO suDTO = new SpmsUserDTO();
		
		BeanUtils.copyProperties(su, suDTO);
		
		return suDTO;
	}

	
	public List<SpmsProductTypeDTO> getAllProductTypeInfo() {
		List<SpmsProductType> list = spmsProductTypeDAO.findAll();
		List<SpmsProductTypeDTO> result = Lists.newArrayList();
		for(int i = 0 ;i< list.size();i++){
			if(list.get(i).getDeleteStauts()==0){
			SpmsProductTypeDTO sptd = new SpmsProductTypeDTO();
			sptd.setId(list.get(i).getId());
			sptd.setNames(list.get(i).getNames());
			sptd.setValidPeriod(list.get(i).getValidPeriod());
			sptd.setValidPeriodText(DictUtil.getDictValue("producttype_validperiod", list.get(i).getValidPeriod()));
			sptd.setMulu(DictUtil.getDictValue("producttype_muluid", list.get(i).getMuluId().toString()));
			sptd.setMuluId(list.get(i).getMuluId());
			sptd.setConfig(DictUtil.getDictValue("producttype_config", list.get(i).getConfigurationInformation().toString()));
			sptd.setConfigurationInformation( list.get(i).getConfigurationInformation());
			sptd.setLianDong(list.get(i).getLianDong());
			sptd.setKongTiaoCount(list.get(i).getKongTiaoCount());
			sptd.setChuangGanCount(list.get(i).getChuangGanCount());
			sptd.setCountRmb(list.get(i).getCountRmb());
			sptd.setZhiLengMax(list.get(i).getZhiLengMax());
			sptd.setZhiLengMix(list.get(i).getZhiLengMix());
			sptd.setZhiReMax(list.get(i).getZhiReMax());
			sptd.setZhiReMix(list.get(i).getZhiReMix());
			sptd.setAreaName(list.get(i).getArea().getName());
			sptd.setDescribes(list.get(i).getDescribes());
			result.add(sptd);
			}
		}
		return result;
	}
	
	public SpmsProductDTO addUserProductBindWF(SpmsProductDTO spmsProductDTO) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//订户
		SpmsUser user = spmsUserDAO.findOne(spmsProductDTO.getSpmsUserDTO().getId());
		//产品
		SpmsProductType spmsProductType = spmsProductTypeDAO.findOne(spmsProductDTO.getSpmsProductTypeDTO().getId());
		SpmsProduct spmsProduct = new SpmsProduct();
		spmsProduct.setSpmsProductType(spmsProductType);
		spmsProduct.setStatus(2);//初始产品为激活-- 绑了设备就是激活的。
		spmsProduct.setSpmsUser(user);
		if(null != spmsProductDTO.getSubscribeDate()){
			spmsProduct.setSubscribeDate(df.parse(df.format(spmsProductDTO.getSubscribeDate())));
		}
		//给产品到期时间赋值
		int validPeriod = Integer.parseInt(spmsProductType.getValidPeriod());
//		if(validPeriod==1){
			Calendar c = Calendar.getInstance();
			c.setTime(spmsProduct.getSubscribeDate());
			c.add(Calendar.MONTH, validPeriod);
			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
//		}else if(validPeriod==2){
//			Calendar c = Calendar.getInstance();
//			c.setTime(spmsProduct.getSubscribeDate());
//			c.add(Calendar.MONTH, 6);
//			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
//		}else if(validPeriod==3){
//			Calendar c = Calendar.getInstance();
//			c.setTime(spmsProduct.getSubscribeDate());
//			c.add(Calendar.YEAR, 1);
//			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
//		}
		
		spmsProductDAO.save(spmsProduct);
		if(spmsProductDTO.getSpmsWorkOrderId() != null) {
			spmsProduct.setSpmsWorkOrder(spmsWorkOrderManager.findBySpe(DynamicSpecifications.bySearchFilter(new SearchFilter("id",spmsProductDTO.getSpmsWorkOrderId()), SpmsWorkOrder.class)).get(0));
		}
		return spmsProductDTO;
	}

	
	public Map<String, Object> ValidationMobile(String userid, String mobile) {
		Map<String, Object> result = Maps.newHashMap();
		if(StringUtil.isNUll(userid)){
			SpmsUser u = spmsUserDAO.getByMobile(mobile);
			if(u == null || StringUtil.isNUll(u.getId())){
				result.put("success", true);
				result.put("msg", "该手机号可以使用");
			}else{
				result.put("success", false);
				result.put("msg", "该手机号已被占用");
			}
		}else{
			SpmsUser u = spmsUserDAO.getByMobile(mobile);
			SpmsUser user = spmsUserDAO.findOne(userid);
			if(u == null || StringUtil.isNUll(u.getId())){
				result.put("success", true);
				result.put("msg", "该手机号可以使用");
			}else{
				if(user.getId().equals(u.getId())){
					result.put("success", true);
					result.put("msg", "该手机号可以使用");
				}else{
					result.put("success", false);
					result.put("msg", "该手机号已被占用");
				}
			}
		}
		return result;
	}
	
	
	public Map<String, Object> ValidationMobileAndEmail(String userid, String mobile, String email) {
		Map<String, Object> result = Maps.newHashMap();
		if(StringUtil.isNUll(userid)){
			SpmsUser u1 = spmsUserDAO.getByMobile(mobile);
			if(u1 == null || StringUtil.isNUll(u1.getId())){
				result.put("success", true);
				result.put("msg", "该手机号可以使用");
			}else{
				result.put("success", false);
				result.put("msg", "该手机号已被占用");
			}
		}else{
			SpmsUser u = spmsUserDAO.getByMobile(mobile);
			SpmsUser user = spmsUserDAO.findOne(userid);
			if(u == null || StringUtil.isNUll(u.getId())){
				result.put("success", true);
				result.put("msg", "该手机号可以使用");
			}else{
				if(user.getId().equals(u.getId())){
					result.put("success", true);
					result.put("msg", "该手机号可以使用");
				}else{
					result.put("success", false);
					result.put("msg", "该手机号已被占用");
				}
			}
		}
		if((boolean) result.get("success")){
			if(null != email && !"".equals(email)){
				if(StringUtil.isNUll(userid)){
					SpmsUser u2 = spmsUserDAO.getByEmail(email);
					if(u2 == null || StringUtil.isNUll(u2.getId())){
						result.put("success", true);
						result.put("msg", "该邮箱可以使用");
					}else{
						result.put("success", false);
						result.put("msg", "该邮箱已被占用");
					}
				}else{
					SpmsUser u = spmsUserDAO.getByEmail(email);
					SpmsUser user = spmsUserDAO.findOne(userid);
					if(u == null || StringUtil.isNUll(u.getId())){
						result.put("success", true);
						result.put("msg", "该邮箱可以使用");
					}else{
						if(user.getId().equals(u.getId())){
							result.put("success", true);
							result.put("msg", "该邮箱可以使用");
						}else{
							result.put("success", false);
							result.put("msg", "该邮箱已被占用");
						}
					}
				}
			}
		}
		return result;
	}

	
	public QueryTranDTO getSpmsUserQuery(QueryTranDTO queryTranDTO) throws IOException {
		List <String> userIds = Lists.newArrayList();
		for(IEntity iEntity : queryTranDTO.getPage().getContent()) {
			SpmsUser spmsUser = (SpmsUser)iEntity;
			userIds.add(spmsUser.getId());
		}
		if(userIds.size() <= 0) {
			return queryTranDTO;
		}
//		List <Object[]> infos = spmsUserDAO.findProductSizeByUserIdsAndStatus(userIds);
//		for(Object[] arr :infos) {
//			int index = userIds.indexOf(arr[0].toString());
//			if(index != -1){
//				((SpmsUser)queryTranDTO.getPage().getContent().get(index)).setProductSize(Integer.valueOf(arr[1].toString()));
//			}
//		}
		return queryTranDTO;
	}

//	@Transactional(readOnly=false)
//	
//	public boolean cancelDeviceBind(String mac) {
//		//SpmsDevice spmsDevice = spmsDeviceDAO.findByMacAndType(mac, "2");
//		SpmsDevice spmsDevice = spmsDeviceDAO.getByMac(mac);
//		spmsUserProductBindingDAO.cancelDeviceBind(spmsDevice.getId());
//		List<RairconSetting> rairconCurveList = rairconCurveDAO
//				.getSpmsRairconCurveByDeviceId(spmsDevice.getId());
//		if (rairconCurveList != null && rairconCurveList.size() > 0) {
//			rairconCurveDAO.delete(rairconCurveList);
//		}
//		String sql = "delete from rairconcurve_clocksetting where spmsDevice_id='"
//				+ spmsDevice.getId() + "'";
//		queryDAO.doExecuteSql(sql);
//		sql ="select user_id from spms_user_product_binding where device_id='"+spmsDevice.getId()+"'";
//		List dhids = queryDAO.getObjects(sql);
//		String dhid="";
//		if(dhids!=null&&dhids.size()>0){
//			dhid=dhids.get(0).toString();
//		}
//		spmsDevice.setStatus(1);
//		spmsDevice.setStorage(2);
//		spmsDevice.setOperStatus(0);
//		spmsDevice.setOnOff(0);
//		spmsDeviceDAO.save(spmsDevice);
//		Map m=new HashMap();
//		m.put("userId", dhid);
//		m.put("commandType",0);
//		m.put("messageType","SERVICEUPDATE");
//		CommandUtil.asyncSendMessage(m);
//		return true;
//	}
//	@Transactional(readOnly=false)
//	
//	public Map<String,Object> reBindDevice(String oldMac,String newMac,String productId){
//		Map <String,Object> map = addDeviceToProduct(newMac, "2", productId);
//		if("true".equals(map.get("success").toString())) {
//			cancelDeviceBind(oldMac);
//		}
//		return map;
//	}
//	@Transactional(readOnly=false)
//	
//	public Map<String,Object> reBindDevice1(String oldMac,String newMac,String dtype,String productId){
//		Map <String,Object> map = addDeviceToProduct(newMac, dtype, productId);
//		if("true".equals(map.get("success").toString())) {
//			cancelDeviceBind(oldMac);
//		}
//		return map;
//	}


	
	public Map<String, Object> validateUser(String mobile) {
		Map<String, Object> map = new HashMap<String, Object>();
		SpmsUser su = spmsUserDAO.getByMobile(mobile);
		if(su != null && su.getUser() != null && su.getUser().getStatus() != null && su.getUser().getStatus() == 0){
			map.put("isValidate", true);
		}else if(su != null && su.getUser() != null && su.getUser().getStatus() != null && su.getUser().getStatus() != 0){
			map.put("isValidate", false);
		}else{
			map.put("isValidate", "true");
		}
		return map;
	}

//批量导入订户
	@Transactional(readOnly=false)
	
	public String saveAll(List<SpmsUserDTO> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		for (SpmsUserDTO spmsUserDTO : list) {
			if (spmsUserDTO != null) {
				if (StringUtils.isBlank(spmsUserDTO.getFullname())
						|| StringUtils.isBlank(spmsUserDTO.getIdNumber())
						|| StringUtils.isBlank(spmsUserDTO.getAddress())
						|| StringUtils.isBlank(spmsUserDTO.getMobile())
						|| StringUtils.isBlank(spmsUserDTO.getEmail())
						||spmsUserDTO.getType()==null) {
					sb3.append(spmsUserDTO.getFullname() + "有为空的信息<br/>");
					 continue;

				}
				String  idReg="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
				Pattern pattern = Pattern.compile(idReg);
				Matcher matcher = pattern.matcher(spmsUserDTO.getIdNumber());
				boolean b= matcher.matches();
				if(!b){
					sb3.append(spmsUserDTO.getFullname() + ":身份证号格式有误<br/>");
					 continue;
				}
				String mobReg="^1\\d{10}$";
				Pattern pattern1 = Pattern.compile(mobReg);
				Matcher matcher1 = pattern1.matcher(spmsUserDTO.getMobile());
				boolean c= matcher1.matches();
				if(!c){
					sb3.append(spmsUserDTO.getFullname() + ":手机号格式有误<br/>");
					 continue;
				}
				String eReg="^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$";
				Pattern pattern3 = Pattern.compile(eReg);
				Matcher matcher3 = pattern3.matcher(spmsUserDTO.getEmail());
				boolean e= matcher3.matches();
				if(!e){
					sb3.append(spmsUserDTO.getFullname() + ":邮箱格式有误<br/>");
					continue;
				}
				String tReg="[1,2]";
				Pattern pattern2 = Pattern.compile(tReg);
				Matcher matcher2 = pattern2.matcher(spmsUserDTO.getType().toString());
				boolean d= matcher2.matches();
				if(!d){
					sb3.append(spmsUserDTO.getFullname() + ":用户类型有误<br/>");
					continue;
				}
				SpmsUser spmsUser = new SpmsUser();
				BeanUtils.copyProperties(spmsUserDTO, spmsUser);
				List<SpmsUser> su = spmsUserDAO
						.findByMoblieAndEmailAndIdNumber(spmsUser.getMobile(),
								spmsUser.getEmail(), spmsUser.getIdNumber());
				SearchFilter searchFilter = new SearchFilter("spmsUserMobile",spmsUser.getMobile());
		        searchFilter.appendOrSearchFilter(new SearchFilter("idNumber",spmsUser.getIdNumber()));
		        searchFilter.appendOrSearchFilter(new SearchFilter("email",spmsUser.getEmail()));
		        List <SpmsWorkOrder> spmsWorkOrders = spmsWorkOrderDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter,SpmsWorkOrder.class));
				
				if ((null != su && su.size() > 0)||((spmsWorkOrders != null)&& (spmsWorkOrders.size()>0)) ) {
					sb.append(spmsUser.getFullname() + "、");
					continue;
				} else {
					UserEntity userEntity = new UserEntity();
					userEntity.setMobile(spmsUser.getMobile());
					userEntity.setUserCode(spmsUser.getMobile());
					userEntity.setEmail(spmsUser.getEmail());
					userEntity.setPassword("123456");
					userEntity.setUserName(spmsUser.getFullname());
					userEntity.setUserType(2);
					userEntity.setStatus(0);
					UserEntity udPersist = userDAO.save(userEntity);
					spmsUser.setUser(udPersist);
					spmsUserDAO.save(spmsUser);
				}
				
			}

		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf("、"));
			sb.append("信息有重复，已忽略，请检查。<br>");
		}
		if (sb2.length() > 0) {
			sb2.deleteCharAt(sb2.lastIndexOf("、"));
			sb2.append("用电区域或服务区域有误，已忽略，请检查。<br>");
		}
		if (sb3.length() > 0) {
			sb3.append("已忽略，请检查。");
		}
		return sb.toString() + sb2.toString()+sb3.toString();
	}


	
	public Map<String, Object> validDevice(Map<String, Object> info) {
		Map<String, Object> result = new HashMap<String, Object>();
//		SpmsDevice device = spmsDeviceDAO.findByMacAndType(info.get("devicemac").toString(), info.get("devicetype").toString());
		SpmsDeviceBase device =spmsAcDAO.findDeviceByMacorSn(info.get("devicemac").toString(), info.get("devicetype").toString());
		if(device == null|| device.getMac() ==null){
			result.put("success", false);
			result.put("msg", "指定类型中不存在Mac为："+info.get("devicemac").toString()+"  的设备，请查证");
		}else{
			result.put("success", true);
			result.put("deviceMac",device.getMac());
		}
		return result;
	}
	
	public List<SpmsDeviceDTO>  getUserDevices(String mobile ,Integer devType)
	{		
		List<SpmsDeviceDTO> devs = Lists.newArrayList();
		UserEntity ue = userDAO.getByMobile(mobile);		
		if (ue == null) {
			return devs;
		}
		
		if (devType.equals(EnumTypesConsts.DeviceType.Dev_Type_All)) {
			for (Integer index : DeviceDAOAccessor.getInstance().getDaoMap().keySet()) {
				devs.addAll(GetUserDevices(ue, index));
			}
		} else {
			devs.addAll(GetUserDevices(ue, devType));
		}
		return devs;		
	}
	
	public UserDeviceRespWraperDTO getUserDevicesWraper(String mobile , Integer devType)
	{
		UserDeviceRespWraperDTO respDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
		
		List<SpmsDeviceDTO> devs = getUserDevices(mobile,devType);
		respDTO.setDevices(devs);
		return respDTO;		
	}
	
	public UserDeviceRespWraperDTO getUserDeviceWraper(String mobile , String mac,Integer devType)
	{
		UserDeviceRespWraperDTO respDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
		SpmsDeviceDTO dto = getUserDevice(mobile,mac,devType);
		if(dto != null){
			respDTO.getDevices().add(dto);
		}
		return respDTO;		
	}
	
	
	//TODO move to device manager?
	public SpmsDeviceDTO getUserDevice(String mobile,String mac ,Integer devType){
		return  getUserDevice(mobile,mac,EnumTypesConsts.UserDeviceType.User_Primary,devType);
	}
	public SpmsDeviceDTO getUserDevice(String mobile,String mac ,Integer isPrimary , Integer devType){
		UserEntity ue = userDAO.getByMobile(mobile);
		if (ue == null) {
			return null;
		}
		SpmsUserDevice ud = spmsUserDeviceDAO.findByMM(mobile, mac,isPrimary);
		
		//check if exists this user - device 
		if (ud == null) {
			return null;
		}

		return spmsDeviceManager.getDeviceDTOById(ud.getDeviceId(), devType);
	}
	
	// get all share users according of the primary mobile+mac
	public List<SpmsUserDeviceDTO> getDeviceUser(SpmsUserDevice ud,Integer type) {
		// TODO double check?
		UserEntity ue = userDAO.getByMobile(ud.getMobile());
		if (ue == null || (ue.getStatus() != null && ue.getStatus() != 0)) {
			return null;
		}
		List<SpmsUserDeviceDTO> dtos = null;
		List<SpmsUserDevice> devices = spmsUserDeviceDAO.findByDeviceMac(ud.getMac(),type);
		if (devices != null) {
			dtos = Lists.newArrayList();
			for (SpmsUserDevice dev : devices) {
				if (dev != null) {
					SpmsUserDeviceDTO dto = new SpmsUserDeviceDTO();
					BeanUtils.copyProperties(dev, dto);
					
					//TODO add aditional info
					//image?
					ue = userDAO.getByMobile(dev.getMobile());
					if (ue != null) {
						dto.setUserName(ue.getUserName());
						dto.setNickname(ue.getNickname());
					}
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}
	
	/************************************************** private ************************************************************/
	
	private List<SpmsDeviceDTO> GetUserDevices(UserEntity ue,Integer type) {
		return GetUserDevices(ue.getId(),type);
	}
	
	private List<SpmsDeviceDTO> GetUserDevices(String uuid,Integer type) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		devices.addAll(spmsUserDeviceDAO.findDevsByUserId(uuid,type));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}

		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			
			Specification bySearchFilter = DynamicSpecifications.bySearchFilter(sfs, DeviceDAOAccessor.getInstance().getDevClz(type));			
			List<?> devList = DeviceDAOAccessor.getInstance().getDAO(type)
					.findAll(bySearchFilter);
			for (Object ac : devList) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				
				if (ac instanceof SpmsDeviceBase) {
					SpmsGateway gateway = ((SpmsDeviceBase) ac).getGateway();
					deviceDTO.setGwMac(gateway.getMac());
				}
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	private List<SpmsDeviceDTO> GetAcsFromUserDevice(String uuid) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		//List<String> devLst = Lists.newArrayList();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		devices.addAll(spmsUserDeviceDAO.findAcsByUserId(uuid));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}

		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			List<SpmsAirCondition> acs = spmsAcDAO
					.findAll(DynamicSpecifications.bySearchFilter(sfs, SpmsAirCondition.class));
			for (SpmsAirCondition ac : acs) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	private List<SpmsDeviceDTO> GetPlugAcsFromUserDevice(String uuid) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		//List<String> devLst = Lists.newArrayList();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		devices.addAll(spmsUserDeviceDAO.findDevsByUserId(uuid,EnumTypesConsts.DeviceType.Dev_Type_Plug_AC));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}

		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			List<SpmsPlugAc> acs = spmsPlugAcDAO
					.findAll(DynamicSpecifications.bySearchFilter(sfs, SpmsPlugAc.class));
			for (SpmsPlugAc ac : acs) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	private List<SpmsDeviceDTO> GetCentralAcsFromUserDevice(String uuid) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		//List<String> devLst = Lists.newArrayList();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		devices.addAll(spmsUserDeviceDAO.findDevsByUserId(uuid,EnumTypesConsts.DeviceType.Dev_Type_Central_AC));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}

		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			List<SpmsCentralAc> acs = spmsCentralAcDAO
					.findAll(DynamicSpecifications.bySearchFilter(sfs, SpmsCentralAc.class));
			for (SpmsCentralAc ac : acs) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	private List<SpmsDeviceDTO> GetWdsFromUserDevice(String uuid) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();

		devices.addAll(spmsUserDeviceDAO.findWdsByUserId(uuid));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}
		
		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			List<SpmsWinDoor> acs = spmsWdDAO
					.findAll(DynamicSpecifications.bySearchFilter(sfs, SpmsWinDoor.class));
			for (SpmsWinDoor ac : acs) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	private List<SpmsDeviceDTO> GetPirsFromUserDevice(String uuid) {
		List<SpmsUserDevice> devices = Lists.newArrayList();
		Map<String, Integer> devMap = Maps.newHashMap();
		List<SpmsDeviceDTO> devs = Lists.newArrayList();

		devices.addAll(spmsUserDeviceDAO.findPirsByUserId(uuid));
		for (SpmsUserDevice ud : devices) {
			devMap.put(ud.getDeviceId(),ud.getIsPrimary());
		}

		if (devices != null && devices.size() > 0) {
			List<SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("id", Operator.IN, devMap.keySet()));
			List<SpmsPir> pirs = spmsPirDAO
					.findAll(DynamicSpecifications.bySearchFilter(sfs, SpmsPir.class));
			for (SpmsPir ac : pirs) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(ac, deviceDTO);
				deviceDTO.setIsPrimary(devMap.get(deviceDTO.getId()));
				devs.add(deviceDTO);
			}
		}
		return devs;
	}
	
	/************************************************ end private **********************************************************/
	

}
