package com.ct274.attendanceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ct274.attendanceapp.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class AttendanceDetailActivity extends AppCompatActivity {
    AttendanceRequests attendanceRequests = new AttendanceRequests();
    ProgressBar progressBar;
    LinearLayout contentContainer;
    Attendance attendanceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);


        progressBar = findViewById(R.id.loading_indicator);
        contentContainer = findViewById(R.id.attendance_detail_content);

        Bundle bundle = getIntent().getExtras();
        String attendanceId = "-1";
        if(bundle != null) {
            attendanceId = bundle.getString("attendance_id");
        }

        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(AttendanceDetailActivity.this, Home.class));
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(getString(R.string.access_token), "");
        if(!accessToken.isEmpty()) {
            startLoading();
            fetchMeetingDetail(accessToken, attendanceId);
        }


    }

    private void fetchMeetingDetail(String token, String meetingId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.getMeetingDetail(token, meetingId);
                    if(response.isSuccessful()) {
                        String data = response.body().string();
                        JSONObject jsonData = new JSONObject(data);
                        String description = jsonData.getString("description");
                        String start_time = jsonData.getString("start_time");
                        String end_time = jsonData.getString("end_time");
                        String day = jsonData.getString("day");
                        String title = jsonData.getString("title");
                        boolean is_registered = jsonData.getBoolean("is_registered");
                        boolean is_host = jsonData.getBoolean("is_host");
                        String attendance_key = jsonData.getString("attendance_key");

                        ArrayList<User> members = new ArrayList<>();
                        JSONArray membersJson = jsonData.getJSONArray("members");
                        for(int i = 0; i < membersJson.length(); i++) {
                            JSONObject memberJSON = membersJson.getJSONObject(i);
                            String memberUsername = memberJSON.getString("username");
                            String memberId = memberJSON.getString("id");
                            String memberFirstName = memberJSON.getString("first_name");
                            String memberLastName = memberJSON.getString("last_name");
                            String memberEmail = memberJSON.getString("email");
                            User user = new User(memberId, memberUsername, memberEmail, memberFirstName, memberLastName );
                            members.add(user);
                        }

                        JSONObject creatorJSON = jsonData.getJSONObject("creator");
                        JSONObject creatorAccountJSON = creatorJSON.getJSONObject("account");
                        String major = creatorJSON.getString("major");
                        String firstName = creatorAccountJSON.getString("first_name");
                        String lastName = creatorAccountJSON.getString("last_name");
                        String username = creatorAccountJSON.getString("username");
                        String email = creatorAccountJSON.getString("email");

                        UserProfile creator = new UserProfile(new User(username, email, firstName, lastName), firstName + " " + lastName, major, "");
                        attendanceDetail = new Attendance(meetingId, title, start_time, end_time, day, description, creator, members);
                        attendanceDetail.setRegistered(is_registered);
                        attendanceDetail.setHost(is_host);
                        attendanceDetail.setShareKey(attendance_key);

                        AttendanceDetailActivity.this.runOnUiThread(()-> {
                            AttendanceDetailFragment attendanceDetailFragment = new AttendanceDetailFragment(attendanceDetail);
                            AttendanceMembersFragment attendanceMembersFragment = new AttendanceMembersFragment(attendanceDetail);


                            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), attendanceDetailFragment, attendanceMembersFragment);
                            ViewPager viewPager = findViewById(R.id.view_pager);
                            viewPager.setAdapter(sectionsPagerAdapter);
                            TabLayout tabs = findViewById(R.id.tabs);
                            tabs.setupWithViewPager(viewPager);
                        });

                    }
                    else {
                        AttendanceDetailActivity.this.runOnUiThread(() -> {
                            Toast.makeText(AttendanceDetailActivity.this, "Something when wrong!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    AttendanceDetailActivity.this.runOnUiThread(()->{
                        stopLoading();
                    });
                }
            }
        }).start();
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
    }
}