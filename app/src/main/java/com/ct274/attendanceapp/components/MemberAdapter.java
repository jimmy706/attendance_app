package com.ct274.attendanceapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends ArrayAdapter<User> {
    private ArrayList<User> members;
    private Context myContext;
    private Attendance attendance;
    public MemberAdapter(@NonNull Context context, ArrayList<User> members, Attendance attendance) {
        super(context, R.layout.member_row, members);
        this.members = members;
        this.attendance = attendance;
        this.myContext = context;
    }

    private class ViewHolder {
        TextView username, full_name;
        CircleImageView avatar;
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
            viewHolder.avatar = convertView.findViewById(R.id.member_avatar);

            ImageButton toggleButton = convertView.findViewById(R.id.toggle_member_menu);
            if(!attendance.isHost()) {
                toggleButton.setVisibility(View.GONE);
            }
            else {
                toggleButton.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(myContext, v);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.attendance_member_row_menu, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()){
                            case R.id.remove_attendance:
                                return true;
                            default:
                                return false;
                        }
                    });
                });
            }

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imagePath = "https://ui-avatars.com/api/?name=" + user.getFirst_name()  + " " + user.getLast_name() +  "&background=0D8ABC&color=fff&rounded=true";
        viewHolder.username.setText(user.getUsername());
        viewHolder.full_name.setText(user.getFirst_name() + " " + user.getLast_name());
        Picasso.get().load(imagePath)
                .placeholder(R.drawable.user_circle_icon)
                .error(R.drawable.user_circle_icon)
                .into(viewHolder.avatar);
        return convertView;
    }
}
