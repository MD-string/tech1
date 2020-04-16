package cn.hand.tech.bean;

import java.io.Serializable;

/**
 * 定位地理信息
 * @date 2016.12.5
 */
public class LocationInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String longitude = ""; //经度
	private String latitude = ""; //纬度
	private String locType = ""; //定位类型
	private String cityName = "深圳";//城市名称
	
	private boolean isLocationSuccess;
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLocType() {
		return locType;
	}
	public void setLocType(String locType) {
		this.locType = locType;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public boolean isLocationSuccess() {
		return isLocationSuccess;
	}
	public void setLocationSuccess(boolean isLocationSuccess) {
		this.isLocationSuccess = isLocationSuccess;
	}
	

}
