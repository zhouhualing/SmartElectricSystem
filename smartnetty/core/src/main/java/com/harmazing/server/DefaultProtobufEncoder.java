package com.harmazing.server;

import com.google.protobuf.MessageLite;
import com.google.protobuf.TextFormat;
import com.harmazing.mq.Command;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.util.AttributeKeyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * Created by ming on 14-9-20.
 */
public class DefaultProtobufEncoder extends MessageToMessageEncoder<Object> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultProtobufEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        String sessionId="";
        Session session = null;
        try {
            session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
            if(session!=null){
            	sessionId = session.getId();
            }
        } catch (Exception e) {
            LOGGER.error("Session Error ", e);
            sessionId = "";
        }
        //LOGGER.debug("Message:\n" + TextFormat.printToString((com.google.protobuf.MessageOrBuilder) msg));
        
        try {
            if(msg instanceof MiddleMessage) {
                MiddleMessage middleMessage = (MiddleMessage) msg;
                MessageProtos.SpmMessage message = middleMessage.spmMessage;
                MessageProtos.SpmMessage.Builder messageBuilder = message.toBuilder();
                MessageProtos.SpmMessage.Header.Builder headerBuilder = messageBuilder.getHeaderBuilder();
                
                messageBuilder.setHeader(headerBuilder.setSession(sessionId));
                msg = messageBuilder.build();     
            }             	
        } catch (Exception e) {
            LOGGER.error("MiddleMessage encode error", e);
            return;
        }
        

        if (msg instanceof MessageLite) {
        	LOGGER.info("Message;\n" + TextFormat.printToString((com.google.protobuf.MessageOrBuilder) msg));
            out.add(wrappedBuffer(((MessageLite) msg).toByteArray()));
            return;
        }
        if (msg instanceof MessageLite.Builder) {
        	LOGGER.info("Message;\n" + TextFormat.printToString((com.google.protobuf.MessageOrBuilder) msg));
            out.add(wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray()));
        }
    }
}
