package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;

/**
 * Created by myftiu on 09/11/14.
 */
public interface ScoreService {

    public void insertScore(int userid, int levelid, int score) throws GamePuzzleException;

    public String getHighestScores(int levelId) throws GamePuzzleException;
}
