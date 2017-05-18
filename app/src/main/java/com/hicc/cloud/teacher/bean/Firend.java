package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2017/5/11/011.
 */

public class Firend {
    private String name;
    private String phone;

    public Firend(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Firend(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
