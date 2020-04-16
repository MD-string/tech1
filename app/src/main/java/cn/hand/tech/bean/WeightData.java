package cn.hand.tech.bean;

import java.io.Serializable;

/**
 * Created by wcf on 2018-04-4.
 */

public class WeightData implements Serializable {

    /**
     * deviceId : 101
     * carNumber : 粤B0000
     * weightFromDevice : 0.01
     * weightFromReal : 0.02
     * handBrakeHardwareStatus : 0
     * sensorStatus : 0
     * handBrakeVoltage : 0
     * uploadDate : 2018-03-13 10:10:11
     * packageNum : 1
     * gpsId : 101
     * speed : 0.02
     * x : 119.3375
     * y : 25.8697998
     * gpsUploadDate : 2018-03-13 10:10:10
     * stableStatus : 0
     * stableValue : 0
     * deviceVoltage : 0
     * binVersion : 0
     * deviceHardwareStatus : 0
     * deviceSoftwareStatus : 0
     * deviceWireStatus : 0
     * shipmentStatus : 0
     * ach1 : 1
     * ach2 : 2
     * ach3 : 3
     * ach4 : 4
     * ach5 : 5
     * ach6 : 6
     * ach7 : 7
     * ach8 : 8
     * ach9 : 9
     * ach10 : 10
     * ach11 : 11
     * ach12 : 12
     * ach13 : 13
     * ach14 : 14
     * ach15 : 15
     * ach16 : 16
     * sch1 : 17
     * sch2 : 18
     * sch3 : 19
     * sch4 : 20
     * sch5 : 21
     * sch6 : 22
     * sch7 : 23
     * sch8 : 24
     * sch9 : 25
     * sch10 : 26
     * sch11 : 27
     * sch12 : 28
     * sch13 : 29
     * sch14 : 30
     * sch15 : 31
     * sch16 : 32

     */

    private String uploadDate;//首页保存时的时间对应time
    private String deviceId;//设备ID
    private String carNumber;//车牌号
    private String weightFromDevice;////设备采集的重量数据
    private String weightFromReal;//保存手动输入的重量
    private String handBrakeHardwareStatus;//手刹手刹状态
    private String sensorStatus;
    private String handBrakeVoltage;
    private String packageNum;//流水号
    private String gpsId;
    private String speed;//速度
    private String x;//经度
    private String y;//纬度
    private String gpsUploadDate;//GPS数据采集时间
    private String stableStatus;//测量状态
    private String stableValue;
    private String deviceVoltage;//设备电压电压
    private String binVersion;
    private String deviceHardwareStatus;
    private String deviceSoftwareStatus;
    private String deviceWireStatus;
    private String shipmentStatus;//装载货物状态
    private String ach1;
    private String ach2;
    private String ach3;
    private String ach4;
    private String ach5;
    private String ach6;
    private String ach7;
    private String ach8;
    private String ach9;
    private String ach10;
    private String ach11;
    private String ach12;
    private String ach13;
    private String ach14;
    private String ach15;
    private String ach16;
    private String sch1;
    private String sch2;
    private String sch3;
    private String sch4;
    private String sch5;
    private String sch6;
    private String sch7;
    private String sch8;
    private String sch9;
    private String sch10;
    private String sch11;
    private String sch12;
    private String sch13;
    private String sch14;
    private String sch15;
    private String sch16;



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

    public String getWeightFromDevice() {
        return weightFromDevice;
    }

    public void setWeightFromDevice(String weightFromDevice) {
        this.weightFromDevice = weightFromDevice;
    }

    public String getWeightFromReal() {
        return weightFromReal;
    }

    public void setWeightFromReal(String weightFromReal) {
        this.weightFromReal = weightFromReal;
    }

    public String getHandBrakeHardwareStatus() {
        return handBrakeHardwareStatus;
    }

    public void setHandBrakeHardwareStatus(String handBrakeHardwareStatus) {
        this.handBrakeHardwareStatus = handBrakeHardwareStatus;
    }

    public String getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public String getHandBrakeVoltage() {
        return handBrakeVoltage;
    }

    public void setHandBrakeVoltage(String handBrakeVoltage) {
        this.handBrakeVoltage = handBrakeVoltage;
    }

    public String getAch1() {
        return ach1;
    }

    public void setAch1(String ach1) {
        this.ach1 = ach1;
    }

    public String getAch2() {
        return ach2;
    }

    public void setAch2(String ach2) {
        this.ach2 = ach2;
    }

    public String getAch3() {
        return ach3;
    }

    public void setAch3(String ach3) {
        this.ach3 = ach3;
    }

    public String getAch4() {
        return ach4;
    }

    public void setAch4(String ach4) {
        this.ach4 = ach4;
    }

    public String getAch5() {
        return ach5;
    }

    public void setAch5(String ach5) {
        this.ach5 = ach5;
    }

    public String getAch6() {
        return ach6;
    }

