package com.example.whatsapp.models;

public class CallList {

    private String calling;
    private String userName;
    private String userProfile;
    private String callType;
    private String date;

    public CallList()
    {

    }

    public CallList(String calling, String userName, String userProfile, String callType, String date) {
        this.calling = calling;
        this.userName = userName;
        this.userProfile = userProfile;
        this.callType = callType;
        this.date = date;
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String userId) {
        this.calling = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
