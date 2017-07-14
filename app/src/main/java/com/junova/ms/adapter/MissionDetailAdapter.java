package com.junova.ms.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.Record;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.utils.DateUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.MissionItemErrorPopWindow;
import com.junova.ms.widgt.MissionItemErrorSportPopWindow;
import com.junova.ms.widgt.OperationTipPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by junova on 2017-02-23.
 */

public class MissionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ONE = 0x1;
    private static final int TYPE_TWO = 0x2;
    private List<MissionDetail> missionDetailList;
    private DetailItemOnClickListener itemOnClickListener;
    List<Error> errors;
    private String upStatus = "";

    public MissionDetailAdapter(String upStatus) {
        missionDetailList = new ArrayList<>();
        errors = new ArrayList<>();
        this.upStatus = upStatus;
    }

    public void addMissionItemList(List<MissionDetail> missionDetailList, List<Error> errors) {
        this.missionDetailList.clear();
        this.missionDetailList.addAll(missionDetailList);
        this.errors.clear();
        this.errors.addAll(errors);
        notifyDataSetChanged();
    }

    public void addMissionItemList(List<MissionDetail> missionDetailList) {
        this.missionDetailList.clear();
        this.missionDetailList.addAll(missionDetailList);
        notifyDataSetChanged();
    }

    public void setItemOnClickListener(DetailItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (missionDetailList.get(position).getIsValue() == 2)
            return TYPE_ONE;
        else
            return TYPE_TWO;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
            return new ViewHolderOne(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail2, parent, false);
            return new ViewHolderTwo(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ONE:
                ((ViewHolderOne) holder).onBind(position, itemOnClickListener);
                break;
            case TYPE_TWO:
                ((ViewHolderTwo) holder).onBind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return missionDetailList.size();
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {
        @Bind(R.id.item_detail_bt_number)
        Button btValue;
        @Bind(R.id.item_detail_name)
        TextView txTitle;
        View itemView;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;

        }

        private void onBind(final int position) {
            final MissionDetail item = missionDetailList.get(position);
            txTitle.setText((position + 1) + "." + item.getTitle());
            if (item.getStatus() == 1) {

                btValue.setTextColor(Color.RED);
                btValue.setText(item.getValue());
            } else if (item.getStatus() == 0) {
                btValue.setTextColor(Color.GREEN);
                btValue.setText(item.getValue());
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ArrayList<String> urls = (ArrayList<String>) item.getOperationTipPic();
                    OperationTipPopWindow tipPopWindow = new OperationTipPopWindow(itemView.getContext(), item.getOperationTipText(), urls);
                    tipPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                    tipPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                    tipPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);
                    return true;
                }
            });
            btValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_value, null);
                    final EditText edVaue = (EditText) view.findViewById(R.id.dialog_et_value);
                    final Record record = new Record();
                    builder.setTitle("请输入数值");
                    builder.setView(view);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edVaue.getText().toString().isEmpty()) {
                                Toasty.info(v.getContext(), "请输入数值").show();
                                return;
                            }
                            int value = Integer.parseInt(edVaue.getText().toString());

                            int currentStatus = 0;
                            //判断数值是否在合理范围内
                            if (value > item.getUpValue() || value < item.getDownValue()) {
                                btValue.setTextColor(Color.RED);
                                btValue.setText(value + "");
                                currentStatus = 1;
                            } else {
                                btValue.setTextColor(Color.GREEN);
                                btValue.setText(value + "");
                                currentStatus = 0;
                            }

                            record.setStatus(currentStatus);
                            record.setMissionItemId(item.getMissionDetailId());

                            record.setCheckTime(DateUtil.getTimestamp());
                            record.setValue(value + "");

                            MsDbManger.getInstance(v.getContext()).updataRecord(item.getMissionDetailId(), record);

                            missionDetailList.get(position).setValue(value + "");
                            missionDetailList.get(position).setStatus(currentStatus);
                            MsDbManger.getInstance(v.getContext()).updataMissionDetail(item.getMissionDetailId(), record.getStatus(), record.getValue());
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (record.getStatus() == 1) {
                                if (PrefUtils.getInt(v.getContext(), "station", 5) < 4) {
                                    final MissionItemErrorSportPopWindow missionItemErrorSportPopWindow = new MissionItemErrorSportPopWindow(itemView.getContext(), item.getMissionDetailId(), item.getStatus(), upStatus);
                                    missionItemErrorSportPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                                    missionItemErrorSportPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                                    missionItemErrorSportPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);
                                } else {
                                    final MissionItemErrorPopWindow missionItemErrorPopWindow = new MissionItemErrorPopWindow(itemView.getContext(), item.getMissionDetailId(), errors, item.getStatus(), upStatus);
                                    missionItemErrorPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                                    missionItemErrorPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                                    missionItemErrorPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);

                                }
                            }
                        }
                    });
                    dialog.show();
                }

            });
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
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }

        private void onBind(final int position, DetailItemOnClickListener itemOnClickListener) {
            final MissionDetail item = missionDetailList.get(position);
            txName.setText((position + 1) + "." + item.getTitle());
            if (item.getStatus() == 1) {
                radioButtonError.setChecked(true);
            } else if (item.getStatus() == 0) {
                radioButtonNormal.setChecked(true);
            }

            radioButtonNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (upStatus.equals("1")) {
                        Toasty.info(itemView.getContext(), "已经提交，不可更改").show();
                        if (item.getStatus() == 1) {
                            radioButtonError.setChecked(true);
                        } else if (item.getStatus() == 0) {
                            radioButtonNormal.setChecked(true);
                        }
                    } else {
                        if (item.getStatus() == 1) {
                            Record record = new Record();
                            record.setStatus(0);
                            record.setCheckTime(DateUtil.getTimestamp());
                            MsDbManger.getInstance(v.getContext()).updataRecord(item.getMissionDetailId(), record);
                            MsDbManger.getInstance(v.getContext()).updataMissionDetail(item.getMissionDetailId(), 0, "");
                        }
                    }

                }
            });
            radioButtonError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (PrefUtils.getInt(v.getContext(), "station", 5) < 4) {
                        final MissionItemErrorSportPopWindow missionItemErrorSportPopWindow = new MissionItemErrorSportPopWindow(itemView.getContext(), item.getMissionDetailId(), item.getStatus(), upStatus);
                        missionItemErrorSportPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                missionDetailList.get(position).setStatus(missionItemErrorSportPopWindow.status);
                                if (missionItemErrorSportPopWindow.status == 1) {
                                    radioButtonError.setChecked(true);

                                } else {
                                    radioButtonNormal.setChecked(true);

                                }
                            }
                        });

                        missionItemErrorSportPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        missionItemErrorSportPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        missionItemErrorSportPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);
                    } else {
                        final MissionItemErrorPopWindow missionItemErrorPopWindow = new MissionItemErrorPopWindow(itemView.getContext(), item.getMissionDetailId(), errors, item.getStatus(), upStatus);
                        missionItemErrorPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                missionDetailList.get(position).setStatus(missionItemErrorPopWindow.status);
                                if (missionItemErrorPopWindow.status == 1) {
                                    radioButtonError.setChecked(true);

                                } else {
                                    radioButtonNormal.setChecked(true);
                                }
                            }
                        });

                        missionItemErrorPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        missionItemErrorPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        missionItemErrorPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);

                    }

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ArrayList<String> urls = (ArrayList<String>) item.getOperationTipPic();
                    OperationTipPopWindow tipPopWindow = new OperationTipPopWindow(itemView.getContext(), item.getOperationTipText(), urls);
                    tipPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                    tipPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                    tipPopWindow.showAtLocation(itemView.getRootView(), Gravity.CENTER, 0, 0);
                    return true;
                }
            });


        }
    }

    interface DetailItemOnClickListener {
        void onClick();
    }

}
