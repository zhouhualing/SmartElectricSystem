package com.harmazing.entity;

public class ZigbeeHALamp extends BasicDevice {
	private int illuminance = 0;
	private int red = 0;
	private int green = 0;
	private int blue = 0;
	
	
	public int getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(int illuminance) {
		this.illuminance = illuminance;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}

}
