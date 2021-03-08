package com.ct274.attendanceapp.helpers;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringHandle {
    public static String formatDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertMapToQueryString(Map<String, String> queries) {
        if(queries.isEmpty()) {
            return "";
        }
        else {
            List<String> queryStrings = new ArrayList<>();
            for(Map.Entry<String, String> entry : queries.entrySet()) {
                queryStrings.add(entry.getKey() + "=" + entry.getValue());
            }


            return String.join("&", queryStrings);
        }
    }
}
