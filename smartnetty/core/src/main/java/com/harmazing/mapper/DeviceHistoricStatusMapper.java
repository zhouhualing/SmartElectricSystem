package com.harmazing.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface  DeviceHistoricStatusMapper {
	final static String TB_WIN_DOOR_STATUS = "spms_win_door_status";
	
	////////////////////////////////////////////////////////////////
	@Insert("INSERT " + TB_WIN_DOOR_STATUS + " (id, user_id, mac, ${field}) VALUES(#{id}, #{user_id}, #{mac}, #{value})")
	public void recordWinDoorHistroyStatus( @Param("id") String id,
							   @Param("user_id") String user_id,
							   @Param("mac") String mac,
							   @Param("field") String field,
						       @Param("value") int value);
}
