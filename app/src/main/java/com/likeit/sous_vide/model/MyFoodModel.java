package com.likeit.sous_vide.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MyFoodModel implements Serializable {


    /**
     * id : 23
     * name : 9999
     * wendu : 55.0
     * time : 120
     * description : 快快乐乐
     * imgs : ["http://sousvide.wbteam.cn/upload/userfood/18/18_foods_1522061325_0.png"]
     * add_time : 2018-03-26 18:48:45
     */

    private int id;
    private String name;
    private String wendu;
    private String time;
    private String description;
    private String add_time;
    private List<String> imgs;

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

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
