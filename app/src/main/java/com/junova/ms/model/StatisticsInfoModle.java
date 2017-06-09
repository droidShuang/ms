package com.junova.ms.model;

/**
 * Created by junova on 2016/10/4 0004.
 */

public class StatisticsInfoModle {


    /**
     * doCount :  128
     * errorCount : 测试内容o50d
     * totalCount : 128
     * undoCount : 测试内容amn6
     * warnCount : 128
     */

    private String doCount;
    private String errorCount;
    private String totalCount;
    private String undoCount;
    private String warnCount;

    public String getDoCount() {
        return doCount;
    }

    public void setDoCount(String doCount) {
        this.doCount = doCount;
    }

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getUndoCount() {
        return undoCount;
    }

    public void setUndoCount(String undoCount) {
        this.undoCount = undoCount;
    }

    public String getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(String warnCount) {
        this.warnCount = warnCount;
    }
}
