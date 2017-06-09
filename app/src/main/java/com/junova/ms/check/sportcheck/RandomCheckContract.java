package com.junova.ms.check.sportcheck;

import android.widget.TextView;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.User;

import java.util.List;
import java.util.Map;

/**
 * Created by junova on 2017-03-06.
 */

public interface RandomCheckContract {
    interface view extends BaseView {
        void showLoadingProgress();

        void hideLoadingProgress();

        void showPart(List<Part> partList, TextView tx);

        void showUser(List<User> userList, TextView tx);

        void toMissionDetailActivity(String missionId);

    }

    interface prensenter extends BasePresenter {
        void upload(Map<String, String> params);

        void save(String title, String describe, String imagePath, Part factoryPart, Part shopPart, Part sectionPart, Part classPart, User user);

        void getParts(String partId, TextView tx);

        void getUsers(String partId, TextView tx);

        void upLoadScanCode(String scanCode);
    }
}
