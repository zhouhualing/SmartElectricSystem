package com.harmazing.spms.user.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.entity.SpmsUser;

@Entity
@Table(name = "spms_user_product_binding")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpmsUserProductBinding extends CommonEntity{
//	
//	private static final long serialVersionUID = 1L;
//	
//	/* 用户ID */
//	@ManyToOne
//	@JoinColumn(name="user_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private SpmsUser spmsUser ;
///* 服务ID */
//	@ManyToOne
//	@JoinColumn(name="product_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private SpmsProduct spmsProduct ;
//	/* 服务类型ID */
//	@ManyToOne
//	@JoinColumn(name="producttype_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private SpmsProductType spmsProductType ;
//	
//	/* 设备类型 */
//	@Column(name="deviceType")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private Integer deviceType ;
///* 设备ID */
//	@ManyToOne
//	@JoinColumn(name="device_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private SpmsDevice spmsDevice ;
//	/*  设备自定义名称  */
//	private String customName;
///*  设备自定义名称  */
//	private Integer displayHomepage = 1;
//	/*网关ID*/
//	private String gwId;
//	
//	public SpmsUser getSpmsUser(){   
//        return spmsUser;
//    }   
//    
//    public void setSpmsUser(SpmsUser spmsUser){   
//        this.spmsUser = spmsUser;   
//    }   
//	
//	public SpmsDevice getSpmsDevice(){
//        return spmsDevice;
//    }   
//    
//    public void setSpmsDevice(SpmsDevice spmsDevice){   
//        this.spmsDevice = spmsDevice;   
//    }   
//    
//    public String getCustomName() {
//  		return customName;
//  	}
//  	public void setCustomName(String customName) {
//  		this.customName = customName;
//  	}
//  	
//    public String getGwId() {
//		return gwId;
//	}
//	public void setGwId(String gwId) {
//		this.gwId = gwId;
//	}
//	public SpmsProduct getSpmsProduct() {
//		return spmsProduct;
//	}
//
//	public void setSpmsProduct(SpmsProduct spmsProduct) {
//		this.spmsProduct = spmsProduct;
//	}
//	public SpmsProductType getSpmsProductType() {
//		return spmsProductType;
//	}
//
//	public Integer getDeviceType() {
//		return deviceType;
//	}
//
//	public void setDeviceType(Integer deviceType) {
//		this.deviceType = deviceType;
//	}
//
//	public static String getDelFlagNormal() {
//		return DEL_FLAG_NORMAL;
//	}
//
//	public Integer getDisplayHomepage() {
//		return displayHomepage;
//	}
//
//	public void setDisplayHomepage(Integer displayHomepage) {
//		this.displayHomepage = displayHomepage;
//	}
//
//	public static String getDelFlagDelete() {
//		return DEL_FLAG_DELETE;
//	}
//
//	public static long getSerialVersionUID() {
//
//		return serialVersionUID;
//	}
//
//	public void setSpmsProductType(SpmsProductType spmsProductType) {
//		this.spmsProductType = spmsProductType;
//	}
//	/* 删除标记（0：正常；1：删除；2：审核；）*/
//	public static final String DEL_FLAG_NORMAL = "0";
//	public static final String DEL_FLAG_DELETE = "1";
//	
}
