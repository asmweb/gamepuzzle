package com.myftiu.king.model;

/**
 * Created by myftiu on 04/11/14.
 */
public class Score implements Comparable<Score> {

    private int points;
    private int userId;


    public Score(int points, int userId) {
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
    public int compareTo(Score o) {
        return Integer.compare(this.points, o.getPoints());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

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
		return "Score{" +
				"points=" + points +
				", userId=" + userId +
				'}';
	}
}
