package com.junova.ms.check.missiontable;

import android.content.Context;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.model.SelecMissionModel;

import java.util.List;

/**
 * Created by junova on 2017-02-17.
 */

public interface MissionTableContract {
    interface view extends BaseView {
        void showTaskTable(List<MissionTable> missionTables);

        void showProgressDialog();

        void hideProgressDialog();

        void refresh();

        void getTableError();


    }

    interface presenter extends BasePresenter {
        void getTaskTable(Context context, int pageIndex);

        void uploadRecord(Context context);

        void upTableId(String missionTableId);

        void upScanCode(String scanCode);
    }
}
