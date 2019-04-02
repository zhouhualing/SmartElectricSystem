package com.harmazing;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.mapper.SysConfigMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ming on 14/10/17.
 */
public class Config {
    private final static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public volatile String SERVER_IP = "127.0.0.1";
    public volatile int SERVER_PORT = 18989;
    public volatile String REDIS_IP = "127.0.0.1";
    public volatile int REDIS_PORT = 6379;
    public volatile String REDIS_PASSWORD ="";
    public volatile int REDIS_TIMEOUT=60*1000;
    public volatile String REDIS_LOG_IP = "127.0.0.1";
    public volatile int REDIS_LOG_PORT = 6380;
    public volatile int REDIS_MAX_TOTAL = 200;
    public volatile int REDIS_MAX_IDLE = 10;
    public volatile long REDIS_MAX_WAIT_MILLIS = 1000000;
    public volatile boolean REDIS_TEST_ON_BORROW = true;
    public volatile boolean REDIS_TEST_ON_RETURN = true;
    public volatile boolean REDIS_TEST_WHILE_IDLE = true;
    public volatile int CLIENT_LOG_INTERVAL = 5;
    public volatile int CLIENT_REPORT_INTERVAL = 5;
    public volatile int NETWORK_OPENTIME = 60;
    public volatile String ACTIVEMQ_BROKERURL ="tcp://localhost:61616";
    public volatile String MONGO_HOST = "localhost";
    public volatile String MONGO_DB = "spms";
    public volatile String MONGO_USER = "admin";
    public volatile String MONGO_PASSWORD = "admin";
    public volatile int MONGO_PORT = 27017;
    public volatile int MONGO_POOL_SIZE = 100;
    public volatile int MONGO_POOL_BLOCK = 50;
    
    public volatile int ACTIVEMQ_MAX=500;
    public volatile int	ACTIVEMQ_IDLE=50;
    public volatile int	ACTIVEMQ_WAIT=60000;
    
    public volatile int	LOWTEMP=-1;
    public volatile int	UPPERTEMP=-1;
    public volatile String MQ_USER="";
    public volatile String MQ_PASSWORD="";
    public volatile int thread_syncgw_sleeptime=5*60*1000;
    public volatile int THREAD_OPENNETWORK_TIMEOUT = 30000;
    public volatile String ACCODE_WEBRUL = "http://localhost:8080/accodes/";
    
    public volatile boolean IFTTT_ENABLED = false;
    public volatile int IFTTT_PERIOD = 5;
    public volatile int IFTTT_CORRECTION_TEMP = 0;
    public volatile int IFTTT_CORRECTION_HUMIDITY = 0;
    public volatile int IFTTT_TIME_REGULATION_INTERVAL=0;
    public volatile String IFTTT_REALTIMERESPONSE_SENSORTYPE="";
    
    public volatile boolean UDP_WATCH = false;
    
    public volatile boolean DATABASE_TO_REDIS = false;

    private volatile static Config instance=new Config();
    
    public Map lowTemp=new HashMap();
    public Map upperTemp=new HashMap();
    
    
    public static Config getInstance() {
//        if(instance == null) {
//            init();
//        }
        return instance;
    }

    private synchronized static void init() {
        if(instance == null) {
            instance = new Config();
        }
    }

