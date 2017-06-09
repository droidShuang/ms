package com.junova.ms.maintain.maintaindetail;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.model.RepairsMissionDetailModel;

/**
 * Created by junova on 2017-04-05.
 */

public class MaintainDetailContract {
    interface view extends BaseView {
        void showLoadingProgressbar();

        void hideLoadingProgressbar();

        void showMissionDetail(RepairsMissionDetailModel detailModel);


    }

    interface presenter extends BasePresenter {
        void getMaintainDetail(String missionDetailId);

        void dealMaintainMission(String maintainId, String abnormalDisposeId, int type);
    }
}
