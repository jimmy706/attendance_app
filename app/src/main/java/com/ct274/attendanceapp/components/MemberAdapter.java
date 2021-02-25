package com.ct274.attendanceapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.models.User;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<User> {
    private ArrayList<User> members;
    private Context myContext;
    private String attendanceId;
    public MemberAdapter(@NonNull Context context, ArrayList<User> members, String attendanceId) {
        super(context, R.layout.member_row, members);
        this.members = members;
        this.attendanceId = attendanceId;
        this.myContext = context;
    }

    private class ViewHolder {
        TextView username, full_name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = members.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.member_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.username = convertView.findViewById(R.id.member_username);
            viewHolder.full_name = convertView.findViewById(R.id.member_full_name);

            ImageButton toggleButton = convertView.findViewById(R.id.toggle_member_menu);
            toggleButton.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(myContext, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.attendance_member_row_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()){
                        case R.id.join_attendance:
                            return true;
                        case R.id.remove_attendance:
                            return true;
                        default:
                            return false;
                    }
                });
            });
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(user.getUsername());
        viewHolder.full_name.setText(user.getFirst_name() + " " + user.getLast_name());

        return convertView;
    }
}
