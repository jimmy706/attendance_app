package com.ct274.attendanceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ct274.attendanceapp.components.AttendanceAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.ct274.attendanceapp.states.UserState;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Response;


public class MyMeetingFragment extends Fragment {
    ListView myMeetingListView;
    String accessToken;
    ArrayList<Attendance> attendances = new ArrayList<>();
    AttendanceAdapter attendanceAdapter;
    ShimmerFrameLayout shimmerContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_meeting, container, false);

        shimmerContainer = rootView.findViewById(R.id.shimmer_view);

        attendanceAdapter = new AttendanceAdapter(getContext(), attendances);
        myMeetingListView = rootView.findViewById(R.id.my_meeting_list);
        myMeetingListView.setAdapter(attendanceAdapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");
        if(!accessToken.isEmpty()) {
            startLoading();
            fetchMyMeeting();
        }

        myMeetingListView.setOnItemClickListener((parent, view, position, id) -> {
            Attendance attendance = attendances.get(position);
            Intent intent = new Intent(getActivity(), AttendanceDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("attendance_id", attendance.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        return rootView;
    }

    private void fetchMyMeeting() {
        AttendanceRequests attendanceRequests = new AttendanceRequests();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.listMyMeeting(accessToken);
                    String data = response.body().string();
                    if(response.isSuccessful()){
                        ArrayList<Attendance> results = new ArrayList<>();
                        JSONObject JSONData = new JSONObject(data);
                        JSONArray JSONAttendances = JSONData.getJSONArray("results");
                        for(int i = 0; i < JSONAttendances.length(); i++) {
                            JSONObject JSONAttendance = JSONAttendances.getJSONObject(i);
                            String attendanceId = JSONAttendance.getString("id");
                            String description = JSONAttendance.getString("description");
                            String start_time = JSONAttendance.getString("start_time");
                            String end_time = JSONAttendance.getString("end_time");
                            String day = JSONAttendance.getString("day");
                            String title = JSONAttendance.getString("title");
                            boolean is_registered = JSONAttendance.getBoolean("is_registed");

                            UserState userState = UserState.getInstance();
                            User user = new User(userState.getId(), userState.getUsername(), userState.getEmail(), userState.getFirst_name(), userState.getLast_name());
                            UserProfile userProfile = new UserProfile(user, userState.getFirst_name() + " " + userState.getLast_name(), userState.getMajor(), userState.getDescription());
                            Attendance attendance = new Attendance(attendanceId,title,start_time,end_time,day,description,userProfile);
                            attendance.setRegistered(is_registered);
                            attendance.setHost(true);
                            results.add(attendance);
                        }

                        getActivity().runOnUiThread(()-> {
                            attendances.addAll(results);
                            attendanceAdapter.notifyDataSetChanged();
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    getActivity().runOnUiThread(()->stopLoading());
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
}