package com.ct274.attendanceapp;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRegisteredMeeting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRegisteredMeeting extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRegisteredMeeting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRegisteredMeeting.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRegisteredMeeting newInstance(String param1, String param2) {
        MyRegisteredMeeting fragment = new MyRegisteredMeeting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_registered_meeting, container, false);

        ArrayList<Attendance> attendances = new ArrayList<>();
        User myUser = new User("b1709272", "b1709272@student.ctu.edu.vn", "Dung", "Dang");
        UserProfile myProfile = new UserProfile(myUser, myUser.getFirst_name() + " " + myUser.getLast_name(), "Technology", "");
        attendances.addAll(Arrays.asList(
                new Attendance("1", "Lorem Ipsum", "14:00", "16:00", "2021-03-01", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("2", "Fusce ut placerat", "13:00", "16:00", "2021-03-01", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("3", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("4", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("5", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("6", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile),
                new Attendance("7", "Vulputate ut pharetra", "13:30", "16:00", "2021-03-02", "Interdum consectetur libero id faucibus nisl. Ut tellus elementum sagittis vitae et leo duis. Pretium lectus quam id leo in vitae turpis. Non odio euismod lacinia at quis", myProfile)
        ));
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getContext(), attendances);
        ListView registeredMeetingLV = rootView.findViewById(R.id.my_registered_meeting_list);
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
}