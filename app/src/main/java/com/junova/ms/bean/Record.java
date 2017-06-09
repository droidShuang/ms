package com.junova.ms.bean;

/**
 * Created by junova on 2017-02-17.
 */

public class Record {
    private String missionItemId = "";//检测项id
    private String missionTableId = "";//任务表id
    private int status;//状态
    private String errorDes = "";//异常描述
    private String errorImage = "";//异常图片
    private String errorId = "";//异常类型id
    private String time = "";//检查时间
    private String value = "";//数值

    private String factoryId = "";
    private String factoryName = "";
    private String sectionId = "";
    private String sectionName = "";
    private String shopId = "";
    private String shopName = "";
    private String classId = "";
    private String className = "";
    private String partId = "";
    private String toUserId = "";
    private String toUserName = "";
    private String isUp = "";//0已上传1未上传
    private String recordId = "";

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Record() {

    }

    public Record(String missionItemId, String missionTableId, int status, String errorDes, String errorImage, String errorId, String time, String value, String isUp, String recordId) {
        this.missionItemId = missionItemId;
        this.missionTableId = missionTableId;
        this.status = status;
        this.errorDes = errorDes;
        this.errorImage = errorImage;
        this.errorId = errorId;
        this.time = time;
        this.value = value;
        this.isUp = isUp;
        this.recordId = recordId;
    }

    public Record(String missionItemId, String missionTableId, int status, String errorDes, String errorImage, String errorId, String time, String value, String factoryId,
                  String factoryName, String sectionId, String sectionName, String shopId, String shopName, String classId, String className, String partId, String toUserId, String toUserName, String isUp) {
        this.missionItemId = missionItemId;
        this.missionTableId = missionTableId;
        this.status = status;
        this.errorDes = errorDes;
        this.errorImage = errorImage;
        this.errorId = errorId;
        this.time = time;
        this.value = value;
        this.factoryId = factoryId;
        this.factoryName = factoryName;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.shopId = shopId;
        this.shopName = shopName;
        this.classId = classId;
        this.className = className;
        this.partId = partId;
        this.toUserId = toUserId;
        this.isUp = isUp;
        this.toUserName = toUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getIsUp() {
        return isUp;
    }

    public void setIsUp(String isUp) {
        this.isUp = isUp;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }

    public String getErrorImage() {
        return errorImage;
    }

    public void setErrorImage(String errorImage) {
        this.errorImage = errorImage;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
