package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;

/**
 * @author by ali myftiu.
 */
public interface ScoreService {

    public void insertScore(int userid, int levelid, int score) throws GamePuzzleException;

    public String getHighestScores(int levelId) throws GamePuzzleException;
}
