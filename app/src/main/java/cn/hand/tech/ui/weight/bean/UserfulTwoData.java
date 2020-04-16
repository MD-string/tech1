package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class UserfulTwoData implements Serializable {
    private static final long serialVersionUID = -6107232210789875682L;
    private String childCode;
    private String name;

    public String getChildCode() {
        return childCode;
    }

    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
