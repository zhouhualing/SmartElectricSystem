/**
 * 
 */
package com.harmazing.spms.device.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.common.entity.GenericEntity;

@MappedSuperclass
public class SpmsDeviceBase extends GenericEntity {

    private static final long serialVersionUID = 1L;

	/* 设备SN */
    @Column(length=32,columnDefinition="varchar(32) default '0'")
	@NotNull(message="设备SN不能为空")
	private String sn;   
        
	/* 设备MAC */
    @Column(length=64,columnDefinition="varchar(64) default '0'")
	@NotNull(message="设备MAC不能为空")
	private String mac;
	/* 设备MAC */

	private String name;
	/**
     * 设备类型
     * dict_device_type
     * 1网关设备 2智能空调设备 3门窗 4 Pir 5插座
     */
	@Column(columnDefinition="int default 0")
    @NotNull(message="设备类型不能为空")
	private Integer type ;
        
	/* 厂家：供应商 */
	@Column(columnDefinition="varchar(256) default 'innolinks'")
	@Length(min=1, max=256)
	@NotNull(message="厂家不能为空")
	private String vender;   
	
	/* 型号 */
	@Column(columnDefinition="varchar(128) default '0'")
	@Length(min=1, max=128)
	@NotNull(message="型号不能为空")
	private String model; 
	
	/* 硬件版本 */
	@Column(columnDefinition="varchar(128) default '0'")
	@Length(min=1, max=128)
	@NotNull(message="硬件版本不能为空")
	private String hardware; 
	
	/* 出厂软件版本 */
	@Column(columnDefinition="varchar(128) default '0'")
	@Length(min=1, max=128)
	@NotNull(message="出厂软件版本不能为空")
	private String software;   
	
	/* 所在仓库 */
	@Column(columnDefinition="int default 0")
	@NotNull(message="所在仓库不能为空")
	private Integer storage; 
	
	/* 当前软件版本 */
	@Column(columnDefinition="varchar(128) default 'V2.0'")
	private String cursoft;   
	
	/**
	 * 设备运营状态
	 * dict_device_status
	 * 1库存 2正常运营 3维修 4报废
	 */
	private Integer status;   
	
	/*  */
	/**
	 * 运行状态
	 * dict_device_operstatus
	 * 0离线1在线2异常
	 */
	private Integer operStatus;
	
	/* 会话ID */
	@Length(min=1, max=64)
	private String sessionId;   
	
	/* 是否可用 */
	private Integer disable=0;  
	
	/* extract common device attributes*/
    private Integer onOff;  
	private Date startTime;
    private Integer runDuration;
    private Integer mode;	
    
	
	private String tag;
        
	public String getSn() {
	    return sn;
	}
	public void setSn(String sn) {
	    this.sn = sn;
	}
	public String getMac() {
	    return mac;
	}
	public void setMac(String mac) {
	    this.mac = mac;
	}
	public String getVender() {
	    return vender;
	}
	public Integer getType() {
	    return type;
	}
	public void setType(Integer type) {
	    this.type = type;
	}
	public void setVender(String vender) {
	    this.vender = vender;
	}
	public String getModel() {
	    return model;
	}
	public void setModel(String model) {
	    this.model = model;
	}
	public String getHardware() {
	    return hardware;
	}
	public void setHardware(String hardware) {
	    this.hardware = hardware;
	}
	public String getSoftware() {
	    return software;
	}
	public void setSoftware(String software) {
	    this.software = software;
	}
	public String getCursoft() {
	    return cursoft;
	}
	public void setCursoft(String cursoft) {
	    this.cursoft = cursoft;
	}
	public Integer getStatus() {
	    return status;
	}
	public void setStatus(Integer status) {
	    this.status = status;
	}
	public Integer getOperStatus() {
	    return operStatus;
	}
	public void setOperStatus(Integer operStatus) {
	    this.operStatus = operStatus;
	}
	public String getSessionId() {
	    return sessionId;
	}
	public void setSessionId(String sessionId) {
	    this.sessionId = sessionId;
	}
	public Integer getDisable() {
	    return disable;
	}
	public void setDisable(Integer disable) {
	    this.disable = disable;
	}
	
	public Integer getStorage() {
		return storage;
	}
	public void setStorage(Integer storage) {
		this.storage = storage;
	}	
	  
    public Integer getOnOff() {
		return onOff;
	}
	public void setOnOff(Integer onOff) {
		this.onOff = onOff;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getRunDuration() {
		return runDuration;
	}
	public void setRunDuration(Integer runDuration) {
		this.runDuration = runDuration;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}	

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gw_id")
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	private SpmsGateway gateway;
	public SpmsGateway getGateway() {
		return gateway;
	}

	public void setGateway(SpmsGateway gateway) {
		this.gateway = gateway;
	}

}
