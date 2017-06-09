package com.junova.ms.bean;

/**
 * Created by yangshuang on 2016/4/11.
 * Description :
 */
public class Part {

    /**
     * userId : svyp
     * userName : huflemxr
     */

    private String partId = "";
    private String partName = "";

    public Part(String partId, String partName) {
        this.partId = partId;
        this.partName = partName;
    }

    public Part() {

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
}
