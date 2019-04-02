package com.harmazing.server;

import com.harmazing.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Created by ming on 14-9-4.
 */
public class DefaultProtobufVarint32FrameDecoder extends ByteToMessageDecoder {

    // TODO maxFrameLength + safe skip + fail-fast option
    //      (just like LengthFieldBasedFrameDecoder)

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        if(in.readableBytes() < 4) {
            return;
        }

        final byte[] buf = new byte[4];

        for(int i=0; i<buf.length; i++) {
            buf[i] = in.readByte();
        }

        int length = ByteUtil.getInt(buf, 0);

        if(length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }

        if(in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        } else {
            out.add(in.readBytes(length));
            return;
        }
    }
}
