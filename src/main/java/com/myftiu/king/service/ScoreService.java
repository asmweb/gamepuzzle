package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.Score;
import com.myftiu.king.utils.ServerUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by myftiu on 04/11/14.
 */
public enum ScoreService {

        SCORE;

        private volatile Map<Integer,Map<Integer,List<Integer>>> users = new ConcurrentHashMap<>();
		private final static Logger LOGGER = Logger.getLogger(ScoreService.class.getName());


	/**
	 * The methods inserts the score for the user in a defined level
	 * @param user is the userId
	 * @param level desired level
	 * @param score the new score for the that level
	 *
	 */
    public void insertScore(int user, int level, int score) throws GamePuzzleException
	{

        List<Integer> addScore = null;

        if (user < 0 || level < 0 || score < 0) {
            LOGGER.log(Level.INFO, "Wrong parameters in insertScore");
            throw new GamePuzzleException("Wrong parameters in insertScore", ServerUtil.HTTP_STATUS_BAD_REQUEST);
        }

        synchronized(users) {


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

                Map<Integer,List<Integer>> addLevel = new ConcurrentHashMap<Integer,List<Integer>>();
                addScore = Collections.synchronizedList(new ArrayList<Integer>());
                addScore.add(score);
                addLevel.put(level, addScore);
                users.put(user, addLevel);
            }
        }

    }


	/**
	 * Gets the highest score for the desired level
	 * @param level
	 * @return highest score for that level
	 */
    public String getHighestScores(int level) {

        Iterator<Integer> userIter = null;
        List<Score> scoreResults = new ArrayList<Score>();
        int user = 0;
        Integer userMaxScore = 0;

        synchronized(users) {


            userIter = users.keySet().iterator();
            while(userIter.hasNext()) { // Search the highest score for each user

                user = userIter.next().intValue();

                if (users.get(user) != null &&
                        users.get(user).get(level) != null &&
                        users.get(user).get(level).size() > 0) {

                    userMaxScore = Collections.max(users.get(user).get(level));
                    scoreResults.add(new Score(user, userMaxScore));

                }
            }
        }

		Collections.sort(scoreResults);
        /* trims the string from the unnecessary chars of the method toString */
        return scoreResults.toString().replaceAll("\\[|\\]|,| ", "").trim();
    }


}
