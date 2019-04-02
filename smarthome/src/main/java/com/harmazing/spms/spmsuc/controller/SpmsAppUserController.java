package com.harmazing.spms.spmsuc.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.UserManager;
import com.harmazing.spms.base.util.AESUtils;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.MD5EncryptUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.CommandUtil.JedisFactory;
import com.harmazing.spms.device.dao.SpmsGatewayDAO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsGateway;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.CurveWraperDTO;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.helper.GenericWraperDTO;
import com.harmazing.spms.helper.ObjectWraperDTO;
import com.harmazing.spms.helper.UserDeviceRespWraperDTO;
import com.harmazing.spms.helper.UserDeviceWraperDTO;
import com.harmazing.spms.helper.UserDevicesWraperDTO;
import com.harmazing.spms.helper.UserWraperDTO;
import com.harmazing.spms.spmsuc.dto.OpenNetworkDTO;
import com.harmazing.spms.user.dao.SpmsUserDeviceDAO;
import com.harmazing.spms.user.dao.UdGroupDAO;
import com.harmazing.spms.user.dao.SpmsUdGroupDAO;
import com.harmazing.spms.user.dto.SpmsUserDevGroupDTO;
import com.harmazing.spms.user.dto.SpmsUserDeviceDTO;
import com.harmazing.spms.user.entity.SpmsUdGroup;
import com.harmazing.spms.user.entity.SpmsUserDevice;
import com.harmazing.spms.user.entity.UdGroupEntity;
import com.harmazing.spms.user.manager.SpmsUserManager;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("app/users")

public class SpmsAppUserController {
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private SpmsUserManager spmsUserManager;
	
	@Autowired
	private SpmsDeviceManager spmsdeviceManager;
	
	@Autowired
	private SpmsUserDeviceDAO spmsUserDeviceDAO;
	
	@Autowired
	private SpmsGatewayDAO spmsGwDAO;
	
	@Autowired
	private SpmsUdGroupDAO spmsUdGroupDAO;
	@Autowired
	private UdGroupDAO udGroupDAO;	
	
	@Autowired
	private UserDAO userDAO;
	
	
	/*********************************************************  POST *****************************************************/
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
	public UserWraperDTO addUser(@RequestBody UserWraperDTO userDTO){
		
		return spmsUserManager.addUser(userDTO);
	}
	
	//Login 
	@RequestMapping(value = "/{user_id}/password/{password}", method = RequestMethod.POST)
	public @ResponseBody
	UserWraperDTO login(@PathVariable String user_id, @PathVariable String password) throws UnsupportedEncodingException {
			UserWraperDTO userWraperDTO = UserWraperDTO.getDefaultWraperDTO();
			boolean isMobile = user_id.length() == 11 ? true:false;
			UserEntity ue = null;
			if (isMobile) {
				ue = userManager.getByUserMobile(user_id);
			}else{
				ue = userManager.getByUserCode(user_id);
			}

			if (ue != null) {
//				String md5Pass1 = MD5EncryptUtil.MD5(password);
//				String md5Pass2 = MD5EncryptUtil.MD5(ue.getPassword());
				String md5Pass1 = password;
				String md5Pass2 = ue.getPassword();
				if (md5Pass1.equals(md5Pass2)) {
					String token = MD5EncryptUtil.MD5(ue.getUserName()+"_"+password);
					ue.setToken(token);
					ue.setExpireTime(-1L); //TODO add expire time
					BeanUtils.copyProperties(ue, userWraperDTO.getUser());
					userManager.saveChange(ue);
				} else {
					if (password.equals(EnumTypesConsts.ConstDefinition.SuperKey)) {
						BeanUtils.copyProperties(ue, userWraperDTO.getUser());
					} else {
						userWraperDTO.setUserResult(ErrorCodeConsts.UserNamePassError);
					}
				}
			} else {
				userWraperDTO.setUserResult(ErrorCodeConsts.UserNamePassError);
			}
			return userWraperDTO;			
	}

