package com.ct274.attendanceapp.models;

public class Enroll {
    private User enroller;
    private String attendance;
    private boolean joined;

    public Enroll(User enroller, String attendance, boolean joined) {
        this.enroller = enroller;
        this.attendance = attendance;
        this.joined = joined;
    }

    public User getEnroller() {
        return enroller;
    }

    public void setEnroller(User enroller) {
        this.enroller = enroller;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "Enroll{" +
                "enroller=" + enroller +
                ", attendance='" + attendance + '\'' +
                ", joined=" + joined +
                '}';
    }
}
