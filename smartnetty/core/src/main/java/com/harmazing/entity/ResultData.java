package com.harmazing.entity;


public class ResultData {
	private String id;
	private String areaId;
	private long startTime;
	private Integer power;
	private Integer reactivePower;
	private Integer powerFactor;
	private Integer apparentPower;
	private Integer reactiveEnergy;
	private Integer reactiveDemand;
	private Integer activeDemand;
	private long demandTime;
	private Integer deviceNum;
	private long accumulatePower;
	
	public Long getAccumulatePower() {
		return accumulatePower;
	}
	public void setAccumulatePower(long accumulatePower) {
		this.accumulatePower = accumulatePower;
	}
	public ResultData(String areaId, long startTime,
			Integer power, Integer reactivePower, Integer powerFactor,
			Integer apparentPower, Integer reactiveEnergy,
			Integer reactiveDemand, Integer activeDemand, long demandTime,long accumulatePower) {
		super();
		this.areaId = areaId;
		this.startTime = startTime;
		this.power = power;
		this.reactivePower = reactivePower;
		this.powerFactor = powerFactor;
		this.apparentPower = apparentPower;
		this.reactiveEnergy = reactiveEnergy;
		this.reactiveDemand = reactiveDemand;
		this.activeDemand = activeDemand;
		this.demandTime = demandTime;
		this.accumulatePower=accumulatePower;
	}
	public ResultData(String areaId, long startTime,
			Integer power, Integer reactivePower, Integer powerFactor,
			Integer apparentPower, Integer reactiveEnergy,
			Integer reactiveDemand, Integer activeDemand, long demandTime,Integer deviceNum,long accumulatePower) {
		super();
		this.areaId = areaId;
		this.startTime = startTime;
		this.power = power;
		this.reactivePower = reactivePower;
		this.powerFactor = powerFactor;
		this.apparentPower = apparentPower;
		this.reactiveEnergy = reactiveEnergy;
		this.reactiveDemand = reactiveDemand;
		this.activeDemand = activeDemand;
		this.demandTime = demandTime;
		this.deviceNum=deviceNum;
		this.accumulatePower=accumulatePower;
	}
	public ResultData(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	public Integer getReactivePower() {
		return reactivePower;
	}
	public void setReactivePower(Integer reactivePower) {
		this.reactivePower = reactivePower;
	}
	public Integer getPowerFactor() {
		return powerFactor;
	}
	public void setPowerFactor(Integer powerFactor) {
		this.powerFactor = powerFactor;
	}
	public Integer getApparentPower() {
		return apparentPower;
	}
	public void setApparentPower(Integer apparentPower) {
		this.apparentPower = apparentPower;
	}
	public Integer getReactiveEnergy() {
		return reactiveEnergy;
	}
	public void setReactiveEnergy(Integer reactiveEnergy) {
		this.reactiveEnergy = reactiveEnergy;
	}
	public Integer getReactiveDemand() {
		return reactiveDemand;
	}
	public void setReactiveDemand(Integer reactiveDemand) {
		this.reactiveDemand = reactiveDemand;
	}
	public Integer getActiveDemand() {
		return activeDemand;
	}
	public void setActiveDemand(Integer activeDemand) {
		this.activeDemand = activeDemand;
	}
	public long getDemandTime() {
		return demandTime;
	}
	public void setDemandTime(long demandTime) {
		this.demandTime = demandTime;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	
}
