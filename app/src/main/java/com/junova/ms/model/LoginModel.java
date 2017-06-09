package com.junova.ms.model;

/**
 * Created by rider on 2016/7/5 0005 14:15.
 * Description :
 */
public class LoginModel {

    /**
     * factoryId : 3
     * userId : 4
     * userName : 测试内容u76h
     * phoneNumber : 3
     * station : 3
     * token : 3
     * userName : miiupr
     */

    private String factoryId;
    private String partId;
    private String partName;
    private String phoneNumber;
    private String station;
    private String token;
    private String userName;
    private String userId;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
