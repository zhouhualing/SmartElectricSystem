package com.harmazing.spms.device.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * on off power meter plug
 * */

@Entity
@Table(name="spms_onoff_pm_plug")
public class SpmsOnOffPmPlug extends SpmsDeviceBase  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
