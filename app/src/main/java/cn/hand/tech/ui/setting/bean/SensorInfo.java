package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class SensorInfo implements Serializable {

    private static final long serialVersionUID =1333784443344053445L;
    private String deviceId;
    private String carNumber;
    private String damageNum;//损坏通道数
    private List<Integer> damageChannels; //传感器通道状态
    /**
     *传感器安装通道
     */
    private List<String>  sensorChannel;
    /**
     *传感器状态：0：传感器好，1：传感器坏，2：不检测
     */
    private List<String> sensorStatus;
    /**
     *离线状态：0：默认，1：离线
     */
    private String offline;
    /**
     *线材损坏标识：0：无，1：损坏
     */
    private String lineBad;
    /**
     *订单记录数，离线24小时才统计磅单数
     */
    private Integer orderNum;
    private String offlineTime;
    private String updateTime;

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

    public String getDamageNum() {
        return damageNum;
    }

    public void setDamageNum(String damageNum) {
        this.damageNum = damageNum;
    }

    public List<Integer> getDamageChannels() {
        return damageChannels;
    }

    public void setDamageChannels(List<Integer> damageChannels) {
        this.damageChannels = damageChannels;
    }

    public List<String> getSensorChannel() {
        return sensorChannel;
    }

    public void setSensorChannel(List<String> sensorChannel) {
        this.sensorChannel = sensorChannel;
    }

    public List<String> getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(List<String> sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public String getOffline() {
        return offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

    public String getLineBad() {
        return lineBad;
    }

    public void setLineBad(String lineBad) {
        this.lineBad = lineBad;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
