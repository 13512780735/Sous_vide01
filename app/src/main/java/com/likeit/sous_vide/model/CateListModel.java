package com.likeit.sous_vide.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CateListModel implements Serializable{

    /**
     * id : 4
     * name : 测试4
     * description : nice chicken
     * more : {"thumbnail":"food/20180120/9a656b3c345159e8cddd387005e04a16.jpg","photos":[{"url":"food/20180120/1c9e214c5313dd3cb12abaa29fc14c94.jpg","name":"u=2053421948,3487038139&amp;fm=85&amp;s=4BA43862CCF527A95F741CC60000A0A1.jpg"}]}
     */

    private int id;
    private String name;
    private String description;
    private MoreBean more;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoreBean getMore() {
        return more;
    }

    public void setMore(MoreBean more) {
        this.more = more;
    }

    public static class MoreBean {
        /**
         * thumbnail : food/20180120/9a656b3c345159e8cddd387005e04a16.jpg
         * photos : [{"url":"food/20180120/1c9e214c5313dd3cb12abaa29fc14c94.jpg","name":"u=2053421948,3487038139&amp;fm=85&amp;s=4BA43862CCF527A95F741CC60000A0A1.jpg"}]
         */

        private String thumbnail;
        private List<PhotosBean> photos;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class PhotosBean {
            /**
             * url : food/20180120/1c9e214c5313dd3cb12abaa29fc14c94.jpg
             * name : u=2053421948,3487038139&amp;fm=85&amp;s=4BA43862CCF527A95F741CC60000A0A1.jpg
             */

            private String url;
            private String name;

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
    }
}
