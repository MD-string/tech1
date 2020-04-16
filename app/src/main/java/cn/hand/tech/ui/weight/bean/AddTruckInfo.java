package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-06-06.
 * 录车
 */

public class AddTruckInfo implements Serializable{

    private static final long serialVersionUID =13337874053445L;
    public String id;   //ID
    public String truckNumber; //车牌
    public String company; //公司
    public String weight; //荷载量
    public String phone;//司机号码
    public String truckType; //车型
    public String alxesNumber; //轴数
    public String sensorNumber; //传感器数量
    public String sensorType;  //传感器类型
    public String polishInstaller1;  //打磨清洗
    public String polishInstaller2;  //打磨清洗
    public String patchInstaller1; //贴片
    public String patchInstaller2; //贴片

    public String sealantInstaller1; //封胶
    public String sealantInstaller2; //封胶

    public String threadingInstaller1;//穿线
    public String threadingInstaller2;//穿线

    public String hostInstaller1; //主机安装
    public String hostInstaller2; //主机安装

    public String startInstallTime;//开始安装时间
    public String  isOnline; //是否在线



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public String getAlxesNumber() {
        return alxesNumber;
    }

    public void setAlxesNumber(String alxesNumber) {
        this.alxesNumber = alxesNumber;
    }

    public String getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getPolishInstaller1() {
        return polishInstaller1;
    }

    public void setPolishInstaller1(String polishInstaller1) {
        this.polishInstaller1 = polishInstaller1;
    }

    public String getPolishInstaller2() {
        return polishInstaller2;
    }

    public void setPolishInstaller2(String polishInstaller2) {
        this.polishInstaller2 = polishInstaller2;
    }

    public String getPatchInstaller1() {
        return patchInstaller1;
    }

    public void setPatchInstaller1(String patchInstaller1) {
        this.patchInstaller1 = patchInstaller1;
    }

    public String getPatchInstaller2() {
        return patchInstaller2;
    }

    public void setPatchInstaller2(String patchInstaller2) {
        this.patchInstaller2 = patchInstaller2;
    }

    public String getSealantInstaller1() {
        return sealantInstaller1;
    }

    public void setSealantInstaller1(String sealantInstaller1) {
        this.sealantInstaller1 = sealantInstaller1;
    }

    public String getSealantInstaller2() {
        return sealantInstaller2;
    }

    public void setSealantInstaller2(String sealantInstaller2) {
        this.sealantInstaller2 = sealantInstaller2;
    }

    public String getThreadingInstaller1() {
        return threadingInstaller1;
    }

    public void setThreadingInstaller1(String threadingInstaller1) {
        this.threadingInstaller1 = threadingInstaller1;
    }

    public String getThreadingInstaller2() {
        return threadingInstaller2;
    }

    public void setThreadingInstaller2(String threadingInstaller2) {
        this.threadingInstaller2 = threadingInstaller2;
    }

    public String getHostInstaller1() {
        return hostInstaller1;
    }

    public void setHostInstaller1(String hostInstaller1) {
        this.hostInstaller1 = hostInstaller1;
    }

    public String getHostInstaller2() {
        return hostInstaller2;
    }

    public void setHostInstaller2(String hostInstaller2) {
        this.hostInstaller2 = hostInstaller2;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getStartInstallTime() {
        return startInstallTime;
    }

    public void setStartInstallTime(String startInstallTime) {
        this.startInstallTime = startInstallTime;
    }
}
