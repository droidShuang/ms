package com.junova.ms.check.missiontable;

import android.widget.TextView;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.SelectMission;
import com.junova.ms.model.SelecMissionModel;

import java.util.List;

/**
 * Created by junova on 2017-02-28.
 */

public interface GetMissionContract {
    interface view extends BaseView {

        void showLoadingDialog();

        void hideLoadingDialog();

        void showModule(List<Module> modules, TextView tx);

        void showPart(List<Part> parts, TextView tx);

        void showMissionList(List<SelectMission> missionTables);

        void noData();
    }

    interface presenter extends BasePresenter {
        void getMissionList(String moduleId, String pageIndex);

        void getModuleList(TextView tx);

        void getPartList(String partId, TextView tx);

        void upTableId(String missionTableId);
    }
}
