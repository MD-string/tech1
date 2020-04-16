package cn.hand.tech.ble.bean;

import java.io.Serializable;

public class GPSTimebean implements Serializable{
	private static final long serialVersionUID = 1242337774234L;
	private String weightTime;
	private String gpsTime;

	public String getweightTime() {
		return weightTime;
	}

	public void setweightTime(String weightTime) {
		this.weightTime = weightTime;
	}

	public String getgpsTime() {
		return gpsTime;
	}

	public void setgpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}
}
