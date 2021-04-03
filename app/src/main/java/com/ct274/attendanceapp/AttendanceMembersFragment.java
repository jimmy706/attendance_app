package com.ct274.attendanceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ct274.attendanceapp.components.MemberAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.requests.AttendanceRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class AttendanceMembersFragment extends Fragment {
    Attendance attendance;
    String accessToken;

    public AttendanceMembersFragment(Attendance attendance) {
        this.attendance = attendance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");

        View rootView = inflater.inflate(R.layout.fragment_attendance_members, container, false);
        ArrayList<User> members = attendance.getMembers();

        MemberAdapter memberAdapter = new MemberAdapter(Objects.requireNonNull(getContext()), attendance.getMembers(), attendance);
        ListView memberListView = rootView.findViewById(R.id.attendance_members);
        memberListView.setAdapter(memberAdapter);

        memberAdapter.onRemoveMember((userId, position) -> {
            memberAdapter.remove(members.get(position));
            memberAdapter.notifyDataSetChanged();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        AttendanceRequests attendanceRequests = new AttendanceRequests();
//                        Response response = attendanceRequests.removeMemberFromMeeting(accessToken, userId, attendance.getId());
//                        String data = response.body().string();
//
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        });

        return rootView;
    }
}