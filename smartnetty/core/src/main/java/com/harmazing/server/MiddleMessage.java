package com.harmazing.server;

import com.harmazing.protobuf.MessageProtos;

/**
 * Created by ming on 14/9/29.
 */
public class MiddleMessage {
    public String channel;
    public MessageProtos.SpmMessage spmMessage;

    public MiddleMessage(MessageProtos.SpmMessage spmMessage, String channel) {
        this.spmMessage = spmMessage;
        this.channel = channel;
    }
}
