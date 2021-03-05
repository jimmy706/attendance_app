package com.ct274.attendanceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ct274.attendanceapp.components.AttendanceAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;
import com.ct274.attendanceapp.requests.AuthRequests;
import com.ct274.attendanceapp.states.UserState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Response;

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
                new Attendance("4", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("5", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("6", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("7", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile)
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

        ImageButton viewMenu = findViewById(R.id.view_user_menu);
        viewMenu.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, UserMenuActivity.class));
        });

        ImageButton addAttendance = findViewById(R.id.add_new_attendance);
        addAttendance.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, CreateMeetingActivity.class));
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) ,Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(getString(R.string.access_token), "");
        if(!accessToken.isEmpty()) {
            getProfileAndStore(accessToken);
        }
    }

    private void getProfileAndStore(String token) {
        AuthRequests authRequests = new AuthRequests(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = authRequests.getMyProfile(token);
                    String jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    UserState userState = UserState.getInstance();

                    if(jsonObject.has("account")) {
                        JSONObject userJSON = jsonObject.getJSONObject("account");
                        String major = jsonObject.getString("major");
                        String description = jsonObject.getString("description");

                        userState.setFirst_name(userJSON.getString("first_name"));
                        userState.setLast_name(userJSON.getString("last_name"));
                        userState.setUsername(userJSON.getString("username"));
                        userState.setEmail(userJSON.getString("email"));
                        userState.setDescription(description);
                        userState.setMajor(major);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}