package com.harmazing.spms.product.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.user.entity.SpmsUser;

/**
 * productEntity
 * @author 杨永玺
 * @version 1.0
 */
@Entity
@Table(name = "spms_product")
public class SpmsProduct extends CommonEntity{   

	private static final long serialVersionUID = 1L;
		/* 产品类型ID */
		@ManyToOne
		@JoinColumn(name="type_id")
		@NotFound(action = NotFoundAction.IGNORE)
		@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
		private SpmsProductType spmsProductType ;
	/* 产品状态 */
		@NotNull(message="产品状态不能为空")
 		private Integer status;

	/* 订户ID */
 		@ManyToOne
		@JoinColumn(name="user_id")
		@NotFound(action = NotFoundAction.IGNORE)
 		@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
		private SpmsUser spmsUser ;
	
		/* 产品订阅时间 */
 		@NotNull(message="产品订阅时间不能为空")
 		private Date subscribeDate;   
	/* 产品激活时间 */
 		private Date activateDate;   
	/* 产品到期时间 */
 		private Date expireDate;   
	/* 初始成本  = 产品类型的总成本*/
 		private double initialCostRmb;   
	/* 用电成本   = 是按月的集合，是每个月的电价成本的累加值*/
 		private double electricityCostRmb;

	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name="spmsWorkOrder_id",foreignKey = @ForeignKey(name="none"))
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private SpmsWorkOrder spmsWorkOrder;

	/* 关联设备类型ID */
 		
 		/*@ManyToOne
 		@JoinColumn(name="device_id")
 		@NotFound(action = NotFoundAction.IGNORE)	
 		@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
 		private SpmsDevice spmsDevice ;*/
/* 	private Integer lianDong; //传感器联动 0 是 1否
 	private Integer zhiLengMix; //制冷最小温度
 	private Integer zhiLengMax; //制冷最大温度
	private Integer zhiReMix; //制热最小温度
 	private Integer zhiReMax;//制热最大温度
 	
 	private Integer kongTiaoCount; //空调最大绑定数
 	private Integer chuangGanCount;//传感器最大绑定数
	public Integer getLianDong() {
		return lianDong;
	}
	public void setLianDong(Integer lianDong) {
		this.lianDong = lianDong;
	}
	public Integer getZhiLengMix() {
		return zhiLengMix;
	}
	public void setZhiLengMix(Integer zhiLengMix) {
		this.zhiLengMix = zhiLengMix;
	}
	public Integer getZhiLengMax() {
		return zhiLengMax;
	}
	public void setZhiLengMax(Integer zhiLengMax) {
		this.zhiLengMax = zhiLengMax;
	}
	public Integer getZhiReMix() {
		return zhiReMix;
	}
	public void setZhiReMix(Integer zhiReMix) {
		this.zhiReMix = zhiReMix;
	}
	public Integer getZhiReMax() {
		return zhiReMax;
	}
	public void setZhiReMax(Integer zhiReMax) {
		this.zhiReMax = zhiReMax;
	}
	public Integer getKongTiaoCount() {
		return kongTiaoCount;
	}
	public void setKongTiaoCount(Integer kongTiaoCount) {
		this.kongTiaoCount = kongTiaoCount;
	}
	public Integer getChuangGanCount() {
		return chuangGanCount;
	}
	public void setChuangGanCount(Integer chuangGanCount) {
		this.chuangGanCount = chuangGanCount;
	}*/
/*	public SpmsDevice getSpmsDevice() {
			return spmsDevice;
		}
		public void setSpmsDevice(SpmsDevice spmsDevice) {
			this.spmsDevice = spmsDevice;
		}*/
	public SpmsProduct() {
		super();
	}

	
	public SpmsProductType getSpmsProductType(){   
        return spmsProductType;
    }   
    
    public void setSpmsProductType(SpmsProductType spmsProductType){   
        this.spmsProductType = spmsProductType;   
    }   
    public SpmsUser getSpmsUser() {
		return spmsUser;
	}
	public void setSpmsUser(SpmsUser spmsUser) {
		this.spmsUser = spmsUser;
	}



	
	public Integer getStatus(){   
        return status;   
    }   
    public void setStatus(Integer status){   
        this.status = status;   
    }

	public SpmsWorkOrder getSpmsWorkOrder() {
		return spmsWorkOrder;
	}

	public void setSpmsWorkOrder(SpmsWorkOrder spmsWorkOrder) {
		this.spmsWorkOrder = spmsWorkOrder;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static String getDelFlagNormal() {
		return DEL_FLAG_NORMAL;
	}

	public static String getDelFlagDelete() {
		return DEL_FLAG_DELETE;
	}

	public Date getSubscribeDate(){
        return subscribeDate;   
    }   
    public void setSubscribeDate(Date subscribeDate){   
        this.subscribeDate = subscribeDate;   
    }   
    
    
    
	
	
	public Date getActivateDate(){   
        return activateDate;   
    }   
    public void setActivateDate(Date activateDate){   
        this.activateDate = activateDate;   
    }   
    
   
     
    
	
	public Date getExpireDate(){   
        return expireDate;   
    }   
    public void setExpireDate(Date expireDate){   
        this.expireDate = expireDate;   
    }   
    
   
	
	public double getInitialCostRmb(){   
        return initialCostRmb;   
    }   
    public void setInitialCostRmb(double initialCostRmb){   
        this.initialCostRmb = initialCostRmb;   
    }   
    
	
	
	public double getElectricityCostRmb(){   
        return electricityCostRmb;   
    }   
    public void setElectricityCostRmb(double electricityCostRmb){   
        this.electricityCostRmb = electricityCostRmb;   
    }   
    
	  
    
    	/* 删除标记（0：正常；1：删除；2：审核；）*/
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
    
    }  