    public void setAch6(String ach6) {
        this.ach6 = ach6;
    }

    public String getAch7() {
        return ach7;
    }

    public void setAch7(String ach7) {
        this.ach7 = ach7;
    }

    public String getAch8() {
        return ach8;
    }

    public void setAch8(String ach8) {
        this.ach8 = ach8;
    }

    public String getAch9() {
        return ach9;
    }

    public void setAch9(String ach9) {
        this.ach9 = ach9;
    }

    public String getAch10() {
        return ach10;
    }

    public void setAch10(String ach10) {
        this.ach10 = ach10;
    }

    public String getAch11() {
        return ach11;
    }

    public void setAch11(String ach11) {
        this.ach11 = ach11;
    }

    public String getAch12() {
        return ach12;
    }

    public void setAch12(String ach12) {
        this.ach12 = ach12;
    }

    public String getAch13() {
        return ach13;
    }

    public void setAch13(String ach13) {
        this.ach13 = ach13;
    }

    public String getAch14() {
        return ach14;
    }

    public void setAch14(String ach14) {
        this.ach14 = ach14;
    }

    public String getAch15() {
        return ach15;
    }

    public void setAch15(String ach15) {
        this.ach15 = ach15;
    }

    public String getAch16() {
        return ach16;
    }

    public void setAch16(String ach16) {
        this.ach16 = ach16;
    }

    public String getSch1() {
        return sch1;
    }

    public void setSch1(String sch1) {
        this.sch1 = sch1;
    }

    public String getSch2() {
        return sch2;
    }

    public void setSch2(String sch2) {
        this.sch2 = sch2;
    }

    public String getSch3() {
        return sch3;
    }

    public void setSch3(String sch3) {
        this.sch3 = sch3;
    }

    public String getSch4() {
        return sch4;
    }

    public void setSch4(String sch4) {
        this.sch4 = sch4;
    }

    public String getSch5() {
        return sch5;
    }

    public void setSch5(String sch5) {
        this.sch5 = sch5;
    }

    public String getSch6() {
        return sch6;
    }

    public void setSch6(String sch6) {
        this.sch6 = sch6;
    }

    public String getSch7() {
        return sch7;
    }

    public void setSch7(String sch7) {
        this.sch7 = sch7;
    }

    public String getSch8() {
        return sch8;
    }

    public void setSch8(String sch8) {
        this.sch8 = sch8;
    }

    public String getSch9() {
        return sch9;
    }

    public void setSch9(String sch9) {
        this.sch9 = sch9;
    }

    public String getSch10() {
        return sch10;
    }

    public void setSch10(String sch10) {
        this.sch10 = sch10;
    }

    public String getSch11() {
        return sch11;
    }

    public void setSch11(String sch11) {
        this.sch11 = sch11;
    }

    public String getSch12() {
        return sch12;
    }

    public void setSch12(String sch12) {
        this.sch12 = sch12;
    }

    public String getSch13() {
        return sch13;
    }

    public void setSch13(String sch13) {
        this.sch13 = sch13;
    }

    public String getSch14() {
        return sch14;
    }

    public void setSch14(String sch14) {
        this.sch14 = sch14;
    }

    public String getSch15() {
        return sch15;
    }

    public void setSch15(String sch15) {
        this.sch15 = sch15;
    }

    public String getSch16() {
        return sch16;
    }

    public void setSch16(String sch16) {
        this.sch16 = sch16;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum;
    }

    public String getGpsId() {
        return gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getGpsUploadDate() {
        return gpsUploadDate;
    }

    public void setGpsUploadDate(String gpsUploadDate) {
        this.gpsUploadDate = gpsUploadDate;
    }

    public String getStableStatus() {
        return stableStatus;
    }

    public void setStableStatus(String stableStatus) {
        this.stableStatus = stableStatus;
    }

    public String getStableValue() {
        return stableValue;
    }

    public void setStableValue(String stableValue) {
        this.stableValue = stableValue;
    }

    public String getDeviceVoltage() {
        return deviceVoltage;
    }

    public void setDeviceVoltage(String deviceVoltage) {
        this.deviceVoltage = deviceVoltage;
    }

    public String getBinVersion() {
        return binVersion;
    }

    public void setBinVersion(String binVersion) {
        this.binVersion = binVersion;
    }

    public String getDeviceHardwareStatus() {
        return deviceHardwareStatus;
    }

    public void setDeviceHardwareStatus(String deviceHardwareStatus) {
        this.deviceHardwareStatus = deviceHardwareStatus;
    }

    public String getDeviceSoftwareStatus() {
        return deviceSoftwareStatus;
    }

    public void setDeviceSoftwareStatus(String deviceSoftwareStatus) {
        this.deviceSoftwareStatus = deviceSoftwareStatus;
    }

    public String getDeviceWireStatus() {
        return deviceWireStatus;
    }

    public void setDeviceWireStatus(String deviceWireStatus) {
        this.deviceWireStatus = deviceWireStatus;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }


}
