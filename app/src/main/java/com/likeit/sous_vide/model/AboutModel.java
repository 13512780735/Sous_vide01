package com.likeit.sous_vide.model;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/27.
 */

public class AboutModel implements Serializable {

    /**
     * about_register :
     &lt;p&gt;keyi的&lt;/p&gt;
     &lt;p&gt;。。。&lt;/p&gt;

     * link_content : &lt;p&gt;hao&lt;/p&gt;
     */

    private String about_register;
    private String link_content;

    public String getAbout_register() {
        return about_register;
    }

    public void setAbout_register(String about_register) {
        this.about_register = about_register;
    }

    public String getLink_content() {
        return link_content;
    }

    public void setLink_content(String link_content) {
        this.link_content = link_content;
    }
}
