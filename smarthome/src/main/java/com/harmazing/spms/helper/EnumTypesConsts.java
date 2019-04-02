package com.harmazing.spms.helper;

import com.google.common.collect.ForwardingNavigableMap;

public class EnumTypesConsts {
	
	public static interface UserType {
		public final static Integer User_Type_Admin = 1;
		public final static Integer User_Type_User = 2;
	}
	public static interface SpmsUserType {
		public final static Integer Spms_User_Type_Enterprise = 1;
		public final static Integer Spms_User_Type_Person = 2;
	}
	public static interface UserDeviceType {
		public final static Integer User_Shared = 0;
		public final static Integer User_Primary = 1;
	}
	
	public static interface DeviceType {
		public static Integer Dev_Type_Reserved = 0;
		public static Integer Dev_Type_Gw = 1;
		public static Integer Dev_Type_AC = 2;
		public static Integer Dev_Type_Plug_AC = 3;
		public static Integer Dev_Type_Central_AC = 4;
		public static Integer Dev_Type_OnOffPlug = 5;
		public static Integer Dev_Type_OnOffPmPlug = 6;
		public static Integer Dev_Type_Wd = 7;
		public static Integer Dev_Type_Pir = 8;
		public static Integer Dev_Type_HtSensor = 9;
		public static Integer Dev_Type_OnOffLight = 10;
		public static Integer Dev_Type_Lamp = 11;
		public static Integer Dev_Type_All = 0xFFFF;
	}
	
	public static interface UserDevGroupOpType {
		public static Integer Dev_Type_Reserved = 0;
		public static Integer Udg_Type_New = 1;
		public static Integer Udg_Type_AddToExist = 2;
		public static Integer Udg_Type_Delete = 4;
	}
	
	public static interface ConstDefinition{
		public static String SuperKey = "InnolinksQ1w2e3r4@1";
	}
}
