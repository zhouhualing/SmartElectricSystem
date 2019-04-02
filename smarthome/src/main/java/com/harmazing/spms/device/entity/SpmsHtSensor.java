/**
 * 
 */
package com.harmazing.spms.device.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.entity.UserEntity;

@Entity
@Table(name="spms_ht_sensor")
public class SpmsHtSensor extends SpmsDeviceBase {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Integer humidity;
	private Integer remain;
	
    public Integer getHumidity() {
		return humidity;
	}
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
	public Integer getRemain() {
	    return remain;
	}
	public void setRemain(Integer remain) {
	    this.remain = remain;
	}	
}

