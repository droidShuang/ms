package com.junova.ms.bean;

import java.util.List;

/**
 * Created by junova on 2017-04-05.
 */

public class OperationRecord {
    /**
     * opeateUserId : 测试内容360f
     * operateImage : ["string1","string2","string3","string4","string5"]
     * operation : nhrckbz
     * time : 1999-10-25
     */

    private String opeateUserName;
    private String operation;
    private String time;
    private List<String> operateImage;
    private String opeateType;

    public String getOpeateUserName() {
        return opeateUserName;
    }

    public void setOpeateUserName(String opeateUserName) {
        this.opeateUserName = opeateUserName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getOperateImage() {
        return operateImage;
    }

    public void setOperateImage(List<String> operateImage) {
        this.operateImage = operateImage;
    }

    public String getOpeateType() {
        return opeateType;
    }

    public void setOpeateType(String opeateType) {
        this.opeateType = opeateType;
    }
}
