package com.harmazing.mq;

import org.codehaus.jackson.map.ObjectMapper;

import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ming on 14/9/29.
 */
public class Command extends Message {

    public final static class Status {
        public final static String NOT_SEND        = "NOT_SEND";
        public final static String WAIT_RESPONE    = "WAIT_RESPONE";
        public final static String SUCCESS         = "SUCCESS";
        public final static String FAIL            = "FAIL";
    }

    public Command(String id) {
        super(id);
    }

    public Command(String id, Map attributes) {
        super(id, attributes);
    }

    public Command(String sessionId, int seqnum) {
        super("MQ:COMMAND:" + sessionId + "-" + seqnum);
    }

    public Command(String sessionId, int seqnum, Map attributes) {
        super("MQ:COMMAND:" + sessionId + "-" + seqnum);
        this.attributes = attributes;
    }

    public void changeState(String status) {
        if(status == null) {
            throw new IllegalArgumentException("status can't be null!");
        }

        if(!status.equals(Status.NOT_SEND) &&
                !status.equals(Status.WAIT_RESPONE) &&
                !status.equals(Status.SUCCESS) &&
                !status.equals(Status.FAIL)) {
            throw new IllegalArgumentException("status is not correct!");
        }
        setAttribute("_status", status);
    }

    private void setChangeStatusTime(long time) {
        setAttribute("_change_status_time", String.valueOf(time));
    }

    public long getChangeStatusTime() {
        String str = getAttribute("_change_status_time");
        return Long.parseLong(str);
    }

    public String getStatus() {
        return getAttribute("_status");
    }

    public void complete() throws IOException {
        String channel = getAttribute("channel");
        Integer result = Integer.valueOf(getAttribute("result"));
        String session = getAttribute("session");
        Map message = new HashMap();
        message.put("result", result);
        message.put("session", session);
        ObjectMapper objectMapper = new ObjectMapper();
    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
        redisContext.publish("RESPONSE:" + channel, objectMapper.writeValueAsString(message));
        redisContext.destroy();
    }
}
