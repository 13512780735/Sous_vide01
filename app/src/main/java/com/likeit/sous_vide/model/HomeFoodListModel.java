package com.likeit.sous_vide.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/24.
 */

public class HomeFoodListModel implements Serializable{

    /**
     * id : 1
     * name : test11
     * wendu : 60
     * time : 40
     * description : xxx
     * imgs : ./public/upload/userfood/8/8_headimg_1516384005.png
     * add_time : 2018-01-24 01:18:11
     */

    private int id;
    private String name;
    private String wendu;
    private String time;
    private String description;
    private String imgs;
    private String add_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
