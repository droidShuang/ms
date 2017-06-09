package com.junova.ms.check.sportcheck;

import android.content.Context;
import android.widget.TextView;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.MissionDetail;

import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.SelectMission;


import java.util.List;
import java.util.Map;

/**
 * Created by junova on 2017-03-03.
 */

public interface SportCheckContract {
    interface view extends BaseView {
        void showLoadingProgressDialog();

        void hideLoadingProgressDialog();

        void showMission(List<SelectMission> missionTables);

        void showMissionDetail(List<MissionDetail> missionDetails);

        void showModel(List<Module> moduleList, TextView tx);

        void showPart(List<Part> partList, TextView tx);

    }

    interface presenter extends BasePresenter {
        void getMissionTable(Map<String, String> params);

        void getMissionDetail(String missionTableId);

        void getModel(TextView tx);

        void getPart(String partId, TextView tx);

        void uploadDetail(Context context, String missionTableId);

        void uploadRecord(Context context);
    }

}
