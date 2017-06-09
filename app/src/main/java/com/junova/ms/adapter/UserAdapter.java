package com.junova.ms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-31.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<User> userList;
    UserAdapter.OnItemClickListener onItemClickListener;

    public UserAdapter() {
        userList = new ArrayList<>();
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addUserList(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        this.notifyDataSetChanged();
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_spiner_tx)
        TextView txTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        void onBind(int position) {
            txTitle.setText(userList.get(position).getUserName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
