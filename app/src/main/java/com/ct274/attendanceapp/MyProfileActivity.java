package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ct274.attendanceapp.ui.main.MyProfileSectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MyProfileActivity extends AppCompatActivity {
    TextView avatar, username, description, full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        MyRegisteredMeeting myRegisteredMeeting = new MyRegisteredMeeting();
        MyMeetingFragment myMeetingFragment = new MyMeetingFragment();

        MyProfileSectionPagerAdapter sectionAdapter = new MyProfileSectionPagerAdapter(getSupportFragmentManager(), this, myMeetingFragment, myRegisteredMeeting);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });




    }
}