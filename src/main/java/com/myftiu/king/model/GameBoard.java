package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class GameBoard implements Comparable<GameBoard> {

    private int points;
    private int userId;


    public GameBoard(int userId, int points) {
        this.points = points;
        this.userId = userId;

    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public int compareTo(GameBoard o) {
        return Integer.compare(o.getPoints(), this.points);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameBoard)) return false;

        GameBoard score = (GameBoard) o;

        if (points != score.points) return false;
        if (userId != score.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = points;
        result = 31 * result + userId;
        return result;
    }

    @Override
	public String toString()
	{
		return userId + "=" + points + "\n";

	}
}
