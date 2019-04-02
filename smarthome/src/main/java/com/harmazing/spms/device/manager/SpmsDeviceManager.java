package com.harmazing.spms.device.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.xml.ws.RespectBinding;

import org.activiti.engine.impl.bpmn.parser.Error;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.ConstantUtil;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.MongoUtil;
import com.harmazing.spms.base.util.PropertyUtil;
import com.harmazing.spms.base.util.RestUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.common.entity.GenericEntity;
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
import com.harmazing.spms.helper.DeviceDAOAccessor;
import com.harmazing.spms.helper.DeviceDAOData;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.helper.UserDeviceRespWraperDTO;
import com.harmazing.spms.helper.UserWraperDTO;
import com.harmazing.spms.helper.Validator;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserDeviceDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.manager.SpmsUserManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.sf.json.JSONObject;

@Service("spmsDeviceManager")
public class SpmsDeviceManager implements IManager {
	@Autowired
    private SpmsGatewayDAO spmsGwDAO;
	@Autowired
    private SpmsAirConditionDAO spmsAcDAO;
	@Autowired
    private SpmsPirDAO spmsPirDAO;
	@Autowired
    private SpmsWinDoorDAO spmsWdDAO;
	@Autowired
    private SpmsPlugAcDAO spmsPlugDAO;
	@Autowired
    private SpmsCentralAcDAO spmsCentralCtrlDAO;
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private SpmsUserProductBindingDAO spmsUserProductBindingDAO;
	@Autowired
	private SpmsUserDAO spmsUserDAO;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private SpmsUserDeviceDAO spmsUserDeviceDAO;

	@Transactional
	public UserDeviceRespWraperDTO updateDeviceField(Class clz ,String mac,String fieldName, String newValue){
		Integer type = null;
		List<DeviceDAOData> daoDatas = DeviceDAOAccessor.getInstance().getAllDAO();
		for(DeviceDAOData data: daoDatas){
			if(data.getDevClass() == clz){
				type = data.getType();
				break;
			}
		}
		if(type == null)
			return UserDeviceRespWraperDTO.getDefaultWraperDTO();
		return updateDeviceField(mac,type,fieldName,newValue);
		
	}
	//(SpmsAirCondition.class,ac_id,"status",status);
	@Transactional
	public UserDeviceRespWraperDTO updateDeviceField(String mac, Integer devType,String fieldName, String newValue) {
		UserDeviceRespWraperDTO deviceRespWraperDTO = UserDeviceRespWraperDTO.getDefaultWraperDTO();
		deviceRespWraperDTO.setUserCode(UserUtil.getCurrentUserCode());

		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);
		SpmsDeviceBase devBase = getDeviceByMac(mac,devType);
		Class<?> paraCls = null;
		if (devBase == null) {
			deviceRespWraperDTO.setUserResult(ErrorCodeConsts.DeviceNotExist);
			return deviceRespWraperDTO;
		}
		byte[] items = fieldName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		String methodName = "set" + new String(items);
		Method m = null;
		Class<?> clazz = DeviceDAOAccessor.getInstance().getDevClz(devType);
		try {
			m = clazz.getMethod(methodName, String.class);
			paraCls = String.class;
			m.invoke(devBase, newValue);
		} catch (Exception e) {

			try {
				m = clazz.getMethod(methodName, Integer.class);
				paraCls = Integer.class;
				m.invoke(devBase, Integer.parseInt(newValue));
			} catch (Exception e1) {
				try {
					m = clazz.getMethod(methodName, Float.class);
					paraCls = Float.class;
					m.invoke(devBase, Float.parseFloat(newValue));
				} catch (Exception e2) {
				}
				
			}
		} 
		if (m == null) {
			deviceRespWraperDTO.setUserResult(ErrorCodeConsts.MethodNotExist);
			return deviceRespWraperDTO;
		}
		
		daoSave(devType, devBase);

