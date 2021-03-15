package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ct274.attendanceapp.components.CheckJoinedMemberAdapter;

import com.ct274.attendanceapp.helpers.HandleResponseData;
import com.ct274.attendanceapp.models.Enroll;
import com.ct274.attendanceapp.requests.AttendanceRequests;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Response;

public class ViewMeetingAttendList extends AppCompatActivity {
    private String meetingId;
    private final AttendanceRequests attendanceRequests = new AttendanceRequests();
    private final ArrayList<Enroll> memberEnrolls = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView listView;
    private CheckJoinedMemberAdapter membersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meeting_attend_list);
        listView = findViewById(R.id.member_list);

        progressBar = findViewById(R.id.loading_progress);
        membersAdapter = new CheckJoinedMemberAdapter(this, memberEnrolls);
        listView.setAdapter(membersAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            meetingId = bundle.getString("attendance_id");
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            listMember();
        }


        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void listMember() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.listMeetingMembers(meetingId);
                    if(response.isSuccessful()) {
                        String data = response.body().string();
                        JSONArray jsonArray = new JSONArray(data);

                        ViewMeetingAttendList.this.runOnUiThread(()-> {
                            try {
                                HandleResponseData handleResponse = new HandleResponseData();
                                memberEnrolls.addAll(handleResponse.getMeetingMemberFromJSON(jsonArray, meetingId));
                                membersAdapter.notifyDataSetChanged();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    ViewMeetingAttendList.this.runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    });
                }
            }
        }).start();
    }
}