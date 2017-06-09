package com.junova.ms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Part;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-30.
 */

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.ViewHolder> {
    List<Part> partList;
    OnItemClickListener onItemClickListener;

    public PartAdapter() {
        super();
        partList = new ArrayList<>();
    }

    public void addPartList(List<Part> partList) {
        this.partList.clear();
        this.partList.addAll(partList);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        return partList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_spiner_tx)
        TextView tx;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(getAdapterPosition());
                    }
                }
            });
        }

        private void onBind(int position) {
            tx.setText(partList.get(position).getPartName());
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
