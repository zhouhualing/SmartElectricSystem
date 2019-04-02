package com.harmazing.server;

import com.harmazing.util.UUIDGenerator;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by ming on 14-9-2.
 */
public class SocketSession implements Session {

    private long creationTime;
    private String id;
    private long lastAccessedTime;
    private int maxInactiveInterval;
    private Hashtable attributes;
//    private boolean validate;

    SocketSession() {
        creationTime = System.nanoTime();
        id = UUIDGenerator.randomUUID();
        attributes = new Hashtable();
        lastAccessedTime = creationTime;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return attributes.keys();
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void refresh() {
        lastAccessedTime = System.nanoTime();
    }

    @Override
    public boolean validate() {
    	return false;
//        if(System.nanoTime() < lastAccessedTime + maxInactiveInterval) {
//            return true;
//        } else {
//            return false;
//        }
    }
}
