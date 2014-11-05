package com.myftiu.king.service;

import com.myftiu.king.model.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by myftiu on 04/11/14.
 */
public enum ScoreService {

        SCORE;

        volatile private Map<Integer,Map<Integer,List<Integer>>> users = new ConcurrentHashMap<Integer,Map<Integer,List<Integer>>>();
        final int numHighestScores = 15;



    /*
     * Usage: permit to insert a score for a user, in a specific level
     *
     * Input:
     *    user = user id
     *    level = level id
     *    score = last score
     *
     * Returns:
     *    -1 Error
     *    0  Score added
     */
    public int insertScore(int user, int level, int score) {

        List<Integer> addScore = null;

        if (user < 0 || level < 0 || score < 0) {
            // The parameters are not valid
            return -1;
        }

        synchronized(users) {

            // this block is synchronized because this is not an atomic operation

            if (users.containsKey(user)) {

                // Existing user

                Map<Integer,List<Integer>> storedUserLevels = users.get(user);

                if (storedUserLevels.containsKey(level)) {

                    // Existing level

                    List<Integer> storedScores =  storedUserLevels.get(level);

                    storedScores.add(score);
                }
                else {

                    // New level for existing user

                    addScore = Collections.synchronizedList(new ArrayList<Integer>());

                    addScore.add(score);
                    storedUserLevels.put(level, addScore);
                    users.put(user, storedUserLevels);
                }
            } else {

                // New user

                Map<Integer,List<Integer>> addLevel =
                        new ConcurrentHashMap<Integer,List<Integer>>();
                addScore = Collections.synchronizedList(new ArrayList<Integer>());

                addScore.add(score);
                addLevel.put(level, addScore);
                users.put(user, addLevel);
            }
        }

        return 0;
    }

    /*
     * Usage: Get the n highest scores for a specific level (max 1 result per user)
     *
     * Input:
     *    level = level id
     *
     * Returns:
     *    null No score found
     *    String containing the list of scores
     */
    public String getHighestScores(int level) {

        Iterator<Integer> userIter = null;
        List<Score> scoreResults = new ArrayList<Score>();
        int user = 0;
        Integer userMaxScore = 0;

        synchronized(users) {
            // this block is synchronized because of the iterator

            userIter = users.keySet().iterator();

            while(userIter.hasNext()) { // Search the highest score for each user

                user = userIter.next().intValue();

                if (users.get(user) != null &&
                        users.get(user).get(level) != null &&
                        users.get(user).get(level).size() > 0) {

                    // I enter here if there is a score for this user at the requested level

                    userMaxScore = Collections.max(users.get(user).get(level));
                    scoreResults.add(new Score(user, userMaxScore));

                }
            }
        }

        String bestResult = retrieveBestScores(scoreResults);

        return bestResult;
    }

    /*
     * Usage: Order a list with the best scores and prepare a CSV string
     *
     * Input:
     *    scores = list of the best scores (not ordered)
     *
     * Returns:
     *    null No scores
     *    String containing the best scores (in inverse order)
     *        (format: user=score\r\nuser=score...)
     */
    private String retrieveBestScores(List<Score> scores) {

        int user = 0;
        int contRes = 0;
        String resScores = null;

        List<Score> scoreList = new ArrayList<Score>();

        scoreList.addAll(scores);

        for(Score score: scores) {
            if (resScores == null)
                resScores = user + "=" + score.getUserId() + "\r\n";
            else
                resScores += user + "=" + score.getUserId() + "\r\n";

            if (++contRes >= numHighestScores) break;
        }



        return resScores;
    }

}
