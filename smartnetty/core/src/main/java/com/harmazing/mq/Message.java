package com.harmazing.mq;

import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ming on 14/9/29.
 */
public class Message {

    protected String id;
    protected Map<String, String> attributes;
    protected int timeout = 10 * 60;
   

    public Message(String id) {
        this.id = id;
        init(id);
    }

    public Message(String id, Map attributes) {
        this.id = id;
        this.attributes = attributes;
        setCreateTime((new Date()).getTime());
    }

    private void init(String id) {
    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
        Map<String, String> map = redisContext.getMessage(id);
        if(map != null) {
            attributes = map;
        } else {
            attributes = new HashMap<String, String>();
            setCreateTime((new Date()).getTime());
        }
        redisContext.destroy();
    }

    /**
     * 添加属性
     * @param key
     * @param value
     */
    public void setAttribute(String key, String value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("key or value can't be null");
        }

        attributes.put(key, value);
    }

    /**
     * 设置属性
     * @param key
     * @return
     */
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 设置创建时间
     * @param time
     */
    public void setCreateTime(long time) {
        setAttribute("_create_time", String.valueOf(time));
    }

    /**
     * 获取创建时间
     * @return
     */
    public long getCreateTime() {
        String str = getAttribute("_create_time");
        return Long.parseLong(str);
    }

    /**
     * 删除属性
     * @param key
     */
    public void removeAttribute(String key) {
        if(key == null) {
            throw new IllegalArgumentException("key can't be null");
        }

        attributes.remove(key);
    }

    public void save() {
    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
        redisContext.setMessage(id, attributes, timeout);
        redisContext.destroy();
    }

    public void delete() {
    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
        redisContext.delMessage(id);
        redisContext.destroy();
    } 
}
