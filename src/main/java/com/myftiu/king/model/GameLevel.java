package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class GameLevel {

    int levelId;

    public GameLevel(int levelId) {
        this.levelId = levelId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameLevel level = (GameLevel) o;

        if (levelId != level.levelId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return levelId;
    }
}
