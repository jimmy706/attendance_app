package com.ct274.attendanceapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ct274.attendanceapp.ui.main.SectionsPagerAdapter;

public class AttendanceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);


        Bundle bundle = getIntent().getExtras();
        String attendanceId = "-1";
        if(bundle != null) {
            attendanceId = bundle.getString("attendance_id");
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString("attendance_id", attendanceId);

            AttendanceDetailFragment attendanceDetailFragment = new AttendanceDetailFragment(attendanceId);
            AttendanceMembersFragment attendanceMembersFragment = new AttendanceMembersFragment(attendanceId);

            attendanceDetailFragment.setArguments(fragmentArgs);
            attendanceMembersFragment.setArguments(fragmentArgs);

            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), attendanceDetailFragment, attendanceMembersFragment);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
        }



        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}