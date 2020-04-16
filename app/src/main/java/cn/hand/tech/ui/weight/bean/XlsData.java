package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class XlsData implements Serializable {
    private static final long serialVersionUID = -6107233410789875682L;
    private String name;
    private String v1;
    private String v2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }
}
