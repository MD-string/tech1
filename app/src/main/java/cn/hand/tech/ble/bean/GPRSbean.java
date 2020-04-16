package cn.hand.tech.ble.bean;

import java.io.Serializable;

public class GPRSbean implements Serializable{
	private static final long serialVersionUID = 12427774234L;
	private String rssi;
	private String netStatus;

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}
}
