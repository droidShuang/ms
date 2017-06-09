package com.junova.ms.bean;

/**
 * Created by junova on 2017-02-21.
 */

public class MissionItem {
    private String missionDetailId;//检测项id
    private String missionTableId;//所属任务表id
    private String title;//检测项标题
    private String isValue;//是否为数值检测
    private String upValue;//数值上限
    private String downValue;//数值下限
    private String operationTipText;//文字操作提示
    private String operationTipImage;//图片操作提示
    private String status;//状态
    private String errorDes;//异常描述
    private String errorImage;//异常图片
    private String errorId;//异常类型id
    private String time;//检查时间

    public MissionItem() {
    }

    public MissionItem(String missionDetailId, String time, String missionTableId, String isValue, String upValue, String downValue, String operationTipText, String operationTipImage, String status, String errorDes, String errorImage, String errorId, String title) {
        this.missionDetailId = missionDetailId;
        this.time = time;
        this.missionTableId = missionTableId;
        this.isValue = isValue;
        this.upValue = upValue;
        this.downValue = downValue;
        this.operationTipText = operationTipText;
        this.operationTipImage = operationTipImage;
        this.status = status;
        this.errorDes = errorDes;
        this.errorImage = errorImage;
        this.errorId = errorId;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsValue() {
        return isValue;
    }

    public void setIsValue(String isValue) {
        this.isValue = isValue;
    }

    public String getDownValue() {
        return downValue;
    }

    public void setDownValue(String downValue) {
        this.downValue = downValue;
    }

    public String getUpValue() {
        return upValue;
    }

    public void setUpValue(String upValue) {
        this.upValue = upValue;
    }

    public String getOperationTipText() {
        return operationTipText;
    }

    public void setOperationTipText(String operationTipText) {
        this.operationTipText = operationTipText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperationTipImage() {
        return operationTipImage;
    }

    public void setOperationTipImage(String operationTipImage) {
        this.operationTipImage = operationTipImage;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
