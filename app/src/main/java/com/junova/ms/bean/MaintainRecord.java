package com.junova.ms.bean;

/**
 * Created by junova on 2017-04-05.
 */

public class MaintainRecord {
    private String maintainId = "";
    private String maintainImage = "";
    private String startTime = "";
    private String endTime = "";
    private String describletion = "";

    public MaintainRecord(String maintainId, String maintainImage, String startTime, String endTime, String describletion) {
        this.maintainId = maintainId;
        this.maintainImage = maintainImage;
        this.startTime = startTime;
        this.endTime = endTime;
        this.describletion = describletion;
    }

    public String getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(String maintainId) {
        this.maintainId = maintainId;
    }

    public String getMaintainImage() {
        return maintainImage;
    }

    public void setMaintainImage(String maintainImage) {
        this.maintainImage = maintainImage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescribletion() {
        return describletion;
    }

    public void setDescribletion(String describletion) {
        this.describletion = describletion;
    }
}
