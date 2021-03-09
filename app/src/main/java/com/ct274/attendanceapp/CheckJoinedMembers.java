package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ct274.attendanceapp.components.CheckJoinedMemberAdapter;
import com.ct274.attendanceapp.listeners.WatchCheckedListener;
import com.ct274.attendanceapp.models.Enroll;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.requests.AttendanceRequests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Response;

public class CheckJoinedMembers extends AppCompatActivity {

    private final AttendanceRequests attendanceRequests = new AttendanceRequests();
    private final ArrayList<Enroll> memberEnrolls = new ArrayList<>();
    private CheckJoinedMemberAdapter checkJoinedMemberAdapter;
    private ProgressBar progressBar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_joined_members);

        checkJoinedMemberAdapter = new CheckJoinedMemberAdapter(this, memberEnrolls, checked -> {
            Toast.makeText(CheckJoinedMembers.this, Boolean.toString(checked), Toast.LENGTH_SHORT).show();
        });
        listView = findViewById(R.id.member_list);
        listView.setAdapter(checkJoinedMemberAdapter);

        progressBar = findViewById(R.id.loading_progress);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String attendanceId = bundle.getString("attendance_id");
            if(attendanceId != null) {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listMember(attendanceId);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(getString(R.string.access_token), "");

        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v -> {

        });
    }

    private void listMember(String meetingId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    memberEnrolls.clear();
                    Response response = attendanceRequests.listMeetingMembers(meetingId);
                    String data = response.body().string();
                    if(response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(data);
                        System.out.println(data);
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject enrollJSON = jsonArray.getJSONObject(i);
                            boolean isJoined = enrollJSON.getBoolean("joined");

                            JSONObject enrollerJSON = enrollJSON.getJSONObject("enroller");
                            String username = enrollerJSON.getString("username");
                            String first_name = enrollerJSON.getString("first_name");
                            String last_name = enrollerJSON.getString("last_name");
                            String email = enrollerJSON.getString("email");
                            String userId = enrollerJSON.getString("user_id");

                            User user = new User(userId, username, email, first_name, last_name);
                            Enroll enroll = new Enroll(user, meetingId, isJoined);
                            memberEnrolls.add(enroll);

                            CheckJoinedMembers.this.runOnUiThread(()-> {
                                checkJoinedMemberAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    CheckJoinedMembers.this.runOnUiThread(()->{
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    });
                }
            }
        }).start();
    }


}