package com.junova.ms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2017-03-27.
 */

public class SpinnerUserAdapter extends BaseAdapter {
    List<User> users;

    public SpinnerUserAdapter() {
        users = new ArrayList<>();
        users.add(new User());
        users.add(new User("adasfas", "1234124"));
    }

    public void addUserList(List<User> users) {
        this.users.clear();
        this.users.add(new User());
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        TextView txTitle = (TextView) view.findViewById(R.id.item_spiner_tx);
        txTitle.setText(users.get(position).getUserName());
        return view;
    }
}
