package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;

/**
 * Created by myftiu on 09/11/14.
 */
public interface SessionService {

    public String createSession(int user) throws GamePuzzleException;

    public int validateSessionKey(String sessionKey) throws GamePuzzleException;
}
