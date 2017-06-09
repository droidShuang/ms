package com.junova.ms.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.model.TeamModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2016/10/4 0004.
 */

public class TeamSpinerAdapter extends BaseAdapter{
    private List<TeamModel> teamList;

    public TeamSpinerAdapter() {
        super();
        teamList = new ArrayList<>();
    }
    public void addTeamList(List<TeamModel> list){
        teamList.clear();
        teamList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return teamList.size();
    }

    @Override
    public TeamModel getItem(int position) {
        return teamList.get(teamList.size()-position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =View.inflate(parent.getContext(), R.layout.item_team,null);
        TextView txName= (TextView) view.findViewById(R.id.item_tx_name);
        txName.setText(getItem(position).getTEAMNAME());
        return view;
    }
}
