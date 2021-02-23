package com.ct274.attendanceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ct274.attendanceapp.components.AttendanceAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Home extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        User myUser = new User("b1709272", "b1709272@student.ctu.edu.vn", "Dung", "Dang");
        UserProfile myProfile = new UserProfile(myUser, myUser.getFirst_name() + " " + myUser.getLast_name(), "Technology", "");
        ArrayList<Attendance> attendances = new ArrayList<>();

        attendances.addAll(Arrays.asList(
                new Attendance("1", "Lorem Ipsum", "14:00", "16:00", "2021-03-01", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("2", "Fusce ut placerat", "13:00", "16:00", "2021-03-01", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile)
                ));
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this, attendances);


        ListView attendanceListView = findViewById(R.id.newest_attendance_listview);
        attendanceListView.setAdapter(attendanceAdapter);

        attendanceListView.setOnItemClickListener((parent, view, position, id) -> {
            Attendance attendance = attendances.get(position);
            Intent intent = new Intent(Home.this, AttendanceDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("attendance_id", attendance.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }
}