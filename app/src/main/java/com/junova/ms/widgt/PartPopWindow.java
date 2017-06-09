package com.junova.ms.widgt;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.adapter.PartAdapter;
import com.junova.ms.bean.Part;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author :杨爽
 * @class name：PartPopWindow
 * @time 2017-03-30 09:50
 * @describe 部门下拉框选择框
 */


public class PartPopWindow extends PopupWindow {
    @Bind(R.id.pop_rv_part)
    RecyclerView rvPart;
    List<Part> partList;
    Context context;
    PartAdapter adapter;
    public String partId = "";
    public String partName = "";

    public PartPopWindow(Context context, List<Part> partList) {
        super(context);
        this.context = context;
        this.partList = partList;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_part, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
        rvPart.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PartAdapter();
        adapter.setOnItemClickListener(new PartAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                partId = partList.get(position).getPartId();
                partName = partList.get(position).getPartName();
                PartPopWindow.this.dismiss();
            }
        });
        rvPart.setAdapter(adapter);
        adapter.addPartList(partList);
    }
}
