package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class CarListModel implements Serializable {

    private static final long serialVersionUID =1333784443344053445L;
    private String carNumber;
    private List<RepairModel> children;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public List<RepairModel> getChildren() {
        return children;
    }

    public void setChildren(List<RepairModel> children) {
        this.children = children;
    }
}
