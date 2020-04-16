package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;

/**
 * Created by wcf on 2018-03-08.
 * Describe:车辆在线实体对象
 */

public class OnLineTruckBean implements Serializable {

    /**
     * deviceId : 45578
     * carNumber : 苏AH1991
     * speed : 0
     * weight : 12.9179459
     * x : 118.9107217
     * y : 32.120505
     */

    private String deviceId;
    private String carNumber;
    private String speed;
    private String weight;
    private String x;
    private String y;
    private String carUploadDate;
    private String gpsUploadDate;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }



    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCarUploadDate() {
        return carUploadDate;
    }

    public void setCarUploadDate(String carUploadDate) {
        this.carUploadDate = carUploadDate;
    }

    public String getGpsUploadDate() {
        return gpsUploadDate;
    }

    public void setGpsUploadDate(String gpsUploadDate) {
        this.gpsUploadDate = gpsUploadDate;
    }
}
