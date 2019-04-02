package com.harmazing.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface SysConfigMapper {

	 /**
     * 获取日志上传频率
     * @param userId
     * @return
     */
    @Select("select * from tb_system_config")
    @ResultType(java.util.Map.class)
    public List<Map<String,String>> getSysConfig();
    
    /*
     * 获取dsm接口温度
     * @return
     */
    @Select("select * from spms_dsm")
    @ResultType(java.util.Map.class)
    public List<Map<String,String>> getDsmTemp();
    
    /*
     * 获取dsm接口温度
     * @return
     */
    @Select("select * from spms_dsm where areaId=#{areaId}")
    @ResultType(java.util.Map.class)
    public List<Map<String,String>> getDsmTempbyArea(@Param("areaId") String areaId);
}
