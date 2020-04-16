package cn.hand.tech.ble.bean;

import java.io.Serializable;

public class BleDevice implements Serializable{
	private static final long serialVersionUID = 124234234234L;
	private String macAddress;
	private String deviceName;
	private int rssi;
	private String state;
	private String model;
	private String realName;
	private boolean isHave;

	public BleDevice() {
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BleDevice(String macAddress, String deviceName, int rssi) {
		this.macAddress = macAddress;
		this.deviceName = deviceName;
		this.rssi = rssi;
	}

	public boolean isHave() {
		return isHave;
	}

	public void setHave(boolean have) {
		isHave = have;
	}

	public String getMacAddress() {
		return this.macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getRssi() {
		return this.rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((macAddress == null) ? 0 : macAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BleDevice other = (BleDevice) obj;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{\"macAddress\":\"" + macAddress + "\", \"deviceName\":\""
				+ deviceName + "\", \"rssi\":\"" + rssi + "\", \"state\":\"" + state + "\"}";
	}

	
	
}
