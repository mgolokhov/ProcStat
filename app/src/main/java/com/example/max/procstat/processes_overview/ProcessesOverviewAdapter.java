package com.example.max.procstat.processes_overview;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.max.procstat.R;
import com.example.max.procstat.data.ProcessInfo;

import java.util.List;

public class ProcessesOverviewAdapter extends RecyclerView.Adapter<ProcessesOverviewAdapter.ProcessViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String packageName);
    }

    private List<ProcessInfo> processInfos;
    private OnItemClickListener onItemClickListener;

    public ProcessesOverviewAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_process, parent,false);
        return new ProcessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        ProcessInfo processInfoItem = processInfos.get(position);
        holder.bind(processInfoItem, onItemClickListener);
        holder.icon.setImageURI(Uri.parse(processInfoItem.getIconPath()));
        holder.appName.setText(processInfoItem.getName());
        holder.packageName.setText(processInfoItem.getPackageName());
    }

    @Override
    public int getItemCount() {
        return processInfos != null ? processInfos.size(): 0;
    }

    public void setData(List<ProcessInfo> data) {
        this.processInfos = data;
        notifyDataSetChanged();
    }

    static class ProcessViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView icon;
        TextView appName;
        TextView packageName;

        ProcessViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            appName = itemView.findViewById(R.id.appName);
            packageName = itemView.findViewById(R.id.packageName);
        }

        void bind(final ProcessInfo processInfoItem, OnItemClickListener onItemClickListener){
            this.itemView.setOnClickListener(v ->
                    onItemClickListener.onItemClick(processInfoItem.getPackageName())
            );
            this.icon.setImageURI(Uri.parse(processInfoItem.getIconPath()));
            this.appName.setText(processInfoItem.getName());
            this.packageName.setText(processInfoItem.getPackageName());
        }
    }
}
