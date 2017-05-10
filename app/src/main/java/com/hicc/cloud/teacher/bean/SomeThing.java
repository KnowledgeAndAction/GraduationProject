package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2017/5/10/010.
 */

public class SomeThing {
    private String nameId;
    private String name;
    private String time;

    public SomeThing(String nameId, String name, String time) {
        this.nameId = nameId;
        this.name = name;
        this.time = time;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
