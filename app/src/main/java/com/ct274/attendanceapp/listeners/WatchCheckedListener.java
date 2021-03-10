package com.ct274.attendanceapp.listeners;

import com.ct274.attendanceapp.models.Enroll;

public interface WatchCheckedListener {
    void onCheckChange(boolean checked, Enroll enroll);
}
