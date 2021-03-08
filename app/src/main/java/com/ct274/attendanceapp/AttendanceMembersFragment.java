package com.ct274.attendanceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ct274.attendanceapp.components.MemberAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AttendanceMembersFragment extends Fragment {
    Attendance attendance;


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
        View rootView = inflater.inflate(R.layout.fragment_attendance_members, container, false);

        MemberAdapter memberAdapter = new MemberAdapter(Objects.requireNonNull(getContext()), attendance.getMembers(), attendance.getId());
        ListView memberListView = rootView.findViewById(R.id.attendance_members);
        memberListView.setAdapter(memberAdapter);


        return rootView;
    }
}