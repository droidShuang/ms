package com.junova.ms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2017-03-16.
 */

public class SpinnerPartAdapter extends BaseAdapter {
    private List<Part> partList;

    public SpinnerPartAdapter() {
        partList = new ArrayList<>();
        partList.add(new Part());
        partList.add(new Part("lalala", "121321"));
    }

    public void addPartList(List<Part> parts) {
        partList.clear();
        partList.add(new Part());
        partList.addAll(parts);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return partList.size();
    }

    @Override
    public Part getItem(int position) {
        return partList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        TextView txTitle = (TextView) view.findViewById(R.id.item_spiner_tx);
        txTitle.setText(partList.get(position).getPartName());
        return view;
    }
}
