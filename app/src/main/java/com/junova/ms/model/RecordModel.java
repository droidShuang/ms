package com.junova.ms.model;

/**
 * Created by junova on 2017-04-12.
 */

public class RecordModel {
    private String checkTime;
    private String errorDes;
    private String errorId;
    private String errorImage;
    private String missionItemId;
    private String missionTableId;
    private String status;
    private String value;
    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorImage() {
        return errorImage;
    }

    public void setErrorImage(String errorImage) {
        this.errorImage = errorImage;
    }

    public String getMissionItemId() {
        return missionItemId;
    }

    public void setMissionItemId(String missionItemId) {
        this.missionItemId = missionItemId;
    }

    public String getMissionTableId() {
        return missionTableId;
    }

    public void setMissionTableId(String missionTableId) {
        this.missionTableId = missionTableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
