package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class RepairModel implements Serializable {

    private static final long serialVersionUID =133378444374053445L;
    public String carNumber;
    public String createTime;//录入时间
    public String creater;//录入用户
    public String deviceId;//设备id
    public List<String> faultPhenomenon;//故障现象
    public String faultTime;//故障时间
    public String faultTypeName;//故障类型
    public String id;
    public List<String> repairedGuide;//维修指导
    public String repairedStatus;//维修状态   0 待维修  1已维修
    public String driverPhone;//司机电话driverPhone
    public String driverName;//司机姓名
    public String lat;
    public String lon;
    public String distance;
    public String x;
    public String y;

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

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(String faultTime) {
        this.faultTime = faultTime;
    }

    public String getFaultTypeName() {
        return faultTypeName;
    }

    public void setFaultTypeName(String faultTypeName) {
        this.faultTypeName = faultTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepairedStatus() {
        return repairedStatus;
    }

    public List<String> getFaultPhenomenon() {
        return faultPhenomenon;
    }

    public void setFaultPhenomenon(List<String> faultPhenomenon) {
        this.faultPhenomenon = faultPhenomenon;
    }

    public List<String> getRepairedGuide() {
        return repairedGuide;
    }

    public void setRepairedGuide(List<String> repairedGuide) {
        this.repairedGuide = repairedGuide;
    }

    public void setRepairedStatus(String repairedStatus) {
        this.repairedStatus = repairedStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
