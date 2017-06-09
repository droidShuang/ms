package com.junova.ms.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.article.ArticleDetailActivity;
import com.junova.ms.model.MainInfoModel;
import com.junova.ms.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-15.
 */

public class ArticleHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MainInfoModel.ArticlesBean> list;


    public ArticleHomeAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_article, parent, false);
        return new HomeArticleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeArticleViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<MainInfoModel.ArticlesBean> getList() {
        return list;
    }

    public void addList(List<MainInfoModel.ArticlesBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class HomeArticleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_article_title)
        TextView txTitle;
        @Bind(R.id.item_article_summary)
        TextView txSummary;
        @Bind(R.id.item_article_time)
        TextView txTime;
        View view;

        public HomeArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), ArticleDetailActivity.class);
                    intent.putExtra("articleId", list.get(getAdapterPosition()).getArticleId());
                    intent.putExtra("title", list.get(getAdapterPosition()).getTitle());
                    intent.putExtra("time", list.get(getAdapterPosition()).getTime());
                    PrefUtils.putBoolean(v.getContext(), list.get(getAdapterPosition()).getArticleId(), true);
                    v.getContext().startActivity(intent);

                }
            });
        }

        public void onBind(int position) {
            MainInfoModel.ArticlesBean articlesBean = list.get(position);
            if (PrefUtils.getBoolean(txTime.getContext(), articlesBean.getArticleId(), false)) {
                view.setBackgroundColor(Color.parseColor("#BDBDBD"));
            }
            txSummary.setText(articlesBean.getSummary());
            txTime.setText(articlesBean.getTime());
            txTitle.setText(articlesBean.getTitle());
        }
    }
}
