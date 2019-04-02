package com.harmazing.spms.helper;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsDeviceBase;

public class DeviceDAOData {
	private Integer type;
	private SpringDataDAO<? extends SpmsDeviceBase> dao;
	private Class daoClass;
	private Class devClass;
	
	public DeviceDAOData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DeviceDAOData(SpringDataDAO<? extends SpmsDeviceBase> dao, Class daoClass, Class devClass ,Integer type ) {
		super();
		this.dao = dao;
		this.type = type;
		this.devClass = devClass;
		this.daoClass = daoClass;
	}
	public SpringDataDAO<? extends SpmsDeviceBase> getDao() {
		return dao;
	}
	public void setDao(SpringDataDAO<? extends SpmsDeviceBase> dao) {
		this.dao = dao;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Class getDevClass() {
		return devClass;
	}
	public void setDevClass(Class devClass) {
		this.devClass = devClass;
	}
	public Class getDaoClass() {
		return daoClass;
	}
	public void setDaoClass(Class cls) {
		this.daoClass = cls;
	}
}
