package cn.hand.tech.ble.bean;

import java.io.Serializable;

public class GPSbean implements Serializable{
	private static final long serialVersionUID = 12427774234L;
	private String starsNumber;
	private String airWireStatus;
	private String rssi;
	private String locationStatus;
	private String gpsTime;
	private String weightTime;

	public String getStarsNumber() {
		return starsNumber;
	}

	public void setStarsNumber(String starsNumber) {
		this.starsNumber = starsNumber;
	}

	public String getAirWireStatus() {
		return airWireStatus;
	}

	public void setAirWireStatus(String airWireStatus) {
		this.airWireStatus = airWireStatus;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}

	public String getweightTime() {
		return weightTime;
	}

	public void setweightTime(String weightTime) {
		this.weightTime = weightTime;
	}
}
