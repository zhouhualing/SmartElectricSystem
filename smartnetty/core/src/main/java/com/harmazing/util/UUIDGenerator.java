package com.harmazing.util;

import java.util.UUID;

/**
 * Created by ming on 14-9-9.
 */
public class UUIDGenerator {
    public final static String randomUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}

