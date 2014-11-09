package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class Level {

    int levelId;

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

        Level level = (Level) o;

        if (levelId != level.levelId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return levelId;
    }
}
