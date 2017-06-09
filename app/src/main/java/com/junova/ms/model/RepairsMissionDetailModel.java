package com.junova.ms.model;

import com.junova.ms.bean.OperationRecord;

import java.util.List;

/**
 * Created by junova on 2017-04-05.
 */

public class RepairsMissionDetailModel {

    /**
     * describe : ozvpeem
     * endTime : 2012-03-22
     * operationRecord : [{"opeateUserId":"测试内容360f","operateImage":["string1","string2","string3","string4","string5"],"operation":"nhrckbz","time":"1999-10-25"}]
     * status : null
     * title : ttb
     */

    private String describe;
    private String endTime;
    private int status;
    private String title;
    private List<OperationRecord> operationRecord;
    private int isLongTerm;
    private int isForword;
    private String abnormalDisposeId;

    public String getAbnormalDisposeId() {
        return abnormalDisposeId;
    }

    public void setAbnormalDisposeId(String abnormalDisposeId) {
        this.abnormalDisposeId = abnormalDisposeId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OperationRecord> getOperationRecord() {
        return operationRecord;
    }

    public void setOperationRecord(List<OperationRecord> operationRecord) {
        this.operationRecord = operationRecord;
    }

    public int getIsLongTerm() {
        return isLongTerm;
    }

    public void setIsLongTerm(int isLongTerm) {
        this.isLongTerm = isLongTerm;
    }

    public int getIsForword() {
        return isForword;
    }

    public void setIsForword(int isForword) {
        this.isForword = isForword;
    }
}
