package com.myftiu.king.exception;

import java.io.IOException;

/**
 * @author by ali myftiu.
 */
public class GamePuzzleException extends IOException {

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
