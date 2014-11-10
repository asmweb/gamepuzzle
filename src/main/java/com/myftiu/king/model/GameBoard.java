package com.myftiu.king.model;

/**
 * @author by ali myftiu.
 */
public class GameBoard implements Comparable<GameBoard> {

	private int score;
	private int userId;

	public GameBoard(int userId, int score)
	{
		this.score = score;
		this.userId = userId;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		GameBoard gameBoard = (GameBoard) o;

		if (score != gameBoard.score)
		{
			return false;
		}
		if (userId != gameBoard.userId)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = score;
		result = 31 * result + userId;
		return result;
	}

	@Override
	public int compareTo(GameBoard o)
	{
		return Integer.compare(o.getScore(), this.score);
	}

	@Override
	public String toString()
	{
		return userId + "=" + score + "\n";

	}


}