    private Config() {
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        try {
            File file = new File("./conf/config.properties");
            if(!file.exists()) {
                LOGGER.info("can't find config file : " + file.getAbsolutePath());
                return;
            }
            fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        } catch (Exception e) {
            LOGGER.info("config file error ", e);
        }

        REDIS_IP = String.valueOf(properties.getProperty("redis.ip", REDIS_IP));
        REDIS_PORT = Integer.valueOf(properties.getProperty("redis.port",
                String.valueOf(REDIS_PORT)));
        REDIS_TIMEOUT=Integer.valueOf(properties.getProperty("redis.timeout",
                String.valueOf(REDIS_TIMEOUT)));
        REDIS_PASSWORD=String.valueOf(properties.getProperty("redis.password",
        		REDIS_PASSWORD));
        REDIS_LOG_IP = String.valueOf(properties.getProperty("redis.log.ip",
        		 REDIS_LOG_IP));
        REDIS_LOG_PORT = Integer.valueOf(properties.getProperty("redis.log.port",
                String.valueOf(REDIS_LOG_PORT)));
        REDIS_MAX_IDLE = Integer.parseInt(properties.getProperty("redis.pool.maxIdle",
                String.valueOf(REDIS_MAX_IDLE)));
        REDIS_MAX_WAIT_MILLIS = Integer.parseInt(properties.getProperty("redis.pool.maxWaitMillis",
                String.valueOf(REDIS_MAX_WAIT_MILLIS)));
        REDIS_MAX_TOTAL = Integer.parseInt(properties.getProperty("redis.pool.maxTotal",
                String.valueOf(REDIS_MAX_TOTAL)));
        REDIS_TEST_ON_BORROW = Boolean.parseBoolean(properties.getProperty("redis.pool.testOnBorrow",
                String.valueOf(REDIS_TEST_ON_BORROW)));
        REDIS_TEST_ON_RETURN = Boolean.parseBoolean(properties.getProperty("redis.pool.testOnReturn",
                String.valueOf(REDIS_TEST_ON_RETURN)));
        REDIS_TEST_WHILE_IDLE = Boolean.parseBoolean(properties.getProperty("redis.pool.testWhileIdle",
                String.valueOf(REDIS_TEST_WHILE_IDLE)));
        SERVER_IP = String.valueOf(properties.getProperty("server.ip",
                SERVER_IP));
        SERVER_PORT = Integer.valueOf(properties.getProperty("server.port",
                String.valueOf(SERVER_PORT)));
        CLIENT_LOG_INTERVAL = Integer.valueOf(properties.getProperty("client.log.interval",
                String.valueOf(CLIENT_LOG_INTERVAL)));
        CLIENT_REPORT_INTERVAL = Integer.valueOf(properties.getProperty("client.report.interval",
                String.valueOf(CLIENT_REPORT_INTERVAL)));
        ACTIVEMQ_BROKERURL = String.valueOf(properties.getProperty("activemq.brokerURL",
        		ACTIVEMQ_BROKERURL));
        ACTIVEMQ_MAX = Integer.valueOf(properties.getProperty("activemq.max",String.valueOf(ACTIVEMQ_MAX)));
        ACTIVEMQ_IDLE = Integer.valueOf(properties.getProperty("activemq.max",String.valueOf(ACTIVEMQ_IDLE)));
        ACTIVEMQ_WAIT = Integer.valueOf(properties.getProperty("activemq.max",String.valueOf(ACTIVEMQ_WAIT)));
        
        
        
        NETWORK_OPENTIME=Integer.valueOf(properties.getProperty("NETWORK_OPENTIME", String.valueOf(NETWORK_OPENTIME)));
        MONGO_HOST = String.valueOf(properties.getProperty("mongo.host",MONGO_HOST));
        MONGO_PORT = Integer.valueOf(properties.getProperty("mongo.port",String.valueOf(MONGO_PORT)));
        MONGO_DB = String.valueOf(properties.getProperty("mongo.db",MONGO_DB));
        MONGO_USER = String.valueOf(properties.getProperty("mongo.user",MONGO_USER));
        MONGO_PASSWORD = String.valueOf(properties.getProperty("mongo.password",MONGO_PASSWORD));
        MONGO_POOL_SIZE = Integer.valueOf(properties.getProperty("mongo.pool.size",String.valueOf(MONGO_POOL_SIZE)));
        MONGO_POOL_BLOCK = Integer.valueOf(properties.getProperty("mongo.pool.block",String.valueOf(MONGO_POOL_BLOCK)));
        MQ_USER = String.valueOf(properties.getProperty("activemq.username",MQ_USER));
        MQ_PASSWORD = String.valueOf(properties.getProperty("activemq.password",MQ_PASSWORD));
        thread_syncgw_sleeptime=Integer.valueOf(properties.getProperty("thread.syncgw.sleeptime", String.valueOf(thread_syncgw_sleeptime)));
        THREAD_OPENNETWORK_TIMEOUT = Integer.valueOf(properties.getProperty("thead.opennetwork.timeout", String.valueOf(THREAD_OPENNETWORK_TIMEOUT)));
        ACCODE_WEBRUL = String.valueOf(properties.getProperty("accode.webURL", ACCODE_WEBRUL));
        UDP_WATCH = Boolean.parseBoolean(properties.getProperty("logger.enableUdpWatch",String.valueOf(UDP_WATCH)));
        
        DATABASE_TO_REDIS = Boolean.parseBoolean(properties.getProperty("database.to.Redis",String.valueOf(DATABASE_TO_REDIS)));
        
        IFTTT_ENABLED = Boolean.parseBoolean(properties.getProperty("ifttt.enabled",String.valueOf(IFTTT_ENABLED)));
        IFTTT_PERIOD  = Integer.valueOf(properties.getProperty("ifttt.period",String.valueOf(IFTTT_PERIOD)));
        IFTTT_CORRECTION_TEMP = Integer.valueOf(properties.getProperty("ifttt.correction.temp",String.valueOf(IFTTT_CORRECTION_TEMP)));;
        IFTTT_CORRECTION_HUMIDITY = Integer.valueOf(properties.getProperty("ifttt.correction.humidity",String.valueOf(IFTTT_CORRECTION_HUMIDITY)));
        IFTTT_TIME_REGULATION_INTERVAL = Integer.valueOf(properties.getProperty("ifttt.time.regulation_interval",String.valueOf(IFTTT_TIME_REGULATION_INTERVAL)));
        IFTTT_REALTIMERESPONSE_SENSORTYPE=String.valueOf(properties.getProperty("ifttt.realtimeresponse.sensortype", IFTTT_REALTIMERESPONSE_SENSORTYPE));
        
        setSysConfig();
        setDsmTemp();
    }
    
