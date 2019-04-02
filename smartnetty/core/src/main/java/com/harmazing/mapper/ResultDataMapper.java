package com.harmazing.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ResultDataMapper{
	public final String TB_NAME = "spms_electro_result_data";
	
	@Insert("INSERT INTO " +TB_NAME+
			" (`id`,`area_id`,`start_time`,`power`,`reactive_power`,`power_factor`," +
			"`apparent_power`,`reactive_energy`,`reactive_demand`,`active_demand`,`demand_time`,`device_num`) VALUES " +
			"(#{id} , #{area_id} , #{start_time} , #{power} , #{reactive_power} , #{power_factor} " +
			", #{apparent_power} , #{reactive_energy} , #{reactive_demand} , #{active_demand}, #{demandTime}, #{deviceNum})")
	public void insertResutData(@Param("id") String id,
			@Param("area_id") String areaId,
			@Param("start_time") Timestamp startTime,
			@Param("power") Integer power,
			@Param("reactive_power") Integer reactivePower,
			@Param("power_factor") Integer powerFactor,
			@Param("apparent_power") Integer apparentPower,
			@Param("reactive_energy") Integer reactiveEnergy,
			@Param("reactive_demand") Integer reactiveDemand,
			@Param("active_demand") Integer activeDemand,
			@Param("demandTime") Timestamp demandTime,
			@Param("deviceNum") Integer deviceNum
			);
/**
  @Update("update " +TB_NAME+" set "+
			"power=power+#{power),"+
			"reactive_power=reactive_power+#{reactive_power},"+
			"power_factor=power_factor+#{power_factor}," +
			" apparent_power=apparent_power+#{apparent_power},"+
			"reactive_energy=reactive_energy+#{reactive_energy},"+
			"reactive_demand=reactive_demand+#{reactive_demand},"+
			"active_demand=active_demand+#{active_demand},"+
			"device_num=device_num+#{device_num}"+
			" where  area_id=#{area_id} and start_time=#{start_time}")
	public void updateResutData(@Param("id2") String id,
			@Param("area_id") String areaId,
			@Param("start_time") Timestamp startTime,
			@Param("power") Integer power,
			@Param("reactive_power") Integer reactivePower,
			@Param("power_factor") Integer powerFactor,
			@Param("apparent_power") Integer apparentPower,
			@Param("reactive_energy") Integer reactiveEnergy,
			@Param("reactive_demand") Integer reactiveDemand,
			@Param("active_demand") Integer activeDemand,
			@Param("demandTime") Timestamp demandTime,
			@Param("deviceNum") Integer deviceNum
			);
  **/
  public void updateResutData(Map para);
  
  public void batchUpdateResult(List para);
  
  public void batchInsertResult(List para);
	
}
