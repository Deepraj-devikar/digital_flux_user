package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.AttendanceData;

import java.util.List;

public class AdapterAttendance extends RecyclerView.Adapter<AdapterAttendance.AttendanceViewHolder> {

    List<AttendanceData> attendanceDataList;
    Context context;

    public AdapterAttendance(List<AttendanceData> attendanceDataList, Context context) {
        this.attendanceDataList = attendanceDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attendance, viewGroup, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, int i) {
        AttendanceData attendanceData = attendanceDataList.get(i);

        attendanceViewHolder.tv_attendance.setText(attendanceData.getAttendance());
        attendanceViewHolder.tv_attendance_date.setText(attendanceData.getDate());

        if (attendanceData.getAttendance().equals("present")){
            attendanceViewHolder.tv_attendance.setTextColor(context.getResources().getColor(R.color.green_A700));
        }else if (attendanceData.getAttendance().equals("absent")){
            attendanceViewHolder.tv_attendance.setTextColor(context.getResources().getColor(R.color.red_A700));
        }

    }

    @Override
    public int getItemCount() {
        return attendanceDataList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lyt_attendance;
        TextView tv_attendance;
        TextView tv_attendance_date;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            lyt_attendance = itemView.findViewById(R.id.lyt_attendance);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            tv_attendance_date = itemView.findViewById(R.id.tv_attendance_date);

        }
    }
}
