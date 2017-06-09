package com.junova.ms.article;

import com.junova.ms.base.BasePresenter;
import com.junova.ms.base.BaseView;
import com.junova.ms.bean.Article;

import java.util.List;

/**
 * Created by junova on 2017-03-10.
 */

public interface ArticleContract {
    interface view extends BaseView {
        void showLoadingDialog();

        void hideLoadingDialog();

        void showArticle(List<Article> articleList);
    }

    interface presenter extends BasePresenter {
        void getArticle(String factoryId);
    }
}
