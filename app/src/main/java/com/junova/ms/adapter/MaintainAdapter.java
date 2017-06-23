package com.junova.ms.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.RepairMission;
import com.junova.ms.maintain.maintaindetail.MaintainDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by yangshuang on 2016/3/31.
 * Description :
 */
public class MaintainAdapter extends RecyclerView.Adapter<MaintainAdapter.MyViewHolder> {
    private Context context;
    private List<RepairMission> list = new ArrayList<>();

    public MaintainAdapter(Context context) {
        super();
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintain, parent, false);
        return new MaintainAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //   holder.txNumber.setText((position + 1) + "");
        holder.onBind(position);
    }

    public void addList(List<RepairMission> list) {
        if (list != null) {
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }

    }

    public RepairMission getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_maintain_tx_title)
        TextView txTitle;
        @Bind(R.id.item_maintain_tx_uper)
        TextView txUper;
        @Bind(R.id.item_maintain_tx_date)
        TextView txDate;
        @Bind(R.id.maintain_item_bt_detail)
        Button btDetail;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(final int position) {
            int index = position + 1;
            txTitle.setText(index + "." + getItem(position).getTitle());
            txDate.setText(getItem(position).getUpTime());
            txUper.setText("提交人：" + getItem(position).getUpUserName());
            btDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, MaintainDetailActivity.class);
                    intent.putExtra("upUserId", getItem(position).getUpUserId());
                    intent.putExtra("time", getItem(position).getUpTime());
                    intent.putExtra("repairsMissionId", getItem(position).getRepairsMissionId());
                    intent.putExtra("dealUserId", getItem(position).getDealUserId());
                    intent.putExtra("upUser", getItem(position).getUpUserName());

                    context.startActivity(intent);
                }
            });
        }
    }
}
