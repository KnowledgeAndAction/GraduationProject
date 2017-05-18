package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2017/5/11/011.
 */

public class News {
    private int imageId;
    private String title;
    private String des;

    public News(int imageId, String title, String des) {
        this.imageId = imageId;
        this.title = title;
        this.des = des;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
