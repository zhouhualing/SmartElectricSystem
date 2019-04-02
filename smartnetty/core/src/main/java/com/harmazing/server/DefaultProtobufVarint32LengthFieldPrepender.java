package com.harmazing.server;

import com.google.protobuf.CodedOutputStream;
import com.harmazing.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by ming on 14-9-4.
 */
public class DefaultProtobufVarint32LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLen = msg.readableBytes();
        int headerLen = 4;
        out.ensureWritable(headerLen + bodyLen);

        CodedOutputStream headerOut =
                CodedOutputStream.newInstance(new ByteBufOutputStream(out), headerLen);
        byte[] bytes = new byte[4];
        ByteUtil.putInt(bytes, bodyLen, 0);
        headerOut.writeRawBytes(bytes);
        headerOut.flush();

        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }
}
