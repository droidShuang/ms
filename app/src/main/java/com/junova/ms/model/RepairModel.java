package com.junova.ms.model;

import com.junova.ms.bean.RepairMission;

import java.util.List;

/**
 * Created by junova on 2017-04-05.
 */

public class RepairModel {
    List<RepairMission> missions;

    public List<RepairMission> getMissions() {
        return missions;
    }

    public void setMissions(List<RepairMission> missions) {
        this.missions = missions;
    }
}
