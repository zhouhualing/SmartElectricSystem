package com.harmazing.server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.TextFormat;
import com.harmazing.Config;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Gateway;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.entity.ZigbeeOOElectricityMeter;
import com.harmazing.protobuf.DebugProtos.Connect;
import com.harmazing.protobuf.DebugProtos.Debug;
import com.harmazing.protobuf.DebugProtos.Disconnect;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.protobuf.LoginProtos;
import com.harmazing.protobuf.LoginProtos.Login;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.protobuf.MessageProtos.SpmMessage;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.util.AttributeKeyUtil;
import com.harmazing.util.MessageUtil;

public class AuthenticateHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticateHandler.class);
    private final static AttributeKey<Session> sessionAttributeKey = AttributeKeyUtil.getSessionAttributeKey();
    volatile ScheduledFuture<?> authTimeout;
    private final long authTimeNanos = TimeUnit.SECONDS.toNanos(60);
    public final static Config CONFIG = Config.getInstance();

    private static Map<String, Integer> gatewayRefMap = new HashMap<String, Integer>();
    
    /////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {
			super.channelActive(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    ///////////////////////////////////////////////////////////////////////////
    private void initialize(ChannelHandlerContext ctx) {
        EventExecutor loop = ctx.executor();
        if (authTimeNanos > 0) {
            authTimeout = loop.schedule( new AuthTimeoutTask(ctx), authTimeNanos, TimeUnit.NANOSECONDS);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	Session session = this.getSession(ctx);    	
    	if(session != null){
    		String gw_mac = (String) session.getAttribute("gw_mac");
    		if(gw_mac.contains("debug_")){
                String type = (String)session.getAttribute("type");
                SpmMessage message = MessageUtil.buildDebugDisconnectMsg(type);
                ActiveChannelGroup.forwardDebugMessage(gw_mac.replace("debug_", ""),  message);  
                
                ReferenceCountUtil.release(message);	    		
    		}else{
    			subtractGatewayRef(gw_mac);
	    		LOGGER.debug("AuthenticateHandler.channelInactive: Close channel wiht session id = " + 
	    		             session.getId() + ", gw_mac=" + session.getAttribute("gw_mac") + 
	    		             ", gw_ref = " + gatewayRefMap.get(gw_mac));
    		}
        }
    	    	
    	try{         
	        closeChannel(ctx);  	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    /////////////////////////////////////////////////////////////////
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtos.SpmMessage message = (MessageProtos.SpmMessage) msg;
        LOGGER.info("Message: \n" + TextFormat.printToString(message));

        MessageProtos.SpmMessage.Header header = message.getHeader();
        
        if (header.getType() == MessageProtos.SpmMessage.MsgType.LOGIN) {
            if (message.hasLogin()) {
                handleLogin(ctx, message);
            }
            ReferenceCountUtil.release(msg);
        } else if( header.getType() == MessageProtos.SpmMessage.MsgType.DEBUG){
        	if(message.hasDebug()){
        		Debug debug = message.getDebug();
        		if( debug.hasConnect()){
        			Connect connect = debug.getConnect();
        			String gw_mac = connect.getMac();        			
        			String type = connect.getType();		
        			
        			Session s = new SocketSession();
            		s.setMaxInactiveInterval(600000);
            		s.setAttribute("gw_mac", "debug_" + gw_mac);
            		s.setAttribute("type", type);
                    s.setAttribute("seqnum", message.getHeader().getSeqnum());
                    s.refresh();
                    authenticateSuccess(ctx, s);
                    
                    ActiveChannelGroup.forwardDebugMessage(gw_mac,  message);  
                    ReferenceCountUtil.release(message);
                    
        		}else if( debug.hasText()){
        			Session session = ctx.channel().attr(sessionAttributeKey).get();
                    String gw_mac = session.getAttribute("gw_mac").toString();
                    if( gw_mac.contains("debug_")){
                    	gw_mac = gw_mac.replaceAll("debug_", "");
                    	
                    }else {
                    	gw_mac = "debug_" + gw_mac;
                    }
                    
                    ActiveChannelGroup.forwardDebugMessage(gw_mac,  message); 
                    ReferenceCountUtil.release(message);
        		}
        	}
        	
        }else {
        	Session session = this.getSession(ctx);
            String sessionId = header.getSession();
            
            if(session == null || sessionId == null || sessionId.isEmpty() || !sessionId.equals(session.getId())){
            	 LOGGER.info(" Invalid Session id in message, do nothong.");
                 ReferenceCountUtil.release(msg);
                 return;
            }
            
            /*
            if(session.validate()){
            	LOGGER.info("Session timeout, release the session.");
            	ReferenceCountUtil.release(msg);
            	closeChannel(ctx);
            	return;
            }
            */
            
            session.refresh();
            ctx.fireChannelRead(msg);
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////
    private void closeChannel(ChannelHandlerContext ctx){
    	ActiveChannelGroup.removeChannel(ctx.channel());
        if(ctx.channel().isOpen()){
        	ctx.channel().close();
        	LOGGER.debug("***AuthenticateHandler.closeChannel.");
        }
        try {
			super.channelInactive(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    ////////////////////////////////////////////////////////////////////////////
    // 1. Login will create session between gateway-netty
    // 2. Login will create new gateway record in database if not exist.
	private void handleLogin(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		LOGGER.debug("Enter AuthenticateHandler.handleLogin");
		MessageProtos.SpmMessage.Header req_header = message.getHeader();
		if(!message.hasLogin()){
			LOGGER.error("AuthenticateHandler.handleLogin: no Login context, do nothing.");
			ctx.close();
			return;
		}
		Login login_msg = message.getLogin();
		String gw_mac    = login_msg.getGatewayMac();
		String sn        = login_msg.getGatewaySerialNumber();
		String software  = login_msg.getSoftwareVersion();
		String hardware  = login_msg.getHardwareVersion();
		String vendor    = login_msg.getManufacturerName();
		String model     = login_msg.getModelName();
		
		String ip_address = ctx.channel().remoteAddress().toString();
		ip_address = ip_address.substring(1, ip_address.lastIndexOf(":"));
		
		// Find/Create the gateway 
		int ret_code = 0;
		DeviceService devSrv = new DeviceServiceImpl();
        Gateway gw = devSrv.getGWByGWMac(gw_mac);
        if( gw == null){
        	LOGGER.info("AuthenticateHandler.handleLogin: no gateway(" + gw_mac + "), create a new one.");
			ret_code = devSrv.createGateway(UUID.randomUUID().toString(), 
					              sn, 
					              gw_mac, 
					              software, 
					              hardware, 
					              model, 
					              vendor, 
					              DeviceSpecific.DeviceType.INNOLINKS_GATEWAY_VALUE,  // INNOLINKS_GATEWAY
					              1,  // onOff=1 means ON status
					              1,
					              ip_address); // operStatus=1 means ON status
        }
        
    	devSrv.updateGWOprAndVersionStatusByMac(gw_mac, 1, 1, software, hardware, ip_address);
    	LOGGER.info("AuthenticateHandler.handleLogin: found gateway(" + gw_mac + "), update onOff and operStatus to 1.");
    	/*
        else if(gw.getOnOff() == 0 || gw.getOperStatus() == 0){
        	LOGGER.info("AuthenticateHandler.handleLogin: found gateway(" + gw_mac + "), update onOff and operStatus to 1.");
        	devSrv.updateGWOprStatusByMac(gw_mac, 1, 1, ip_address);
        }*/
		
      
		// Create Session
		Session s = new SocketSession();
		s.setMaxInactiveInterval(600000);
		s.setAttribute("gw_mac", gw_mac);
        s.setAttribute("seqnum", req_header.getSeqnum());
        s.refresh();         
        		
		MessageProtos.SpmMessage.Builder rsp_msg = MessageProtos.SpmMessage.newBuilder();
		MessageProtos.SpmMessage.Header.Builder rsp_header = MessageProtos.SpmMessage.Header.newBuilder();
		rsp_header.setSeqnum( req_header.getSeqnum());
		rsp_header.setType(MessageProtos.SpmMessage.MsgType.LOGIN_RESPONSE);
		rsp_header.setSession(s.getId());
		rsp_msg.setHeader( rsp_header);
		
		LoginProtos.LoginResponse.Builder rsp_builder = MessageUtil.createLoginResonseBuilder(message, ret_code, ret_code==0?"" : "create GW failed.");		
		rsp_msg.setLoginresponse(rsp_builder);		
        authenticateSuccess(ctx, s);        	        
        ctx.writeAndFlush(rsp_msg.build());
        ReferenceCountUtil.release(message);
     
        addGatewayRef(gw_mac);
    }

	//////////////////////////////////////////////////////////////////////
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("READER_IDLE timeout.");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                LOGGER.info("WRITER_IDLE timeout.");
            } else if (event.state() == IdleState.ALL_IDLE) {
                LOGGER.info("ALL_IDLE timeout");
            }
            return;
        }
    }
   
    //////////////////////////////////////////////////////////////////
    private void authenticateSuccess(ChannelHandlerContext ctx, Session session) {
        ctx.channel().attr(sessionAttributeKey).set(session);
        ActiveChannelGroup.addChannel(ctx.channel());
        
        LOGGER.debug("Session created succesfully.");
    }

    ////////////////////////////////////////////////////////////////
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	LOGGER.error("AuthenticateHandler.exceptionCaught with cause:",cause);
    	
    	cause.printStackTrace();        
        ctx.close();
    }

    /////////////////////////////////////////////////////////////////
    private final class AuthTimeoutTask implements Runnable {

        private final ChannelHandlerContext ctx;

        AuthTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            LOGGER.info("AUTHTIMEOUTTASK");
            if (!ctx.channel().isOpen()) {
                return;
            }
            try{
	            Session s = ctx.channel().attr(sessionAttributeKey).get();
	            if (s == null || s.validate()) {
	                LOGGER.info("CHANNEL SESSION TIMEOUT!");
					specialHandle(ctx);
	                ctx.channel().close();
	                return;
	            }
            }catch(Exception e){
            	e.printStackTrace();
            }
            ctx.executor().schedule(this, authTimeNanos, TimeUnit.NANOSECONDS);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    public Session getSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
    }
      
    ////////////////////////////////////////////////////////////
    public void specialHandle(ChannelHandlerContext ctx) {
    	//TODO:        
    }
    
    
	/////////////////////////////////////////////////////////////////////
	public void UpdateDeviceOffline( String gw_mac) {
		DeviceService dev_srv = new DeviceServiceImpl();
		List<AirCondition> acs = dev_srv.getAcsByGwMac(gw_mac);
		Iterator ac_it = acs.iterator();
		while(ac_it.hasNext()){
			AirCondition ac = (AirCondition)ac_it.next();
			dev_srv.updateACIntField(ac.getMac(), "operStatus", 0);
		}
	
		List<ZigbeeOO> oos = dev_srv.getOOsByGwMac(gw_mac);
		Iterator oo_it = oos.iterator();
		while( oo_it.hasNext()){
			ZigbeeOO oo = (ZigbeeOO)oo_it.next();
			dev_srv.updateOODevIntField(oo.getMac(), "operStatus", 0);
		}
		
		List<ZigbeeOOElectricityMeter> oes = dev_srv.getElecMetersByGwMac(gw_mac);
		Iterator oe_it = oes.iterator();
		while( oe_it.hasNext()){
			ZigbeeOOElectricityMeter oe = (ZigbeeOOElectricityMeter)oe_it.next();
			dev_srv.updateElecMaterIntField(oe.getMac(), "operStatus", 0);
		}
	
		dev_srv.updateGWOprStatusByMac(gw_mac, 0, 0, "");
	}
	
	/////////////////////////////////////////////////////////////////////
	private void addGatewayRef(String gw_mac){
		int value = 1;
		if( gatewayRefMap.containsKey(gw_mac)){
			value = gatewayRefMap.get(gw_mac);
			value ++;
			gatewayRefMap.put(gw_mac, value);
		}else{
			gatewayRefMap.put(gw_mac, value);
		}		
	}
	
	///////////////////////////////////////////////////////////////////
	private void subtractGatewayRef(String gw_mac){
		
		int value = 0;
		if( gatewayRefMap.containsKey(gw_mac)){
			value = gatewayRefMap.get(gw_mac);
			value --;
			if( value <= 0){
				gatewayRefMap.remove(gw_mac);
				
				UpdateDeviceOffline(gw_mac);
			}else{
				gatewayRefMap.put(gw_mac, value);
			}
		}
	}
}


