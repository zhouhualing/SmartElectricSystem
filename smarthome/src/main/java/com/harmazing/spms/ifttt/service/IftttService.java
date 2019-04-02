package com.harmazing.spms.ifttt.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.DeviceDAOAccessor;
import com.harmazing.spms.ifttt.dao.SpmsIftttStrategyDAO;
import com.harmazing.spms.ifttt.dto.SpmsIftttConditionDTO;
import com.harmazing.spms.ifttt.dto.SpmsIftttDeviceDTO;
import com.harmazing.spms.ifttt.entity.SpmsIftttStrategy;
import com.harmazing.spms.spmsuc.Manager.SpmsAppManager;
import net.sf.json.JSONObject;

@Service("iftttService")
public class IftttService {
	@Autowired
	private SpmsDeviceManager spmsDeviceManager;
	@Autowired
	private SpmsAppManager spmsAppManager;
	@Autowired
	private SpmsIftttStrategyDAO spmsIftttStrategyDAO;
	
	private String pattern = "/^([A-Za-z_])+$";

	public void turnOnOffAcCommand(String paras) throws UnsupportedEncodingException {
		JSONObject jsonObject = JSONObject.fromObject(paras);
		String mac = (String) jsonObject.get("mac");
		String onOff = (String) jsonObject.get("onOff");
		Integer type = (Integer) jsonObject.get("type");

		SpmsDeviceBase device = spmsDeviceManager.getDeviceByMac(mac, type);
		if (device == null || device.getGateway() == null) {
			return;
		}
		Map<String, Object> result = null;
		if (onOff.equals("0")) {
			result = spmsAppManager.changeAcStatus(device.getGateway().getMac(), mac, type, CommandUtil.CommandType.OFF,
					0);
		} else if (onOff.equals("1")) {
			result = spmsAppManager.changeAcStatus(device.getGateway().getMac(), mac, type, CommandUtil.CommandType.ON,
					1);
		}
	}

	public void ifThisThen() {

		try {
			List<SpmsIftttStrategy> items = spmsIftttStrategyDAO.findAll();

			for (SpmsIftttStrategy item : items) {
				String condDTO = item.getIfConditionDTO();
				JSONObject jsonObject = JSONObject.fromObject(condDTO);
				Map<String, Class> classMap =  new HashMap<String, Class>(); 
				classMap.put("deviceDTOs", SpmsIftttDeviceDTO.class);
				SpmsIftttConditionDTO conditionDTO = (SpmsIftttConditionDTO) JSONObject.toBean(jsonObject,SpmsIftttConditionDTO.class,classMap);

				String period = conditionDTO.getPeriod();
				if (!isInPeriod(period))
					return;

				String time = conditionDTO.getTime();
				List<String> times = StringUtil.split(time, "-");
				boolean isInTime = isInTime(times.get(0), times.get(1));
				if (!isInTime)
					return;
				
				String ifCondition = item.getIfCondition();

				List<SpmsIftttDeviceDTO> deviceDTOs = conditionDTO.getDeviceDTOs();
				StringBuffer sb = new StringBuffer();
				for (int i=0;i<deviceDTOs.size();i++) {
					SpmsIftttDeviceDTO deviceDTO = deviceDTOs.get(i);
					SpmsDeviceBase dev = spmsDeviceManager.getDeviceByMac(deviceDTO.getMac(), deviceDTO.getType());
					//List<String> vars = StringUtil.split(item.getIfConditionMeta(), ",");
					Set<String> vars = getMetaProperties(StringUtil.split(ifCondition,",").get(i));
					for (Object var : vars) {
						String tmpVar = var.toString().trim();

						String methodName = StringUtil.getAccesserName(tmpVar, true);

						Class<?> clazz = DeviceDAOAccessor.getInstance().getDevClz(deviceDTO.getType());

						Method method = clazz.getMethod(methodName);
						Object value = method.invoke(dev);
						sb.append(String.format("var %s = %s;\n", tmpVar, value.toString()));

					}

					sb.append("(" + StringUtil.split(ifCondition,",").get(i) + ");");
					String script = sb.toString();
					ScriptEngineManager manager = new ScriptEngineManager();
					ScriptEngine engine = manager.getEngineByName("javascript");
					Boolean result = (Boolean) engine.eval(script);
					if (!result) {
						return;
					}

				}

				String action = item.getAction();
				String para = item.getActionPara();
				Method method = this.getClass().getDeclaredMethod(action, String.class);
				method.invoke(this, para);

			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isInPeriod(String period) {
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		int week = c.get(Calendar.DAY_OF_WEEK);
		switch (period.toLowerCase()) {
		case "day":
			return true;
		case "mon":
			if (week != Calendar.MONDAY)
				return false;
		case "tue":
			if (week != Calendar.TUESDAY)
				return false;
		case "wed":
			if (week != Calendar.WEDNESDAY)
				return false;
		case "thu":
			if (week != Calendar.THURSDAY)
				return false;
		case "fri":
			if (week != Calendar.FRIDAY)
				return false;
		case "sat":
			if (week != Calendar.SATURDAY)
				return false;
		case "sun":
			if (week != Calendar.SUNDAY)
				return false;
		default:
			return true;
		}
	}

	// 07:10:00-09:20:10
	public boolean isInTime(String strDateBegin, String strDateEnd) {
		 String strDate = getTimeOfDate();
		// 截取当前时间时分秒
		int strDateH = Integer.parseInt(strDate.substring(0, 2));
		int strDateM = Integer.parseInt(strDate.substring(3, 5));
		int strDateS = Integer.parseInt(strDate.substring(6));

		// 截取开始时间时分秒
		int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
		int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
		int strDateBeginS = Integer.parseInt(strDate.substring(6));
		// 截取结束时间时分秒
		int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
		int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
		int strDateEndS = Integer.parseInt(strDate.substring(6));

		if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
			// 当前时间小时数在开始时间和结束时间小时数之间
			if (strDateH > strDateBeginH && strDateH < strDateEndH) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM >= strDateBeginM && strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM == strDateBeginM && strDateS >= strDateBeginS
					&& strDateS <= strDateEndS) {
				return true;
			}
			// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
			else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
			} else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM
					&& strDateS <= strDateEndS) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private Set<String> getMetaProperties(String input) {
		Pattern p = Pattern.compile("([A-Za-z_])+");
		Matcher m = p.matcher(input);
		Set<String> result = Sets.newHashSet();
		while(m.find()){
			String matcher = m.group();
			if(matcher.trim().toLowerCase().equals("null"))
				continue;
			result.add(m.group());
		}	
		
		return result;
	}
	
	private String getTimeOfDate() {
		String returnStr;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr.substring(returnStr.lastIndexOf(' ')+1);
	}
}
