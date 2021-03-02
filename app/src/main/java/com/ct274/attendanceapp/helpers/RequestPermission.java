package com.ct274.attendanceapp.helpers;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class RequestPermission {
    public static void requestWriteSettingPermission(Activity activity, int code) {
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_SETTINGS}, code);
    }
}
