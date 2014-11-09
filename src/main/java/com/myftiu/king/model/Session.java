package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class Session {

        private long storedTime; // The time this session has been created
        private int user; // User associated to this session

        public Session(int user, long storedTime) {

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
