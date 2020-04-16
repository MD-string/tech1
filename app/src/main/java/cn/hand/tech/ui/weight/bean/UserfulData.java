package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class UserfulData implements Serializable {
    private static final long serialVersionUID = -6107233410789875682L;
    private String parentCode;
    private List<UserfulTwoData> v1;

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<UserfulTwoData> getV1() {
        return v1;
    }

    public void setV1(List<UserfulTwoData> v1) {
        this.v1 = v1;
    }
}
