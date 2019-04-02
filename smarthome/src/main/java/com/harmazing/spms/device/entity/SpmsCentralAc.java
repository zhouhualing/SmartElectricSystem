/**
 * 
 */
package com.harmazing.spms.device.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "spms_central_ac")
public class SpmsCentralAc extends SpmsDeviceBase {
	private static final long serialVersionUID = 1L;
	private Integer temp;
	private Integer acTemp;
	private Float power;
	private Integer speed;
	private Integer direction;

	// 上下摆风
	private Integer upDownSwing;
	// 左右摆风
	private Integer leftRightSwing;
	private Integer accumulatePower;

	private Integer floorTemp;

	private Integer upperTemp;

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

	public Integer getAcTemp() {
		return acTemp;
	}

	public void setAcTemp(Integer acTemp) {
		this.acTemp = acTemp;
	}

	public Integer getFloorTemp() {
		return floorTemp;
	}

	public void setFloorTemp(Integer floorTemp) {
		this.floorTemp = floorTemp;
	}

	public Integer getUpperTemp() {
		return upperTemp;
	}

	public void setUpperTemp(Integer upperTemp) {
		this.upperTemp = upperTemp;
	}

	public Float getPower() {
		return power;
	}

	public void setPower(Float power) {
		this.power = power;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getAccumulatePower() {
		return accumulatePower;
	}

	public void setAccumulatePower(Integer accumulatePower) {
		this.accumulatePower = accumulatePower;
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

}
