package com.junova.ms.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.SelectMission;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-04-23.
 */

public class SelectMissionTableAdapter extends RecyclerView.Adapter<SelectMissionTableAdapter.ViewHolder> {
    TableItemClickListener clickListener;
    List<SelectMission> missionList;

    public SelectMissionTableAdapter() {
        missionList = new ArrayList<>();

    }

    public void addMissionTable(List<SelectMission> missionTables) {
        this.missionList.clear();
        this.missionList.addAll(missionTables);
        notifyDataSetChanged();
    }

    public void clearList() {
        missionList.clear();
        notifyDataSetChanged();
    }

    public void setClickListener(TableItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return missionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        @Bind(R.id.item_mission_tx_date)
        TextView txDate;
        @Bind(R.id.item_mission_cb)
        AppCompatCheckBox checkBox;
        @Bind(R.id.item_mission_tx_title)
        TextView txTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

        }

        void onBind(final int position) {
            SelectMission missionTable = missionList.get(position);

            txTitle.setText(missionTable.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        Logger.d(getAdapterPosition() + " position ");
                        clickListener.onClicked(missionList.get(position).getMissionTableId());
                    }

                }
            });
        }
    }

    public interface TableItemClickListener {
        void onClicked(String missionTableId);
    }
}
