package com.junova.ms.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.*;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.MissionTable;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangshuang on 2016/3/25.
 * Description :
 */
public class MissionTableAdapter extends Adapter<MissionTableAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<MissionTable> list;
    private String undo = "0";

    public MissionTableAdapter(Context context) {
        super();
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void addList(List<MissionTable> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public List<MissionTable> getList() {
        return list;
    }

    public String getUndo() {
        return undo;
    }

    private MissionTable getItem(int position) {
        return list.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MissionTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.onBind(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {
        @Bind(R.id.item_mission_tx_number)
        TextView txNubmer;//任务序号


        @Bind(R.id.item_mission_tx_date)

        TextView txDate;//任务日期

        @Bind(R.id.item_mission_tx_state)
        TextView txState;//任务状态
        @Bind(R.id.item_mission_tx_name)
        TextView txName;//任务名称
        @Bind(R.id.item_mission)
        CardView layout;
        @Bind(R.id.item_mission_tx_isup)
        TextView txIsUp;
        @Bind(R.id.item_iv_isup)
        ImageView ivIsup;
        boolean isInTime = true;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            final MissionTable table = getItem(position);

//            if (!table.getStartTime().isEmpty() && !table.getEndTime().isEmpty()) {
//                Date date = new Date();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                try {
//                    Date startTime = dateFormat.parse(table.getStartTime());
//                    Date endTime = dateFormat.parse(table.getEndTime());
//                    Date currentTime = dateFormat.parse(dateFormat.format(date));
//                    if (currentTime.before(endTime) && currentTime.after(startTime)) {
//                        isInTime = true;
//                    } else {
//                        isInTime = false;
//                    }
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    isInTime = true;
//                }
//            } else {
//                isInTime = true;
//            }
            String missionStatus = table.getNormalCount() + "/" + table.getErrorCount() + "/" + table.getTotalCount();
            SpannableString spanString = new SpannableString(missionStatus);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            String strDate[] = missionStatus.split("/");
            spanString.setSpan(greenSpan, 0, strDate[0].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(redSpan, strDate[0].length() + 1, strDate[0].length() + strDate[1].length() + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(blackSpan, strDate[0].length() + strDate[1].length() + 2, missionStatus.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            txState.setText(spanString);
            txNubmer.setText((position + 1) + "");
            txName.setText(table.getTitle());
            txDate.setText("任务下发时间：" + table.getTime());
            if (table.getStatus().equals("1")) {
//                txIsUp.setVisibility(View.VISIBLE);
//                txIsUp.setText("已上传");
                ivIsup.setVisibility(View.VISIBLE);
            } else {
                ivIsup.setVisibility(View.INVISIBLE);
            }
            //设置item的点击事件
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInTime) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(table.getMissionTableId(), table.getModuleId(), table.getIdentifyingCode(), table.getStatus());
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示:");
                        builder.setMessage("该任务需在" + table.getStartTime() + "-" + table.getEndTime() + "内执行");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                    }

                }
            });
            //item的长按事件
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemLongClick(table);
                    }
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String missionTableId, String modileId, String identifyingCode, String status);

        void onItemLongClick(MissionTable missionTable);
    }
}
