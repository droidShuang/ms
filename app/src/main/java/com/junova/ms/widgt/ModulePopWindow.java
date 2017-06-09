package com.junova.ms.widgt;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.junova.ms.R;
import com.junova.ms.adapter.ModuleAdapter;
import com.junova.ms.adapter.PartAdapter;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-30.
 */

public class ModulePopWindow extends PopupWindow {
    @Bind(R.id.pop_rv_part)
    RecyclerView rvPart;
    List<Module> moduleList;
    Context context;
    ModuleAdapter adapter;
    public String moduleId = "";
    public String moduleName = "";

    public ModulePopWindow(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
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
        adapter = new ModuleAdapter();
        rvPart.setAdapter(adapter);
        adapter.setOnItemClickListener(new ModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                moduleId = moduleList.get(position).getModuleId();
                moduleName = moduleList.get(position).getModuleName();
                ModulePopWindow.this.dismiss();
            }
        });
        adapter.addModuleList(moduleList);
    }
}