    public void setSysConfig(){
    	 DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
         SqlSessionFactory sf= dataSourceSessionFactory.getSqlSessionFactory();
         SqlSession session=sf.openSession();
         try{
	         SysConfigMapper configMapper=session.getMapper(SysConfigMapper.class);
	         List<Map<String,String>> lst=configMapper.getSysConfig();	         
	         if(lst!=null){
		         for(int i=0;i<lst.size();i++){
		        	 Map m=lst.get(i);
			         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("logTimeD") && m.get("config_value")!=null){
			        	 CLIENT_LOG_INTERVAL=Integer.parseInt(m.get("config_value").toString());
			         }
			         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("logTime") && m.get("config_value")!=null){
			        	CLIENT_REPORT_INTERVAL=Integer.parseInt(m.get("config_value").toString());
			         }
			         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("openGetWay") && m.get("config_value")!=null){
			        	 NETWORK_OPENTIME=Integer.parseInt(m.get("config_value").toString());
				     }
			         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("lowTemp") && m.get("config_value")!=null){
			        	 LOWTEMP=Integer.parseInt(m.get("config_value").toString());
			         }
			         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("upperTemp") && m.get("config_value")!=null){
			        	 UPPERTEMP=Integer.parseInt(m.get("config_value").toString());
			         }
	//		         if(m!=null && m.get("config_name")!=null && m.get("config_name").equals("updateLink") && m.get("config_value")!=null){
	//		        	 updateLink=m.get("config_value");
	//			     }
		         }
	         }
         }catch(Exception e){
        	 e.printStackTrace();
         }finally{
        	 session.close();
         }
         
    }
    public void setDsmTemp(){
    	 DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
         SqlSessionFactory sf= dataSourceSessionFactory.getSqlSessionFactory();
         SqlSession session=sf.openSession();
         SysConfigMapper configMapper=session.getMapper(SysConfigMapper.class);
         List<Map<String,String>> lst=configMapper.getDsmTemp();
         session.close();
         if(lst!=null){
	         for(int i=0;i<lst.size();i++){
	        	 Map m=lst.get(i);
	        	 lowTemp.put(m.get("areaId"),m.get("lowTemp"));
	        	 upperTemp.put(m.get("areaId"),m.get("upperTemp"));
	         }
         }
    }
    public void setDsmTempByAreaId(String areaId){
   	 DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
        SqlSessionFactory sf= dataSourceSessionFactory.getSqlSessionFactory();
        SqlSession session=sf.openSession();
        SysConfigMapper configMapper=session.getMapper(SysConfigMapper.class);
        List<Map<String,String>> lst=configMapper.getDsmTempbyArea(areaId);
        session.close();
        if(lst!=null){
	         for(int i=0;i<lst.size();i++){
	        	 Map m=lst.get(i);
	        	 lowTemp.put(m.get("areaId"),m.get("lowTemp"));
	        	 upperTemp.put(m.get("areaId"),m.get("upperTemp"));
	         }
        }
   }
    public String getPro(String key){
    	 Properties properties = new Properties();
    	 try {
			properties.load(new FileInputStream("./conf/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 return properties.getProperty(key);
    }

}
