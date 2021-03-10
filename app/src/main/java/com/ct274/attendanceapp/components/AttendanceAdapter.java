package com.ct274.attendanceapp.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.helpers.StringHandle;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;


public class AttendanceAdapter extends ArrayAdapter<Attendance>  {
    Context myContext;
    ArrayList<Attendance> data;
    private AttendanceRequests attendanceRequests = new AttendanceRequests();
    private String accessToken = "";

    public AttendanceAdapter(@NonNull Context context, ArrayList<Attendance> data) {
        super(context, R.layout.attendance_row, data);
        this.myContext = context;
        this.data = data;
    }



    private static class ViewHolder {
        TextView title, day, start_time, end_time, description, username;
        CircleImageView avatar;
    }


    @SuppressLint("ResourceAsColor")
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
            viewHolder.username = convertView.findViewById(R.id.username);
            viewHolder.avatar = convertView.findViewById(R.id.creator_avatar);
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
        viewHolder.username.setText(attendanceItem.getCreator().getAccount().getUsername());
        String avatarPath = "https://ui-avatars.com/api/?name=" + attendanceItem.getCreator().getFull_name() +  "&background=0D8ABC&color=fff&rounded=true";
        ToggleButton registerButton = convertView.findViewById(R.id.register_button);

        registerButton.setChecked(attendanceItem.isRegistered());

        SharedPreferences sharedPreferences = myContext.getSharedPreferences("shared_token", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access", "");

        registerButton.setOnClickListener(v -> {
            if(attendanceItem.isRegistered()) {
                leaveMeeting(accessToken, attendanceItem.getId());
            }
            else {
                joinMeeting(accessToken, attendanceItem.getId());
            }
        });

        Picasso.get().load(avatarPath)
                .error(R.drawable.user_circle_icon)
                .placeholder(R.drawable.user_circle_icon)
                .into(viewHolder.avatar);

        return convertView;
    }

    private void joinMeeting(String token, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.requestJoinMeeting(token, id);
                    System.out.println(response.body().string());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void leaveMeeting(String token, String id) {
        try {
            Response response = attendanceRequests.requestLeaveMeeting(token, id);
            System.out.println(response.body().string());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
