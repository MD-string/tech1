package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class CarNumberInfo implements Serializable {

    private static final long serialVersionUID =1333784443344053445L;
    private String bindCarId;
    private String carAxleAmount;
    private String carNumber;
    private String carType;
    private String companyId;
    private String deviceId;
    private String id;
    private String inputDate;
    private String parentName;
    private String phone;
    private String driverName;
    private String hwVersion; //硬件版本号
    /**
     *设备运行状态0：停止运行，1：正在运行，2：正在升级，3：升级失败
     */
    private String runStatus;

    /**
     *程序运行版本号
     */
    private String version;
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBindCarId() {
        return bindCarId;
    }

    public void setBindCarId(String bindCarId) {
        this.bindCarId = bindCarId;
    }

    public String getCarAxleAmount() {
        return carAxleAmount;
    }

    public void setCarAxleAmount(String carAxleAmount) {
        this.carAxleAmount = carAxleAmount;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
