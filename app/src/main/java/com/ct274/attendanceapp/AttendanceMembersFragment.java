package com.ct274.attendanceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ct274.attendanceapp.components.MemberAdapter;
import com.ct274.attendanceapp.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AttendanceMembersFragment extends Fragment {

    private ArrayList<User> members;
    private String attendanceId;


    public AttendanceMembersFragment(ArrayList<User> members, String attendanceId) {
        // Required empty public constructor
        this.members = members;
        this.attendanceId = attendanceId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance_members, container, false);

        MemberAdapter memberAdapter = new MemberAdapter(Objects.requireNonNull(getContext()), members, attendanceId);
        ListView memberListView = rootView.findViewById(R.id.attendance_members);
        memberListView.setAdapter(memberAdapter);


        return rootView;
    }
}