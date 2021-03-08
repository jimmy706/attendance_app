package com.ct274.attendanceapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.models.Enroll;
import com.ct274.attendanceapp.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckJoinedMemberAdapter extends ArrayAdapter<Enroll> {

     ArrayList<Enroll> enrolls;
     Context myContext;

    public CheckJoinedMemberAdapter(@NonNull Context context, int resource, ArrayList<Enroll> enrolls) {
        super(context, R.layout.member_check_joined_row, enrolls);
        this.myContext = context;
        this.enrolls = enrolls;
    }

    private class ViewHolder {
        TextView username, full_name;
        CircleImageView avatar;
        CheckBox checkJoined;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Enroll enroll = enrolls.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.member_check_joined_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.username = convertView.findViewById(R.id.member_username);
            viewHolder.full_name = convertView.findViewById(R.id.member_full_name);
            viewHolder.avatar = convertView.findViewById(R.id.member_avatar);
            viewHolder.checkJoined = convertView.findViewById(R.id.check_joined);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkJoined.setChecked(enroll.isJoined());

        return convertView;
    }
}
