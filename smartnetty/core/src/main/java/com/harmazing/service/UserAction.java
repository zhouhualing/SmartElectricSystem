package com.harmazing.service;

public interface UserAction {
	////////////////////////////////////////////////////////////////
	public void record4Sensor(String table, String mac, int status);
	public void record4Ac( String table, String mac, int status, int on_off, int mode, int temp, int speed);
	public void record4Lamp( String table, String mac, int status, int on_off, int illuminance, int red, int green, int blue);
	public void record4HTSensor( String table, String mac, int status, int temp, int humidity);
}
