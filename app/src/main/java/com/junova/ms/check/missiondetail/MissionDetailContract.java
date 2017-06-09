package com.junova.ms.check.missiondetail;

import android.content.Context;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;

import java.util.List;

/**
 * Created by junova on 2017-02-21.
 */

public interface MissionDetailContract {
    interface view extends BaseView {
        void showLoadingDialog();

        void hideLoadingDialog();

        void getMissionItemError();

        void getMissionItemSuccess(List<MissionDetail> missionDetails, List<Error> errors);

    }

    interface presenter extends BasePresenter {
        void getMissionItemList(String missionTableId, boolean verfice);

        void checkIdentifyingCode();

        void upLoadRecord(Context context, String missionTableId);
    }
}
