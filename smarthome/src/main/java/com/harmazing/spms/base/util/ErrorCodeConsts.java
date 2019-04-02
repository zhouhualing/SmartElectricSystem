package com.harmazing.spms.base.util;

import java.util.Map;

import com.google.common.collect.Maps;;


public class ErrorCodeConsts {
	
private static final Map<String,String> map = Maps.newHashMap();

public static String Success = "201";

public static String GatewayNotOnline = "301";
public static String DeviceNotOnline = "302";

public static String UnknowError = "401";
public static String UserNamePassEmpty = "408";
public static String UserNotExist = "409";

public static String UserAlreadyExist = "410";
public static String DeviceNotExist = "411";
public static String DeviceTypeNotExist = "412";
public static String UserDevicePairAlreadyExist = "413";
public static String ClassNameNotExist = "414";
public static String UserDeviceNotExist = "415";
public static String UserPrimaryDeviceExist = "416";


public static String SendMessageFailed = "417";
public static String UserNamePassError = "418";
public static String MethodNotExist = "419";
public static String ParaNotRight = "420";
public static String DeviceTypeNotRight = "421";

public static String UserNotPrimary = "450";
public static String UserDeviceAlreadyExist = "451";
public static String UserGroupDeviceExist = "452";
public static String UserDeviceGroupNotExist = "453";

public static String OverTime = "501";

public static String ModsigNotFound = "2001";

public static String NotImplemented = "5000";

static
{	
	map.put(Success,"成功.");
	map.put(GatewayNotOnline,"网关连接失败.");
	map.put(DeviceNotOnline,"设备不在线.");
	map.put(UnknowError,"未知错误.");
	map.put(UserNamePassEmpty,"用户名/密码 为空.");
	map.put(UserNotExist,"用户不存在.");
	map.put(UserAlreadyExist,"用户已存在.");
	map.put(DeviceNotExist,"设备不存在.");
	map.put(DeviceTypeNotExist,"设备类型不存在.");
	map.put(DeviceTypeNotRight,"设备类型不正确.");
	map.put(UserDevicePairAlreadyExist,"用户设备绑定已经存在.");
	map.put(ClassNameNotExist,"class不存在.");
	map.put(UserDeviceNotExist,"用户设备记录不存在.");
	map.put(UserPrimaryDeviceExist,"Primary设备记录存在.");
	map.put(SendMessageFailed,"消息发送失败.");
	map.put(UserNamePassError,"用户名密码错误.");
	map.put(MethodNotExist,"方法不存在.");
	map.put(ParaNotRight,"参数格式不正确.");
	map.put(UserNotPrimary,"用户不是Primary用户.");
	map.put(UserDeviceAlreadyExist,"用户和设备的绑定关系已存在.");
	map.put(UserGroupDeviceExist,"用户组存在设备.");
	map.put(OverTime,"连接超时.");
	map.put(ModsigNotFound,"Modsig 没有找到.");
	map.put(UserDeviceGroupNotExist,"User device group没有找到.");
	
	
	map.put(NotImplemented,"没有实现");
	
}

public static String getValue(String key) {
	return map.get(key);	
}


}
