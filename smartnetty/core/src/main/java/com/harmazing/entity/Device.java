package com.harmazing.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device {

    private String id;
    //类型,网关还是空调
    private String type;
    //厂商
    private String vender;
    //
    private String model;
    //名称
    private String name;
    //mac地址
    private String mac;
    //序列号
    private String sn;
    //硬件版本号
    private String hardware;
    //软件版本号
    private String software;
    //当前软件版本
    private String currentSoftware;
    //仓库
    private String storage;
    //运行状态,在线不在线
    private Integer operationStatus;
    private String userId;
    private Integer disable;
    private String session;
    private Integer delete;
    //开关状态
    private Integer onOff;
    //室内温度
    private Integer temperature;
    private Integer humidity;
    //空调设定的温度
    private Integer acTemperature;
    private Integer power;
    private Integer speed;
    private Integer direction;
    private Timestamp startTime;
    private Integer mode;
    private Integer remain;
    private Long accumulatePower;
    private String server;
    private String eleArea;
    
   

	private List<Map> airconServiceList = new ArrayList<Map>();

    public Device() {
    }

    public HashMap<String, String> toMap() {
    	HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("operationStatus", operationStatus==null?null:String.valueOf(operationStatus));
        map.put("session",session);
        map.put("onOff", onOff==null?null:String.valueOf(onOff));
        map.put("temperature", temperature==null?null:String.valueOf(temperature));
        map.put("acTemperature", acTemperature==null?null:String.valueOf(acTemperature));
        map.put("power",power==null?null:String.valueOf(power));
        map.put("speed", speed==null?null:String.valueOf(speed));
        map.put("direction",direction==null?null:String.valueOf(direction));
        if(startTime == null) {
            startTime = new Timestamp(System.currentTimeMillis());
        }       
        map.put("startTime",startTime.toString().substring(0,19));
        map.put("mode",mode==null?null:String.valueOf(mode));
        map.put("remain",remain==null?null:String.valueOf(remain));
        map.put("accumulatePower", accumulatePower==null?null:String.valueOf(accumulatePower));
        map.put("server", server);
        map.put("eleArea", this.eleArea);
        map.put("software",software);
        map.put("hardware",hardware);
        return map;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVender() {
		return vender;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public String getCurrentSoftware() {
		return currentSoftware;
	}

	public void setCurrentSoftware(String currentSoftware) {
		this.currentSoftware = currentSoftware;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Integer getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(Integer operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Integer getDelete() {
		return delete;
	}

	public void setDelete(Integer delete) {
		this.delete = delete;
	}

	public Integer getOnOff() {
		return onOff;
	}

	public void setOnOff(Integer onOff) {
		this.onOff = onOff;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	public Integer getAcTemperature() {
		return acTemperature;
	}

	public void setAcTemperature(Integer acTemperature) {
		this.acTemperature = acTemperature;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public Long getAccumulatePower() {
		return accumulatePower;
	}

	public void setAccumulatePower(Long accumulatePower) {
		this.accumulatePower = accumulatePower;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getEleArea() {
		return eleArea;
	}

	public void setEleArea(String eleArea) {
		this.eleArea = eleArea;
	}

	public List<Map> getAirconServiceList() {
		return airconServiceList;
	}

	public void setAirconServiceList(List<Map> airconServiceList) {
		this.airconServiceList = airconServiceList;
	}
}
