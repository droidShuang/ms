package com.junova.ms.model;

/**
 * Created by junova on 2016/10/4 0004.
 */

public class HistoryInfoModel {

    /**
     * TOTALCOUNT : 0
     * ERRORCOUNT : 0
     * UNDOCOUNT : 0
     * DOCOUNT : 0
     * WARNCOUNT : 0
     */

    private String TOTALCOUNT;
    private String ERRORCOUNT;
    private String UNDOCOUNT;
    private String DOCOUNT;
    private String WARNCOUNT;

    public String getTOTALCOUNT() {
        return TOTALCOUNT;
    }

    public void setTOTALCOUNT(String TOTALCOUNT) {
        this.TOTALCOUNT = TOTALCOUNT;
    }

    public String getERRORCOUNT() {
        return ERRORCOUNT;
    }

    public void setERRORCOUNT(String ERRORCOUNT) {
        this.ERRORCOUNT = ERRORCOUNT;
    }

    public String getUNDOCOUNT() {
        return UNDOCOUNT;
    }

    public void setUNDOCOUNT(String UNDOCOUNT) {
        this.UNDOCOUNT = UNDOCOUNT;
    }

    public String getDOCOUNT() {
        return DOCOUNT;
    }

    public void setDOCOUNT(String DOCOUNT) {
        this.DOCOUNT = DOCOUNT;
    }

    public String getWARNCOUNT() {
        return WARNCOUNT;
    }

    public void setWARNCOUNT(String WARNCOUNT) {
        this.WARNCOUNT = WARNCOUNT;
    }
}
