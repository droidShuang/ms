package com.junova.ms.article;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.adapter.ArticleAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.Article;
import com.junova.ms.check.missiondetail.MissionDetailListActivity;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ArticleActivity extends BaseActivity implements ArticleContract.view {

    @Bind(R.id.article_rv_article)
    RecyclerView rvArticle;
    ArticleAdapter adapter;
    ProgressDialog progressDialog;
    ArticlePrensenterImpl prensenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("文章中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleActivity.this.finish();
            }
        });
        init();
    }

    void init() {
        adapter = new ArticleAdapter();
        rvArticle.setLayoutManager(new LinearLayoutManager(this));
        prensenter = new ArticlePrensenterImpl(this);
        rvArticle.setAdapter(adapter);
        prensenter.subscribe();
        prensenter.getArticle(PrefUtils.getString(this, "factoryId", ""));
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showArticle(List<Article> articleList) {
        if (articleList.isEmpty()) {
            Toasty.info(this, "当前没有任何数据").show();
        }else{
            adapter.addArticleList(articleList);
        }

    }
}
