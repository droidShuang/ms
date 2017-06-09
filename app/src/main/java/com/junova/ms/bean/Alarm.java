package com.junova.ms.bean;

/**
 * Created by yangshuang on 2016/4/15.
 * Description :
 */
public class Alarm {
    private String ALARMTYPE;
    private String ALARM_ID;
    private String PROJECTID;

    public String getPROJECTID() {
        return PROJECTID;
    }

    public void setPROJECTID(String PROJECTID) {
        this.PROJECTID = PROJECTID;
    }

    public String getALARMTYPE() {
        return ALARMTYPE;
    }

    public void setALARMTYPE(String ALARMTYPE) {
        this.ALARMTYPE = ALARMTYPE;
    }

    public String getALARM_ID() {
        return ALARM_ID;
    }

    public void setALARM_ID(String ALARM_ID) {
        this.ALARM_ID = ALARM_ID;
    }
}
