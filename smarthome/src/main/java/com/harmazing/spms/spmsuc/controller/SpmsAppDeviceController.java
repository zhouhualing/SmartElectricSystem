package com.harmazing.spms.spmsuc.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.JsonObject;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.base.util.CommandUtil.JedisFactory;
import com.harmazing.spms.device.dao.SpmsAirConditionDAO;
import com.harmazing.spms.device.dao.SpmsCentralAcDAO;
import com.harmazing.spms.device.dao.SpmsGatewayDAO;
import com.harmazing.spms.device.dao.SpmsLampDAO;
import com.harmazing.spms.device.dao.SpmsPirDAO;
import com.harmazing.spms.device.dao.SpmsPlugAcDAO;
import com.harmazing.spms.device.dao.SpmsWinDoorDAO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsCentralAc;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.entity.SpmsGateway;
import com.harmazing.spms.device.entity.SpmsLamp;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.entity.SpmsPlugAc;
import com.harmazing.spms.device.entity.SpmsWinDoor;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.CurveWraperDTO;
import com.harmazing.spms.helper.DeviceDAOAccessor;
import com.harmazing.spms.helper.DeviceWraperDTO;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.helper.GenericWraperDTO;
import com.harmazing.spms.helper.ObjectWraperDTO;
import com.harmazing.spms.helper.UserDeviceRespWraperDTO;
import com.harmazing.spms.helper.UserDeviceWraperDTO;
import com.harmazing.spms.helper.Validator;
import com.harmazing.spms.helper.UserWraperDTO;
import com.harmazing.spms.spmsuc.Manager.SpmsAppManager;
import com.harmazing.spms.spmsuc.Manager.SpmsUcManager;
import com.harmazing.spms.user.dao.SpmsUserDeviceDAO;
import com.harmazing.spms.user.entity.SpmsUserDevice;
import com.harmazing.spms.user.manager.SpmsUserManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.NewBeanInstanceStrategy;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("app")
public class SpmsAppDeviceController {
	
	@Autowired
	private SpmsDeviceManager spmsDeviceManager;
	
	@Autowired
	private SpmsAppManager spmsAppManager;
	
	@Autowired
    private SpmsUserManager spmsUserManager;
	@Autowired
	private SpmsUcManager spmsUcManager;
	@Autowired
	private SpmsUserDeviceDAO spmsUserDeviceDAO;
	@Autowired
	private SpmsGatewayDAO spmsGwDAO;
	@Autowired
	private SpmsAirConditionDAO spmsAcDAO;
	@Autowired
	private SpmsWinDoorDAO spmsWdDAO;
	@Autowired
	private SpmsPirDAO spmsPirDAO;
	@Autowired
	private SpmsPlugAcDAO spmsPlugDAO;
	@Autowired
	private SpmsLampDAO spmsLampDAO;
	
	@Autowired
	private QueryDAO queryDAO;
	
