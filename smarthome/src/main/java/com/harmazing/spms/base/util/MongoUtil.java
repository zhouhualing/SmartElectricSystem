package com.harmazing.spms.base.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {
	private final static PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static Properties properties;
	private static final String propertiesSearchPath = "prop/*.properties";
	
	public final static Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);
    private static MongoClient mongo = null;
    
    private MongoUtil() {
 
    }
 
    /**
     * 根据名称获取DB，相当于是连接
     * 
     * @param dbName
     * @return
     */
    public static MongoDatabase getDB(String dbName) {
//        if (mongo == null) {
//            // 初始化
//            init();
//        }
        return mongo.getDatabase(dbName);
    }
 
    /**
     * 初始化连接池，设置参数。
     */
     static{
        String host;
		try {
			Resource[] resources = pathMatchingResourcePatternResolver
					.getResources(propertiesSearchPath);
			Properties properties = new Properties();
			for (Resource resource : resources) {
				InputStream temp = resource.getInputStream();
				properties.load(temp);
			}
			host = properties.getProperty("mongo.host");
			int port =  Integer.parseInt(properties.getProperty("mongo.port"));// 端口
			int poolSize =  Integer.parseInt(properties.getProperty("mongo.pool.size"));// 连接数量
			int blockSize =  Integer.parseInt(properties.getProperty("mongo.pool.block")); // 等待队列长度
        // 其他参数根据实际情况进行添加
        	Builder opt = MongoClientOptions.builder(); 
    		opt.connectionsPerHost(poolSize);
    		opt.threadsAllowedToBlockForConnectionMultiplier(blockSize);
    		opt.maxWaitTime(5000);
    		opt.socketTimeout(0);
    		opt.writeConcern(WriteConcern.UNACKNOWLEDGED);
    		opt.connectTimeout(15000);
    		MongoCredential mc = MongoCredential.createCredential(properties.getProperty("mongo.user"), properties.getProperty("mongo.db"), properties.getProperty("mongo.password").toCharArray());
    		List<MongoCredential> list = new ArrayList<MongoCredential>();
    		list.add(mc);
    		mongo = new MongoClient(new ServerAddress(host,port),list,opt.build());
    		
        } catch (Exception e) {
        	e.printStackTrace();
        	LOGGER.error(e.getMessage());
        }
    }
    
    public static void insert(String user,String password,String dbName, String collectionName, Map<String,Object> data){
    	try {
    		MongoDatabase db = getDB(dbName);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			Document obj = new Document(data);
			collection.insertOne(obj);
			LOGGER.debug("mongodb insert success");
		} catch (Exception e) {
			e.printStackTrace();
        	LOGGER.error("mongodb insert unsuccess because of : " + e.getMessage());
		}
    }
    
    public static void insert(String user,String password,String dbName, String collectionName, List<Map<String,Object>> datas){
    	try {
    		MongoDatabase db = getDB(dbName);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			
			List<Document> list = new ArrayList<Document>();
			for (Map<String, Object> data : datas) {
				Document obj = new Document(data);
				list.add(obj);
			}
			collection.insertMany(list);
			LOGGER.debug("mongodb insert success");
    	} catch (Exception e) {
    		e.printStackTrace();
    		LOGGER.error("mongodb insert unsuccess because of : " + e.getMessage());
    	}
    }
}
