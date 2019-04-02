package com.harmazing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import com.harmazing.util.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class MyTest {
	public static void main(String[] args) throws Exception {
//		DB db = MongoUtil.getDB("spms");
//		DBCollection coll = db.getCollection("logs");
//		groupMongo(db, coll);
//		mapReduceMongo(db, coll);
//		findMongo(db, coll);
//		insertMongo(db, coll);
//		insertMysql();
		for (int i = 0; i < 200; i++) {
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					int i = 0;
					while(i<10){
						MongoDatabase db = MongoUtil.getDB("spms");
						MongoCollection<Document> coll = db.getCollection("logs");
						try {
							insertMongo(db, coll);
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					}
				}
			});
			t.start();
		}
	}
	
	private static void insertMysql() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8", "root", "123456");
		PreparedStatement stmt = conn.prepareStatement("insert into log(id,eleArea,updateTime,currentPower,accumulatePower,reactivePower,apparentPower) valuse(?,?,?,?,?,?,?)");
		
	}

	private static void findMongo(MongoDatabase db, MongoCollection coll){
		FindIterable cursor = coll.find().limit(100);
		MongoCursor<Document> c = cursor.iterator();
		while(c.hasNext()){
			System.out.println(c.next());
		}
	}

//	private static void groupMongo(MongoDatabase db, MongoCollection coll){
//		BasicDBObject keys = new BasicDBObject();
//		keys.put("eleArea", true);
//		keys.put("updateTime", true);
//		BasicDBObject initial = new BasicDBObject();
//		initial.put("currentPower",0);
//		initial.put("accumulatePower",0);
//		initial.put("reactivePower",0);
//		initial.put("apparentPower",0);
//		String reduce = "function (doc, out) { "
//				+ " out.currentPower = out.currentPower + parseInt(doc.currentPower); "
//				+ " out.accumulatePower = parseInt(doc.accumulatePower) + out.accumulatePower;"
//				+ " out.reactivePower = parseInt(doc.reactivePower) + out.reactivePower;"
//				+ " out.apparentPower = parseInt(doc.apparentPower) + out.apparentPower;"
//				+ "}";
//
//		long t1 = System.currentTimeMillis();
//		coll.
//		BasicDBList list = (BasicDBList) coll.group(keys, null, initial, reduce);
//		long t2 = System.currentTimeMillis();
//		Iterator ite = list.iterator();
//		while(ite.hasNext()){
//			System.out.println(ite.next().toString());
//		}
//		System.out.println(t2-t1);
//		System.out.println(coll.find().size());
//	}
	
	private static void mapReduceMongo(MongoDatabase db, MongoCollection coll){
		String map = "function () { "
				+ "emit({eleArea:this.eleArea,updateTime:this.updateTime}," +
				"{currentPower:0,accumulatePower:0,reactivePower:0,apparentPower:0}); "
				+ "}";
		String reduce = "function (key, values) { " +
				" var result = {currentPower:0,accumulatePower:0,reactivePower:0,apparentPower:0}; " +
				" values.forEach(function(value){" 
				+ " result.currentPower = parseInt(value.currentPower) + result.currentPower; "
				+ " result.accumulatePower = parseInt(value.accumulatePower) + result.accumulatePower;"
				+ " result.reactivePower = parseInt(value.reactivePower) + result.reactivePower;"
				+ " result.apparentPower = parseInt(value.apparentPower) + result.apparentPower;"
				+ " });"
				+ " return result; "
				+ "}";
		

		System.out.println(map);
		System.out.println(reduce);

		long t1 = System.currentTimeMillis();
		MapReduceIterable result = coll.mapReduce(map, reduce);
		long t2 = System.currentTimeMillis();

		Iterator<Document> ite = result.iterator();
		while(ite.hasNext()){
			System.out.println(ite.next());
		}
		System.out.println(t2-t1);
	}
	
	private static void insertMongo(MongoDatabase db, MongoCollection coll){
		List<Document> list = new ArrayList<Document>();
		Random r = new Random();
//		if(!db.isAuthenticated()){
//			db.authenticate("admin", "admin".toCharArray());
//		}
//		for (int i = 0; i < 5; i++) {
			Document obj = new Document();
//			obj.put("_id", i);
			obj.put("eleArea", (r.nextInt(5)%2+1) + "");
			obj.put("updateTime", "2015-10-11 15:15:0" + r.nextInt(5));
			obj.put("currentPower", r.nextInt(5));
			obj.put("accumulatePower", r.nextInt(5));
			obj.put("reactivePower", r.nextInt(5));
			obj.put("apparentPower", r.nextInt(5));
			list.add(obj);
//		}
		coll.insertOne(obj);
	}
}
