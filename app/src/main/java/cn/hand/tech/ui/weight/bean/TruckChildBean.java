package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by wcf on 2018-03-08.
 */

public class TruckChildBean implements Serializable {
    private String name;
    private String sign;
    private String childId;
    private boolean isSelectedOrChrend;
    private String  parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isSelectedOrChrend() {
        return isSelectedOrChrend;
    }

    public void setSelectedOrChrend(boolean selectedOrChrend) {
        isSelectedOrChrend = selectedOrChrend;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
