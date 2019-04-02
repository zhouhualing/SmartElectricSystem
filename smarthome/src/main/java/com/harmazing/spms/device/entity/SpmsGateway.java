/**
 * 
 */
package com.harmazing.spms.device.entity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.harmazing.spms.area.entity.Area;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="spms_gateway")
public class SpmsGateway extends SpmsDeviceBase {

    private static final long serialVersionUID = 1L;
    
	@Length(min=7, max=100)
	@Column(name = "ip")
	private String ip;
    
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public static long getSerialversionuid() {
	    return serialVersionUID;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="area_id")
    private Area area;    

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
