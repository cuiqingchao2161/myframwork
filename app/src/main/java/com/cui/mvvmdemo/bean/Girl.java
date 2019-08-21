package com.cui.mvvmdemo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/13.
 */

public class Girl implements Serializable {
    private String name;
    private String introduction;
    private String picPath;

    public Girl(String name, String introduction, String picPath) {
        this.name = name;
        this.introduction = introduction;
        this.picPath = picPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
