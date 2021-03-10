package com.ct274.attendanceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ct274.attendanceapp.components.CheckJoinedMemberAdapter;
import com.ct274.attendanceapp.components.LoadingDialog;
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
    private String accessToken;
    private LoadingDialog loadingDialog;
    private String attendanceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_joined_members);

        loadingDialog = new LoadingDialog(this);
        listView = findViewById(R.id.member_list);

        progressBar = findViewById(R.id.loading_progress);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            attendanceId = bundle.getString("attendance_id");
            if(attendanceId != null) {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listMember(attendanceId);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");

        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v -> {
                loadingDialog.startLoadingDialog();
                handleRequestCheckEnroll(attendanceId);

        });
    }

    private void listMember(String meetingId) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    memberEnrolls.clear();
                    Response response = attendanceRequests.listMeetingMembers(meetingId);
                    String data = response.body().string();
                    if(response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(data);
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
                                checkJoinedMemberAdapter = new CheckJoinedMemberAdapter(CheckJoinedMembers.this, memberEnrolls, (checked, selectEnroll) -> {
                                    int position = memberEnrolls.indexOf(selectEnroll);
                                    if(position != -1) {
                                        for(int j = 0; j < memberEnrolls.size(); j++) {
                                            if(j == position) {
                                                Enroll enroll1 = memberEnrolls.get(j);
                                                enroll1.setJoined(checked);
                                                memberEnrolls.set(j, enroll1);
                                                break;
                                            }
                                        }
                                    }
                                });
                                listView.setAdapter(checkJoinedMemberAdapter);
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

    private void handleRequestCheckEnroll(String meetingId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray requestData = new JSONArray();
                    for(int i = 0; i < memberEnrolls.size(); i++) {
                        Enroll enroll = memberEnrolls.get(i);
                        JSONObject dataJSON = new JSONObject();
                        dataJSON.put("joined", enroll.isJoined());
                        dataJSON.put("user_id", enroll.getEnroller().getId());
                        requestData.put(dataJSON);
                    }
                    System.out.println(requestData.toString());
                    Response response = attendanceRequests.checkEnrollMultipleMembers(accessToken, requestData.toString(), meetingId);

                    System.out.println(response.body().string());
                    if(response.isSuccessful()) {
                        CheckJoinedMembers.this.runOnUiThread(()-> {
                            Toast.makeText(CheckJoinedMembers.this, "Update checklist success", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                    else {
                        CheckJoinedMembers.this.runOnUiThread(()-> {
                            Toast.makeText(CheckJoinedMembers.this, "Fail to update checklist", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    CheckJoinedMembers.this.runOnUiThread(()->{
                        loadingDialog.closeDialog();
                    });
                }
            }
        }).start();
    }
}