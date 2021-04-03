package com.ct274.attendanceapp.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.listeners.WatchRemoveMember;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class MemberAdapter extends ArrayAdapter<User> {
    private ArrayList<User> members;
    private Context myContext;
    private WatchRemoveMember watchRemoveMember;
    private Attendance attendance;
    public MemberAdapter(@NonNull Context context, ArrayList<User> members, Attendance attendance) {
        super(context, R.layout.member_row, members);
        this.members = members;
        this.attendance = attendance;
        this.myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = members.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.member_row, parent, false);
            TextView username = convertView.findViewById(R.id.member_username);
            username.setText(user.getUsername());
            TextView full_name = convertView.findViewById(R.id.member_full_name);
            full_name.setText(user.getFirst_name() + " " + user.getLast_name());
            CircleImageView avatar = convertView.findViewById(R.id.member_avatar);
            String imagePath = "https://ui-avatars.com/api/?name=" + user.getFirst_name()  + " " + user.getLast_name() +  "&background=0D8ABC&color=fff&rounded=true";

            Picasso.get().load(imagePath)
                    .placeholder(R.drawable.user_circle_icon)
                    .error(R.drawable.user_circle_icon)
                    .into(avatar);

            ImageButton toggleButton = convertView.findViewById(R.id.toggle_member_menu);
            toggleButton.setVisibility(View.GONE);
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
                                removeMember(user.getId(), item.getOrder());
                                return false;
                            default:
                                return false;
                        }
                    });
                });
            }

        }


        return convertView;
    }

    public void onRemoveMember(WatchRemoveMember watchRemoveMember) {
        this.watchRemoveMember = watchRemoveMember;
    }

    private void removeMember(String id, int i) {
       if(watchRemoveMember != null) {
           watchRemoveMember.onRemoveMember(id, i);
       }
    }
}
