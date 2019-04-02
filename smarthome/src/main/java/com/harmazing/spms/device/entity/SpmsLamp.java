package com.harmazing.spms.device.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="spms_lamp")
public class SpmsLamp extends SpmsDeviceBase  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer  illuminance;
	private Integer  red;
	private Integer  green;
	private Integer  blue;
	
	public Integer getIlluminance() {
		return illuminance;
	}
	public Integer getRed() {
		return red;
	}
	public Integer getGreen() {
		return green;
	}
	public Integer getBlue() {
		return blue;
	}
	public void setIlluminance(Integer illuminance) {
		this.illuminance = illuminance;
	}
	public void setRed(Integer red) {
		this.red = red;
	}
	public void setGreen(Integer green) {
		this.green = green;
	}
	public void setBlue(Integer blue) {
		this.blue = blue;
	}	
}
