package com.junova.ms.model;

/**
 * Created by junova on 2016/9/29 0029.
 */

public class TaskRecordModel {
    private String taskItemId="";//任务详情id
    private String taskTableId="";//任务表id
    private String projectId="";//管理项id
    private String errorId="";//异常类型id
    private String description="";//异常描述
    private String date="";//保存日期
    private String status="";//01正常 02异常 00 未检查
    private String isUpload="";//0未上传，1 已上传
    private String imagePath="";//异常图片，多张图片以";"分隔
    private String errorName="";//异常名

    public TaskRecordModel() {

    }

    public TaskRecordModel(String taskItemId, String errorName, String taskTableId, String errorId, String projectId, String description, String date, String status, String imagePath, String isUpload) {
        this.taskItemId = taskItemId;
        this.errorName = errorName;
        this.taskTableId = taskTableId;
        this.errorId = errorId;
        this.projectId = projectId;
        this.description = description;
        this.date = date;
        this.status = status;
        this.imagePath = imagePath;
        this.isUpload = isUpload;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTaskItemId() {
        return taskItemId;
    }

    public void setTaskItemId(String taskItemId) {
        this.taskItemId = taskItemId;
    }

    public String getTaskTableId() {
        return taskTableId;
    }

    public void setTaskTableId(String taskTableId) {
        this.taskTableId = taskTableId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
