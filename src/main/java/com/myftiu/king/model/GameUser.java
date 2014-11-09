package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class GameUser {

    int userId;

    public GameUser(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUser user = (GameUser) o;

        if (userId != user.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userId;
    }
}
