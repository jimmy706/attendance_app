package com.ct274.attendanceapp.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHandle {
    public static String formatDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }
}
