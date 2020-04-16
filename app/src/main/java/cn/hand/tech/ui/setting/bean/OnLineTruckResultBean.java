package cn.hand.tech.ui.setting.bean;


/**
 * Created by wcf on 2018-05-03.
 */

public class OnLineTruckResultBean {
    private String token;
    private String message;
    private OnLineTruckBean result;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OnLineTruckBean getResult() {
        return result;
    }

    public void setResult(OnLineTruckBean result) {
        this.result = result;
    }
}
