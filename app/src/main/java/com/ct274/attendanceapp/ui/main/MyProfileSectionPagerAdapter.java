package com.ct274.attendanceapp.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ct274.attendanceapp.MyMeetingFragment;
import com.ct274.attendanceapp.MyRegisteredMeeting;
import com.ct274.attendanceapp.R;


public class MyProfileSectionPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final MyMeetingFragment myMeetingFragment;
    private final MyRegisteredMeeting myRegisteredMeeting;
    private static final int[] TAB_TITLES = new int[]{R.string.my_profile_tab_1, R.string.my_profile_tab_2};

    public MyProfileSectionPagerAdapter(@NonNull FragmentManager fm, Context context, MyMeetingFragment myMeetingFragment, MyRegisteredMeeting myRegisteredMeeting) {
        super(fm);
        this.mContext = context;
        this.myMeetingFragment = myMeetingFragment;
        this.myRegisteredMeeting = myRegisteredMeeting;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return myRegisteredMeeting;
            default:
                return myMeetingFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
