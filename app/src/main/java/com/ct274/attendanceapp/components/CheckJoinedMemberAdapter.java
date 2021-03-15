package com.ct274.attendanceapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.listeners.WatchCheckedListener;
import com.ct274.attendanceapp.models.Enroll;
import com.ct274.attendanceapp.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckJoinedMemberAdapter extends ArrayAdapter<Enroll> {

    ArrayList<Enroll> enrolls;
    Context myContext;
    WatchCheckedListener watchCheckedListener;
    private boolean readOnly = false;

    public CheckJoinedMemberAdapter(@NonNull Context context, ArrayList<Enroll> enrolls, WatchCheckedListener watchCheckedListener) {
        super(context, R.layout.member_check_joined_row, enrolls);
        this.myContext = context;
        this.enrolls = enrolls;
        this.watchCheckedListener = watchCheckedListener;
    }

    public CheckJoinedMemberAdapter(@NonNull Context context, ArrayList<Enroll> enrolls) {
        super(context, R.layout.member_check_joined_row, enrolls);
        this.myContext = context;
        this.enrolls = enrolls;
        this.readOnly = true;
    }

    private class ViewHolder {
        TextView username, full_name;
        CircleImageView avatar;
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
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(enroll != null) {
            CheckBox checkJoined = convertView.findViewById(R.id.check_joined);

            if(readOnly) {
                checkJoined.setEnabled(false);
            }

            checkJoined.setChecked(enroll.isJoined());
            checkJoined.setOnClickListener(v -> {
                boolean checked = ((CompoundButton) v).isChecked();
                if(watchCheckedListener != null) {
                    watchCheckedListener.onCheckChange(checked, enroll);
                }
            });
            viewHolder.full_name.setText(enroll.getEnroller().getFirst_name() + " " + enroll.getEnroller().getLast_name());
            viewHolder.username.setText(enroll.getEnroller().getUsername());
        }

        return convertView;
    }



}
