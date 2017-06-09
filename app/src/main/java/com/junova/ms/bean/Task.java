package com.junova.ms.bean;

/**
 * Created by yangshuang on 2016/4/10.
 * Description :
 */
public class Task {
    private String CHACKCONTENT, STATUS, FGMP, VISUAL;
    private String TASK_ID, TASKTAB_ID, UP, DOWN, TASKINFO_ID;

    public String getCHACKCONTENT() {
        return CHACKCONTENT;
    }

    public String getTASKINFO_ID() {
        return TASKINFO_ID;
    }

    public void setTASKINFO_ID(String TASKINFO_ID) {
        this.TASKINFO_ID = TASKINFO_ID;
    }

    public void setCHACKCONTENT(String CHACKCONTENT) {
        this.CHACKCONTENT = CHACKCONTENT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getFGMP() {
        return FGMP;
    }

    public void setFGMP(String FGMP) {
        this.FGMP = FGMP;
    }

    public String getVISUAL() {
        return VISUAL;
    }

    public void setVISUAL(String VISUAL) {
        this.VISUAL = VISUAL;
    }


    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public String getTASKTAB_ID() {
        return TASKTAB_ID;
    }

    public void setTASKTAB_ID(String TASKTAB_ID) {
        this.TASKTAB_ID = TASKTAB_ID;
    }


    public String getUP() {
        return UP;
    }

    public void setUP(String UP) {
        this.UP = UP;
    }

    public String getDOWN() {
        return DOWN;
    }

    public void setDOWN(String DOWN) {
        this.DOWN = DOWN;
    }
}
