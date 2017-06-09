package com.junova.ms.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.model.ProjectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2016/10/4 0004.
 */

public class ProjectSpinerAdapter extends BaseAdapter{
    private List<ProjectModel> projectModelList;

    public ProjectSpinerAdapter() {
        super();
        projectModelList = new ArrayList<>();
    }
    public void addProjectList(List<ProjectModel> list){
        projectModelList.clear();
        projectModelList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return projectModelList.size();
    }

    @Override
    public ProjectModel getItem(int position) {
        return projectModelList.get(projectModelList.size()-position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =View.inflate(parent.getContext(), R.layout.item_team,null);
        TextView txName= (TextView) view.findViewById(R.id.item_tx_name);
        txName.setText(getItem(position).getPROJECTNAME());
        return view;
    }
}
