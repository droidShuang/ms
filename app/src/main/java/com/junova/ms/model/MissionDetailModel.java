package com.junova.ms.model;

import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;

import java.util.List;

/**
 * Created by junova on 2017-03-16.
 */

public class MissionDetailModel {


    private List<Error> errorKind;
    private List<MissionDetail> missionDetails;

    public List<Error> getErrorKind() {
        return errorKind;
    }

    public void setErrorKind(List<Error> errorKind) {
        this.errorKind = errorKind;
    }

    public List<MissionDetail> getMissionDetails() {
        return missionDetails;
    }

    public void setMissionDetails(List<MissionDetail> missionDetails) {
        this.missionDetails = missionDetails;
    }


}
