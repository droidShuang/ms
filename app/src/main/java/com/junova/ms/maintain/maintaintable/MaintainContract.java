package com.junova.ms.maintain.maintaintable;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.RepairMission;

import java.util.List;

/**
 * Created by junova on 2017-02-22.
 */

public interface MaintainContract {
    interface view extends BaseView {
        void showLoadingDialog();

        void hideLoadingDialog();

        void showMaintainMissionList(List<RepairMission> missionList);

        void getMaintainMissionListError(int status, String message);


    }

    interface presenter extends BasePresenter {
        void getMaintainList();
    }
}
