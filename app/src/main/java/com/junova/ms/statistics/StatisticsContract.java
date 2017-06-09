package com.junova.ms.statistics;

import android.widget.TextView;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.model.StatisticsInfoModle;

import java.util.List;
import java.util.Map;

/**
 * Created by junova on 2017-03-02.
 */

public interface StatisticsContract {
    interface presenter extends BasePresenter {
        void getCountData();

        void getParts(String partId, TextView tx);

        void getChartData(Map<String, String> params);

        void getModel(TextView tx);
    }

    interface view extends BaseView {
        void showLoadingProgress();

        void hideLoadingProgress();

        void showChart(StatisticsInfoModle statisticsInfoModle);

        void showCountData(StatisticsInfoModle statisticsInfoModle);

        void showPart(List<Part> partList, TextView tx);

        void showModel(List<Module> moduleList, TextView tx);


    }
}
