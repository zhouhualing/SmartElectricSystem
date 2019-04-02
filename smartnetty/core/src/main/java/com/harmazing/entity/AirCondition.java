package com.harmazing.entity;

import java.sql.Timestamp;
import java.util.HashMap;

public class AirCondition extends BasicDevice {
	private Integer acTemp;
	private Integer temp;
	private Integer humidity;
	private Integer power;
	private Integer speed;
	private Integer upDownSwing;
	private Integer leftRightSwing;
	private long  accumulatedPower;
	private Integer acType;
	private Integer rcuId;
	private Integer energy;
	private String update_time; 
	
	

	public Integer getAcTemp() {
		return acTemp;
	}



	public void setAcTemp(Integer acTemp) {
		this.acTemp = acTemp;
	}



	public Integer getTemp() {
		return temp;
	}



	public void setTemp(Integer temp) {
		this.temp = temp;
	}



	public Integer getHumidity() {
		return humidity;
	}



	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}



	public Integer getPower() {
		return power;
	}



	public void setPower(Integer power) {
		this.power = power;
	}



	public Integer getSpeed() {
		return speed;
	}



	public void setSpeed(Integer speed) {
		this.speed = speed;
	}



	public Integer getUpDownSwing() {
		return upDownSwing;
	}



	public void setUpDownSwing(Integer upDownSwing) {
		this.upDownSwing = upDownSwing;
	}



	public Integer getLeftRightSwing() {
		return leftRightSwing;
	}



	public void setLeftRightSwing(Integer leftRightSwing) {
		this.leftRightSwing = leftRightSwing;
	}



	public long getAccumulatedPower() {
		return accumulatedPower;
	}



	public void setAccumulatedPower(long accumulatedPower) {
		this.accumulatedPower = accumulatedPower;
	}



	public Integer getAcType() {
		return acType;
	}



	public void setAcType(Integer acType) {
		this.acType = acType;
	}



	public Integer getRcuId() {
		return rcuId;
	}



	public void setRcuId(Integer rcuId) {
		this.rcuId = rcuId;
	}



	public Integer getEnergy() {
		return energy;
	}



	public void setEnergy(Integer energy) {
		this.energy = energy;
	}



	public String getUpdate_time() {
		return update_time;
	}



	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}



	public HashMap<String, String> toMap() {
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("type", "AC");
        map.put("id", id);
        map.put("operationStatus", this.operStatus ==null?null:String.valueOf(this.operStatus));        
        map.put("onOff", this.onOff==null?null:String.valueOf(onOff));
        map.put("temperature", this.temp==null?null:String.valueOf(this.acTemp));
        map.put("acTemperature", this.acTemp==null?null:String.valueOf(this.temp));
        map.put("power", power==null?null:String.valueOf(power));
        map.put("speed", speed==null?null:String.valueOf(speed));
        map.put("upDownSwing", upDownSwing==null?null:String.valueOf(upDownSwing));
        map.put("leftRightSwing", leftRightSwing==null?null:String.valueOf(leftRightSwing));
        map.put("startTime",startTime.toString().substring(0,19));
        map.put("mode",mode==null?null:String.valueOf(mode));        
        map.put("accumulatePower", this.accumulatedPower==0?null:String.valueOf(this.accumulatedPower));        
        map.put("software", this.softwareVer);
        map.put("hardware", this.hardwareVer);
        if(startTime == null) {
            startTime = new Timestamp(System.currentTimeMillis());
        } 
        //map.put("server", server);
        //map.put("eleArea", this.eleArea);
        //map.put("remain",remain==null?null:String.valueOf(remain));
        //map.put("session",session);
        
        return map;
	}
}
