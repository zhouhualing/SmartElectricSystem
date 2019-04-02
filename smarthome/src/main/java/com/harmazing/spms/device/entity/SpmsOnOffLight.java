package com.harmazing.spms.device.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="spms_onoff_light")
public class SpmsOnOffLight extends SpmsDeviceBase  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
