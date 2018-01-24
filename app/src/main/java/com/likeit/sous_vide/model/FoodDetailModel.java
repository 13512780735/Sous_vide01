package com.likeit.sous_vide.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class FoodDetailModel implements Serializable{

    /**
     * name : 测试2
     * wendu : 60
     * time : 30
     * description : test is test
     * cat_name : beaf
     * more : {"thumbnail":"food/20180120/4775929710942cba669fb75e76966451.jpg","photos":[{"url":"food/20180120/74bd605d3e7994634dd86b2523431058.jpg","name":"timg (1).jpg"}]}
     */

    private String name;
    private String wendu;
    private String time;
    private String description;
    private String cat_name;
    private MoreBean more;

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

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public MoreBean getMore() {
        return more;
    }

    public void setMore(MoreBean more) {
        this.more = more;
    }

    public static class MoreBean {
        /**
         * thumbnail : food/20180120/4775929710942cba669fb75e76966451.jpg
         * photos : [{"url":"food/20180120/74bd605d3e7994634dd86b2523431058.jpg","name":"timg (1).jpg"}]
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
             * url : food/20180120/74bd605d3e7994634dd86b2523431058.jpg
             * name : timg (1).jpg
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