		// update DTO
		SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
		try {
			m = SpmsDeviceDTO.class.getDeclaredMethod(methodName, paraCls);
			if (paraCls == Integer.class) {
				m.invoke(deviceDTO, Integer.parseInt(newValue));
			} else if(paraCls == Float.class){
				m.invoke(deviceDTO, Float.parseFloat(newValue));
			}else{
				m.invoke(deviceDTO, newValue);
			}
			deviceRespWraperDTO.getDevices().add(deviceDTO);
		} catch (Exception e) {
			deviceRespWraperDTO.setUserResult(ErrorCodeConsts.UnknowError);
			return deviceRespWraperDTO;
		}
		return deviceRespWraperDTO;
	}
    
    
    @Transactional(readOnly=false)
    
    public SpmsDeviceDTO doEditBaseInfo(SpmsDeviceDTO spmsDeviceDTO) {
    	SpmsDeviceBase spmsDevice = null;
    	switch(spmsDeviceDTO.getType())
    	{
    	case 1:
    	case 2:
    	case 3:
    		spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	case 4:
    		spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	case 5:
    		spmsDevice = spmsPirDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	}
		if(spmsDevice == null)
			return spmsDeviceDTO;
	    spmsDevice.setSn(spmsDeviceDTO.getSn());
	    spmsDevice.setMac(spmsDeviceDTO.getMac());
	    spmsDevice.setVender(spmsDeviceDTO.getVender());
	    spmsDevice.setMode(spmsDeviceDTO.getMode());
	    spmsDevice.setHardware(spmsDeviceDTO.getHardware());
	    spmsDevice.setSoftware(spmsDeviceDTO.getSoftware());
	    
	    switch(spmsDeviceDTO.getType())
    	{
    	case 1:
    	case 2:
    	case 3:
    		spmsDevice = spmsAcDAO.saveAndFlush((SpmsAirCondition)spmsDevice);
    		break;
    	case 4:
    		spmsDevice = spmsWdDAO.saveAndFlush((SpmsWinDoor)spmsDevice);
    		break;
    	case 5:
    		spmsDevice = spmsPirDAO.saveAndFlush((SpmsPir)spmsDevice);
    		break;
    	}
		return spmsDeviceDTO;
    }
    @Transactional
    
    public SpmsDeviceDTO doEditOpreInfo(SpmsDeviceDTO spmsDeviceDTO){
    	SpmsDeviceBase spmsDevice = null;
    	switch(spmsDeviceDTO.getType())
    	{
    	case 1:
    	case 2:
    	case 3:
    		spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	case 4:
    		spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	case 5:
    		spmsDevice = spmsPirDAO.findOne(spmsDeviceDTO.getId());
    		break;
    	}
    	
    	spmsDevice.setCursoft(spmsDeviceDTO.getCursoft());
    	switch(spmsDeviceDTO.getType())
    	{
    	case 1:
    	case 2:
    	case 3:
    		spmsDevice = spmsAcDAO.saveAndFlush((SpmsAirCondition)spmsDevice);
    		break;
    	case 4:
    		spmsDevice = spmsWdDAO.saveAndFlush((SpmsWinDoor)spmsDevice);
    		break;
    	case 5:
    		spmsDevice = spmsPirDAO.saveAndFlush((SpmsPir)spmsDevice);
    		break;
    	}
    	return spmsDeviceDTO;
    }
    
    /* (non-Javadoc)
     * @see com.harmazing.spms.device.manager.SpmsDeviceManager#doSave(com.harmazing.spms.device.dto.SpmsDeviceDTO)
     */
    
    @Transactional
    public SpmsDeviceDTO doSave(SpmsDeviceDTO spmsDeviceDTO) {
    	
    	SpmsDeviceBase spmsDevice = null;
		if (spmsDeviceDTO.getId() != null) {
			switch (spmsDeviceDTO.getType()) {
	    	case 1:
	    	case 2:
	    	case 3:
				spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
				break;
			case 4:
				spmsDevice = spmsWdDAO.findOne(spmsDeviceDTO.getId());
				break;
			case 5:
				spmsDevice = spmsPirDAO.findOne(spmsDeviceDTO.getId());
				break;
			}
		}else {
			switch (spmsDeviceDTO.getType()) {
	    	case 1:
	    	case 2:
	    	case 3:
				spmsDevice = new SpmsAirCondition();
				break;
			case 4:
				spmsDevice = new SpmsWinDoor();
				break;
			case 5:
				spmsDevice = new SpmsPir();
				break;
			}
		}
		BeanUtils.copyProperties(spmsDeviceDTO, spmsDevice);
		spmsDevice.setOperStatus(0);
		spmsDevice.setOnOff(0);
    	switch(spmsDeviceDTO.getType())
    	{
    	case 1:
    	case 2:
    	case 3:
    		spmsDevice = spmsAcDAO.saveAndFlush((SpmsAirCondition)spmsDevice);
    		break;
    	case 4:
    		spmsDevice = spmsWdDAO.saveAndFlush((SpmsWinDoor)spmsDevice);
    		break;
    	case 5:
    		spmsDevice = spmsPirDAO.saveAndFlush((SpmsPir)spmsDevice);
    		break;
    	}
		spmsDeviceDTO.setId(spmsDevice.getId());
		return spmsDeviceDTO;
    }
    @Transactional(readOnly = false)
    public boolean deleteByIds(String ids){
    	if(StringUtils.isNotBlank(ids)){
    		if(ids.indexOf(',')>0){
    			String[] idArra = ids.split(",");
    			for(String id : idArra){
    					spmsAcDAO.doDeleteById(id);
    					spmsWdDAO.doDeleteById(id);
    					spmsPirDAO.doDeleteById(id);
    			}
    			return true;
    		}else{
				spmsAcDAO.doDeleteById(ids);
				spmsWdDAO.doDeleteById(ids);
				spmsPirDAO.doDeleteById(ids);
    			return true;
    		}
    		
    	}else{
    		return false;
    	}
    }
	
	public List<Object[]> findAllPowerById(String deviceid) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,power from spms_ac_status where device_id ='"+ deviceid +"' " +
				" and DATE_FORMAT(start_time,'%Y-%m-%d') >= '" + date + "' ORDER BY start_time asc";		
		
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((BigInteger) m.get("power")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> findAllReactivePowerById(String deviceid)  throws ParseException{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,reactivePower from spms_ac_status where device_id ='"+ deviceid +"'  and DATE_FORMAT(start_time,'%Y-%m-%d') >= '" + date + "' ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("reactivePower")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	public List<Object[]> findAllPowerFactorById(String deviceid)  throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,powerFactor from spms_ac_status where device_id ='"+ deviceid +"'  and DATE_FORMAT(start_time,'%Y-%m-%d') >= '" + date + "' ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("powerFactor")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	public List<Object[]> findAllApparentPowerById(String deviceid) throws ParseException  {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,apparentPower from spms_ac_status where device_id ='"+ deviceid +"' and DATE_FORMAT(start_time,'%Y-%m-%d') >= '" + date + "' ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("apparentPower")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}

	public List<Object[]> findAllEpById(Map<String, Object> info) throws ParseException  {
		//根据统计类型 查询不同表
		String sql = "";
		// 统计类型
		String rbtn = info.get("rbtn").toString();
		String deviceId = info.get("id").toString();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String formatStr = "";
		if(rbtn.equals("1")){
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,accumulatePower/1000 as power from spms_ac_status where device_id = '"+ deviceId+ "' " +
					"AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"+date+"' ORDER BY start_time asc";
			
		}else if(rbtn.equals("2")){
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,max(accumulatePower)/1000 as power from spms_ac_status where device_id = '"+ deviceId+ "' " +
					"AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"+date+"' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		}else if(rbtn.equals("3")){
			date = date.substring(0,7);
			formatStr = "yyyy-MM-dd";
			sql = " select date,accumulatePower/1000 as power from spms_ac_status_month where device_id = '" + deviceId + "' and substr(date,1,7) ='" + date + "' order by date";
		}else if(rbtn.equals("4")){
			date = date.substring(0,4);
			formatStr = "yyyy-MM";
			sql = " select date,accumulatePower/1000 as power from spms_ac_status_year where device_id = '" + deviceId + "' and substr(date,1,4) ='" + date + "' order by date";
		}
		
		
		List<Map<String,Object>> d = queryDAO.getMapObjects(sql);
		//如果该用户没有绑定网关，则就没有对应的设备。
		List<Object[]> datas = new ArrayList<Object[]>();
		for (Map<String,Object> map : d) {
			//int power = ((BigInteger) map.get("power")).intValue();
			double power = ((BigDecimal) map.get("power")).doubleValue();
			long time =  new SimpleDateFormat(formatStr).parse(map.get("date").toString()).getTime(); 
			Object[] data = new Object[] { time, power };
			datas.add(data);
		}
		return datas;
	}


	
	public List<Object[]> findAllReactiveEnergyById(Map<String, Object> info) throws ParseException  {
		// 根据统计类型 查询不同表
		String sql = "";
		// 统计类型
		String rbtn = info.get("rbtn").toString();
		String deviceId = info.get("id").toString();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String formatStr = "";
		if (rbtn.equals("1")) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,reactiveEnergy as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' ORDER BY start_time asc";

		} else if (rbtn.equals("2")) {
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,max(reactiveEnergy) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		} else if (rbtn.equals("3")) {
			date = date.substring(0, 7);
			formatStr = "yyyy-MM-dd";
			sql = " select date,reactiveEnergy as power from spms_ac_status_month where device_id = '"
					+ deviceId
					+ "' and substr(date,1,7) ='"
					+ date
					+ "' order by date";
		} else if (rbtn.equals("4")) {
			date = date.substring(0, 4);
			formatStr = "yyyy-MM";
			sql = " select date,reactiveEnergy as power from spms_ac_status_year where device_id = '"
					+ deviceId
					+ "' and substr(date,1,4) ='"
					+ date
					+ "' order by date";
		}

		List<Map<String, Object>> d = queryDAO.getMapObjects(sql);
		// 如果该用户没有绑定网关，则就没有对应的设备。
		List<Object[]> datas = new ArrayList<Object[]>();
		for (Map<String, Object> map : d) {
			int power = ((Integer) map.get("power")).intValue();
			long time = new SimpleDateFormat(formatStr).parse(
					map.get("date").toString()).getTime();
			Object[] data = new Object[] { time, power };
			datas.add(data);
		}
		return datas;
	}


	
	public List<Object[]> findAllActiveDemandById(String deviceid) throws ParseException  {
		List<Object[]> result = Lists.newArrayList();
		String sql = "select demandTime,activeDemand from spms_ac_status where device_id ='"+ deviceid +"'  AND activeDemand is not null ORDER BY demandTime asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("activeDemand")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("demandTime").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("demandTime")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> findAllReactiveDemandById(String deviceid) throws ParseException  {
		List<Object[]> result = Lists.newArrayList();
		String sql = "select demandTime,reactiveDemand from spms_ac_status where device_id ='"+ deviceid +"' AND reactiveDemand is not null ORDER BY demandTime asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("reactiveDemand")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("demandTime").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("demandTime")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> findAllCurrentById(String deviceid) throws ParseException  {
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,current from spms_ac_status where device_id ='"+ deviceid +"' ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("current")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> findAllVoltageById(String deviceid) throws ParseException  {
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,voltage from spms_ac_status where device_id ='"+ deviceid +"'  ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("voltage")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> findAllFrequencyById(String deviceid) throws ParseException  {
		List<Object[]> result = Lists.newArrayList();
		String sql = "select start_time,frequency from spms_ac_status where device_id ='"+ deviceid +"'  ORDER BY start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size(); i ++){
			Map<String ,Object> m = list.get(i);
			float power = ((Integer) m.get("frequency")).intValue();
			//Long time = DateUtil.parseStringToDate(m.get("start_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			Long time = ((Date)m.get("start_time")).getTime();
			result.add(new Object[] { time, power });
		}
		return result;
	}


	
	public List<Object[]> deviceData(String deviceid, String type) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		
		if(type.equals("1")){
			String sql = "select * from spms_gw_status where device_id ='"+ deviceid +"' and DATE_FORMAT(create_time,'%Y-%m-%d %H') >= '" + date + "' ORDER BY create_time asc";
			List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
			for(int i = 0 ; i < list.size(); i ++){
				Map<String ,Object> m = list.get(i);
				int power = ((Integer) m.get("status")).intValue();
				//Long time = DateUtil.parseStringToDate(m.get("create_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
				Long time = ((Date)m.get("create_time")).getTime();
				result.add(new Object[] { time, power });
			}
		}else if(type.equals("3") || type.equals("4")){
//			MongoDatabase db = MongoUtil.getDB("spms");
//			MongoCollection<Document> coll = db.getCollection("WD_LOG:");
//			Document d = new Document();
//			Calendar c = Calendar.getInstance();
//			c.add(Calendar.DATE, -1);
//			d.put("timestamp", new Document("$gt",c.getTime().getTime()+""));
//			Iterator<Document> ite = coll.find(d).iterator();
			String sql = "select * from spms_win_door_status where device_id ='"+ deviceid +"' and DATE_FORMAT(operate_time,'%Y-%m-%d %H') >= '" + date + "' ORDER BY operate_time asc";
			List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
			for(int i = 0 ; i < list.size(); i ++){
				Map<String ,Object> m = list.get(i);
				int power = ((Integer) m.get("operate_type")).intValue();
				//Long time = DateUtil.parseStringToDate(m.get("operate_time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
				Long time = ((Date)m.get("operate_time")).getTime();
				result.add(new Object[] { time, power });
			}
		}
		return result;
	}

	public Map<String, Object> getDevcieRuninfo(String deviceid, String type) {
		Map<String,Object> result = Maps.newHashMap();
		SpmsDeviceBase device = findDevice(deviceid);
		if(device!=null && device.getId()!=null){
			if(type.equals("1")){
				result.put("success", true);
				result.put("operstatus", device.getOperStatus());
				result.put("devicelist", findDeviceByGw(deviceid));
			}else if(type.equals("2")){
				result.put("success", true);
				result.put("type", device.getType());
				result.put("onoff", device.getOnOff());
//				result.put("temp", device.getTemp());
//				result.put("actemp", device.getAcTemp());
				result.put("mode", device.getMode());
//				result.put("speed", device.getSpeed());
				result.put("operstatus", device.getOperStatus());
			}else if(type.equals("3")){
				result.put("success", true);
				result.put("type", device.getType());
				result.put("onoff", device.getOnOff());
				result.put("operstatus", device.getOperStatus());
//				result.put("remain", device.getRemain());
			}
		}else{
			result.put("success", false);
			result.put("msg", "没有找到设备");
		}
		
		return result;
	}
	
	public List<Map<String, Object>> findDeviceByGw(String gwid) {
		List<Map<String, Object>> result = Lists.newArrayList();
		List<Map<String, Object>> list = Lists.newArrayList();
		String sql = "select sd.type,sd.id,sd.mac,sd.operStatus as operStatus from spms_user_product_binding surb,spms_device sd where surb.gwId='"+gwid+"' and surb.device_id=sd.id";
		list =  queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> m = list.get(i);
			int type = (int) m.get("type");
			String typetext = DictUtil.getDictValue("device_type", String.valueOf(type));
			m.put("typetext", typetext);
			result.add(m);
		}
		return result;
	}


	
	public Map<String, Object> getDeviceConfigInfo(String deviceid) {
		Map<String, Object> result = Maps.newHashMap();
//		List<SpmsUserProductBinding> temp = spmsUserProductBindingDAO.findByDevice(deviceid);
//		if(temp.size()==0){
//			result.put("bind", false);
//			result.put("customname", "未绑定");
//			result.put("status", "未绑定");
//			result.put("mode", "未绑定");
//			result.put("temp", "未绑定");
//			result.put("speed", "未绑定");
//		}else{
//			SpmsUserProductBinding supb = temp.get(0);
//			SpmsProductType spt = supb.getSpmsProductType();
//			result.put("bind", true);
//			result.put("customname", supb.getCustomName()==null?"未设置":supb.getCustomName());
//			result.put("status", "未锁定");
//			result.put("mode", DictUtil.getDictValue("producttype_config", String.valueOf(spt.getConfigurationInformation())));
//			result.put("temp", "制冷："+spt.getZhiLengMix()+"-"+spt.getZhiLengMax()+"℃  / 制热："+spt.getZhiReMix()+"-"+spt.getZhiReMax()+"℃");
//			result.put("speed", "自动/静音/5/4/3/2/1");
//		}
		return result;
	}


	
	public String saveAll(List<SpmsDeviceBase> list) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<SpmsDeviceBase> insert = new ArrayList<SpmsDeviceBase>();
		Map macMap=new HashMap();
		Map snMap=new HashMap();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String userId=UserUtil.getCurrentUser().getId();
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		entityManager.getTransaction().begin();	
		
		DataSource ds=(DataSource)SpringUtil.getBeanByName("dataSource");
		Connection conn=ds.getConnection();
		conn.setAutoCommit(false);
		Statement st=conn.createStatement();
		boolean bs=false;
		for (int i = 0; i < list.size() ; i++) {
			bs=false;
			//判断导入项是否为空
			if(null == list.get(i).getCursoft() || "".equals(list.get(i).getCursoft()) ||
				null == list.get(i).getSoftware() || "".equals(list.get(i).getSoftware()) ||
				null == list.get(i).getHardware() || "".equals(list.get(i).getHardware()) ||
				null == list.get(i).getMac() || "".equals(list.get(i).getMac()) ||
				//null == list.get(i).getModel() || "".equals(list.get(i).getModel()) ||
				null == list.get(i).getSn() || "".equals(list.get(i).getSn()) ||
				null == list.get(i).getStartTime() || "".equals(list.get(i).getStartTime()) ||
				null == list.get(i).getVender() || "".equals(list.get(i).getVender())){
				sb.append("第" + (i+1) + "行缺失数据，已忽略，请检查。<br/>");
				continue;
			}
			try {
				list.get(i).setModel(list.get(i).getHardware().substring(0, list.get(i).getHardware().indexOf("_")));
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行硬件版本不规范,已忽略，请检查。<br/>");
				continue;
			}
			
			//严重mac是否重复
//			List<SpmsDevice> sd = spmsDeviceDAO.findByMacAndSn(list.get(i).getMac(), list.get(i).getSn());
//			String sql = "select 1 from spms_device where mac = '" + list.get(i).getMac() + "' or sn = '" + list.get(i).getSn() + "'";
//			List sd = queryDAO.getObjects(sql);
			
//			if(null != sd && sd.size() > 0){
//				sb.append("第" + (i+1) + "行:" + "设备(sn:"+list.get(i).getSn() +",mac:" + list.get(i).getMac() + ") 重复添加，已忽略，请检查。<br/>");
//				continue;
//			}else{
//				spmsDeviceDAO.save(list.get(i));
//				insert.add(list.get(i));
//			}
			try{
//				spmsDeviceDAO.save(list.get(i));
				/*********************************/
				SpmsDeviceBase sd=list.get(i);
				//String dt=sdf.format(sd.getCreateDate());
				String sql="insert into spms_device ("
						+ "id,version, createDate, "
						+ "createUser_id, lastModifyDate,lastModifyUser_id, "
						+ " cursoft, disable,"
						+ " hardware, mac, onOff, operStatus, "
						+ " sn, software,model,"
						+ " status, storage,type, vender) values"
						+ " ("
						+ "'"+UUID.randomUUID().toString()+"',"+1+",'"+"',"
						+ "'"+userId+"','"+"','"+userId+"',"
						+ "'"+sd.getCursoft()+"',0,"
						+ "'"+sd.getHardware()+"','"+sd.getMac()+"',0,0,"
						+ "'"+sd.getSn()+"','"+sd.getSoftware()+"','"+sd.getModel()+"',"
						+ "'"+sd.getStatus()+"','"+sd.getStorage()+"','"+sd.getType()+"','"+sd.getVender()+"'"
						+ ")";
//				System.out.println(i+"========"+sql);
//				queryDAO.doExecuteSql(sql);
				
//				Query query = entityManager.createNativeQuery(sql);
//				query.executeUpdate();	
//				entityManager.getd
				st.execute(sql);
				/***********************************/
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("第" + (i+1) + "行:" + "设备(sn:"+list.get(i).getSn() +",mac:" + list.get(i).getMac() + ") 重复添加，已忽略，请检查。<br/>");
				sb.append("第" + (i+1) + "行导入失败,请检查设备mac和是否重复，或者数据格式是否正确<br/>");
			}
			if(i!=0 && i%500==0){
				bs=true;
//				System.out.println("insert commit");
//				entityManager.getTransaction().commit();
				//entityManager.getTransaction().begin();	
				conn.commit();
			}
//			System.out.println(i+"*******"+bs);
		}
		if(bs==false){
//			entityManager.getTransaction().commit();
			conn.commit();
		}