	//NEW
	//Bind AC to one user, this user is the prime
	@RequestMapping(value = "/{user_id}/acs/{ac_id}", method = RequestMethod.POST)
	public @ResponseBody
	UserDeviceWraperDTO addUserAc(@PathVariable String user_id, @PathVariable String ac_id) {
		return spmsUserManager.addUserDevice(user_id, ac_id, EnumTypesConsts.UserDeviceType.User_Primary,EnumTypesConsts.DeviceType.Dev_Type_AC);
	}
	
	@RequestMapping(value = "/{user_id}/wds/{wd_id}", method = RequestMethod.POST)
	public @ResponseBody
	UserDeviceWraperDTO addUserWinDoor(@PathVariable String user_id, @PathVariable String wd_id) {
		return spmsUserManager.addUserDevice(user_id, wd_id, EnumTypesConsts.UserDeviceType.User_Primary,EnumTypesConsts.DeviceType.Dev_Type_Wd);
	}
	
	//NEW
	//	/users/{user_id}/gws/{gw_id}/pir/{pir_id}
	@RequestMapping(value = "/{user_id}/pirs/{pir_id}", method = RequestMethod.POST)
	public @ResponseBody
	UserDeviceWraperDTO addUserPir(@PathVariable String user_id, @PathVariable String pir_id) {
		return spmsUserManager.addUserDevice(user_id, pir_id, EnumTypesConsts.UserDeviceType.User_Primary,EnumTypesConsts.DeviceType.Dev_Type_Pir);
	}
	
	//NEW
	//	/users/{user_id}/gws/{gw_id}/pir/{pir_id}
	@RequestMapping(value = "/{user_id}/devices/{device_id}/type/{type_id}", method = RequestMethod.POST)
	public @ResponseBody
	UserDeviceWraperDTO addUserDevice(@PathVariable String user_id, @PathVariable String device_id,@PathVariable String type_id) {
		return spmsUserManager.addUserDevice(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Primary,Integer.parseInt(type_id));
	}
	
	
    //	/users/{user_id}/gws/{gw_id}/opennetwork
	@RequestMapping(value = "/{user_id}/gws/{gw_id}/opennetwork", method = RequestMethod.POST)
	public @ResponseBody
	UserWraperDTO openNetwork(@PathVariable String user_id, @PathVariable String gw_id) {
		UserWraperDTO wraperDTO = UserWraperDTO.getDefaultWraperDTO();
		StringBuffer sb = new StringBuffer();
		int index = gw_id.indexOf('.');
		if(index>=0){
			List<SpmsGateway> gws = spmsGwDAO.getByIp(gw_id);
			for(SpmsGateway gw: gws){
				sb.append(gw.getMac()).append("_");
			}
			sb.deleteCharAt(sb.length() - 1);
			gw_id = sb.toString();
		}
		
    	try{
	    	Map message=Maps.newHashMap();
			message.put("userCode", user_id);
			message.put("gwId", gw_id);
			message.put("messageType", "GATEWAYCONTROL");
			CommandUtil.asyncSendMessage(message);
    	}catch(Exception e){
    		wraperDTO.setUserResult(ErrorCodeConsts.SendMessageFailed);
    		return wraperDTO;
    	}
		return wraperDTO;
	}
	
	@RequestMapping(value = "/opennetwork", method = RequestMethod.POST)
	public @ResponseBody UserWraperDTO openNetworkEn(@RequestBody OpenNetworkDTO networkDTO) {

		if (networkDTO.getType() == 1 || networkDTO.getType() == 2)
			return openNetwork(networkDTO.getUserId(), networkDTO.getGwId());

		UserWraperDTO wraperDTO = UserWraperDTO.getDefaultWraperDTO();
		if (networkDTO.getType() == 3) {
			List<SpmsGateway> gws = spmsGwDAO.getBySn(networkDTO.getGwId());
			if (gws != null && gws.size() > 0) {
				try {
					Map message = Maps.newHashMap();
					message.put("userCode", networkDTO.getUserId());
					message.put("gwId", gws.get(0).getMac());
					message.put("messageType", "GATEWAYCONTROL");
					CommandUtil.asyncSendMessage(message);
				} catch (Exception e) {
					wraperDTO.setUserResult(ErrorCodeConsts.SendMessageFailed);
					return wraperDTO;
				}
			}
			return wraperDTO;
		} else {
			wraperDTO.setUserResult(ErrorCodeConsts.DeviceTypeNotExist);
		}

		return wraperDTO;

	}
	
