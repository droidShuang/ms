package com.junova.ms.login;

import android.widget.TextView;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.Part;

import java.util.List;

/**
 * @author :杨爽
 * @class name：LoginContract
 * @time 2017-02-13 14:56
 * @describe
 */

public interface LoginContract {

    /**
     * @author 杨爽
     * @version 1.0
     * @time 2017-02-13  14:57
     * @describe
     */
    interface view extends BaseView {
        //跳转到主界面
        void toHome();

        void loginError(String error);

        void showFactory(List<Part> partList, TextView tx);

        void loginSuccess();

        void showProgressBar();

        void hideProgressBar();

    }

    /**
     * @author 杨爽
     * @version 1.0
     * @time 2017-02-13  14:57
     * @describe a
     */
    interface presenter extends BasePresenter {
        void doLogin(String userName, String password);

        void getFactory(TextView tx);
    }
}
