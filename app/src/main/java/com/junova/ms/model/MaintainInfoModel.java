package com.junova.ms.model;

/**
 * Created by junova on 2016/9/28 0028.
 */

public class MaintainInfoModel {


    /**
     * MAINTAINID : a50ad820e24543e391680aa7803809ee
     * UPUSERNAME : 测试1
     * CREATED : 2016-10-03 19:55:04
     * OPERATIONHISTORY : fasdfas
     * TITLE : fasdfas
     */

    private String MAINTAINID;
    private String UPUSERNAME;
    private String CREATED;
    private String OPERATIONHISTORY;
    private String TITLE;
    private String IMAGEPATHS;

    public String getIMAGEPATHS() {
        return IMAGEPATHS;
    }

    public void setIMAGEPATHS(String IMAGEPATHS) {
        this.IMAGEPATHS = IMAGEPATHS;
    }

    public String getMAINTAINID() {
        return MAINTAINID;
    }

    public void setMAINTAINID(String MAINTAINID) {
        this.MAINTAINID = MAINTAINID;
    }

    public String getUPUSERNAME() {
        return UPUSERNAME;
    }

    public void setUPUSERNAME(String UPUSERNAME) {
        this.UPUSERNAME = UPUSERNAME;
    }

    public String getCREATED() {
        return CREATED;
    }

    public void setCREATED(String CREATED) {
        this.CREATED = CREATED;
    }

    public String getOPERATIONHISTORY() {
        return OPERATIONHISTORY;
    }

    public void setOPERATIONHISTORY(String OPERATIONHISTORY) {
        this.OPERATIONHISTORY = OPERATIONHISTORY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
