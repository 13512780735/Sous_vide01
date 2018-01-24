package com.likeit.sous_vide.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Registermodel implements Serializable{

    /**
     * ukey : 1dpamBPouF1xZlhut5zWnZZpjFon9ApI
     * info : {"id":12,"user_login":"","user_nickname":"","avatar":"http://sousvide.wbteam.cn/static/images/headicon.png","mobile":"13512780756","area":"86","last_login_time":"2018-01-24 11:29:09","sex":0,"birthday":"1970-01-01","create_time":"2018-01-24 11:29:09"}
     */

    private String ukey;
    private InfoBean info;

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 12
         * user_login :
         * user_nickname :
         * avatar : http://sousvide.wbteam.cn/static/images/headicon.png
         * mobile : 13512780756
         * area : 86
         * last_login_time : 2018-01-24 11:29:09
         * sex : 0
         * birthday : 1970-01-01
         * create_time : 2018-01-24 11:29:09
         */

        private int id;
        private String user_login;
        private String user_nickname;
        private String avatar;
        private String mobile;
        private String area;
        private String last_login_time;
        private int sex;
        private String birthday;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
