package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class GameScore implements Comparable<GameScore>{

    int score;

    public GameScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameScore score1 = (GameScore) o;

        if (score != score1.score) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return score;
    }


    @Override
    public int compareTo(GameScore o) {
        return Integer.compare(this.getScore(), o.getScore());
    }
}
