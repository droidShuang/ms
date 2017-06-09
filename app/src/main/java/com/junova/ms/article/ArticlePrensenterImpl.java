package com.junova.ms.article;

import android.content.Context;

import com.junova.ms.api.MsApi;
import com.junova.ms.bean.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-03-10.
 */

public class ArticlePrensenterImpl implements ArticleContract.presenter {
    ArticleContract.view articleView;
    Context context;

    public ArticlePrensenterImpl(ArticleContract.view articleView) {
        this.articleView = articleView;
        context = (Context) articleView;
    }

    @Override
    public void getArticle(String factoryId) {
        articleView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("factoryId", factoryId);
        MsApi.getInstance().getArticleList(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Article>>() {
            @Override
            public void accept(@NonNull List<Article> articles) throws Exception {
                articleView.hideLoadingDialog();
                articleView.showArticle(articles);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                articleView.hideLoadingDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                articleView.hideLoadingDialog();
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
