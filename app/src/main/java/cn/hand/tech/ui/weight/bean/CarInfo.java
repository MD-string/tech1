package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-06-06.
 * 设备
 */

public class CarInfo implements Serializable{

    public String carNumber;
    public String deviceId;
    public String uploadDate;
    public String mac;
    public String url;
    public String version;
    public boolean isNew;

    public String getCarNumber() {
        return carNumber;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
