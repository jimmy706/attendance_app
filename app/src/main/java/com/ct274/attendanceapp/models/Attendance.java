package com.ct274.attendanceapp.models;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Attendance {
    String id;
    LocalTime start_time;
    LocalTime end_time;
    Date day;
    UserProfile creator;
    ArrayList<User> members;

    @SuppressLint("SimpleDateFormat")
    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Attendance(String id, String start_time, String end_time, String day, UserProfile creator, User[] members) {
        this.id = id;
        this.start_time = (LocalTime.parse(start_time));
        this.end_time = LocalTime.parse(end_time);
        this.creator = creator;
        this.members = new ArrayList<>(Arrays.asList(members));
        this.day = parseDate(day);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Attendance(String id, String start_time, String end_time, String day, UserProfile creator) {
        this.id = id;
        this.start_time = (LocalTime.parse(start_time));
        this.end_time = LocalTime.parse(end_time);
        this.creator = creator;
        this.members = new ArrayList<>();
        this.day = parseDate(day);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public UserProfile getCreator() {
        return creator;
    }

    public void setCreator(UserProfile creator) {
        this.creator = creator;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id='" + id + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", day=" + day +
                ", creator=" + creator +
                ", members=" + members +
                '}';
    }
}
