package com.ct274.attendanceapp.components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.helpers.StringHandle;
import com.ct274.attendanceapp.models.Attendance;

import java.util.ArrayList;


public class AttendanceAdapter extends ArrayAdapter<Attendance>  {
    Context myContext;
    ArrayList<Attendance> data;
    public AttendanceAdapter(@NonNull Context context, ArrayList<Attendance> data) {
        super(context, R.layout.attendance_row, data);
        this.myContext = context;
        this.data = data;
    }

    private static class ViewHolder {
        TextView title, day, start_time, end_time, description, full_name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Attendance attendanceItem = data.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.attendance_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.attendance_title);
            viewHolder.day =  convertView.findViewById(R.id.day);
            viewHolder.start_time = convertView.findViewById(R.id.start_time);
            viewHolder.end_time = convertView.findViewById(R.id.end_time);
            viewHolder.description = convertView.findViewById(R.id.description);
            viewHolder.full_name = convertView.findViewById(R.id.full_name);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        viewHolder.title.setText(attendanceItem.getTitle());
        viewHolder.day.setText(StringHandle.formatDate(attendanceItem.getDay()));
        viewHolder.start_time.setText(attendanceItem.getStart_time());
        viewHolder.end_time.setText(attendanceItem.getEnd_time());
        viewHolder.description.setText(attendanceItem.getDescription());
        viewHolder.full_name.setText(attendanceItem.getCreator().getFull_name());
        return convertView;
    }


}
