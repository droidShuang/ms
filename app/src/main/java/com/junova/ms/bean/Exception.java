package com.junova.ms.bean;

/**
 * Created by yangshuang on 2016/4/14.
 * Description :
 */
public class Exception {
    private String TASKEXCEPTION_ID;//异常id
    private String STARTUSER;//处理人
    private String TOUSER;//待处理人
    private String DESC;//描述
    private String STATUS;//状态 0 异常提交 1 报警已确认 2已完成
    private String ADDTIME;//添加时间
    private String CONTENT;// 异常内容
    private String IMAGEURL="";//异常图片 多张图片已；分隔
    private String TASKINFO_ID;
    private String ALARM_ID;

    public String getALARM_ID() {
        return ALARM_ID;
    }

    public void setALARM_ID(String ALARM_ID) {
        this.ALARM_ID = ALARM_ID;
    }

    public String getTASKINFO_ID() {
        return TASKINFO_ID;
    }

    public void setTASKINFO_ID(String TASKINFO_ID) {
        this.TASKINFO_ID = TASKINFO_ID;
    }

    public String getTASKEXCEPTION_ID() {
        return TASKEXCEPTION_ID;
    }

    public void setTASKEXCEPTION_ID(String TASKEXCEPTION_ID) {
        this.TASKEXCEPTION_ID = TASKEXCEPTION_ID;
    }

    public String getSTARTUSER() {
        return STARTUSER;
    }

    public void setSTARTUSER(String STARTUSER) {
        this.STARTUSER = STARTUSER;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getTOUSER() {
        return TOUSER;
    }

    public void setTOUSER(String TOUSER) {
        this.TOUSER = TOUSER;
    }

    public String getADDTIME() {
        return ADDTIME;
    }

    public void setADDTIME(String ADDTIME) {
        this.ADDTIME = ADDTIME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setIMAGEURL(String IMAGEURL) {
        this.IMAGEURL = IMAGEURL;
    }
}
