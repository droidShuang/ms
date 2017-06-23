package com.junova.ms.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.ErrorHistory;
import com.junova.ms.maintain.maintaindetail.MaintainDetailActivity;
import com.junova.ms.model.ErrorHistoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2016/10/4 0004.
 */

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ErrorHolder> {
    private List<ErrorHistory> errorHistoryList;

    public ErrorAdapter() {
        super();
        errorHistoryList = new ArrayList<>();
    }

    @Override
    public ErrorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
        return new ErrorHolder(view);
    }

    @Override
    public void onBindViewHolder(ErrorHolder holder, int position) {
        holder.onBind(position);
    }

    public void addList(List<ErrorHistory> list) {
        errorHistoryList.clear();
        errorHistoryList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return errorHistoryList.size();
    }


    class ErrorHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_tx_index)
        TextView txIndex;
        @Bind(R.id.item_tx_date)
        TextView txDate;
        @Bind(R.id.item_tx_user)
        TextView txUser;
        @Bind(R.id.item_tx_title)
        TextView txTitle;

        public ErrorHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), MaintainDetailActivity.class);
                    intent.putExtra("upUserId", errorHistoryList.get(getAdapterPosition()).getUpUserId());
                    intent.putExtra("time", errorHistoryList.get(getAdapterPosition()).getUpTime());
                    intent.putExtra("repairsMissionId", errorHistoryList.get(getAdapterPosition()).getRepairsMissionId());
                    intent.putExtra("dealUserId", errorHistoryList.get(getAdapterPosition()).getDealUserId());
                    intent.putExtra("upUser", errorHistoryList.get(getAdapterPosition()).getUpUserName());
                    v.getContext().startActivity(intent);
                }
            });
        }

        private void onBind(int position) {
            txIndex.setText((position + 1) + "");
            txDate.setText(errorHistoryList.get(position).getUpTime());
            txUser.setText(errorHistoryList.get(position).getUpUserName());
            txTitle.setText(errorHistoryList.get(position).getTitle());
        }
    }
}
