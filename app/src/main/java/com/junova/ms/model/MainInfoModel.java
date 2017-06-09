package com.junova.ms.model;

import java.util.List;

/**
 * Created by junova on 2016/9/28 0028.
 */

public class MainInfoModel {
    /**
     * articles : [{"articleId":"egopvfntrr","summary":"jtwbngnx","time":"2004-01-31","title":"ynpf"},{"articleId":"jce","summary":"bbuqpbh","time":"1988-04-09","title":"krrqtc"},{"articleId":"rvjshead","summary":"ezjousrxx","time":"2007-05-13","title":"wusbtdunl"}]
     * checkNumber : 11
     * repairsNumber : 11
     */

    private int checkNumber;
    private int repairsNumber;
    private List<ArticlesBean> articles;

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public int getRepairsNumber() {
        return repairsNumber;
    }

    public void setRepairsNumber(int repairsNumber) {
        this.repairsNumber = repairsNumber;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {
        /**
         * articleId : egopvfntrr
         * summary : jtwbngnx
         * time : 2004-01-31
         * title : ynpf
         */

        private String articleId;
        private String summary;
        private String time;
        private String title;

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    /**
     * MAINTAINNUMBER : 0 任务维修数
     * TASKNUMBER : 0 任务数
     */


}
