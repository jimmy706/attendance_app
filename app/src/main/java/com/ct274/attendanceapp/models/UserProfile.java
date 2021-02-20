package com.ct274.attendanceapp.models;

public class UserProfile {
    User account;
    String full_name;
    String major;
    String description;

    public UserProfile(User account, String full_name, String major, String description) {
        this.account = account;
        this.full_name = full_name;
        this.major = major;
        this.description = description;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "account=" + account +
                ", full_name='" + full_name + '\'' +
                ", major='" + major + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
