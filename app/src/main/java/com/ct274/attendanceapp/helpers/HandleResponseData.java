package com.ct274.attendanceapp.helpers;

import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.Enroll;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.models.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HandleResponseData {
    public ArrayList<Attendance> getMyRegisteredAttendancesFromJSON(JSONArray jsonArray)throws Exception {
        ArrayList<Attendance> results = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAttendance = jsonArray.getJSONObject(i);
            String attendanceId = jsonAttendance.getString("attendance_id");
            String description = jsonAttendance.getString("description");
            String start_time = jsonAttendance.getString("start_time");
            String end_time = jsonAttendance.getString("end_time");
            String day = jsonAttendance.getString("day");
            String title = jsonAttendance.getString("title");
            JSONObject creatorJSON = jsonAttendance.getJSONObject("creator");
            JSONObject userJSON = creatorJSON.getJSONObject("account");
            String username = userJSON.getString("username");
            String first_name = userJSON.getString("first_name");
            String last_name = userJSON.getString("last_name");

            User user = new User(username, "", first_name, last_name);
            UserProfile userProfile = new UserProfile(user, first_name + " " + last_name, "", "");
            Attendance attendance = new Attendance(attendanceId, title, start_time, end_time, day,description, userProfile);
            attendance.setRegistered(true);
            results.add(attendance);
        }

        return results;
    }

    public ArrayList<Enroll> getMeetingMemberFromJSON(JSONArray jsonArray, String meetingId) throws JSONException {
        ArrayList<Enroll> result = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject enrollJSON = jsonArray.getJSONObject(i);
            boolean isJoined = enrollJSON.getBoolean("joined");

            JSONObject enrollerJSON = enrollJSON.getJSONObject("enroller");
            String username = enrollerJSON.getString("username");
            String first_name = enrollerJSON.getString("first_name");
            String last_name = enrollerJSON.getString("last_name");
            String email = enrollerJSON.getString("email");
            String userId = enrollerJSON.getString("user_id");

            User user = new User(userId, username, email, first_name, last_name);
            Enroll enroll = new Enroll(user, meetingId, isJoined);
            result.add(enroll);

        }
        return result;
    }
}
