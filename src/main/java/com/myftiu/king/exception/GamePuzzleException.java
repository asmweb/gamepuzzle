package com.myftiu.king.exception;

/**
 * @author by ali myftiu on 06/11/14.
 */
public class GamePuzzleException extends Exception{

	private int statusCode;

	public GamePuzzleException() {
		super();
	}

	public GamePuzzleException(String message, Throwable cause) {
		super(message, cause);
	}

	public GamePuzzleException(String message) {
		super(message);
	}

	public GamePuzzleException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

}
