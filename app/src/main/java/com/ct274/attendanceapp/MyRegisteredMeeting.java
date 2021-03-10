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
import com.ct274.attendanceapp.helpers.HandleResponseData;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Response;

public class MyRegisteredMeeting extends Fragment {
    ListView registeredMeetingLV;
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
        View rootView = inflater.inflate(R.layout.fragment_my_registered_meeting, container, false);

        shimmerContainer = rootView.findViewById(R.id.shimmer_view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");
        if(!accessToken.isEmpty()) {
            startLoading();
            fetchMyRegisterMeeting();
        }

        attendanceAdapter = new AttendanceAdapter(getContext(), attendances);
        registeredMeetingLV = rootView.findViewById(R.id.my_registered_meeting_list);
        registeredMeetingLV.setAdapter(attendanceAdapter);
        registeredMeetingLV.setOnItemClickListener((parent, view, position, id) -> {
            Attendance attendance = attendances.get(position);
            Intent intent = new Intent(getActivity(), AttendanceDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("attendance_id", attendance.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });


        return rootView;
    }

    private void fetchMyRegisterMeeting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AttendanceRequests attendanceRequests = new AttendanceRequests();
                try {
                    Response response = attendanceRequests.listMyRegisteredMeetings(accessToken);
                    if(response.isSuccessful()) {
                        String data = response.body().string();
                        JSONArray jsonArray = new JSONArray(data);
                        HandleResponseData handleResponseData = new HandleResponseData();
                        ArrayList<Attendance> results = handleResponseData.getMyRegisteredAttendancesFromJSON(jsonArray);

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
                    getActivity().runOnUiThread(()-> {
                        stopLoading();
                    });
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