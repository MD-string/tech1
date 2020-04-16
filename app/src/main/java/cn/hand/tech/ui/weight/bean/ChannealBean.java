package cn.hand.tech.ui.weight.bean;

public class ChannealBean {
    private static final long serialVersionUID =133345553445L;
    private  String  passName;//通道名
    private  String  passValue;//通道值
    private  String  mvv;  // mv/v
    private  String  mvValue;
    private String   mvStr;

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getPassValue() {
        return passValue;
    }

    public void setPassValue(String passValue) {
        this.passValue = passValue;
    }

    public String getMvv() {
        return mvv;
    }

    public void setMvv(String mvv) {
        this.mvv = mvv;
    }

    public String getMvValue() {
        return mvValue;
    }

    public void setMvValue(String mvValue) {
        this.mvValue = mvValue;
    }

    public String getMvStr() {
        return mvStr;
    }

    public void setMvStr(String mvStr) {
        this.mvStr = mvStr;
    }
}
