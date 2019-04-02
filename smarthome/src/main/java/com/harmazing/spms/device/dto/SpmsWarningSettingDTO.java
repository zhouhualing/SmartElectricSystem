/**
 * 
 */
package com.harmazing.spms.device.dto;

import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.dto.CommonDTO;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月10日
 */
public class SpmsWarningSettingDTO extends CommonDTO {
    
    	/**
    	 * 区域id
    	 */
	private String areaId;
	/* 区域名称 */
	private String name;
	
	/* 最大负载 */
	private Integer maxLoad;
	
	/* 平均负载 */
	private Integer avgLoad;  
	
	/* 持续时长 */
	private Integer runDuration;   
	
	/* 网关连接数 */
	private Integer gwJoinNum; 
	
	/* 空调连接数 */
	private Integer acJoinNum;  
	
	/* 平均空调温度 */
	private Float acAvgTemp;   
	
	/* 预警级别 */
	private Integer level;

	public Integer getMaxLoad() {
	    return maxLoad;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setMaxLoad(Integer maxLoad) {
	    this.maxLoad = maxLoad;
	}

	public Integer getAvgLoad() {
	    return avgLoad;
	}

	public void setAvgLoad(Integer avgLoad) {
	    this.avgLoad = avgLoad;
	}

	public Integer getRunDuration() {
	    return runDuration;
	}

	public void setRunDuration(Integer runDuration) {
	    this.runDuration = runDuration;
	}

	public Integer getGwJoinNum() {
	    return gwJoinNum;
	}

	public void setGwJoinNum(Integer gwJoinNum) {
	    this.gwJoinNum = gwJoinNum;
	}

	public Integer getAcJoinNum() {
	    return acJoinNum;
	}

	public void setAcJoinNum(Integer acJoinNum) {
	    this.acJoinNum = acJoinNum;
	}

	public Float getAcAvgTemp() {
	    return acAvgTemp;
	}

	public void setAcAvgTemp(Float acAvgTemp) {
	    this.acAvgTemp = acAvgTemp;
	}

	public Integer getLevel() {
	    return level;
	}

	public void setLevel(Integer level) {
	    this.level = level;
	}
	
}
