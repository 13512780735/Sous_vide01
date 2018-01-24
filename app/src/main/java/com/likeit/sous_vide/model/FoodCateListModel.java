package com.likeit.sous_vide.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/24.
 */

public class FoodCateListModel implements Serializable{

    /**
     * id : 392
     * name : beaf
     */

    private int id;
    private String name;

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
}
