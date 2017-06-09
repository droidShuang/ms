package com.junova.ms.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.utils.PrefUtils;

import java.util.ArrayList;
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

/**
 * Created by junova on 2017-02-28.
 */

public class MissionTable2Adapter extends RecyclerView.Adapter<MissionTable2Adapter.ViewHolder> {
    List<MissionTable> missionTables;
    private TableItemClickListener clickListener;

    public MissionTable2Adapter() {
        missionTables = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission2, parent, false);
        return new ViewHolder(view);
    }

    public void clearList() {
        missionTables.clear();
        notifyDataSetChanged();
    }

    public TableItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(TableItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return missionTables.size();
    }

    public void addMissionTable(List<MissionTable> missionTables) {
        this.missionTables.clear();
        this.missionTables.addAll(missionTables);
        notifyDataSetChanged();
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClicked(missionTables.get(getAdapterPosition()).getMissionTableId());
                    }

                }
            });
        }

        void onBind(int position) {
            MissionTable missionTable = missionTables.get(position);
       //     txDate.setText(missionTable.getTime());
            txTitle.setText(missionTable.getTitle());
        }
    }

    public interface TableItemClickListener {
        void onClicked(String missionTableId);
    }
}
