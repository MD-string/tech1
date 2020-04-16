package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class A1 implements Serializable {

    private static final long serialVersionUID =13332444374053445L;
    private String str;
    private boolean isCheck;


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
