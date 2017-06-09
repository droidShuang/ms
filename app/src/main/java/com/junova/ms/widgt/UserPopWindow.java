package com.junova.ms.widgt;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.junova.ms.R;

import com.junova.ms.adapter.UserAdapter;

import com.junova.ms.bean.User;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-31.
 */

public class UserPopWindow extends PopupWindow {
    @Bind(R.id.pop_rv_part)
    RecyclerView rvPart;
    List<User> userList;
    Context context;
    UserAdapter adapter;
    public String userId = "";
    public String userName = "";

    public UserPopWindow(Context context, List<User> userList) {
        super(context);
        this.context = context;
        this.userList = userList;
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
        adapter = new UserAdapter();
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                userId = userList.get(position).getUserId();
                userName = userList.get(position).getUserName();
                UserPopWindow.this.dismiss();
            }
        });
        rvPart.setAdapter(adapter);
        adapter.addUserList(userList);
    }
}