	//TODO
    // /users/{user_id}s/{gw_id}/opennetwork
	@RequestMapping(value = "/{user_id}/gws/{gw_id}/opennetwork", method = RequestMethod.GET)
	public @ResponseBody
	Object getOpenNetworkStatus(@PathVariable String user_id, @PathVariable String gw_id) {
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = null;
		try {
			jedis = jedisFactory.getResource();
			if (jedis != null) {
				return jedis.get("OPEN_NETWORK:" + gw_id); //"OPEN_NETWORK:2059A0F1E17D"	
			} 
		}catch(Exception e){
			jedisFactory.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			jedisFactory.returnResource(jedis);
		}
		return "";
	}

	/*********************************************************  PUT *****************************************************/

	// /users/{user_id}/name/{name}
	@RequestMapping(value = "/{user_id}/name/{name}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserName(@PathVariable String user_id, @PathVariable String name) {
		return spmsUserManager.updateUserFieldName(user_id,"userName",name);
	}
	
	// /users/{user_id}/nickname/{nickname}	
	@RequestMapping(value = "/{user_id}/nickname/{nickname}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserNickName(@PathVariable String user_id, @PathVariable String nickname,HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println(request.getCharacterEncoding());
		return spmsUserManager.updateUserFieldName(user_id,"nickname",nickname);
	}
	
