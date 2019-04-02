package com.harmazing.spms.product.entity;



import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.entity.CommonEntity;
/**
 * Product_typeEntity
 * @author 杨永玺
 * @version 1.0
 */
@Entity
@Table(name = "spms_product_type")
public class SpmsProductType  extends CommonEntity{   

	private static final long serialVersionUID = 1L;
	/* 产品类型名称 */
		/*@Length(min=1, max=32)
		@NotNull(message="产品类型名称不能为空")*/
 		private String names;   
	/* 产品目录ID */
	
		@NotNull(message="服务目录不能为空")
 		private Integer muluId;
	/* 产品状态 */
 		private Integer status;
	/* 产品有效期限 */
 		@Length(min=1, max=32)
 		@NotNull(message="产品有效期限不能为空")
 		private String validPeriod;   
	/* 所属区域 */
 		@ManyToOne
		@JoinColumn(name="area_id")
		@NotFound(action = NotFoundAction.IGNORE)
 		@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
 		@NotNull(message="产品所属区域不能为空")
 		private Area area; 
 		
	

		/* 文字描述 */
 		private String describes;   
	/* 产品总费用 */

 		@NotNull(message="产品价格不能为空")
 		private double countRmb;   
	/* 收费方式（0预付全款1预付月付） */
 		//@NotNull(message="收费方式（0预付全款1预付月付）不能为空")
 		private Integer rmbType;
	/* 预留 */
 		@Length(min=1, max=64)
 		private String refundable;   
	/* 预留 */
 		@Length(min=1, max=64)
 		private String upgradeable;   
	/* 预留 */
 		//@Length(min=1, max=64)
 		//@NotNull(message="预留不能为空")
 		private Integer stops;   
	/* 初始成本 */
 		@NotNull(message="初始成本不能为空")
 		private double indexRmb;   
	/* 电价成本 */
 		@NotNull(message="电价成本不能为空")
 		private double electrovalenceRmb;   
	/* 服务配置 */
 		
 		@NotNull(message="服务配置不能为空")
 		private Integer configurationInformation;   
	/* 协议温度访问c1,c2,h1,h2 */

 		//@NotNull(message="协议温度访问c1,c2,h1,h2不能为空")
 		/*private String temperatureRange; */
	/* 设备关联类型
 		@Length(min=1, max=64)
 		@NotNull(message="设备关联类型不能为空")
 		private String deviceType;*/
 		//删除标记
 		private Integer deleteStauts;
 		
 		private Integer lianDong; //传感器联动 0 是 1否

 	 	private Integer zhiLengMix; //制冷最小温度
 	 	private Integer zhiLengMax; //制冷最大温度
 		private Integer zhiReMix; //制热最小温度
 	 	private Integer zhiReMax;//制热最大温度
 	 	
 	 	private Integer kongTiaoCount; //空调最大绑定数
 	 	private Integer chuangGanCount;//传感器最大绑定数
 	 	
 	 	private Integer sensorEnabled;//是否允许绑定门窗
 	 	
 		public Integer getDeleteStauts() {
			return deleteStauts;
		}
		public void setDeleteStauts(Integer deleteStauts) {
			this.deleteStauts = deleteStauts;
		}
		public Area getArea() {
			return area;
		}
		public void setArea(Area area) {
			this.area = area;
		} 	
	
	public String getNames(){   
        return names;   
    }   
    public void setNames(String name){   
        this.names = name;   
    }   
    
	
	public Integer getMuluId(){   
        return muluId;   
    }   
    public void setMuluId(Integer muluId){   
        this.muluId = muluId;   
    }   
    
	
	
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
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static String getDelFlagNormal() {
		return DEL_FLAG_NORMAL;
	}

	public static String getDelFlagDelete() {
		return DEL_FLAG_DELETE;
	}

	public String getValidPeriod(){
        return validPeriod;   
    }   
    public void setValidPeriod(String validPeriod){   
        this.validPeriod = validPeriod;   
    }   
    
/*	@Length(min=1, max=64)
	@NotNull(message="所属区域不能为空")
	public String getAreaId(){   
        return areaId;   
    }   
    public void setAreaId(String areaId){   
        this.areaId = areaId;   
    }  */ 
    
	
	public String getDescribes(){   
        return describes;   
    }   
    public void setDescribes(String describe){   
        this.describes = describe;   
    }   
    
	
	public double getCountRmb(){   
        return countRmb;   
    }   
    public void setCountRmb(double countRmb){   
        this.countRmb = countRmb;   
    }   
    
	
	
	public Integer getRmbType(){   
        return rmbType;   
    }   
    public void setRmbType(Integer rmbType){   
        this.rmbType = rmbType;   
    }   
    
	
	public String getRefundable(){   
        return refundable;   
    }   
    public void setRefundable(String refundable){   
        this.refundable = refundable;   
    }   
    
	
	public String getUpgradeable(){   
        return upgradeable;   
    }   
    public void setUpgradeable(String upgradeable){   
        this.upgradeable = upgradeable;   
    }   
    
	public Integer getStops(){   
        return stops;   
    }   
    public void setStops(Integer stop){   
        this.stops = stop;   
    }   
    
	
	
	public double getIndexRmb(){   
        return indexRmb;   
    }   
    public void setIndexRmb(double indexRmb){   
        this.indexRmb = indexRmb;   
    }   
    
	
	
	public double getElectrovalenceRmb(){   
        return electrovalenceRmb;   
    }   
    public void setElectrovalenceRmb(double electrovalenceRmb){   
        this.electrovalenceRmb = electrovalenceRmb;   
    }   
    
	
	public Integer getConfigurationInformation(){   
        return configurationInformation;   
    }   
    public void setConfigurationInformation(Integer configurationInformation){   
        this.configurationInformation = configurationInformation;   
    }   
    
	
	/*public String getTemperatureRange(){   
        return temperatureRange;   
    }   
    public void setTemperatureRange(String temperatureRange){   
        this.temperatureRange = temperatureRange;   
    }   */
    
	
	/*public String getDeviceType(){   
        return deviceType;   
    }   
    public void setDeviceType(String deviceType){   
        this.deviceType = deviceType;   
    }   */
    
    	public Integer getSensorEnabled() {
		return sensorEnabled;
	}
	public void setSensorEnabled(Integer sensorEnabled) {
		this.sensorEnabled = sensorEnabled;
	}


	/* 删除标记（0：正常；1：删除；2：审核；）*/
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	
    
    }  
