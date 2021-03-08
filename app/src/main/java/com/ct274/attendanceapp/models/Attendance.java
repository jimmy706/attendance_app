package com.ct274.attendanceapp.models;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Attendance {
    String id;
    String title;
    String start_time;
    String end_time;
    Date day;
    String description;
    UserProfile creator;
    ArrayList<User> members;
    boolean isRegistered = false;
    boolean isHost = false;

    private Date getDate(String date) throws ParseException {
        String pattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(date);
    }

    public Attendance(String id, String title, String start_time, String end_time, String day, String description, UserProfile creator, ArrayList<User> members)  {
        this.id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.creator = creator;
        this.members = members;
        try{
            this.day = getDate(day);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Attendance(String id, String title, String start_time, String end_time, String day, String description, UserProfile creator) {
        this.id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.creator = creator;
        this.members = new ArrayList<>();
        try{
            this.day = getDate(day);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Date getDay() {
        return day;
    }

    public String getFormatDay() {
        String pattern = "dd-MM-yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(this.day);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", day=" + day +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                ", members=" + members +
                '}';
    }
}
