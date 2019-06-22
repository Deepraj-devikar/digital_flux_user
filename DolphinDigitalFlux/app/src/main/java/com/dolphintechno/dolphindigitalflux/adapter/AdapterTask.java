package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.media.tv.TvView;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.TaskData;

import java.util.List;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.TaskViewHolder> {

    List<TaskData> taskDataList;
    Context context;

    public AdapterTask(List<TaskData> taskDataList, Context context) {
        this.taskDataList = taskDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {

        TaskData taskData = taskDataList.get(i);

        taskViewHolder.tv_task_status.setText(taskData.getTaskStatus());
        taskViewHolder.tv_task_date.setText(taskData.getTaskDate());

    }

    @Override
    public int getItemCount() {
        return taskDataList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tv_task_status, tv_task_date;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_task_status = (TextView) itemView.findViewById(R.id.tv_task_status);
            tv_task_date = (TextView) itemView.findViewById(R.id.tv_task_date);
        }
    }
}
