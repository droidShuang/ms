package com.junova.ms.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.article.ArticleDetailActivity;
import com.junova.ms.bean.Article;
import com.junova.ms.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-10.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Article> articleList;

    public ArticleAdapter() {

        articleList = new ArrayList<>();

    }

    public void addArticleList(List<Article> articleList) {
        this.articleList.clear();
        this.articleList.addAll(articleList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArticleViewHolder) holder).onBind(position);
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }


    class ArticleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_article_title)
        TextView txTitle;
        @Bind(R.id.item_article_summary)
        TextView txSummary;
        View view;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), ArticleDetailActivity.class);
                    intent.putExtra("articleId", articleList.get(getAdapterPosition()).getArticleId());
                    intent.putExtra("title", articleList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("time", articleList.get(getAdapterPosition()).getTime());

                    v.getContext().startActivity(intent);
                }
            });
        }

        public void onBind(int position) {
            if (PrefUtils.getBoolean(txTitle.getContext(), articleList.get(position).getArticleId(), false)) {
                view.setBackgroundColor(Color.parseColor("#BDBDBD"));
            }
            txTitle.setText(articleList.get(position).getTitle());
            txSummary.setText(articleList.get(position).getSummary());
        }
    }
}
