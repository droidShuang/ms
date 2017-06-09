package com.junova.ms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2017-03-16.
 */

public class SpinnerModuleAdapter extends BaseAdapter {
    private List<Module> modules;


    public SpinnerModuleAdapter() {
        modules = new ArrayList<>();
    }

    public void addModules(List<Module> modules) {
        this.modules.clear();
        this.modules.addAll(modules);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public Module getItem(int position) {
        return modules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        TextView txTitle = (TextView) view.findViewById(R.id.item_spiner_tx);
        txTitle.setText(modules.get(position).getModuleName());
        return view;
    }
}
