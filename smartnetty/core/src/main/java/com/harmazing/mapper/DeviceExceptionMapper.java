package com.harmazing.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface DeviceExceptionMapper {
	final static String TB_NAME = "spms_device_error";	
	
	@Insert("insert into "+TB_NAME +"(id,deviceId,errorCode,createDate) values("
			+"#{id},"
			+"#{deviceId},"
			+"#{exceptionCode},"
			+"NOW())")
    public void setAcException(@Param("id") String id,@Param("deviceId") String deviceId,@Param("exceptionCode") String exceptionCode);
	
	@Select("select deviceId from "+TB_NAME+" where deviceId=#{deviceId}")
    public String getAcException(@Param("deviceId") String deviceId);
	
	@Update("update "+TB_NAME+" set errorCode=#{exceptionCode} where deviceId=#{deviceId}")
    public void updateAcException(@Param("deviceId") String deviceId,@Param("exceptionCode") String exceptionCode);
}
