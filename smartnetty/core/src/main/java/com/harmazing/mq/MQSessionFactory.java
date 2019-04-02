package com.harmazing.mq;

import javax.jms.Session;

import org.apache.commons.pool.PoolableObjectFactory;

public class MQSessionFactory implements PoolableObjectFactory {

	@Override
	public void activateObject(Object arg0) throws Exception {
		
		
	}
	@Override
	public void destroyObject(Object arg0) throws Exception {
		Session session=(Session)arg0;
		session.close();		
	}

	@Override
	public Object makeObject() throws Exception {
		Session session=MQUtil.getConn().createSession(false,Session.AUTO_ACKNOWLEDGE);
		return session;
	}

	@Override
	public void passivateObject(Object arg0) throws Exception {
		
		
	}
	@Override
	public boolean validateObject(Object arg0) {
		return true;
	}

}
