package com.ct274.attendanceapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ct274.attendanceapp.AttendanceDetailFragment;
import com.ct274.attendanceapp.AttendanceMembersFragment;
import com.ct274.attendanceapp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.attendance_tab_1, R.string.attendance_tab_2};
    private final Context mContext;
    private AttendanceDetailFragment attendanceDetailFragment;
    private AttendanceMembersFragment attendanceMembersFragment;
    public SectionsPagerAdapter(Context context, FragmentManager fm, AttendanceDetailFragment attendanceDetailFragment, AttendanceMembersFragment attendanceMembersFragment) {
        super(fm);
        mContext = context;
        this.attendanceDetailFragment = attendanceDetailFragment;
        this.attendanceMembersFragment = attendanceMembersFragment;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 1:
                return attendanceMembersFragment;
            default:
                return attendanceDetailFragment;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }


}