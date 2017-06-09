package com.junova.ms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.Module;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by junova on 2017-03-21.
 */

public class SpinerErrorAdapter extends RecyclerView.Adapter<SpinerErrorAdapter.ErrorViewHolder> {

    SpinerErrorAdapter.OnItemClickListener onItemClickListener;
    List<Error> errorList;

    public SpinerErrorAdapter() {
        errorList = new ArrayList<>();
    }

    @Override
    public ErrorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent,false);

        return new ErrorViewHolder(view);
    }

    public void addErrorList(List<Error> errors) {
        errorList.clear();
        errorList.addAll(errors);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ErrorViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return errorList.size();
    }

    class ErrorViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_spiner_tx)
        TextView txTitle;

        public ErrorViewHolder(View itemView) {
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
            txTitle.setText(errorList.get(position).getErrorName());
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
