package cn.hand.tech.ui.setting.bean;


import java.util.List;

/**
 * Created by hand-hitech2 on 2018-03-08.
 */

public class OnLineTruckListBean {
    private String token;
    private String message;
    private List<OnLineTruckBean> result;

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

    public List<OnLineTruckBean> getResult() {
        return result;
    }

    public void setResult(List<OnLineTruckBean> result) {
        this.result = result;
    }
}

