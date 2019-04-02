package com.harmazing.util;

import com.harmazing.server.Session;
import io.netty.util.AttributeKey;

/**
 * Created by ming on 14-9-19.
 */
public class AttributeKeyUtil {

    public final static AttributeKey<Session> SESSION_ATTRIBUTE_KEY = new AttributeKey<Session>("session");

    public final synchronized static AttributeKey<Session> getSessionAttributeKey() {
        return SESSION_ATTRIBUTE_KEY;
    }
}
