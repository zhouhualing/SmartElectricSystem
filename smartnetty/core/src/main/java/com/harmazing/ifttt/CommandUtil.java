package com.harmazing.ifttt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by ming on 14-9-18.
 */
public class CommandUtil {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CommandUtil.class);
	private static ExecutorService pool = Executors.newFixedThreadPool(500);
	
	// private final static String REDIS_SERVER = "112.126.68.80";
	// private final static int REDIS_PORT = 6379;
	// private final static JedisPool JEDISPOOL = new JedisPool(REDIS_SERVER,
	// REDIS_PORT);
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private final static long TIMEOUT = 20 * 1000;

	public final static class Scope {
		public final static String AREA = "AREA"; // 区域
		public final static String USER = "USER"; // 用户
		public final static String DEVICE = "DEVICE"; // 设备
	}

	public static interface CommandType {
		public final static String ON = "ON";
		public final static String OFF = "OFF";
		public final static String MODE_SET = "MODE_SET";
		public final static String FAN_SET = "FAN_SET";
		public final static String TEMP_SET = "TEMP_SET";
	}
	
	public static interface IrControlCommandType {
		public final static String IR_RCU_UPDATE = "IR_RCU_UPDATE";
		public final static String IR_TX_CODE = "IR_TX_CODE";
	}

	/**
	 * 订阅
	 */
	private final static class Subscribe {

		private static volatile Subscribe instance;
		private static volatile SubscribeListener subscribeListener;
		private static volatile Jedis jedis;
		private static volatile Thread thread;

		public final static class Patterns {
			public final static String RESPONSE_COMMAND = "RESPONSE:COMMAND:*";
			public final static String RESPONSE_GETGWINFO = "RESPONSE:GETGWINFO:*";
			public final static String RESPONSE_GETSENSORINFO = "RESPONSE:GETSENSORINFO:*";
			public final static String RESPONSE_SERVICEUPDATE="RESPONSE:SERVICEUPDATE:*";
			public final static String RESPONSE_TIMERCURV="RESPONSE:TIMERCURV:*";
		}

		private static synchronized void init() {
			if (instance == null) {
				Jedis jedis;
				try {
					jedis = new Jedis(
							Config.getInstance().REDIS_IP,
							Config.getInstance().REDIS_PORT);
					jedis.auth(Config.getInstance().REDIS_PASSWORD);
					instance = new Subscribe(jedis);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		public static Subscribe getInstance() {
			if (instance == null) {
				init();
			}
			connect();
			return instance;
		}

		private Subscribe(Jedis jedis) {
			this.jedis = jedis;
			connect();
			subscribeListener = new SubscribeListener();
			thread = new SubscribeThread(this.jedis);
			thread.start();
			while (!thread.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					LOGGER.debug("", e);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				LOGGER.debug("", e);
			}
		}

		private class SubscribeThread extends Thread {
			private Jedis jedis;

			public SubscribeThread(Jedis jedis) {
				this.jedis = jedis;
			}

			@Override
			public void run() {
				while (true) {
					if (!jedis.isConnected()) {
						jedis.connect();
					}
					try {
						jedis.psubscribe(subscribeListener, new String[] {
								Patterns.RESPONSE_COMMAND,
								Patterns.RESPONSE_GETGWINFO,
								Patterns.RESPONSE_GETSENSORINFO });
					} catch (Exception e) {
						jedis.close();
						LOGGER.debug("psubscribe error:", e);
					}
				}
			}
		}

		private static void connect() {
			if (!jedis.isConnected()) {
				jedis.connect();
			}
		}

		public synchronized void addListener(String channel,
				ListenerRunable listenerRunable) {
			subscribeListener.addListener(channel, listenerRunable);
		}

		public synchronized void removeListener(String channel) {
			subscribeListener.removeListener(channel);
		}
	}

	private final static class SubscribeListener extends JedisPubSub {
		private Map<String, ListenerRunable> listeners;
		private Timer timer;

		public SubscribeListener() {
			listeners = new HashMap<String, ListenerRunable>();
			timer = new Timer();
			timer.schedule(new TimeoutTask(this), 5 * 1000, 5 * 1000);
		}

		private class TimeoutTask extends TimerTask {
			SubscribeListener subscribeListener;

			public TimeoutTask(SubscribeListener subscribeListener) {
				this.subscribeListener = subscribeListener;
			}

			@Override
			public void run() {

				long now = new Date().getTime();
				ArrayList<String> timeoutList = new ArrayList<String>();
				Iterator<String> iterator = subscribeListener.listeners.keySet().iterator();
				while (iterator.hasNext()) {
					String channel = iterator.next();
					ListenerRunable runable = subscribeListener.getListener(channel);
					if (now > runable.create + runable.timeout) {
						runable.start(-2);
						LOGGER.debug("TIMEOUT CHANNEL:" + channel);
						timeoutList.add(channel);
					}
				}

				iterator = timeoutList.iterator();
				while (iterator.hasNext()) {
					String channel = iterator.next();
					subscribeListener.removeListener(channel);
				}
			}
		}

		@Override
		public void onMessage(String channel, String message) {
			LOGGER.debug("channel: " + channel + ", message:" + message);
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			LOGGER.debug("pattern: " + pattern + ", channel: " + channel
					+ ", message: " + message);
			ListenerRunable runable = this.getListener(channel);
			try {
				if (runable != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					Map map = objectMapper.readValue(message, Map.class);
					int result = (Integer) map.get("result");
					runable.start(result);
				}
			} catch (Exception e) {
				LOGGER.debug("run error: ", e);
			} finally {
				removeListener(channel);
			}
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			LOGGER.debug("channel: " + channel);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			LOGGER.debug("channel: " + channel);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			LOGGER.debug("pattern: " + pattern);
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			LOGGER.debug("pattern: " + pattern);
		}

		public synchronized void addListener(String channel,
				ListenerRunable runable) {
			listeners.put(channel, runable);
		}

		public synchronized void removeListener(String channel) {
			listeners.remove(channel);
		}

		public ListenerRunable getListener(String channel) {
			return listeners.get(channel);
		}
	}

	public static abstract class ListenerRunable {
		public long create = new Date().getTime();
		public long timeout = TIMEOUT;
		public volatile boolean start = false;
		public volatile int result = 0;

		public synchronized void start(int result) {
			if (!start) {
				try {
					LOGGER.debug("result :" + result);
					this.result = result;
					run(result);
				} finally {
					start = true;
				}
			}
		}

		public abstract void run(int result);
	}

	public final static class JedisFactory {
		private volatile static JedisFactory instance;
		private volatile static JedisPool jedisPool;

		public static JedisFactory getInstance() {
			if (instance == null) {
				init();
			}
			return instance;
		}

		private JedisFactory() {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(20);
			config.setMaxTotal(2000);
			config.setMinIdle(20);
			config.setMaxWaitMillis(10000);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			config.setTestWhileIdle(true);
			try {
				jedisPool = new JedisPool(config,
						Config.getInstance().REDIS_IP,
						Config.getInstance().REDIS_PORT,
						2000,
						Config.getInstance().REDIS_PASSWORD);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private synchronized static void init() {
			if (instance == null) {
				instance = new JedisFactory();
			}
			return;
		}

		public  Jedis getResource() {
			Jedis jedis = jedisPool.getResource();
			if(!jedis.isConnected()){
				jedis.connect();
			}
			return jedis;
		}

		public  void returnResource(Jedis jedis) {
			if(jedis != null)
			    jedisPool.returnResource(jedis);
		}
		
		public  void returnBrokenResource(Jedis jedis) {
			if(jedis != null)
			    jedisPool.returnBrokenResource(jedis);
		}

	}

	/**
	 * 异步发送消息结束后存在回调
	 * 
	 * @param message
	 * @param runable
	 * @throws IOException
	 */
	public final static void asyncSendMessage(Map message,
			ListenerRunable runable) throws IOException {
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = jedisFactory.getResource();
		try {
			Subscribe subscribe = Subscribe.getInstance();
			String messageType = (String) message.get("messageType");
			if (messageType == null
					|| (!messageType.equals("COMMAND")
							&& !messageType.equals("IRCONTROL") 
							&& !messageType.equals("GETGWINFO") 
							&& !messageType.equals("GETSENSORINFO")
							&& !messageType.equals("SERVICEUPDATE")
							&& !messageType.equals("TIMERCURV")
							&& !messageType.equals("GATEWAYCONTROL")
							)) {
				throw new IllegalArgumentException("messageType is error!");
			}

			String channel = messageType + ":" + UUID.randomUUID().toString();
			String responseChannel = "RESPONSE:" + channel;

			subscribe.addListener(responseChannel, runable);
			long l = jedis.publish(channel, OBJECT_MAPPER.writeValueAsString(message));
			// LOGGER.info("publish result:" + l);
		} catch (Exception e) {
			LOGGER.error("", e);
			jedisFactory.returnBrokenResource(jedis);
		} finally {
			jedisFactory.returnResource(jedis);
		}
	}

	/**
	 * 不包含回掉
	 * 
	 * @param message
	 * @throws IOException
	 */
	public final static void asyncSendMessage(Map message) {
		AsyncPoolRunnable apr = new AsyncPoolRunnable();
		apr.setMessage(message);
		pool.execute(apr);
	}
	
	private static class AsyncPoolRunnable implements Runnable{
		private Map message;
		@Override
		public void run() {
			JedisFactory jedisFactory = JedisFactory.getInstance();
			Jedis jedis = jedisFactory.getResource();
			
			try {
				String messageType = (String) message.get("messageType");
				if (messageType == null
						|| (!messageType.equals("COMMAND")
								&& !messageType.equals("IRCONTROL") 
								&& !messageType.equals("GETGWINFO") 
								&& !messageType.equals("GETSENSORINFO")
								&& !messageType.equals("SERVICEUPDATE")
								&& !messageType.equals("TIMERCURV")
								&& !messageType.equals("GATEWAYCONTROL"))) {
					throw new IllegalArgumentException("messageType is error!");
				}

				String channel = messageType + ":" + UUID.randomUUID().toString();

				long l = jedis.publish(channel,
						OBJECT_MAPPER.writeValueAsString(message));
			} catch (Exception e) {
				LOGGER.error("", e);
				jedisFactory.returnBrokenResource(jedis);
			} finally {
				jedisFactory.returnResource(jedis);
			}
		}

		public Map getMessage() {
			return message;
		}

		public void setMessage(Map message) {
			this.message = message;
		}
	}

	/**
	 * 同步发送消息
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 */
	public final static int syncSendMessage(Map message) throws IOException {
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = jedisFactory.getResource();
		int result = Integer.MIN_VALUE;
		try {
			Subscribe subscribe = Subscribe.getInstance();
			String messageType = (String) message.get("messageType");
			if (messageType == null
					|| (!messageType.equals("COMMAND")
							&& !messageType.equals("IRCONTROL")
							&& !messageType.equals("GETGWINFO") 
							&& !messageType.equals("GETSENSORINFO")
							&& !messageType.equals("SERVICEUPDATE")
							&& !messageType.equals("TIMERCURV")
							&& !messageType.equals("GATEWAYCONTROL"))) {
				throw new IllegalArgumentException("messageType is error!");
			}

			String channel = messageType + ":" + UUID.randomUUID().toString();

			ListenerRunable runable = new ListenerRunable() {
				@Override
				public void run(int result) {

				}
			};
			String responseChannel = "RESPONSE:" + channel;
			LOGGER.debug(channel);
			subscribe.addListener(responseChannel, runable);
			
			long l = jedis.publish(channel,
					OBJECT_MAPPER.writeValueAsString(message));
			String tmp = OBJECT_MAPPER.writeValueAsString(message);
			System.out.println("*****************" + responseChannel);
			
			// LOGGER.info("publish result:" + l);
			while (true) {
				if (runable.start) {
					break;
				}
				Thread.sleep(50);
			}
			result = runable.result;
		} catch (Exception e) {
			LOGGER.error("", e);
			e.printStackTrace();
			jedisFactory.returnBrokenResource(jedis);
		} finally {
			jedisFactory.returnResource(jedis);
			return result;
		}
	}

	/**
	 * 生成控制消息
	 * 
	 * @param scope
	 * @param id
	 * @param sensorId
	 * @param commandType
	 * @param stringParameter
	 * @param intParameter
	 * @return
	 */
	public final static Map buildCommandMessageMap(String scope, String id,
			String sensorId, String commandType, String stringParameter,
			Integer intParameter) {
		if (scope == null
				|| (!Scope.AREA.equals(scope) && !Scope.USER.equals(scope) && !Scope.DEVICE
						.equals(scope))) {
			throw new IllegalArgumentException("scope is not right!");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messageType", "COMMAND");
		map.put("scope", scope);
		map.put("id", id);
		map.put("sensorId", sensorId);
		map.put("commandType", commandType);
		map.put("stringParameter", stringParameter);
		map.put("intParameter", intParameter);
		return map;
	}
	/**
	 * 生成控制消息
	 * 
	 * @param id
	 * @param sensorId
	 * @param commandType
	 * @param modsig
	 * @return
	 */
	public final static Map buildIrControlMessageMap(String id,
			String sensorId, String commandType, String modsig) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messageType", "IRCONTROL");
		map.put("id", id);
		map.put("sensorId", sensorId);
		map.put("commandType", commandType);
		map.put("modsig", modsig);
		return map;
	}

	/**
	 * 生成获取网关信息消息
	 * 
	 * @param scope
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public final static Map buildGetGWInfoMessageMap(String scope, String id)
			throws Exception {
		if (scope == null
				|| (!Scope.USER.equals(scope) && !Scope.DEVICE.equals(scope))) {
			throw new Exception("scope is not right!");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("messageType", "GETGWINFO");
		map.put("scope", scope);
		map.put("id", id);
		return map;
	}

	/**
	 * 生成获取传感器信息消息
	 * 
	 * @param scope
	 * @param id
	 * @param sensorId
	 * @return
	 * @throws Exception
	 */
	public final static Map buildGetSensorInfoMessageMap(String scope,
			String id, String sensorId) throws Exception {
		if (scope == null
				|| (!Scope.USER.equals(scope) && !Scope.DEVICE.equals(scope))) {
			throw new Exception("scope is not right!");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("messageType", "GETSENSORINFO");
		map.put("scope", scope);
		map.put("id", id);
		map.put("sensorId", sensorId);
		return map;
	}

	public static void asyncSendMessage(List<Map> listCommand) {
		AsyncRunnable ar = new AsyncRunnable();
		ar.setListCommand(listCommand);
		Thread t = new Thread(ar);
		t.start();
	}
	
	private static class AsyncRunnable implements Runnable{
		private List<Map> listCommand;
		@Override
		public void run() {
			JedisFactory jedisFactory = JedisFactory.getInstance();
			Jedis jedis = jedisFactory.getResource();
			try {
				for (Map message : listCommand) {
					String messageType = (String) message.get("messageType");
					if (messageType == null
							|| (!messageType.equals("COMMAND")
									&& !messageType.equals("IRCONTROL")
									&& !messageType.equals("GETGWINFO") 
									&& !messageType.equals("GETSENSORINFO")
									&& !messageType.equals("SERVICEUPDATE")
									&& !messageType.equals("TIMERCURV")
									&& !messageType.equals("GATEWAYCONTROL"))) {
						throw new IllegalArgumentException("messageType is error!");
					}

					String channel = messageType + ":" + UUID.randomUUID().toString();

					long l = jedis.publish(channel,
							OBJECT_MAPPER.writeValueAsString(message));
				}
				
			} catch (Exception e) {
				LOGGER.error("", e);
				jedisFactory.returnBrokenResource(jedis);
			} finally {
				jedisFactory.returnResource(jedis);
			}
		}
		
		public List<Map> getListCommand() {
			return listCommand;
		}
		public void setListCommand(List<Map> listCommand) {
			this.listCommand = listCommand;
		}
		
	}
}
