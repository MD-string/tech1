package cn.hand.tech.ui.data.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class EmailModel implements Serializable {
    private String id;
    private String emailAddr;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
