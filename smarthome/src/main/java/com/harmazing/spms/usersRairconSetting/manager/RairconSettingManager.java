/**
 * 
 */
package com.harmazing.spms.usersRairconSetting.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.device.dao.*;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.usersRairconSetting.dto.DeviceErrorDTO;
import com.harmazing.spms.usersRairconSetting.entity.Clocksetting;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;
import com.harmazing.spms.usersRairconSetting.entity.deviceCurve;

public interface RairconSettingManager extends IManager {

	List<RairconSetting> QueryRairconCurve(String deviceId);

	List<Clocksetting> QueryRairconCurveSetting(String user);

	void SaveRairconCurve(RairconSetting rairconSetting,String userId);

	void delRairconCurve(String curveid,String userId);

	void addSettingCurve(Clocksetting clocksetting,String userId);

	void delSettingCurve(String rairconSettingid,String userId);

	long QueryRairconCurveNum(String deviceId);

	void addSettingCurves(String deviceId,Clocksetting c,String userId);

	RairconSetting addCurveT(String deviceid);

	RairconSetting QueryRairconSetting(String rairconSettingid);

	void delClock(String rairconSettingid);

	List<Clocksetting> QueryTimingSet(String string);

	SpmsAirCondition queryDevice(String string);

	Clocksetting queryClockSetById(String id);

	Clocksetting updateclockSet(Map<String, Object> map, SpmsAirCondition spmsDevice,String userId);

	List<Clocksetting> QueryCurveClock(String curveid,String alone);

	List QueryRairconCurveForUserForApp(String userId);

	List<RairconSetting> QueryRairconCurveForUser(String userId);

	boolean queryCurveRepeat2(String deviceId, String curveid, Map mm);

	boolean queryClockRepeat(String deviceId, String clocking, String clocksettingId, Map mm);

	boolean saveRepeatPopup(String curveid, Map mm,String deviceId);

	void updateclockSetForApp(HttpServletRequest request, SpmsAirCondition spmsDevice,String userId);

	boolean queryCurveRepeatForApp(String deviceId, String curveid,
			HttpServletRequest request);

	boolean queryClockRepeatForApp(String deviceId, String clocking, HttpServletRequest request);

	void saveRepeatPopupForApp(String string, HttpServletRequest request);

	boolean updateClock(String clockId,String userId, Map<String, Object> m);

	void updateCurveRepeat(String string, String string2, Map map);

	RairconSetting QueryCurveById(String curveId);

	long getRairconCurveCountForUser(String userId);

	List<RairconSetting> QueryRairconCurveAll();

	long QueryRairconCurveAllNum();

	SpmsUser querySpmsUserById(String userId);

	void CoverCurveRepeat(String deviceId, String curveId, Map mm);

	void updateClockForRepeat(Clocksetting clocksetting, String deviceId,String clocking, Map mm);

	long QueryRairconClockNum(String deviceId);

	long QueryRairconClockAllNum();

	boolean tempJudge(String id, String minTemps, String maxTemps);

	long QueryRairconCurveNumByUserId(String minTemps, String maxTemps);

	RairconSetting addCurveTforP(String userId,String deviceId);

	List<deviceCurve> queryDeviceCurveForCurveId(String string, String deviceId);

	List<RairconSetting> QueryRairconCurveForDeviceId(String deviceId,String userId);

	void delSettingCurveForApp(String curveId, String userId);

	long QueryCurveNumForCurveDevice(String deviceId, String minTemp, String maxTemp);
	
	void delSettingCurveSetting(String clocksettingid,String userId);

}
		