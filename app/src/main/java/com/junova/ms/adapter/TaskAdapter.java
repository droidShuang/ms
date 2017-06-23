package com.junova.ms.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.ms.R;
import com.junova.ms.app.App;
import com.junova.ms.bean.Exception;
import com.junova.ms.model.TaskItemInfoModel;
import com.junova.ms.model.TaskRecordModel;
import com.junova.ms.utils.CommonUntil;
import com.junova.ms.widgt.MyGridView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangshuang on 2016/3/29.
 * Description :任务列表适配器
 */

public class TaskAdapter  {
//    private Context context;
//    private ExpandableListView listView;
//    private OnItemClickListener onItemClickListener;//item点击监听器
//    private List<TaskItemInfoModel> list = new ArrayList<>();//任务集合
//    private HashMap<String, String> flagMap = new HashMap<>();
//
//    private HashMap<Integer, Exception> exceptionHashMap = new HashMap<>();
//    private List<TaskRecordModel> recordModelList;
//    private List<TaskItemInfoModel.ErrorKind> errorKindList;
//    private String taskTableId = "";
//    private String projectId = "";
//
//    public List<TaskRecordModel> getRecordModelList() {
//        return recordModelList;
//    }
//
//    public TaskAdapter(Context context, ExpandableListView listView, String projectId, String taskTableId) {
//        this.context = context;
//        this.listView = listView;
//        this.projectId = projectId;
//        this.taskTableId = taskTableId;
//        recordModelList = new ArrayList<>();
//        errorKindList = new ArrayList<>();
//
//    }
//
//    public HashMap<String, String> getFlagMap() {
//        return flagMap;
//    }
//
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public void addList(final List<TaskItemInfoModel> list) {
//        this.list.clear();
//        errorKindList.clear();
//        this.list.addAll(list);
//        errorKindList.addAll(list.get(0).getERRORKINDLIST());
//
//        Observable.create(new Observable.OnSubscribe<TaskRecordModel>() {
//            @Override
//            public void call(Subscriber<? super TaskRecordModel> subscriber) {
//                for (TaskItemInfoModel item : list) {
//                    subscriber.onNext(App.getDbManger().readRecord(item.getITEMID()));
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<TaskRecordModel>() {
//            @Override
//            public void call(TaskRecordModel taskRecordModel) {
//                recordModelList.add(taskRecordModel);
//
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        }, new Action0() {
//            @Override
//            public void call() {
//
//                if (recordModelList.size() > 0) {
//                    if (list.size() == recordModelList.size()) {
//
//                        notifyDataSetChanged();
//                    }
//                } else {
//                    notifyDataSetChanged();
//                }
//
//            }
//        });
//
//
//    }
//
//    public List<TaskItemInfoModel> getList() {
//        return this.list;
//    }
//
//    @Override
//    public int getGroupCount() {
//        return list.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return 1;
//    }
//
//    @Override
//    public TaskItemInfoModel getGroup(int groupPosition) {
//        return list.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return null;
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return 0;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return 0;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//        final GroupHolder holder;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_detail, null);
//            holder = new GroupHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (GroupHolder) convertView.getTag();
//        }
//        holder.onBind(groupPosition);
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(final int groupPosition, int childPosition,
//                             boolean isLastChild, View convertView, ViewGroup parent) {
//        final ChildHolder holder;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_detail_child, null);
//            holder = new ChildHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ChildHolder) convertView.getTag();
//        }
//        holder.onBind(groupPosition);
//
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return false;
//    }
//
//    class GroupHolder {
//        @Bind(R.id.item_detail_group_name)
//        TextView txName;//检测项名称
//        @Bind(R.id.item_detail_group_state)
//        RadioGroup radioGroup;//检测项状态
//        @Bind(R.id.item_detail_group_layout)
//        RelativeLayout layout;
//        @Bind(R.id.item_detail_group_number)
//        TextView txNumber;
//        @Bind(R.id.item_detail_child_bt_normal)
//        RadioButton radioButtonNormal;
//        @Bind(R.id.item_detail_child_bt_erro)
//        RadioButton radioButtonError;
//
//
//        public GroupHolder(View view) {
//
//            ButterKnife.bind(this, view);
//        }
//
//        public void onBind(final int position) {
//            radioGroup.setTag(new Integer(position));
//            txName.setText(getGroup(position).getTITLE());
//            txNumber.setText((position + 1) + "");
//
//
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            layout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemLongClick(position);
//                    }
//                    return false;
//                }
//            });
//
//            if (recordModelList.size() == list.size() && recordModelList.get(position) != null) {
//
//                switch (recordModelList.get(position).getStatus()) {
//                    case "1":
//
//                        radioGroup.check(R.id.item_detail_child_bt_normal);
//                        break;
//                    case "2":
//                        radioGroup.check(R.id.item_detail_child_bt_erro);
//                        if (!listView.isGroupExpanded(position)) {
//                            listView.expandGroup(position);
//
//                        }
//                        break;
//                    default:
//                        radioGroup.clearCheck();
//                        break;
//                }
//
//            } else {
//                if (recordModelList.size() == list.size()) {
//                    TaskRecordModel recordModel = new TaskRecordModel();
//                    recordModel.setStatus("1");
//                    recordModel.setProjectId(projectId);
//                    recordModel.setIsUpload("false");
//                    recordModel.setTaskTableId(taskTableId);
//                    recordModel.setTaskItemId(list.get(position).getITEMID());
//                    recordModel.setDate(CommonUntil.getCheckTime());
//                    recordModelList.set(position, recordModel);
//                }
//
//            }
//            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    Integer pos = (Integer) group.getTag();
//                    switch (checkedId) {
//                        case R.id.item_detail_child_bt_normal:
//
//                            if (listView.isGroupExpanded(pos)) {
//                                listView.collapseGroup(pos);
//                                recordModelList.get(pos).setStatus("1");
//                                notifyDataSetChanged();
//
//                            }
//
//                            break;
//                        case R.id.item_detail_child_bt_erro:
//
//                            if (!listView.isGroupExpanded(pos)) {
//                                listView.expandGroup(pos);
//                                recordModelList.get(pos).setStatus("2");
//                                notifyDataSetChanged();
//                            }
//                            break;
//                    }
//
//                }
//            });
//        }
//    }
//
//    class ChildHolder {
//        @Bind(R.id.item_detail_child_spiner)
//        Spinner spinnerErroKind;//选择异常状态
//        @Bind(R.id.item_detail_child_edt)
//        EditText etNote;//备注
//        @Bind(R.id.item_detail_child_gv)
//        MyGridView gridViewImag;//图片
//        @Bind(R.id.item_detail_child_bt_save)
//        Button btSave;
//
//        public ChildHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//
//        public void onBind(final int position) {
//
//            SpinerAdapter spinerAdapter = new SpinerAdapter(context, errorKindList);
//            spinnerErroKind.setAdapter(spinerAdapter);
//            final GridImageAdapter gridImageAdapter = new GridImageAdapter(context);
//            gridViewImag.setAdapter(gridImageAdapter);
//            if (!TextUtils.isEmpty(recordModelList.get(position).getErrorId())) {
//                for (int i = 0; i < errorKindList.size(); i++) {
//                    if (errorKindList.get(i).getERRORID().equals(recordModelList.get(position).getErrorId())) {
//                        spinnerErroKind.setSelection(i);
//                        break;
//                    }
//                }
//            }
//            if (!TextUtils.isEmpty(recordModelList.get(position).getDescription())) {
//                etNote.setText(recordModelList.get(position).getDescription());
//            }
//            if (!TextUtils.isEmpty(recordModelList.get(position).getImagePath())) {
//
//                ArrayList<String> pathList = new ArrayList<>();
//                String[] paths = recordModelList.get(position).getImagePath().split(";");
//                for (String path :
//                        paths) {
//                    if (!TextUtils.isEmpty(path)) {
//                        pathList.add(path);
//                    }
//                }
//
//                gridImageAdapter.addImage(pathList);
//            }
//
//            gridViewImag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    if (position == gridImageAdapter.getCount() - 1) {
//                        if (gridImageAdapter.getCount() > 3) {
//                            return;
//                        } else {
//                            //  GalleryFinal.openGalleryMuti(1000, 3 - position, new MyCallback(gridImageAdapter));
//                            if (!gridImageAdapter.getForbiden()) {
//                                showChoosePhotoDialog(position, gridImageAdapter);
//                            }
//                        }
//                    }
//                }
//            });
//            btSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    recordModelList.get(position).setDate(CommonUntil.getCheckTime());
//                    recordModelList.get(position).setDescription(etNote.getText().toString());
//                    Logger.d("record    " + etNote.getText().toString());
//                    recordModelList.get(position).setErrorId(((TaskItemInfoModel.ErrorKind) spinnerErroKind.getSelectedItem()).getERRORID());
//                    recordModelList.get(position).setErrorName(((TaskItemInfoModel.ErrorKind) spinnerErroKind.getSelectedItem()).getERRORNAME());
//                    recordModelList.get(position).setImagePath(gridImageAdapter.getUrlString());
//                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//        }
//    }
//
//    private class MyCallback implements GalleryFinal.OnHanlderResultCallback {
//        private GridImageAdapter adapter;
//        private int position;
//
//        public MyCallback(GridImageAdapter adapter, int position) {
//            this.adapter = adapter;
//            this.position = position;
//        }
//
//        @Override
//        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//            ArrayList<String> urlList = new ArrayList<>();
//            for (PhotoInfo photoInfo : resultList) {
//                urlList.add("file://" + photoInfo.getPhotoPath());
//            }
//            adapter.addImage(urlList);
//            recordModelList.get(position).setImagePath(adapter.getUrlString());
//
//        }
//
//        @Override
//        public void onHanlderFailure(int requestCode, String errorMsg) {
//            Toast.makeText(context, "faliure", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick();
//
//        void onItemLongClick(int position);
//
//    }
//
//    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("提示");
//        builder.setMessage("请先验证");
//        builder.setPositiveButton("确定", null);
//        builder.create().show();
//    }
//
//    private void showChoosePhotoDialog(final int position, final GridImageAdapter gridImageAdapter) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("选择方式");
//        View dialogView = View.inflate(context, R.layout.dialog_chose_photo, null);
//        Button btCamera = (Button) dialogView.findViewById(R.id.bt_camera);
//        Button btGallery = (Button) dialogView.findViewById(R.id.bt_gallery);
//        builder.setView(dialogView);
//        final AlertDialog dialog = builder.create();
//        btCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GalleryFinal.openCamera(1001, new MyCallback(gridImageAdapter, position));
//                dialog.cancel();
//            }
//        });
//        btGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GalleryFinal.openGalleryMuti(1000, 3 - position, new MyCallback(gridImageAdapter, position)); GalleryFinal.openCamera(1001, new MyCallback(gridImageAdapter, position));
//                dialog.cancel();
//            }
//        });
//
//
//        dialog.show();
//
//    }
}
