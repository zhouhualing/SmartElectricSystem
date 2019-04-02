/**
 * 
 */
package com.harmazing.spms.device.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.entity.CommonEntity;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月10日
 */
@Entity
@Table(name="spms_warning_setting")
public class SpmsWarningSetting extends CommonEntity {
    
	private static final long serialVersionUID = 1L;
	
	/* 区域ID */
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area ;
	
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
	/**
	 * dict_warnset_level
	 */
	private Integer level;
	public void setAcAvgTemp(Float acAvgTemp) {
		this.acAvgTemp = acAvgTemp;
	}

	public Area getArea() {
	    return area;
	}

	public void setArea(Area area) {
	    this.area = area;
	}

	public Integer getMaxLoad() {
	    return maxLoad;
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


	public Integer getLevel() {
	    return level;
	}

	public void setLevel(Integer level) {
	    this.level = level;
	}

	public static long getSerialversionuid() {
	    return serialVersionUID;
	}
	
}
