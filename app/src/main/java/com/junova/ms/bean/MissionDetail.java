package com.junova.ms.bean;

import java.util.List;

/**
 * Created by junova on 2017-02-17.
 */

public class MissionDetail {

    /**
     * downValue : 5
     * errorKind : [{"errorId":"eeypnv","errorName":"jhxplpms"},{"errorId":"ckiypbr","errorName":"bgog"},{"errorId":"joeyvq","errorName":"mjqs"}]
     * isValue : 2
     * missionDetailId : kqt
     * operationTipPic : ["wklv"]
     * operationTipText : vwy
     * status : 1
     * title : dprlzf
     * upValue : 10
     * value : 测试内容3lh6
     */

    private int downValue;
    private int isValue;
    private String missionDetailId;
    private String operationTipText;
    private int status;//0 正常 1 异常 3 尚未操作状态
    private String title;
    private int upValue;
    private String value;
    private String isUp = "";
    private List<String> operationTipPic;
    private String recordId = "";

    public MissionDetail() {
    }

    public MissionDetail(int downValue, int isValue, String missionDetailId, String operationTipText, int status, String title, int upValue, String value, List<String> operationTipPic, String isUp, String recordId) {
        this.downValue = downValue;
        this.isValue = isValue;
        this.missionDetailId = missionDetailId;
        this.operationTipText = operationTipText;
        this.status = status;
        this.title = title;
        this.upValue = upValue;
        this.value = value;
        this.isUp = isUp;
        this.operationTipPic = operationTipPic;
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getDownValue() {
        return downValue;
    }

    public void setDownValue(int downValue) {
        this.downValue = downValue;
    }

    public int getIsValue() {
        return isValue;
    }

    public void setIsValue(int isValue) {
        this.isValue = isValue;
    }

    public String getMissionDetailId() {
        return missionDetailId;
    }

    public void setMissionDetailId(String missionDetailId) {
        this.missionDetailId = missionDetailId;
    }

    public String getIsUp() {
        return isUp;
    }

    public void setIsUp(String isUp) {
        this.isUp = isUp;
    }

    public String getOperationTipText() {
        return operationTipText;
    }

    public void setOperationTipText(String operationTipText) {
        this.operationTipText = operationTipText;
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

    public int getUpValue() {
        return upValue;
    }

    public void setUpValue(int upValue) {
        this.upValue = upValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public List<String> getOperationTipPic() {
        return operationTipPic;
    }

    public void setOperationTipPic(List<String> operationTipPic) {
        this.operationTipPic = operationTipPic;
    }


}
