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
import java.util.Objects;

public class AttendanceMembersFragment extends Fragment {

    private String attendanceId;


    public AttendanceMembersFragment(String attendanceId) {
        // Required empty public constructor
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

        ArrayList<User> members = new ArrayList<>();
        members.addAll(Arrays.asList(
                new User("1", "B1709272", "b1709272@student.ctu.edu.vn", "Dung", "Dang"),
                new User("2","B19072", "camchi@gmail.com", "Cam", "Chi"),
                new User("3", "B1709273", "b1709273@student.ctu.edu.vn", "Cam", "Thi"),
                new User("4", "B1709274", "b1709274@student.ctu.edu.vn", "Tien", "Nguyen"),
                new User("5", "B1709275", "b1709275@student.ctu.edu.vn", "Lorem", "Ipsum")
                ));

            MemberAdapter memberAdapter = new MemberAdapter(Objects.requireNonNull(getContext()), members, attendanceId);
            ListView memberListView = rootView.findViewById(R.id.attendance_members);
            memberListView.setAdapter(memberAdapter);


        return rootView;
    }
}