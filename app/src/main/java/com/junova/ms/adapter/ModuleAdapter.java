package com.junova.ms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Module;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-30.
 */

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    List<Module> moduleList;
    OnItemClickListener onItemClickListener;

    public ModuleAdapter() {
        moduleList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addModuleList(List<Module> moduleList) {
        this.moduleList.clear();
        this.moduleList.addAll(moduleList);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
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
            txTitle.setText(moduleList.get(position).getModuleName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
