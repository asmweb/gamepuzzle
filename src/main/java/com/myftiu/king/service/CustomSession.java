package com.myftiu.king.service;

/**
 * Created by myftiu on 04/11/14.
 */
public class CustomSession {

    long storedTime; // The time this session has been created
    int user; // User associated to this session

    public CustomSession(long storedTime, int user) {

        this.storedTime = storedTime;
        this.user = user;
    }

    public long getStoredTime() {
        return storedTime;
    }
    public void setStoredTime(long storedTime) {
        this.storedTime = storedTime;
    }
    public int getUser() {
        return user;
    }
    public void setUser(int user) {
        this.user = user;
    }

}
