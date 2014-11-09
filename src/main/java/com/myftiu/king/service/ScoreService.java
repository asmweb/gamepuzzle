package com.myftiu.king.service;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.GameBoard;
import com.myftiu.king.utils.Validation;

import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author by ali myftiu.
 */
public enum ScoreService {

        SCORE;

        private volatile Map<Integer,Map<Integer,List<Integer>>> users = new ConcurrentHashMap<>();
		private final static Logger LOGGER = Logger.getLogger(ScoreService.class.getName());
        private final static int MAX_RESULT_LIST = 15;


	/**
	 * The methods inserts the score for the user in a defined level
	 * @param userid is the userId
	 * @param levelid desired level
	 * @param score the new score for the that level
	 *
	 */
    public void insertScore(int userid, int levelid, int score) throws GamePuzzleException
	{

        List<Integer> addScore ;

        Validation.validateAddingScore(userid, levelid, score);

        synchronized(users) {


            if (users.containsKey(userid)) {

                Map<Integer,List<Integer>> storedUserLevels = users.get(userid);

                LOGGER.log(Level.INFO, "user " + userid + " was found");

                if (storedUserLevels.containsKey(levelid)) {

                    LOGGER.log(Level.INFO, "adding a another score " + score + " for user's " + userid + " level " + levelid);
                    List<Integer> storedScores =  storedUserLevels.get(levelid);

                    storedScores.add(score);
                }
                else {

                    LOGGER.log(Level.INFO, "adding a new score " + score + " for user's " + userid + " level " + levelid);

                    addScore = Collections.synchronizedList(new ArrayList<Integer>());

                    addScore.add(score);
                    storedUserLevels.put(levelid, addScore);
                    users.put(userid, storedUserLevels);
                }
            } else {

                LOGGER.log(Level.INFO, "no user was found, creating a new entry");
                Map<Integer,List<Integer>> addLevel = new ConcurrentHashMap<Integer,List<Integer>>();
                addScore = Collections.synchronizedList(new ArrayList<Integer>());
                addScore.add(score);
                addLevel.put(levelid, addScore);
                users.put(userid, addLevel);
            }
        }

    }


	/**
	 * Gets the highest score for the desired level
	 * @param levelId
	 * @return highest score for that level
	 */
    public String getHighestScores(int levelId) throws GamePuzzleException {

        Validation.validateLevelId(levelId);

        List<GameBoard> scoreResults = new ArrayList<>();
        int userMaxScore;
        int index = 0;

        LOGGER.log(Level.INFO, "gathering the first " + MAX_RESULT_LIST + " scores for level  " + levelId);

        synchronized(users) {

            for(Map.Entry<Integer,Map<Integer,List<Integer>>> userEntry: users.entrySet()) {
                if (users.get(userEntry.getKey()) != null &&
                        users.get(userEntry.getKey()).get(levelId) != null &&
                        users.get(userEntry.getKey()).get(levelId).size() > 0) {

                    userMaxScore = Collections.max(users.get(userEntry.getKey()).get(levelId));
                    scoreResults.add(new GameBoard(userEntry.getKey(), userMaxScore));

                }
                if (++index >= MAX_RESULT_LIST) break;
            }

        }

		Collections.sort(scoreResults);
        /* trims the string from the unnecessary chars of the method toString */
        return scoreResults.toString().replaceAll("\\[|\\]|,| ", "").trim();
    }


}
