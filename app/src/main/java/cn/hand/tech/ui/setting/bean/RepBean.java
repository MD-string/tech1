package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class RepBean implements Serializable {

    private static final long serialVersionUID =13332444374053445L;
    private List<A1> list1;
    private List<A1> list2;
    private List<A1> list3;

    private List<A1> list4;
    private List<A1> list5;
    private List<A1> list6;

    private List<A1> list7;
    private String repairRecord;
    private String repairPerson;
    private String remark;
    private String repairZong;

    public List<A1> getList1() {
        return list1;
    }

    public void setList1(List<A1> list1) {
        this.list1 = list1;
    }

    public List<A1> getList2() {
        return list2;
    }

    public void setList2(List<A1> list2) {
        this.list2 = list2;
    }

    public List<A1> getList3() {
        return list3;
    }

    public void setList3(List<A1> list3) {
        this.list3 = list3;
    }

    public List<A1> getList4() {
        return list4;
    }

    public void setList4(List<A1> list4) {
        this.list4 = list4;
    }

    public List<A1> getList5() {
        return list5;
    }

    public void setList5(List<A1> list5) {
        this.list5 = list5;
    }

    public List<A1> getList6() {
        return list6;
    }

    public void setList6(List<A1> list6) {
        this.list6 = list6;
    }

    public List<A1> getList7() {
        return list7;
    }

    public void setList7(List<A1> list7) {
        this.list7 = list7;
    }

    public String getRepairPerson() {
        return repairPerson;
    }

    public void setRepairPerson(String repairPerson) {
        this.repairPerson = repairPerson;
    }

    public String getRepairRecord() {
        return repairRecord;
    }

    public void setRepairRecord(String repairRecord) {
        this.repairRecord = repairRecord;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepairZong() {
        return repairZong;
    }

    public void setRepairZong(String repairZong) {
        this.repairZong = repairZong;
    }
}
