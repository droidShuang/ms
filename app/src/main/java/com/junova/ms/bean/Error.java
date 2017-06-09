package com.junova.ms.bean;

/**
 * Created by junova on 2017-02-17.
 */

public class Error {
    private String errorName;//异常名称
    private String errorId;//异常类型id
    private String missionDetailId;//检测项id
    private String missionTableId;

    public Error() {
        super();
    }


    public Error(String errorId, String errorName, String missionDetailId) {
        this.errorId = errorId;
        this.errorName = errorName;
        this.missionDetailId = missionDetailId;
    }

    public Error(String errorName, String errorId, String missionTableId, String missionDetailId) {
        this.errorName = errorName;
        this.errorId = errorId;
        this.missionTableId = missionTableId;
        this.missionDetailId = missionDetailId;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getMissionDetailId() {
        return missionDetailId;
    }

    public void setMissionDetailId(String missionDetailId) {
        this.missionDetailId = missionDetailId;
    }

    public String getMissionTableId() {
        return missionTableId;
    }

    public void setMissionTableId(String missionTableId) {
        this.missionTableId = missionTableId;
    }
}