	@RequestMapping(value = "/gws", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addGws(@RequestBody DeviceWraperDTO devDTO){	
		devDTO.setUserResult(ErrorCodeConsts.Success);
		//check exist?
		SpmsGateway gw = new SpmsGateway();
		BeanUtils.copyProperties(devDTO.getDevice(), gw);

		spmsGwDAO.save(gw);
		return devDTO;
	}
	
	@RequestMapping(value = "/gws", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getGws(){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.setDevices(spmsDeviceManager.getDevicesDTO(EnumTypesConsts.DeviceType.Dev_Type_Gw));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/gws/{gw_id}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getGwInfo(@PathVariable String gw_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.getDevices().add(spmsDeviceManager.getDeviceDTO(gw_id,EnumTypesConsts.DeviceType.Dev_Type_Gw));
		return deviceRespWraperDTO;
	}
	
	// /gws/ip/{gw_ip}/
	@RequestMapping(value = "/gws/ip/{gw_ip}", method = RequestMethod.GET)
	public @ResponseBody Object getGatewayFromIp(@PathVariable String gw_ip) {
		ObjectWraperDTO wraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		wraperDTO.setUserResult(ErrorCodeConsts.Success);

		List<SpmsGateway> gws = spmsGwDAO.getByIp(gw_ip);
		List<String> gwStrings = Lists.newArrayList();
		for (SpmsGateway gw : gws) {
			if (gw == null || StringUtil.isNUllOrEmpty(gw.getMac()))
				continue;
			gwStrings.add(gw.getMac());
		}
		wraperDTO.setData(gwStrings);
		return wraperDTO;
	}
	
	@RequestMapping(value = "/gws/{gw_id}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deleteGw(@PathVariable String gw_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(gw_id,EnumTypesConsts.DeviceType.Dev_Type_Gw);   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());		
		return deviceRespWraperDTO;
	}
	
    // /gws/{gw_id}/opennetwork
	@RequestMapping(value = "/gws/{gw_id}/opennetwork", method = RequestMethod.GET)
	public @ResponseBody
	Object getOpenNetworkStatus(@PathVariable String gw_id) {
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = null;
		try {
			jedis = jedisFactory.getResource();
			if(jedis != null){
			    return jedis.get("OPEN_NETWORK:" + gw_id);     //"OPEN_NETWORK:2059A0F1E17D"	
			}
		} catch (Exception e) {
			jedisFactory.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			jedisFactory.returnResource(jedis);
		}
		return "";
	}		
	
	
    // todo
	@RequestMapping(value = "/gws/{gw_mac}/device/{dev_mac}/rcu/{rcu_id}/update", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> updateRcuId(@PathVariable String gw_mac,@PathVariable String dev_mac,@PathVariable String rcu_id) {
		
		Map<String, Object> result = spmsAppManager.updateRcuId(gw_mac,dev_mac,rcu_id);
		return result;
	}
	
	@RequestMapping(value = "/acs", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addAcs(@RequestBody DeviceWraperDTO devDTO){	
		devDTO.setUserResult(ErrorCodeConsts.Success);
		
		//we need to check whether the AC has bound with user?
		SpmsAirCondition ac = new SpmsAirCondition();
		BeanUtils.copyProperties(devDTO.getDevice(), ac);
		ac.setType(EnumTypesConsts.DeviceType.Dev_Type_AC);

		spmsAcDAO.save(ac);
		return devDTO;
	}
	
	@RequestMapping(value = "/acs", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getAcs(){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.setDevices(spmsDeviceManager.getDevicesDTO(EnumTypesConsts.DeviceType.Dev_Type_AC));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/acs/{ac_id}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getAcInfo(@PathVariable String ac_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		SpmsDeviceDTO dto = spmsDeviceManager.getDeviceDTO(ac_id,EnumTypesConsts.DeviceType.Dev_Type_AC);
		if(dto != null)
		    deviceRespWraperDTO.getDevices().add(dto);
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/acs/{ac_id}/power", method = RequestMethod.GET)
    @ResponseBody
	public GenericWraperDTO getAcPower(@PathVariable String ac_id){	
		GenericWraperDTO genericWraperDTO = GenericWraperDTO.getDefaultWraperDTO();
	   		
		Float power = spmsDeviceManager.getDeviceDTO(ac_id,EnumTypesConsts.DeviceType.Dev_Type_AC).getPower();
		String strPower = (power == null? "":power.toString());
		Map<String, Object> map = Maps.newHashMap();
		map.put("userCode",UserUtil.getCurrentUserCode());
		map.put("power",strPower);
		genericWraperDTO.getData().add(map);
		return genericWraperDTO;
	}
	
	@RequestMapping(value = "/acs/{rcuId}/restriction", method = RequestMethod.GET)
    @ResponseBody
	public ObjectWraperDTO getAcRestriction(@PathVariable String rcuId){	
		ObjectWraperDTO genericWraperDTO = ObjectWraperDTO.getDefaultWraperDTO();
		Object restriction = spmsDeviceManager.getAcRestriction(rcuId);
		if(restriction != null){			
			genericWraperDTO.setData(restriction);
		}		
		return genericWraperDTO;
	}
	
	@RequestMapping(value = "/acs/{ac_id}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deleteAc(@PathVariable String ac_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(ac_id,EnumTypesConsts.DeviceType.Dev_Type_AC);
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());	
		return deviceRespWraperDTO;
	}
	
	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/status/{status}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updateDeviceStatus(@PathVariable String ac_id, @PathVariable String status) {		
		return spmsDeviceManager.updateDeviceField(SpmsAirCondition.class,ac_id,"status",status);
	}
	
	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/acTemp/{acTemp}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateDeviceTemp(@PathVariable String ac_id, @PathVariable String acTemp) {
		SpmsAirCondition ac = spmsAcDAO.getByMac(ac_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (ac == null || ac.getGateway() == null || StringUtil.isNUllOrEmpty(ac.getGateway().getMac())) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}		
		Map<String, Object> result = null;
		try {
			result = spmsAppManager.changeAcStatus(ac.getGateway().getMac(), ac_id, CommandUtil.CommandType.TEMP_SET,
					Integer.parseInt(acTemp));
		} catch (Exception e) {
			dto.setUserResult(ErrorCodeConsts.ParaNotRight);
			return dto;
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	
//  /acs/{ac_id}/modsig/{modsig}
	@RequestMapping(value = "/acs/{ac_id}/modsig/{modsig}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateAcPlugStatus(@PathVariable String ac_id, @PathVariable String modsig) {
		SpmsAirCondition ac = spmsAcDAO.getByMac(ac_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (ac == null || ac.getGateway() == null || StringUtil.isNUllOrEmpty(ac.getGateway().getMac())) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}
		
		Map<String, Object> result = null;
		try {
			result = spmsAppManager.changeAcPlugStatus(ac.getGateway().getMac(), ac_id, CommandUtil.IrControlCommandType.IR_TX_CODE,modsig);
		} catch (Exception e)
		{
			dto.setUserResult(ErrorCodeConsts.ParaNotRight);
			return dto;
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	
	
	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/onOff/{onOff}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateAcOnOff(@PathVariable String ac_id, @PathVariable String onOff) {
		SpmsAirCondition ac = spmsAcDAO.getByMac(ac_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (ac == null || ac.getGateway() == null ) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}
		Map<String, Object> result = null;
		if(onOff.equals("0")){			
			result = spmsAppManager.changeAcStatus(ac.getGateway().getMac(), ac_id,CommandUtil.CommandType.OFF,0);
		}
		else if(onOff.equals("1")){
			result = spmsAppManager.changeAcStatus(ac.getGateway().getMac(), ac_id,CommandUtil.CommandType.ON,1);
			
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	
	//irgb style like 98.100.255.100
	@RequestMapping(value = "/lamp/{lamp_id}/IRGB/{irgb}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO changeLampColor(@PathVariable String lamp_id, @PathVariable String irgb){
		SpmsLamp lamp = spmsLampDAO.getByMac(lamp_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (lamp == null || lamp.getGateway() == null ) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}

		Map<String, Object> result = null;
		List<String> irgbList = StringUtil.split(irgb);
		List<Integer> irgbIntList = Lists.newArrayList();
		if (irgbList.size()!=4) {
			dto.setUserResult(ErrorCodeConsts.ParaNotRight);
			return dto;
		}
		else
		{
			for (String s : irgbList) {
				irgbIntList.add(Integer.parseInt(s));
			}
		}
		result = spmsAppManager.changeLampStatus(lamp.getGateway().getMac(), lamp_id, (Integer[])irgbIntList.toArray(new Integer[4]));
		dto.setUserResult((String) result.get("status"));
		return dto;
	}
	
	//  below is default method
	@RequestMapping(value = "/acs/{ac_id}/{field_name}/{field_value}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updateAcFields(@PathVariable String ac_id, @PathVariable String field_name,@PathVariable String field_value) {		
		return spmsDeviceManager.updateDeviceField(SpmsAirCondition.class,ac_id,field_name,field_value);
	}
	

	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/mode/{mode}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateAcMode(@PathVariable String ac_id, @PathVariable String mode) {
		SpmsAirCondition ac = spmsAcDAO.getByMac(ac_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (ac == null || ac.getGateway() == null || StringUtil.isNUllOrEmpty(ac.getGateway().getMac())) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}		
		Map<String, Object> result = null;
		try {
			result = spmsAppManager.changeAcStatus(ac.getGateway().getMac(), ac_id, CommandUtil.CommandType.MODE_SET,
					Integer.parseInt(mode));
		} catch (Exception e) {
			dto.setUserResult(ErrorCodeConsts.ParaNotRight);
			return dto;
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/speed/{speed}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateAcFanSpeed(@PathVariable String ac_id, @PathVariable String speed) {		
		SpmsAirCondition ac = spmsAcDAO.getByMac(ac_id);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (ac == null || ac.getGateway() == null || StringUtil.isNUllOrEmpty(ac.getGateway().getMac())) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}		
		Map<String, Object> result = null;
		try {
			result = spmsAppManager.changeAcStatus(ac.getGateway().getMac(), ac_id, CommandUtil.CommandType.FAN_SET,
					Integer.parseInt(speed));
		} catch (Exception e) {
			dto.setUserResult(ErrorCodeConsts.ParaNotRight);
			return dto;
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	//  /acs/{ac_id}/status
	@RequestMapping(value = "/acs/{ac_id}/timers/{timers}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updateDeviceTimers(@PathVariable String ac_id, @PathVariable String timers) {		
		return spmsDeviceManager.updateDeviceField(SpmsAirCondition.class,ac_id,"startTime",timers);
	}	
	
	@RequestMapping(value = "/wds", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addWinDoors(@RequestBody DeviceWraperDTO devDTO){	
		devDTO.setUserResult(ErrorCodeConsts.Success);		
		SpmsWinDoor wd = new SpmsWinDoor();
		BeanUtils.copyProperties(devDTO.getDevice(), wd);
		wd.setType(EnumTypesConsts.DeviceType.Dev_Type_Wd);
		spmsWdDAO.save(wd);
		return devDTO;
	}
	
	@RequestMapping(value = "/wds", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getWds(){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.setDevices(spmsDeviceManager.getDevicesDTO(EnumTypesConsts.DeviceType.Dev_Type_Wd));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/wds/{wd_id}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getWdInfo(@PathVariable String wd_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.getDevices().add(spmsDeviceManager.getDeviceDTO(wd_id,EnumTypesConsts.DeviceType.Dev_Type_Wd));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/wds/{wd_id}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deleteWd(@PathVariable String wd_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(wd_id,EnumTypesConsts.DeviceType.Dev_Type_Wd);   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());		
		return deviceRespWraperDTO;
	}
	
	//  below is default method
	@RequestMapping(value = "/wds/{wd_id}/{field_name}/{field_value}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updateWdFields(@PathVariable String wd_id, @PathVariable String field_name,@PathVariable String field_value) {		
		return spmsDeviceManager.updateDeviceField(SpmsWinDoor.class,wd_id,field_name,field_value);
	}
	
	@RequestMapping(value = "/pirs", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addPirs(@RequestBody DeviceWraperDTO devDTO){	
		devDTO.setUserResult(ErrorCodeConsts.Success);		
		SpmsPir pir = new SpmsPir();
		BeanUtils.copyProperties(devDTO.getDevice(), pir);
		pir.setType(EnumTypesConsts.DeviceType.Dev_Type_Pir);
		spmsPirDAO.save(pir);
		return devDTO;
	}
	
	@RequestMapping(value = "/pirs", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getPirs(){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.setDevices(spmsDeviceManager.getDevicesDTO(EnumTypesConsts.DeviceType.Dev_Type_Pir));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/pirs/{pir_id}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getPirInfo(@PathVariable String pir_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.getDevices().add(spmsDeviceManager.getDeviceDTO(pir_id,EnumTypesConsts.DeviceType.Dev_Type_Pir));
		return deviceRespWraperDTO;
	}	

	//get power curve	
	@RequestMapping(value = "/acs/curve", method = RequestMethod.POST)
    @ResponseBody
	public ObjectWraperDTO getDeviceEleRecord(@RequestBody CurveWraperDTO dto){	
		ObjectWraperDTO wraperDTO = ObjectWraperDTO.getDefaultWraperDTO();		
		Map<String,List<Object[]>> ret = Maps.newHashMap();

		JSONArray array;
		try {
			array = JSONArray.fromObject(dto.getDeviceId());
		} catch (Exception e) {
			wraperDTO.setUserResult(ErrorCodeConsts.ParaNotRight);
			wraperDTO.setDescription(wraperDTO.getDescription()+"\n"+e.getMessage());
			return wraperDTO;
		}	
		
		List<Object[]> results = null;
		try {
			for (Object devIdObj : array) {
				String devId = devIdObj.toString().trim();
				if(StringUtil.isNUllOrEmpty(devId)){
					continue;
				}

				if (dto.getType() == 1) {
					if (dto.getEndTime() == null) {
						results = spmsDeviceManager.getAcPowerRecords(devId);
					} else {
						results = spmsDeviceManager.getAcPowerRecords(devId, dto.getEndTime());
					}
				} else {
					if (dto.getEndTime() == null) {
						results = spmsDeviceManager.getAcEnergyRecords(devId, dto.getStyle());
					} else {
						results = spmsDeviceManager.getAcEnergyRecords(devId, dto.getStyle(), dto.getEndTime());
					}
				}
				if (results != null) {
					ret.put(devId, results);
				}
			}
			wraperDTO.setData(ret);
			
		} catch (ParseException e) {
			wraperDTO.setData(null);
			e.printStackTrace();
		}

		return wraperDTO;
	}
	
	
	@RequestMapping(value = "/pirs/{pir_id}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deletePir(@PathVariable String pir_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(pir_id,EnumTypesConsts.DeviceType.Dev_Type_Pir);   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		return deviceRespWraperDTO;
	}
	
	//  below is default method
	@RequestMapping(value = "/pirs/{pir_id}/{field_name}/{field_value}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updatePirFields(@PathVariable String pir_id, @PathVariable String field_name,@PathVariable String field_value) {		
		return spmsDeviceManager.updateDeviceField(SpmsPir.class,pir_id,field_name,field_value);
	}
	
	@RequestMapping(value = "/plugacs", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addPlugs(DeviceWraperDTO devDTO){	
		devDTO.setUserResult(ErrorCodeConsts.Success);		
		SpmsPlugAc plugAc = new SpmsPlugAc();
		BeanUtils.copyProperties(devDTO, plugAc);
		plugAc.setType(EnumTypesConsts.DeviceType.Dev_Type_Plug_AC);
		spmsPlugDAO.save(plugAc);
		return devDTO;
	}
	@RequestMapping(value = "/plugacs", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getPlugs(){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.setDevices(spmsDeviceManager.getDevicesDTO(EnumTypesConsts.DeviceType.Dev_Type_Plug_AC));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/plugacs/{plugs_id}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getPlugInfo(@PathVariable String plug_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
	   
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());
		
		deviceRespWraperDTO.getDevices().add(spmsDeviceManager.getDeviceDTO(plug_id,EnumTypesConsts.DeviceType.Dev_Type_Plug_AC));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/plugacs/{plug_id}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deletePlug(@PathVariable String plug_id){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(plug_id,EnumTypesConsts.DeviceType.Dev_Type_Plug_AC); 
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());		
		return deviceRespWraperDTO;
	}
	
	//NEW TODO
	@RequestMapping(value = "/devices", method = RequestMethod.POST)
    @ResponseBody
	public DeviceWraperDTO addDevice(@RequestBody DeviceWraperDTO devDTO){		
		devDTO.setUserResult(ErrorCodeConsts.Success);
		if(!(Validator.Instance().checkDeviceDTO(devDTO.getDevice()))){
			devDTO.setUserResult(ErrorCodeConsts.ParaNotRight);
			return devDTO;
		}
		SpmsDeviceDTO dto = spmsDeviceManager.addDevice(devDTO.getDevice());
		if(dto == null){
			devDTO.setUserResult(ErrorCodeConsts.UnknowError);
		}		
		
		return devDTO;
	}

	
	@RequestMapping(value = "/devices/{device_id}/type/{type}", method = RequestMethod.GET)
    @ResponseBody
	public UserDeviceRespWraperDTO getDeviceInfo(@PathVariable String device_id,@PathVariable Integer type){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
		
		
		deviceRespWraperDTO.getDevices().add(spmsDeviceManager.getDeviceDTOByMac(device_id, type));
		return deviceRespWraperDTO;
	}
	
	@RequestMapping(value = "/devices/{device_id}/type/{type}", method = RequestMethod.DELETE)
    @ResponseBody
	public UserDeviceRespWraperDTO deleteDevice(@PathVariable String device_id , @PathVariable Integer type){	
		UserDeviceRespWraperDTO deviceRespWraperDTO = spmsDeviceManager.deleteDevice(device_id, type);
		return deviceRespWraperDTO;
	}
	
	//  below is default method
	@RequestMapping(value = "/devices/{device_id}/{field_name}/{field_value}/type/{type}", method = RequestMethod.PUT)
	public @ResponseBody
	UserDeviceRespWraperDTO updateDeviceFields(@PathVariable String device_id, @PathVariable String field_name,@PathVariable String field_value,@PathVariable Integer type) {
		Class<?> clz = DeviceDAOAccessor.getInstance().getDevClz(type);
		return spmsDeviceManager.updateDeviceField(device_id,type,field_name,field_value);
	}

	@RequestMapping(value = "/devices/{device_id}/onOff/{onOff}/type/{type}", method = RequestMethod.PUT)
	public @ResponseBody
	GenericWraperDTO updateDeviceOnOff(@PathVariable String device_id, @PathVariable String onOff, @PathVariable Integer type) {		
		SpmsDeviceBase device = spmsDeviceManager.getDeviceByMac(device_id, type);
		GenericWraperDTO dto = GenericWraperDTO.getDefaultWraperDTO();
		if (device == null || device.getGateway() == null ) {
			dto.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return dto;
		}
		Map<String, Object> result = null;
		if(onOff.equals("0")){			
			result = spmsAppManager.changeAcStatus(device.getGateway().getMac(), device_id,type,CommandUtil.CommandType.OFF,0);
		}
		else if(onOff.equals("1")){
			result = spmsAppManager.changeAcStatus(device.getGateway().getMac(), device_id,type,CommandUtil.CommandType.ON,1);			
		}
		dto.setUserResult((String)result.get("status"));
		return dto;
	}
	
	// share device to others
	@RequestMapping(value = "/devices/{device_id}/owner/{owner_id}/users/{user_id}", method = RequestMethod.POST)
    @ResponseBody
	public UserDeviceWraperDTO shareDevice(@PathVariable String device_id,@PathVariable String owner_id,@PathVariable String user_id){		
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		if(owner_id.equals(user_id))
			return userWraperDTO;
		
		Map<String,Object> ret = Validator.Instance().checkUserExist(user_id);
		if(!(boolean)ret.get("success")){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
			return userWraperDTO;
		}
		ret = Validator.Instance().checkUserDeviceExist(owner_id, device_id, EnumTypesConsts.UserDeviceType.User_Primary);
		if(!(boolean)ret.get("success")){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotPrimary);
			return userWraperDTO;
		}		
		
		SpmsUserDevice ud = (SpmsUserDevice)ret.get("result");
		if(ret.get("idType").toString().equals("mobile")){
			return spmsUserManager.addUserDevice(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared,
					ud.getDeviceType());
		}else{
			return spmsUserManager.addUserDeviceById(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared,
					ud.getDeviceType());
		}
	}
	
	// share device to others without validation
	@RequestMapping(value = "/devices/{device_id}/users/{user_id}", method = RequestMethod.POST)
	@ResponseBody
	public UserDeviceWraperDTO shareDevice(@PathVariable String device_id, @PathVariable String user_id) {
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();

		Map<String, Object> ret = Validator.Instance().checkUserExist(user_id);
		if (!(boolean) ret.get("success")) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
			return userWraperDTO;
		}
		
		ret = Validator.Instance().checkUserDeviceExist(user_id, device_id);
		if ((boolean) ret.get("success")) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceAlreadyExist);
			return userWraperDTO;
		} else {
			//if no primary return error
			ret = Validator.Instance().checkUserDeviceExist(device_id,EnumTypesConsts.UserDeviceType.User_Primary);
			if(!(boolean) ret.get("success")){
				userWraperDTO.setUserResult(ErrorCodeConsts.UserNotPrimary);
				return userWraperDTO;
			}
			SpmsUserDevice ud = (SpmsUserDevice) ret.get("result");
			
			if (ret.get("idType").toString().equals("mobile")) {
				return spmsUserManager.addUserDevice(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared,
						ud.getDeviceType());
			} else {
				return spmsUserManager.addUserDeviceById(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared,
						ud.getDeviceType());
			}
		}
	}

	// Change this original primary bound user to another user
	@RequestMapping(value = "/devices/{device_id}/owner/{owner_id}/users/{user_id}/auth", method = RequestMethod.POST)
	public @ResponseBody UserDeviceWraperDTO changePrimaryUser(@PathVariable String device_id, @PathVariable String owner_id
			, @PathVariable String user_id) {
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		
		if(owner_id.equals(user_id))
			return userWraperDTO;

		Map<String,Object> ret = Validator.Instance().checkUserDeviceExist(owner_id,device_id,EnumTypesConsts.UserDeviceType.User_Primary);
		if(!(boolean)ret.get("success")){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			return userWraperDTO;
		}else {
			SpmsUserDevice udPrimary = (SpmsUserDevice)ret.get("result");
			String newId = null;
			String newMobile = null;
			
			ret = Validator.Instance().checkUserDeviceExist(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared);
			if ((boolean) ret.get("success")) {
				SpmsUserDevice ud = (SpmsUserDevice) ret.get("result");
				newId = ud.getId();
				newMobile = ud.getMobile();
				spmsUserManager.deleteUserDeviceById(newId);
			} else {
				ret = Validator.Instance().checkUserExist(user_id);
				if (!(boolean) ret.get("success")) {
					userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
					return userWraperDTO;
				} else {
					UserEntity newUser  = (UserEntity) ret.get("result");
					newId = newUser.getId();
					newMobile = newUser.getMobile();					
				}
			}			
			
			udPrimary.setUserId(newId);
			udPrimary.setMobile(newMobile);
		
			BeanUtils.copyProperties(udPrimary,userWraperDTO.getUserDevice());
			spmsUserDeviceDAO.save(udPrimary);
		}
		return userWraperDTO;
	}
	
	// cancel share device to others
	@RequestMapping(value = "/devices/{device_id}/owner/{owner_id}/users/{user_id}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserDeviceWraperDTO cancelShareDevice(@PathVariable String device_id, @PathVariable String owner_id,
			@PathVariable String user_id) {
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		if(owner_id.equals(user_id))
			return userWraperDTO;
		
		Map<String, Object> ret = Validator.Instance().checkUserDeviceExist(owner_id, device_id);
		if (!(boolean) ret.get("success")) {
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			return userWraperDTO;
		} else {
			SpmsUserDevice ud1 = (SpmsUserDevice) ret.get("result");
			if (ud1.getIsPrimary() == 1) {
				if (owner_id.equals(user_id)) {
					SpmsUserDevice ud = (SpmsUserDevice) ret.get("result");
					spmsUserManager.resetUserDevice(device_id);
					BeanUtils.copyProperties(ud, userWraperDTO.getUserDevice());
					return userWraperDTO;
				} else {
					ret = Validator.Instance().checkUserDeviceExist(user_id, device_id, EnumTypesConsts.UserDeviceType.User_Shared);
					if (!(boolean) ret.get("success")) {
						userWraperDTO.setUserResult(ErrorCodeConsts.UserNotExist);
						return userWraperDTO;
					} else {

						SpmsUserDevice ud2 = (SpmsUserDevice) ret.get("result");
						spmsUserManager.deleteUserDeviceById(ud2.getId());
						BeanUtils.copyProperties(ud2, userWraperDTO.getUserDevice());
						return userWraperDTO;
					}
				}
			} else {
				if (!owner_id.equals(user_id)) {
					userWraperDTO.setUserResult(ErrorCodeConsts.UserNotPrimary);
					return userWraperDTO;
				} else {
					spmsUserManager.deleteUserDeviceById(ud1.getId());
					BeanUtils.copyProperties(ud1, userWraperDTO.getUserDevice());
					return userWraperDTO;
				}
			}
		}
	}
	
	//1. If user_id is not primary,cancel share device to oneself
	//2. If user_id is primary, delete all user_device
	@RequestMapping(value = "/devices/{device_id}/users/{user_id}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserDeviceWraperDTO cancelShareDevice(@PathVariable String device_id, @PathVariable String user_id) {
		
		UserDeviceWraperDTO userWraperDTO = UserDeviceWraperDTO.getDefaultWraperDTO();
		
		Map<String,Object> ret = Validator.Instance().checkUserDeviceExist(user_id, device_id);
		if(!(boolean)ret.get("success")){
			userWraperDTO.setUserResult(ErrorCodeConsts.UserDeviceNotExist);
			return userWraperDTO;
		}		
		SpmsUserDevice ud =(SpmsUserDevice) ret.get("result");
		if(ud.getIsPrimary() == 0){
			spmsUserManager.deleteUdGroupByUdId(ud.getId());
			spmsUserManager.deleteUserDeviceById(ud.getId());
		}else{
			spmsUserManager.resetUserDevice(null,device_id);
		}
		
		BeanUtils.copyProperties(ud,userWraperDTO.getUserDevice());
		return userWraperDTO;
	}
	
}
