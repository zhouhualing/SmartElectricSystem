package com.harmazing.util;

import com.harmazing.protobuf.SensorProtos;

/**
 * Created by ming on 14-9-10.
 */
public class WindoorUtil {
    public final static class State {
        public final static boolean CLOSE   = false;
        public final static boolean OPEN    = true;
    }

    public final static boolean getStateValue(SensorProtos.WinDoorSensorSpecificInfo.WinDoorState winDoorState) {
        if(winDoorState == SensorProtos.WinDoorSensorSpecificInfo.WinDoorState.CLOSED) {
            return State.CLOSE;
        } else if(winDoorState == SensorProtos.WinDoorSensorSpecificInfo.WinDoorState.OPENED) {
            return State.OPEN;
        } else {
            return Boolean.TRUE;
        }
    }
}
