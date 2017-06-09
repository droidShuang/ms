package com.junova.ms.main;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.model.MainInfoModel;

import java.util.List;

/**
 * Created by junova on 2017-02-16.
 */

public interface MainContract {
    interface view extends BaseView {
        /**
         * @author 杨爽
         * @time 2017-02-16  09:48
         * @describe 显示主页文章
         * @version 1.0
         */

        void showMainArtical(List<MainInfoModel.ArticlesBean> articles);

        /**
         * @author 杨爽
         * @time 2017-02-16  09:48
         * @describe 显示首页任务数
         * @version 1.0
         */

        void showMissionNumber(String checkNumber, String maintainNumber);

        /**
         * @author 杨爽
         * @time 2017-02-16  09:49
         * @describe 显示用户信息
         * @version 1.0
         */

        void showUserInfo();
    }

    interface presenter extends BasePresenter {
        /**
         * @author 杨爽
         * @time 2017-02-16  09:50
         * @describe 获取用户信息
         * @version 1.0
         */
        void getUserInfo();

        /**
         * @author 杨爽
         * @time 2017-02-16  09:50
         * @describe 网络获取主页的文章 以及任务数信息
         * @version 1.0
         */
        void getMainInfo();
    }
}
