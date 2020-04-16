package cn.hand.tech.ui.weight.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by wcf on 2018-05-03.
 */

public class CompanyResultBean implements Serializable {
    private String token;
    private String message;
    private List<CompanyBean> result;

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

    public List<CompanyBean> getResult() {
        return result;
    }

    public void setResult(List<CompanyBean> result) {
        this.result = result;
    }
}
