package com.junova.ms.article;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.Article;
import com.junova.ms.model.ArticleDetailModel;
import com.junova.ms.utils.LoadingDialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ArticleDetailActivity extends BaseActivity {
    @Bind(R.id.article_detail_webview)
    WebView webView;
    String articleId = "";
    ProgressDialog progressDialog;
    @Bind(R.id.article_detail_tx_title)
    TextView txTitle;
    @Bind(R.id.article_detail_tx_time)
    TextView txTime;
    String title = "";
    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("文章中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.this.finish();
            }
        });
        articleId = getIntent().getStringExtra("articleId");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        txTime.setText(time);
        txTitle.setText(title);
        getArticleDetail();
    }

    void getArticleDetail() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
        Map<String, String> parmas = new HashMap<>();
        parmas.put("articleId", articleId);
        MsApi.getInstance().getArticleDetail(this, parmas).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ArticleDetailModel>() {
            @Override
            public void accept(@NonNull ArticleDetailModel articleDetailModel) throws Exception {
                webView.loadDataWithBaseURL(null, articleDetailModel.getContent(), null, "utf-8", null);
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        });
    }

}
