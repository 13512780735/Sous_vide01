package com.likeit.sous_vide.model;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/29.
 */

public class AdModel implements Serializable {
    String url;
    String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
