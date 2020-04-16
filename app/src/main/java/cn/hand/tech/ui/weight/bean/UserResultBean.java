package cn.hand.tech.ui.weight.bean;


/**
 * Created by wcf on 2018-03-09.
 */

public class UserResultBean {
    private String token;
    private String message;
    private UserBean result;

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

    public UserBean getResult() {
        return result;
    }

    public void setResult(UserBean result) {
        this.result = result;
    }
}
