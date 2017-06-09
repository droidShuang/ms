package com.junova.ms.model;

import java.util.List;

/**
 * Created by junova on 2016/9/28 0028.
 */

public class TaskItemInfoModel {

    /**
     * ITEMID : 32fb0cf816a84d0c87a22de0bf3542ce
     * TITLE : 工序描述
     * ERRORKINDLIST : [{"ERRORNAME":"报警类型3","ERRORID":"f78cfce70303494daa4841535c9095d8"},{"ERRORNAME":"警报1","ERRORID":"d66be84b417243e181400f59f2614f3c"}]
     */

    private String ITEMID;
    private String TITLE;
    /**
     * ERRORNAME : 报警类型3
     * ERRORID : f78cfce70303494daa4841535c9095d8
     */

    private List<ErrorKind> ERRORKINDLIST;

    public String getITEMID() {
        return ITEMID;
    }

    public void setITEMID(String ITEMID) {
        this.ITEMID = ITEMID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public List<ErrorKind> getERRORKINDLIST() {
        return ERRORKINDLIST;
    }

    public void setERRORKINDLIST(List<ErrorKind> ERRORKINDLIST) {
        this.ERRORKINDLIST = ERRORKINDLIST;
    }

    public static class ErrorKind {
        private String ERRORNAME;
        private String ERRORID;

        public String getERRORNAME() {
            return ERRORNAME;
        }

        public void setERRORNAME(String ERRORNAME) {
            this.ERRORNAME = ERRORNAME;
        }

        public String getERRORID() {
            return ERRORID;
        }

        public void setERRORID(String ERRORID) {
            this.ERRORID = ERRORID;
        }
    }
}
