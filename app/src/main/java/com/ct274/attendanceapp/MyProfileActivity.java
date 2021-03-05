package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ct274.attendanceapp.states.UserState;
import com.ct274.attendanceapp.ui.main.MyProfileSectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    TextView username, description, full_name, major;
    CircleImageView avatar;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        username = findViewById(R.id.username);
        description = findViewById(R.id.description);
        full_name = findViewById(R.id.full_name);
        major = findViewById(R.id.major);
        avatar = findViewById(R.id.avatar);

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

        UserState userState = UserState.getInstance();
        String textFullName = userState.getFirst_name() + " " + userState.getLast_name();
        String avatarPath = "https://ui-avatars.com/api/?name=" + textFullName +  "&background=0D8ABC&color=fff&rounded=true";

        username.setText(userState.getUsername());
        full_name.setText(textFullName);
        description.setText(userState.getDescription());
        major.setText(userState.getMajor());

        Picasso.get().load(avatarPath)
                .error(R.drawable.user_circle_icon)
                .placeholder(R.drawable.user_circle_icon)
                .into(avatar);
        System.out.println(userState);

    }
}