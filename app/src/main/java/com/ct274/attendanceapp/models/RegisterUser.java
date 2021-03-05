package com.ct274.attendanceapp.models;

public class RegisterUser extends User{
    String password;
    String major;
    String description;

    public RegisterUser(String username, String email, String first_name, String last_name, String password, String major, String description) {
        super(username, email, first_name, last_name);
        this.password = password;
        this.major = major;
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
