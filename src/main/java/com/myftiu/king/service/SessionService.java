package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;

/**
 * @author by ali myftiu.
 */
public interface SessionService {

    public String createSession(int user) throws GamePuzzleException;

    public int validateSessionKey(String sessionKey) throws GamePuzzleException;
}
