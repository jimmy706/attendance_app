package com.ct274.attendanceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ct274.attendanceapp.components.AttendanceAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.ct274.attendanceapp.requests.AuthRequests;
import com.ct274.attendanceapp.states.UserState;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Response;

public class Home extends AppCompatActivity {
    AttendanceAdapter attendanceAdapter;
    AttendanceRequests attendanceRequests = new AttendanceRequests();
    private static final int MEETING_ITEMS_SIZE = 7;
    ArrayList<Attendance> attendances = new ArrayList<>();
    ShimmerFrameLayout shimmerContainer;
    boolean isLoadMore = false;
    int currentPage = 1;
    int numPage = 1;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        shimmerContainer = findViewById(R.id.shimmer_view);

        attendanceAdapter = new AttendanceAdapter(this, attendances);

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
            getMeetingList(accessToken, 1);
            startLoading();
        }

        RadioGroup viewAttendanceRadioGroup = findViewById(R.id.view_attendance_state);
        viewAttendanceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.view_newest:
                    attendances.clear();
                    attendanceAdapter.notifyDataSetChanged();
                    startLoading();
                    currentPage = 1;
                    getMeetingList(accessToken, currentPage);
                    break;
                case R.id.view_registered:
                    attendances.clear();
                    attendanceAdapter.notifyDataSetChanged();
                    startLoading();
                    listRegisteredMeeting(accessToken);
                    break;
            }
        });

        attendanceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount && totalItemCount != 0 && !isLoadMore && currentPage < numPage) {
                    isLoadMore = true;
                    startLoading();
                    currentPage += 1;
                    getMeetingList(accessToken, currentPage);
                }
            }
        });
    }

    private void listRegisteredMeeting(String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.listMyRegisteredMeetings(token);
                    if(response.isSuccessful()) {
                        String data = response.body().string();
                        JSONArray jsonArray = new JSONArray(data);
                        ArrayList<Attendance> results = new ArrayList<>();

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonAttendance = jsonArray.getJSONObject(i);
                            String attendanceId = jsonAttendance.getString("attendance_id");
                            String description = jsonAttendance.getString("description");
                            String start_time = jsonAttendance.getString("start_time");
                            String end_time = jsonAttendance.getString("end_time");
                            String day = jsonAttendance.getString("day");
                            String title = jsonAttendance.getString("title");
                            String location = jsonAttendance.getString("location");
                            JSONObject creatorJSON = jsonAttendance.getJSONObject("creator");
                            JSONObject userJSON = creatorJSON.getJSONObject("account");
                            String username = userJSON.getString("username");
                            String first_name = userJSON.getString("first_name");
                            String last_name = userJSON.getString("last_name");

                            User user = new User(username, "", first_name, last_name);
                            UserProfile userProfile = new UserProfile(user, first_name + " " + last_name, "", "");
                            Attendance attendance = new Attendance(attendanceId, title, start_time, end_time, day,description, location, userProfile);
                            attendance.setRegistered(true);
                            results.add(attendance);
                        }

                        Home.this.runOnUiThread(()-> {
                            attendances.addAll(results);
                            attendanceAdapter.notifyDataSetChanged();
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Home.this.runOnUiThread(()-> {
                        stopLoading();
                        if(isLoadMore) {
                            isLoadMore = false;
                        }
                    });
                }
            }
        }).start();
    }

    private void getMeetingList(String token, int page) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.listAttendance(token, page, MEETING_ITEMS_SIZE);
                    String data = response.body().string();
                    if(response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray meetingsJSON = jsonObject.getJSONArray("results");
                        List<Attendance> results = new ArrayList<>();
                        numPage = jsonObject.getInt("num_pages");
                        for(int i = 0; i < meetingsJSON.length(); i++) {
                            JSONObject jsonAttendance = meetingsJSON.getJSONObject(i);
                            String attendanceId = jsonAttendance.getString("id");
                            String description = jsonAttendance.getString("description");
                            String start_time = jsonAttendance.getString("start_time");
                            String end_time = jsonAttendance.getString("end_time");
                            String day = jsonAttendance.getString("day");
                            String location = jsonAttendance.getString("location");
                            boolean isRegistered = jsonAttendance.getBoolean("is_registered");
                            String title = jsonAttendance.getString("title");
                            JSONObject creatorJSON = jsonAttendance.getJSONObject("creator");
                            JSONObject userJSON = creatorJSON.getJSONObject("account");
                            String username = userJSON.getString("username");
                            String first_name = userJSON.getString("first_name");
                            String last_name = userJSON.getString("last_name");

                            User user = new User(username, "", first_name, last_name);
                            UserProfile userProfile = new UserProfile(user, first_name + " " + last_name, "", "");
                            Attendance attendance = new Attendance(attendanceId, title, start_time, end_time, day,description, location, userProfile);
                            attendance.setRegistered(isRegistered);
                            results.add(attendance);
                        }
                        Home.this.runOnUiThread(()-> {
                            attendances.addAll(results);
                            attendanceAdapter.notifyDataSetChanged();
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Home.this.runOnUiThread(()-> {
                      stopLoading();
                    });
                }
            }
        }).start();
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

    private void startLoading() {
        shimmerContainer.startShimmer();
        shimmerContainer.setVisibility(View.VISIBLE);
    }


    private void stopLoading() {
        shimmerContainer.startShimmer();
        shimmerContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}