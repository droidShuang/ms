package com.junova.ms.bean;

/**
 * Created by junova on 2017-02-17.
 */

public class MissionTable {
    private String title;//标题
    private String missionTableId;//任务表Id
    private String time;//下发时间
    private String startTime;//任务开始执行时间
    private String endTime;//任务结束时间
    private String partId;//所属部门id
    private String moduleId;//所属模块id
    private String kind;//任务类型，每日/月度
    private String identifyingCode="";//验证条形码
    private String totalCount;
    private String errorCount;
    private String normalCount;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MissionTable() {
        super();
    }


    public MissionTable(String title, String missionTableId, String time, String startTime, String endTime, String partId, String moduleId, String kind, String identifyingCode, String totalCount, String errorCount, String normalCount, String status) {
        this.title = title;
        this.missionTableId = missionTableId;
        this.time = time;
        this.startTime = startTime;
        this.endTime = endTime;
        this.partId = partId;
        this.moduleId = moduleId;
        this.kind = kind;
        this.identifyingCode = identifyingCode;
        this.totalCount = totalCount;
        this.errorCount = errorCount;
        this.normalCount = normalCount;
        this.status = status;

    }


    public MissionTable(String title, String missionTableId, String time, String startTime, String endTime, String moduleId, String kind, String identifyingCode, String totalCount, String errorCount, String normalCount, String status) {
        this.title = title;
        this.missionTableId = missionTableId;
        this.time = time;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moduleId = moduleId;
        this.kind = kind;
        this.identifyingCode = identifyingCode;
        this.totalCount = totalCount;
        this.errorCount = errorCount;
        this.normalCount = normalCount;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMissionTableId() {
        return missionTableId;
    }

    public void setMissionTableId(String missionTableId) {
        this.missionTableId = missionTableId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }

    public String getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(String normalCount) {
        this.normalCount = normalCount;
    }
}
