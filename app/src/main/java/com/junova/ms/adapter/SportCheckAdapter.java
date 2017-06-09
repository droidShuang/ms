package com.junova.ms.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.MissionItem;
import com.junova.ms.bean.MissionTable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-06.
 */

public class SportCheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<MissionItem> missionItemList;
    List<MissionTable> missionTableList;
    private int type;

    public SportCheckAdapter(int type, List<MissionTable> missionTableList, List<MissionItem> missionItemList) {
        this.type = type;
        this.missionTableList = missionTableList;
        this.missionItemList = missionItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (type) {
            case 0:
                View zeroView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission2, parent, false);
                return new ViewHolderZero(zeroView);
            case 1:
                View oneView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission2, parent, false);
                return new ViewHolderOne(oneView);
            case 2:
                View twoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission2, parent, false);
                return new ViewHolderTwo(twoView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public List<MissionItem> getMissionItemList() {
        return missionItemList;
    }

    public void setMissionItemList(List<MissionItem> missionItemList) {
        this.missionItemList = missionItemList;
    }

    public List<MissionTable> getMissionTableList() {
        return missionTableList;
    }

    public void setMissionTableList(List<MissionTable> missionTableList) {
        this.missionTableList = missionTableList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    class ViewHolderZero extends RecyclerView.ViewHolder {

        View itemView;
        @Bind(R.id.item_mission_tx_date)
        TextView txDate;
        @Bind(R.id.item_mission_cb)
        AppCompatCheckBox checkBox;
        @Bind(R.id.item_mission_tx_title)
        TextView txTitle;

        public ViewHolderZero(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void onBind(int position) {

        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @Bind(R.id.item_detail_name)
        TextView txName;//检测项名称
        @Bind(R.id.item_detail_state)
        RadioGroup radioGroup;//检测项状态
        @Bind(R.id.item_detail_layout)
        RelativeLayout layout;

        @Bind(R.id.item_detail_bt_normal)
        RadioButton radioButtonNormal;
        @Bind(R.id.item_detail_bt_erro)
        RadioButton radioButtonError;
        View itemView;

        public ViewHolderOne(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            this.itemView = itemView;
        }

        private void onBind(int position, MissionDetailAdapter.DetailItemOnClickListener itemOnClickListener) {
            MissionItem item = missionItemList.get(position);
            txName.setText(item.getTitle());
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                }
            });
            if (itemOnClickListener != null) {
                itemOnClickListener.onClick();
            }


        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {
        @Bind(R.id.item_detail_bt_number)
        Button btValue;
        @Bind(R.id.item_detail_name)
        TextView txTitle;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void onBind(int position) {

        }
    }
}
