package com.myftiu.king.utils;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;

import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author by ali myftiu.
 */
public class Validation {

    private final static Logger LOGGER = Logger.getLogger(Validation.class.getName());

    public static void validateAddingScore(int userId, int levelId, int score) throws GamePuzzleException {
        validateUser(userId);
        validateLevelId(levelId);
        validateScore(score);
    }

    public static void validateUser(int userId) throws GamePuzzleException {
        if (userId < 0 || userId > ServerConfig.MAX_USER_ID) {
            LOGGER.log(Level.INFO, "Invalid userId " + userId);
            throw new GamePuzzleException("Invalid userId " + userId, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }

    public static void validateLevelId(int levelId) throws GamePuzzleException {
        if (levelId < 0 || levelId > ServerConfig.MAX_LEVEL_ID) {
            LOGGER.log(Level.INFO, "Invalid levelId " + levelId);
            throw new GamePuzzleException("Invalid levelId " + levelId, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }

    public static void validateScore(int score) throws GamePuzzleException {
        if (score < 0 || score > ServerConfig.MAX_SCORE) {
            LOGGER.log(Level.INFO, "Invalid score " + score);
            throw new GamePuzzleException("Invalid score " + score, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }


}
