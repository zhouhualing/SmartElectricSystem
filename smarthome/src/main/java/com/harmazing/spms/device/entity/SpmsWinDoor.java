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
@Table(name="spms_win_door")
public class SpmsWinDoor extends SpmsDeviceBase {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Integer remain;    
    
	public Integer getRemain() {
	    return remain;
	}
	public void setRemain(Integer remain) {
	    this.remain = remain;
	}
}
