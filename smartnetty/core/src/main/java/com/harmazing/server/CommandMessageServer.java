package com.harmazing.server;

import com.harmazing.Config;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.redis.CommandListener;
import com.harmazing.redis.RedisContextFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * Created by ming on 14-9-23. 
 */
public final class CommandMessageServer extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommandMessageServer.class);
    ActiveCommandQueue activeQueu = null;
    OpenNetworkCache openNetworkCache = null;
    
    public CommandMessageServer(ActiveCommandQueue queue, OpenNetworkCache cache){
    	this.activeQueu = queue;
    	this.openNetworkCache = cache;
    }
    
    @Override
    public void run() {
        LOGGER.info("CommandMessageServer Start!");
        Config config = Config.getInstance();
        Jedis jedis = new Jedis(config.REDIS_IP, config.REDIS_PORT, 0);
        if(!config.REDIS_PASSWORD.equals("")){
        	jedis.auth(config.REDIS_PASSWORD);
        }
//        final Jedis jedis=RedisContextFactory.getInstance().getJedis();
        CommandListener commandListener = new CommandListener(this.activeQueu, this.openNetworkCache);
        boolean bs=true;
        while(true) {
        	if(!bs){
        		jedis = new Jedis(config.REDIS_IP, config.REDIS_PORT, 0);
                jedis.auth(config.REDIS_PASSWORD);
        	}
            try {
                if(!jedis.isConnected()) {
                    jedis.connect();
                }
                jedis.psubscribe(commandListener,
                        new String[]{
                                CommandListener.Patterns.COMMAND,
                                CommandListener.Patterns.IRCONTROL,
                                CommandListener.Patterns.GETGWINFO,
                                CommandListener.Patterns.GETSENSORINFO,
                                CommandListener.Patterns.SERVICEUPDATE,
                                CommandListener.Patterns.TIMERCURV,
                                CommandListener.Patterns.GATEWAYCONTROL,
                                CommandListener.Patterns.HALAMPCONTROL
                        });
            } catch (Exception e) {
                LOGGER.error(e.toString());
                e.printStackTrace();
            } finally {
                jedis.close();
                bs=false;
            }
        }
//        LOGGER.debug("CommandMessageServer end!");
    }
}