//		
//		entityManager.close();
		st.close();
		conn.close();
		return sb.toString();
	}


	
	public List<Map<String, Object>> getStorageAll() {
		List<Map<String, Object>> list = Lists.newArrayList();
		String sql="select code,value from dict_device_storage";
		list =  queryDAO.getMapObjects(sql);
		return list;
	}

/**
 * 门窗传感用电信息折线图所需数据
 */
	
	public List<Map<String, Object>> findSpmsAcStatusForDeviceId(String deviceid) {
		// TODO Auto-generated method stub
		String sql = "select * from spms_ac_status where device_id ='"+ deviceid +"'  ORDER BY start_time asc";
		return queryDAO.getMapObjects(sql);
	}


	
	public Map<String, Object> getRefreshZigbee(String type, String deviceId) {
		// TODO Auto-generated method stub
		Map<String, Object> sdt = new HashMap<String,Object>();
		String zigbeeJson = "";
		if("1".equals(type)){//网关
			List<Map<String, Object>> gateway_zigbeeList = queryDAO.getMapObjects("SELECT * from spms_gateway_zigbee t1 where t1.gwid='"+deviceId+"' ");
			if( gateway_zigbeeList != null && gateway_zigbeeList.size() != 0 ){
				Map<String, Object> m = gateway_zigbeeList.get(0);
				zigbeeJson = "{\"zigbeeChannel\":"+m.get("channel")+
						",\"zigbeeChannelMask\":"+m.get("channel")+",\"updateTime\":"+m.get("updateTime")+",\"zigbeeTxPower\":"+m.get("txpower")+"}";
			}else{
				zigbeeJson = "{\"zigbeeChannel\":0,\"updateTime\":\"\",\"zigbeeChannelMask\":0,\"zigbeeTxPower\":0}";
			}
			sdt.put("zigbee",zigbeeJson);
		}else if("2".equals(type)){//空调
			List<Map<String, Object>> ac_zigbeeList = queryDAO.getMapObjects("SELECT * from spms_ac_zigbee t1 where t1.deviceId='"+deviceId+"' ");
			if( ac_zigbeeList != null && ac_zigbeeList.size() != 0 ){
				Map<String, Object> m = ac_zigbeeList.get(0);
				zigbeeJson = "{\"averageRxRssi\":"+m.get("averageRxRssi")+",\"averageRxLQI\":"+m.get("averageRxLQI")+
				",\"averageTxRssi\":"+m.get("averageTxRssi")+",\"updateTime\":"+m.get("updateTime")+",\"averageTxLQI\":"+m.get("averageTxLQI")+"}";
			}else{
				zigbeeJson = "{\"averageRxRssi\":0,\"averageRxLQI\":0,\"updateTime\":\"\",\"averageTxRssi\":0,\"averageTxLQI\":0}";
			}
			sdt.put("zigbee",zigbeeJson);
		}
		return sdt;
	}
	
	public String getUserIdByDevice(String type, String deviceId){
		String sql="";
		String userId="";
		if("1".equals(type)){//网关
			sql="select user_id from spms_user_product_binding where gwId='"+deviceId+"'";
		}else if("2".equals(type)){//空调
			sql="select user_id from spms_user_product_binding where device_id='"+deviceId+"'";
		}
		if(!sql.equals("")){
			List <Map<String,Object>> lst=queryDAO.getMapObjects(sql);
			if(lst!=null && lst.size()>0){
				userId=(String) lst.get(0).get("user_id");
			}
		}
		return userId;
	}
	
	public List<SpmsDeviceBase> findByMacAndSn(String mac,String sn){
		List<SpmsDeviceBase> sd = Lists.newArrayList();
		List tmp = spmsAcDAO.findByMacAndSn(mac, sn);
		if(tmp != null)
			sd.addAll(tmp);
		tmp = spmsAcDAO.findByMacAndSn(mac, sn);
		if(tmp != null)
			sd.addAll(tmp);
		tmp = spmsPirDAO.findByMacAndSn(mac, sn);
		if(tmp != null)
			sd.addAll(tmp);
		return sd;
	}
	

	public SpmsDeviceBase findDevice(String devId){
		SpmsDeviceBase device = getDevice(devId, EnumTypesConsts.DeviceType.Dev_Type_All,"findOne");		
		return device;		
	}
	
	//NEW

	
    public SpmsDeviceDTO getSpmsDevice(SpmsDeviceDTO sdt) {
    	SpmsDeviceDTO spmsDevice = getDeviceDTOById(sdt.getId(),EnumTypesConsts.DeviceType.Dev_Type_All);
    	BeanUtils.copyProperties(spmsDevice, sdt);
    	sdt.setStorageText(DictUtil.getDictValue("device_storage", spmsDevice.getStorage().toString()));
    	sdt.setStatusText(DictUtil.getDictValue("device_status", spmsDevice.getStatus().toString()));
    	/**
    	 * 	spms_gateway_zigbee 网关zigbee信息表
			spms_ac_zigbee 空调zigbee信息表
    	 */
    	String zigbeeJson = "";
    	if(sdt.getType() == 1){//网关
    		List<Map<String, Object>> gateway_zigbeeList = queryDAO.getMapObjects("SELECT * from spms_gateway_zigbee t1 where t1.gwid='"+sdt.getId()+"' ");
    		if( gateway_zigbeeList != null && gateway_zigbeeList.size() != 0 ){
    			Map<String, Object> m = gateway_zigbeeList.get(0);
    			String updateTime=null;
    			if(m.get("updateTime")!=null){
    				updateTime=m.get("updateTime").toString();
    			}
    			zigbeeJson = "{\"zigbeeChannel\":"+m.get("channel")+
    					",\"updateTime\":\""+updateTime+"\",\"zigbeeChannelMask\":"+m.get("channel")+",\"zigbeeTxPower\":"+m.get("txpower")+"}";
    			//required int32  zigbeeChannel       = 7;           (页面显示：Zigbee通道）
    			//required int32  zigbeeChannelMask   = 8; （页面显示：Zigbee通道掩码）
    			// required int32  zigbeeTxPower    = 9;       （页面显示：Zigbee发送功率）
    			sdt.setZigbee(zigbeeJson);
    		}else{
    			zigbeeJson = "{\"zigbeeChannel\":\"\",\"updateTime\":\"\",\"zigbeeChannelMask\":\"\",\"zigbeeTxPower\":\"\"}";
    			sdt.setZigbee(zigbeeJson);
    		}
    	}else if(sdt.getType() == 2){//空调
    		List<Map<String, Object>> ac_zigbeeList = queryDAO.getMapObjects("SELECT * from spms_ac_zigbee t1 where t1.deviceId='"+sdt.getId()+"' ");
    		if( ac_zigbeeList != null && ac_zigbeeList.size() != 0 ){
    			Map<String, Object> m = ac_zigbeeList.get(0);
    			String updateTime=null;
    			if(m.get("updateTime")!=null){
    				updateTime=m.get("updateTime").toString();
    			}
    			if(updateTime==null){
    				updateTime="";
    			}
    			zigbeeJson = "{\"averageRxRssi\":"+m.get("averageRxRssi")+",\"averageRxLQI\":"+m.get("averageRxLQI")+
    			",\"updateTime\":\""+updateTime+"\",\"averageTxRssi\":"+m.get("averageTxRssi")+",\"averageTxLQI\":"+m.get("averageTxLQI")+"}";
    			/*
    			 * AC：
					required int32 averageRxRssi    = 1;            （页面显示：RxRssi平均值）
				    required int32 averageRxLQI        = 2;         （页面显示：RxLQI平均值）
				    required int32 averageTxRssi    = 3;            （页面显示：RxRssi平均值）
				    required int32 averageTxLQI        = 4;         （页面显示：TxLQI平均值）
    			 */
    			sdt.setZigbee(zigbeeJson);
    		}else{
    			zigbeeJson = "{\"averageRxRssi\":\"\",\"averageRxLQI\":\"\",\"updateTime\":\"\",\"averageTxRssi\":\"\",\"averageTxLQI\":\"\"}";
    			sdt.setZigbee(zigbeeJson);
    		}
    	}
    	
    	//TODO query primary user?
//    	if(spmsDevice.getType()!=1){
//    		List<SpmsUserProductBinding> temp = spmsUserProductBindingDAO.findByDevice(sdt.getId());
//        	if(temp.size()>0){
//        		SpmsUserProductBinding s = temp.get(0);
//        		SpmsUser su = s.getSpmsUser();
//        		sdt.setBindUserID(su.getId());
//        		sdt.setBindUserName(su.getFullname());
//        	}else{
//        		sdt.setBindUserID("0");
//        		sdt.setBindUserName("设备暂未绑定");
//        	}
//    	}else{
//    		String id = sdt.getId();
//    		SpmsUser su = spmsUserDAO.findByGw(id);
//    		if(su!=null && !StringUtil.isNUll(su.getId())){
//    			sdt.setBindUserID(su.getId());
//        		sdt.setBindUserName(su.getFullname());
//    		}else{
//    			sdt.setBindUserID("0");
//        		sdt.setBindUserName("设备暂未绑定");
//    		}
//    	}
    	return sdt;
    }
    
    //NEW	
	public List<SpmsDeviceDTO> getDevicesDTO(Integer devType) {
		List<SpmsDeviceDTO> deviceDTOs = Lists.newArrayList();
		List<SpmsDeviceBase> devs = getDevices(devType, "findAll");
		for (SpmsDeviceBase dev : devs) {
			if (dev != null) {
				SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
				BeanUtils.copyProperties(dev, deviceDTO);
				SpmsGateway gateway = dev.getGateway();
				if (gateway != null) {
					deviceDTO.setGwMac(dev.getGateway().getMac());
				}
				deviceDTOs.add(deviceDTO);
			}
		}
		return deviceDTOs;
	}

	public SpmsDeviceBase getDeviceByMac(String mac) {
		return getDeviceByMac(mac, EnumTypesConsts.DeviceType.Dev_Type_All);
	}
	
	public SpmsDeviceBase getDeviceByMac(String mac, Integer devType) {
		return getDevice(mac, devType, "findByMAC");
	}
	
	public SpmsDeviceBase getDeviceById(String deviceId) {
		return getDeviceById(deviceId,EnumTypesConsts.DeviceType.Dev_Type_All);
	}
	
	public SpmsDeviceBase getDeviceById(String deviceId ,Integer devType){
	    return 	getDevice(deviceId,devType,"findOne");
	}
	
	//NEW
	//TODO move to device manager?	
	public SpmsDeviceDTO getDeviceDTOByMac(String mac ,Integer devType){		
		return getDeviceDTO(mac,devType,"findByMAC");
	}
	
	
	public SpmsDeviceDTO getDeviceDTOByMac(String mac){
		return getDeviceDTOByMac(mac,EnumTypesConsts.DeviceType.Dev_Type_All);
	}	
	
	public SpmsDeviceDTO getDeviceDTOById(String deviceId ,Integer devType){
	    return 	getDeviceDTO(deviceId,devType,"findOne");
	}
	
	public SpmsDeviceDTO getDeviceDTO(String deviceId ,Integer devType){
		SpmsDeviceDTO deviceDTO = getDeviceDTOByMac(deviceId,devType);
		if(deviceDTO == null)
			deviceDTO = getDeviceDTOById(deviceId,devType);
		
		return deviceDTO;
	}
	
	public SpmsDeviceDTO getDeviceDTO(String deviceId ,Integer devType, String findMethod){
		SpmsDeviceBase device = getDevice(deviceId,devType,findMethod);
		if(device == null)
			return null;
		SpmsDeviceDTO deviceDTO = new SpmsDeviceDTO();
		BeanUtils.copyProperties(device, deviceDTO);
		SpmsGateway gateway = device.getGateway();
		if (gateway != null) {
			deviceDTO.setGwMac(device.getGateway().getMac());
		}
		return deviceDTO;
	}
	
	public SpmsDeviceBase getDevice(String deviceId ,Integer devType, String findMethod){
		Object device = null;
		
		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);
		
		if (dao != null) {
			try {
				Method m = DeviceDAOAccessor.getInstance().getClz(devType).getDeclaredMethod(findMethod, String.class);
				device = m.invoke(dao, deviceId);
				if(device != null)
					return (SpmsDeviceBase)device;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			if(devType == EnumTypesConsts.DeviceType.Dev_Type_All){
				for (DeviceDAOData dataData : DeviceDAOAccessor.getInstance().getAllDAO()) {
					try {
						Method m = dataData.getDao().getClass().getDeclaredMethod(findMethod, String.class);
						device = m.invoke(dataData.getDao(), deviceId);
						if (device != null)
							return (SpmsDeviceBase)device;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else{
				return null;
			}
		}
		return null;
	}
	
	public List<SpmsDeviceBase> getDevices(Integer devType,String findMethod) {
		Object ret = null;
		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);
		List<SpmsDeviceBase> list = Lists.newArrayList();
		
		if (dao != null) {
			try {
				Method m = Class.forName(DeviceDAOAccessor.getInstance().getClsName(devType))
						.getMethod(findMethod);
				ret = m.invoke(dao);
				
				if(ret instanceof SpmsDeviceBase){
					list.add((SpmsDeviceBase)ret);
				}
				else if(ret instanceof List<?>){
					list.addAll((List<? extends SpmsDeviceBase>) ret);				
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			if(devType == EnumTypesConsts.DeviceType.Dev_Type_All){
				for (DeviceDAOData dataData : DeviceDAOAccessor.getInstance().getAllDAO()) {
					try {
						Method m = dataData.getDao().getClass().getMethod(findMethod);
						ret = m.invoke(dataData.getDao());
						if (ret != null) {
							if (ret instanceof SpmsDeviceBase) {
								list.add((SpmsDeviceBase) ret);
							} else if (ret instanceof List<?>) {
								list.addAll((List<? extends SpmsDeviceBase>) ret);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else{
				return null;
			}
		}
		return list;
	}
	
	@Transactional(readOnly=false)
	public UserDeviceRespWraperDTO deleteDevice(String mac,Integer devType){
		deleteDeviceByMac(mac,devType);
		//deleteDeviceById(mac,devType);
		deleteUserDevice(mac);
		return UserDeviceRespWraperDTO.getDefaultWraperDTO();
	}
	
	@Transactional(readOnly=false)
	public UserDeviceRespWraperDTO deleteDeviceByMac(String mac,Integer devType){
		return deleteDevice(mac,devType,"deleteByMac");
	}
	
	@Transactional(readOnly=false)
	public UserDeviceRespWraperDTO deleteDeviceById(String id,Integer devType){
		return deleteDevice(id,devType,"doDeleteById");
	}	
	
	@Transactional(readOnly=false)
	public UserDeviceRespWraperDTO deleteDevice(String mac,Integer devType,String delMethod) {
		Object dao = DeviceDAOAccessor.getInstance().getDAO(devType);
		
		if (dao != null) {
			try {
				Method m = Class.forName(DeviceDAOAccessor.getInstance().getClsName(devType))
						.getMethod(delMethod,String.class);
				m.invoke(dao,mac);
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			if(devType == EnumTypesConsts.DeviceType.Dev_Type_All){
				for (DeviceDAOData dataData : DeviceDAOAccessor.getInstance().getAllDAO()) {
					try {
						Method m = dataData.getDao().getClass().getMethod(delMethod);
						m.invoke(dataData.getDao());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return UserDeviceRespWraperDTO.getDefaultWraperDTO();
	}
	
	public SpmsDeviceDTO addDevice(SpmsDeviceDTO devDTO){
		Integer type = devDTO.getType();
		Class cls = DeviceDAOAccessor.getInstance().getDevClz(type);
		
		Object object = null;
		try {
			object = cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		BeanUtils.copyProperties(devDTO, object);
		if(!daoSave(type,object))
			return null;
		BeanUtils.copyProperties(object,devDTO);
					
		return devDTO;
	}
	
	private boolean daoSave(Integer type,Object obj) {		
		Object dao = DeviceDAOAccessor.getInstance().getDAO(type);
		Class<?> clz = DeviceDAOAccessor.getInstance().getClz(type);
		try {
			Method method = clz.getMethod("save", Object.class);
			method.invoke(dao, obj);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;	
	}
	
	public List<Object[]> getAcPowerRecords(String deviceId, Long endTime) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
		Timestamp last = new Timestamp(endTime);
    	String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
    	String sql = "select start_time as time,case when on_off = 0 then 0 else power end as power from spms_ac_status where device_id='"+deviceId+"' and start_time > '"+date+"'  order by start_time asc";
    	List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		Map<String,Object> map = list.get(i);
    		float power = Float.valueOf(map.get("power").toString());
			//long time = DateUtil.parseStringToDate(map.get("time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			long time = ((Date)map.get("time")).getTime();
			result.add(new Object[]{time,power});
    	}
		return result;
	}
	
	public List<Object[]> getAcPowerRecords(String deviceId) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);

		return getAcPowerRecords(deviceId,c.getTimeInMillis());
	}
	
	public List<Object[]> getAcEnergyRecords(String deviceId, String rbtn,Long endTime) throws ParseException {
		//if <0 , meaning current time - endTime
		if(endTime<0){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, endTime.intValue());
			endTime = c.getTimeInMillis();
		}
		
		List<Object[]> result = Lists.newArrayList();
    	Timestamp last = new Timestamp(endTime);
		String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		String formatStr = "";
		String sql = "";
		if (rbtn.equals("1")) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,truncate(accumulatePower/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					//+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d-%H-%i-%s') >= '"					
					+ date
					+ "' ORDER BY start_time asc";

		} else if (rbtn.equals("2")) {
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,max(accumulatePower)-min(accumulatePower) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d %H') >= '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		} else if (rbtn.equals("3")) {
			formatStr = "yyyy-MM-dd";
			date = date.substring(0, 7);
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d') as date,max(accumulatePower)-min(accumulatePower) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m') = '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d') ORDER BY start_time asc";			
		} else if (rbtn.equals("4")) {
			date = date.substring(0, 4);
			formatStr = "yyyy-MM";
			sql = "select DATE_FORMAT(start_time,'%Y-%m') as date,max(accumulatePower)-min(accumulatePower) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y') = '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m') ORDER BY start_time asc";	
		}
    	List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		Map<String,Object> map = list.get(i);
    		if((map.get("power") == null) || map.get("date") == null)
    			continue;
    		Double power = Double.valueOf(map.get("power").toString());
    		long time = new SimpleDateFormat(formatStr).parse(
					map.get("date").toString()).getTime();
			result.add(new Object[]{time,power});
    	}
		return result;
	}
	
	/*
	 * old implematation
	public List<Object[]> getAcEnergyRecords(String deviceId, String rbtn,Long endTime) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
    	Timestamp last = new Timestamp(endTime);
		String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(last);
		String formatStr = "";
		String sql = "";
		if (rbtn.equals("1")) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,truncate(accumulatePower/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					//+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d-%H-%i-%s') >= '"					
					+ date
					+ "' ORDER BY start_time asc";

		} else if (rbtn.equals("2")) {
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,truncate(max(accumulatePower)/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					//+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d %H') >= '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		} else if (rbtn.equals("3")) {
			date = date.substring(0, 7);
			formatStr = "yyyy-MM-dd";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_month where device_id = '"
					+ deviceId
					+ "' and substr(date,1,7) ='"
					+ date
					+ "' ";
			
			Date dt = new Date(); 
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);   
			String temp_str=sdf.format(dt);
			String sql2 = "";
			if (temp_str.substring(0,7).equals(date)) {
				sql2 = "select DATE_FORMAT(start_time,'%Y-%m-%d') as date,truncate(max(accumulatePower)/1000,3) as power from spms_ac_status where device_id = '"
						+ deviceId + "' "
						+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') = '" + temp_str
						+ "' ";
				
				sql = sql + " union " + sql2;
			}
		} else if (rbtn.equals("4")) {
			date = date.substring(0, 4);
			formatStr = "yyyy-MM";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_year where device_id = '"
					+ deviceId
					+ "' and substr(date,1,4) ='"
					+ date
					+ "' order by date";
		}
    	List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		Map<String,Object> map = list.get(i);
    		if((map.get("power") == null) || map.get("date") == null)
    			continue;
    		Double power = Double.valueOf(map.get("power").toString());
    		long time = new SimpleDateFormat(formatStr).parse(
					map.get("date").toString()).getTime();
			result.add(new Object[]{time,power});
    	}
		return result;
	}	
    */
	public List<Object[]> getAcEnergyRecords(String deviceId, String rbtn)  throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);

		return getAcEnergyRecords(deviceId,rbtn,c.getTimeInMillis());
	}

	public Object getAcRestriction(String rcuId) {
		String acCodesUrl;
		try {
			acCodesUrl = PropertyUtil.getPropertyInfo(ConstantUtil.AcCodes_KEY_URL);
		} catch (IOException e) {
			acCodesUrl = "http://123.56.88.237:8891/";  //hard code here if exception
			e.printStackTrace();
		}
		String para = String.format("restriction/?rcu_id=%s",rcuId);
		
		try {
			String resp = RestUtil.httpGet(acCodesUrl+para);
			
			JSONObject jsonObject = JSONObject.fromObject(resp);
			
			if (((Integer)jsonObject.get("status")) == 0) {
				if(jsonObject.containsKey("restriction")){
					Object restrictionObj = jsonObject.get("restriction");
					if(restrictionObj != null)
						return restrictionObj;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	@Transactional
	public void deleteUserDevice(String mac){
		spmsUserDeviceDAO.deletebyDevMac(mac); //delete user-dev relation
	}
	
}
