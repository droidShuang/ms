package com.junova.ms.statistics;

import com.junova.ms.bean.ErrorHistory;
import com.junova.ms.model.ErrorHistoryModel;

import java.util.List;
import java.util.Map;

/**
 * Created by junova on 2017-04-17.
 */

public interface ErrorStatisticsContract {
    interface prensenter {
        void getErrorList(Map<String, String> parmns);
    }

    interface view {
        void showLoadingProgress();

        void hideLoadingProgress();

        void showErrorList(List<ErrorHistory> errorHistories);
    }
}