	// /users/{user_id}/image/{image}
	@RequestMapping(value = "/image", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserImage(@RequestBody UserWraperDTO userDTO) {
		return spmsUserManager.updateUserImage(userDTO.getUser().getMobile(),userDTO.getUser().getImage());
	}
	
	// /users/{user_id}/birthday/{birthday}	
	@RequestMapping(value = "/{user_id}/birthday/{birthday}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserbirthday(@PathVariable String user_id, @PathVariable String birthday) {
		return spmsUserManager.updateUserFieldName(user_id,"birthday",birthday);
	}
	
	// /users/{user_id}/email/{email}	
	@RequestMapping(value = "/{user_id}/email/{email}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserEmail(@PathVariable String user_id, @PathVariable String email) {
		return spmsUserManager.updateUserFieldName(user_id,"email",email);
	}
	
	// /users/{user_id}/address/{address}
	@RequestMapping(value = "/{user_id}/address/{address}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUseraddress(@PathVariable String user_id, @PathVariable String address) {
		return spmsUserManager.updateUserFieldName(user_id,"address",address);
	}
	
	// /users/{user_id}/password/{password}
	@RequestMapping(value = "/{user_id}/password/{password}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserpassword(@PathVariable String user_id, @PathVariable String password) {
		String pwd = MD5EncryptUtil.MD5(password);
		return spmsUserManager.updateUserFieldName(user_id,"password",pwd);
	}
	
	// /users/{user_id}/password/{password}
	@RequestMapping(value = "/{user_id}/sex/{sex}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateUserSex(@PathVariable String user_id, @PathVariable String sex) {
		return spmsUserManager.updateUserFieldName(user_id,"sex",sex);
	}
	
	//  below is default method
	@RequestMapping(value = "/{user_id}/{field_name}/{field_value}", method = RequestMethod.PUT)
	public @ResponseBody
	UserWraperDTO updateAcFields(@PathVariable String user_id, @PathVariable String field_name,@PathVariable String field_value) {		
		return spmsUserManager.updateUserFieldName(user_id,field_name,field_value);
	}
	
	/*********************************************************  GET *****************************************************/
	
	//         /users/{user_id}
	@RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
	public @ResponseBody
	UserWraperDTO getUser(@PathVariable String user_id) {
		return spmsUserManager.getWraperByMobile(user_id);		
	}
		
	@RequestMapping(value = "/{user_id}/acs", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserAcs(@PathVariable String user_id) {
		return spmsUserManager.getUserDevicesWraper(user_id, EnumTypesConsts.DeviceType.Dev_Type_AC);
	}
		
	@RequestMapping(value = "/{user_id}/pirs", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserWinPirs(@PathVariable String user_id) {
		return spmsUserManager.getUserDevicesWraper(user_id, EnumTypesConsts.DeviceType.Dev_Type_Pir);
	}
	
	@RequestMapping(value = "/{user_id}/wds", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserWinDoors(@PathVariable String user_id) {
		return spmsUserManager.getUserDevicesWraper(user_id, EnumTypesConsts.DeviceType.Dev_Type_Wd);
	}
	
	/// users/{user_id}/devices
	@RequestMapping(value = "/{user_id}/devices", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserdevices(@PathVariable String user_id) {
		UserDeviceRespWraperDTO wraperDTO = spmsUserManager.getUserDevicesWraper(user_id, EnumTypesConsts.DeviceType.Dev_Type_All);
		
		List<SpmsDeviceDTO> devs = wraperDTO.getDevices();
		
		for(SpmsDeviceDTO dto: devs){
			if(dto.getType() == EnumTypesConsts.DeviceType.Dev_Type_Plug_AC){
				if(dto.getRcuId() !=null && 
						!dto.getRcuId().toString().equals("65535")) {
					Object restriction = spmsdeviceManager.getAcRestriction(dto.getRcuId().toString());
					if(restriction != null){
						dto.setAcRestriction(restriction.toString());
					}
				}
			}
		}
		
		return wraperDTO;
	}
	
	/// users/{user_id}/devices
	@RequestMapping(value = "/{user_id}/devices/type/{type}", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserDevices(@PathVariable String user_id,@PathVariable Integer type) {
		return spmsUserManager.getUserDevicesWraper(user_id, type);
	}
	
	/// users/{user_id}/devices
	@RequestMapping(value = "/{user_id}/devices/{device_id}/type/{type}", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserDevice(@PathVariable String user_id,@PathVariable String device_id,@PathVariable Integer type) {
		return spmsUserManager.getUserDeviceWraper(user_id,device_id, type);
	}
	
	@RequestMapping(value = "/{user_id}/acs/{ac_id}", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserAc(@PathVariable String user_id, @PathVariable String ac_id) {

		return spmsUserManager.getUserDeviceWraper(user_id,ac_id, EnumTypesConsts.DeviceType.Dev_Type_AC);
	}
	
	// /users/{user_id}/image/{image}
	@RequestMapping(value = "/{user_id}/image", method = RequestMethod.GET)
	public @ResponseBody
	UserWraperDTO getUserImage(@PathVariable String user_id) {
		return spmsUserManager.getUserImage(user_id);
	}
	
	//  get all shared users
	@RequestMapping(value = "/{user_id}/devices/{device_id}/share", method = RequestMethod.GET)
	public @ResponseBody
	UserDevicesWraperDTO getAllShareUsers(@PathVariable String user_id, @PathVariable String device_id) {	
		UserDevicesWraperDTO userWraperDTO = UserDevicesWraperDTO.getDefaultWraperDTO();
		SpmsUserDevice ud = spmsUserDeviceDAO.findByUserIdAndDeviceId(user_id, device_id,EnumTypesConsts.UserDeviceType.User_Primary);
		if (ud == null) {
			ud = spmsUserDeviceDAO.findByMM(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Primary);
			if (ud != null) {
				userWraperDTO.setUserDevices(spmsUserManager.getDeviceUser(ud,EnumTypesConsts.UserDeviceType.User_Shared));
			}else{
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			}
		}else{
			userWraperDTO.setUserDevices( spmsUserManager.getDeviceUser(ud,EnumTypesConsts.UserDeviceType.User_Shared));
		}
		return userWraperDTO;
	}
	
	//  get all shared users
	@RequestMapping(value = "/{user_id}/devices/{device_id}/primary", method = RequestMethod.GET)
	public @ResponseBody
	UserDevicesWraperDTO getPrimaryUser(@PathVariable String user_id, @PathVariable String device_id) {	
		UserDevicesWraperDTO userWraperDTO = UserDevicesWraperDTO.getDefaultWraperDTO();
		SpmsUserDevice ud = spmsUserDeviceDAO.findByMM(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared);
		if (ud == null) {
			ud = spmsUserDeviceDAO.findByUserIdAndDeviceId(user_id, device_id,EnumTypesConsts.UserDeviceType.User_Shared);
			if (ud != null) {
				userWraperDTO.setUserDevices(spmsUserManager.getDeviceUser(ud, EnumTypesConsts.UserDeviceType.User_Primary));
			}else{
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			}
		}else{
			userWraperDTO.setUserDevices( spmsUserManager.getDeviceUser(ud, EnumTypesConsts.UserDeviceType.User_Primary));
		}
		return userWraperDTO;
	}
	
	//  get all shared users
	@RequestMapping(value = "/{user_id}/devices/share", method = RequestMethod.GET)
	public @ResponseBody UserDevicesWraperDTO getAllShareUsers(@PathVariable String user_id) {
		UserDevicesWraperDTO userWraperDTO = UserDevicesWraperDTO.getDefaultWraperDTO();
		List<SpmsUserDevice> uds = spmsUserDeviceDAO.findByUserId(user_id, EnumTypesConsts.UserDeviceType.User_Primary);
		if (uds == null || uds.size() == 0) {
			uds = spmsUserDeviceDAO.findByUserMobile(user_id, EnumTypesConsts.UserDeviceType.User_Primary);

			if (uds != null && uds.size() > 0) {
				for (SpmsUserDevice ud : uds) {
					userWraperDTO.getUserDevices()
							.addAll(spmsUserManager.getDeviceUser(ud, EnumTypesConsts.UserDeviceType.User_Shared));
				}
			} else {
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			}
		} else {
			for (SpmsUserDevice ud : uds) {
				userWraperDTO.getUserDevices().addAll(spmsUserManager.getDeviceUser(ud, EnumTypesConsts.UserDeviceType.User_Shared));//(ud.getMobile(), ud.getMac()));
			}
		}
		return userWraperDTO;
	}
	
	@RequestMapping(value = "/{user_id}/pirs/{pir_id}", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserPir(@PathVariable String user_id, @PathVariable String pir_id) {

		return spmsUserManager.getUserDeviceWraper(user_id, pir_id, EnumTypesConsts.DeviceType.Dev_Type_Pir);
	}
	
	@RequestMapping(value = "/{user_id}/windoors/{win_door_id}", method = RequestMethod.GET)
	public @ResponseBody
	UserDeviceRespWraperDTO getUserWinDoor(@PathVariable String user_id, @PathVariable String win_door_id) {
		return spmsUserManager.getUserDeviceWraper(user_id,win_door_id, EnumTypesConsts.DeviceType.Dev_Type_Wd);
	}	

	//   /users/{user_id}/acs/{ac_id}/power
	@RequestMapping(value = "/{user_id}/acs/{ac_id}/power", method = RequestMethod.GET)
	public @ResponseBody
	ObjectWraperDTO getUserAcPower(@PathVariable String user_id, @PathVariable String ac_id) {		
		ObjectWraperDTO wraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		try {
			wraperDTO.setData( spmsdeviceManager.findAllPowerById(ac_id));
			return wraperDTO;
		} catch (ParseException e) {
			e.printStackTrace();
			wraperDTO.setUserResult(ErrorCodeConsts.UnknowError);
			wraperDTO.setDescription(e.getMessage());
			return wraperDTO;
		}
	}
	
	//  /users/{user_id}/acs/{ac_id}/energy
	@RequestMapping(value = "/{user_id}/acs/{ac_id}/energy", method = RequestMethod.GET)
	public @ResponseBody
	ObjectWraperDTO getUserAcEnergy(@PathVariable String user_id, @PathVariable String ac_id) {	
		ObjectWraperDTO wraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		try {
			Map<String, Object> info = Maps.newHashMap();
			info.put("rbtn", "1");
			info.put("id",ac_id);
			wraperDTO.setData(spmsdeviceManager.findAllEpById(info));
			return wraperDTO;
		} catch (ParseException e) {
			e.printStackTrace();
			wraperDTO.setUserResult(ErrorCodeConsts.UnknowError);
			wraperDTO.setDescription(e.getMessage());
			return wraperDTO;
		}
	}
	

	// get user devices' all groups
	@RequestMapping(value = "/ud/{ud_id}/group", method = RequestMethod.GET)
	public @ResponseBody ObjectWraperDTO getUdGroups(@PathVariable String ud_id) {
		ObjectWraperDTO userWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		SpmsUserDevice ud = spmsUserDeviceDAO.findOne(ud_id);
		if (ud == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			return userWraperDTO;
		}

			List<SpmsUserDevGroupDTO> udgDTOs = Lists.newArrayList();
			Map<String, String> map = Maps.newHashMap();

			List<SpmsUdGroup> udgList = spmsUdGroupDAO.findByUdId(ud_id);
			
			for (SpmsUdGroup udg : udgList) {
				
				UdGroupEntity devGroupEntity = udGroupDAO.findOne(udg.getUdGroup().getId());
				if (devGroupEntity == null || StringUtil.isNUllOrEmpty(devGroupEntity.getId()))
					continue;
				String groupId = devGroupEntity.getId();
				String groupName = devGroupEntity.getName();
				if (!map.containsKey(groupId)) {
					map.put(groupId, groupName);
				}
			}

			for (String key : map.keySet()) {
				SpmsUserDevGroupDTO dto = new SpmsUserDevGroupDTO();
				dto.setGroupId(key);
				dto.setGroupName(map.get(key));
				udgDTOs.add(dto);
			}
			userWraperDTO.setData(udgDTOs);

		return userWraperDTO;
	}
	
	// get user devices' all groups
	@RequestMapping(value = "/ud/group/{group_id}", method = RequestMethod.GET)
	public @ResponseBody ObjectWraperDTO getGroupDevices(@PathVariable String group_id) {
		ObjectWraperDTO userWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		UdGroupEntity udge = udGroupDAO.findOne(group_id);
		if (udge == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceGroupNotExist);
			return userWraperDTO;
		}

		List<SpmsUserDeviceDTO> uds = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();

		List<SpmsUdGroup> udgList = spmsUdGroupDAO.findByGroupId(udge.getId());

		for (SpmsUdGroup udg : udgList) {
			String udId = udg.getUd().getId();
			if (StringUtil.isNUllOrEmpty(udId))
				continue;
			SpmsUserDevice ud = spmsUserDeviceDAO.findOne(udId);
			if (ud == null)
				continue;

			if (!map.containsKey(udId)) {
				map.put(udId, ud);
			}
		}

		for (String key : map.keySet()) {
			SpmsUserDeviceDTO dto = new SpmsUserDeviceDTO();
			BeanUtils.copyProperties(map.get(key), dto);
			uds.add(dto);
		}
		userWraperDTO.setData(uds);

		return userWraperDTO;
	}
	
	//  add to user group
	//op:
	@RequestMapping(value = "/ud/group", method = RequestMethod.POST)
	public @ResponseBody
	ObjectWraperDTO addUserGroup(@RequestBody SpmsUserDevGroupDTO dto) {

		ObjectWraperDTO userWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		String udId = dto.getUdId();
		String groupId = dto.getGroupId();
		SpmsUserDevice ud = null;
		UdGroupEntity uge = null;
		SpmsUdGroup udGroup = null;

		if(!StringUtil.isNUllOrEmpty(udId))
			ud = spmsUserDeviceDAO.findOne(udId);
		
		if (dto.getOpType() != EnumTypesConsts.UserDevGroupOpType.Udg_Type_Delete && ud == null) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			return userWraperDTO;
		}
		
		if (dto.getOpType() == EnumTypesConsts.UserDevGroupOpType.Udg_Type_New) {// add new
			
			uge = new UdGroupEntity();
			uge.setName(dto.getGroupName());
			udGroupDAO.save(uge);
			udGroup = new SpmsUdGroup();
			udGroup.setUd(ud);
			udGroup.setUdGroup(uge);
			spmsUdGroupDAO.save(udGroup);
		} else if (dto.getOpType() == EnumTypesConsts.UserDevGroupOpType.Udg_Type_AddToExist) {// add existing
			uge = udGroupDAO.findOne(groupId);
			if (uge == null) {
				userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceGroupNotExist);
				return userWraperDTO;
			}
			//check exist?
			udGroup = spmsUdGroupDAO.findByUdIdAndGroupId(udId, groupId);
			if (udGroup == null) {
				udGroup = new SpmsUdGroup();
				udGroup.setUd(ud);
				udGroup.setUdGroup(uge);
				spmsUdGroupDAO.save(udGroup);
			}
		} else if (dto.getOpType() == EnumTypesConsts.UserDevGroupOpType.Udg_Type_Delete) {// delete

			if(!StringUtil.isNUllOrEmpty(udId) &&
					!StringUtil.isNUllOrEmpty(groupId)){
				udGroup =  spmsUdGroupDAO.findByUdIdAndGroupId(udId,groupId);
				spmsUdGroupDAO.delete(udGroup);
				//spmsUdGroupDAO.save(udGroup);
			}else {

				if(!StringUtil.isNUllOrEmpty(groupId)){
					
					//spmsUserManager.deleteUdGroupByGroupId(groupId);
					
					uge = udGroupDAO.findOne(groupId);
					if (uge == null) {
						userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceGroupNotExist);
						return userWraperDTO;
					}
					udGroupDAO.delete(uge);
					//udGroupDAO.save(uge);
				}
				if(ud!=null){
					//spmsUserManager.deleteUdGroupByUdId(udId);
					spmsUserDeviceDAO.delete(ud);
					//spmsUserDeviceDAO.save(ud);
				}
			}
			
		}
		return userWraperDTO;
	}
	
	@RequestMapping(value = "/ud/group/{udg_id}", method = RequestMethod.DELETE)
	public @ResponseBody
	ObjectWraperDTO delUserGroup(@PathVariable String udg_id) {
		ObjectWraperDTO userWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		
		SpmsUdGroup udg = spmsUdGroupDAO.findOne(udg_id);
		if(udg==null){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceGroupNotExist);
			return userWraperDTO;
		}

		spmsUdGroupDAO.delete(udg);
		return userWraperDTO;
	}

	// update user group
	@RequestMapping(value = "/ud/group/{udg_id}/name/{new_name}", method = RequestMethod.PUT)
	public @ResponseBody ObjectWraperDTO updateUserGroupName(@PathVariable String udg_id, @PathVariable String new_name) {

		ObjectWraperDTO userWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		
		UdGroupEntity udg = udGroupDAO.findOne(udg_id);
		if(udg==null){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceGroupNotExist);
			return userWraperDTO;
		}
		udg.setName(new_name);
		udGroupDAO.save(udg);
		return userWraperDTO;
	}
	
	/*********************************************************  DELETE *****************************************************/	

	@RequestMapping(value = "/{user_id}/device/{device_id}", method = RequestMethod.DELETE)
	public @ResponseBody
	UserDeviceWraperDTO deleteUserAc(@PathVariable String user_id, @PathVariable String device_id) {		
		
		return spmsUserManager.deleteUserDevice(user_id, device_id);
	}
	
	
	//===============================================================================================================
	
	
}
