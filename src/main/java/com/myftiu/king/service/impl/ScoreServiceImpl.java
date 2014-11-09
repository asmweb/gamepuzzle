package com.myftiu.king.service.impl;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.GameBoard;
import com.myftiu.king.model.GameLevel;
import com.myftiu.king.model.GameScore;
import com.myftiu.king.model.GameUser;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.utils.Validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author by ali myftiu.
 */
public class ScoreServiceImpl implements ScoreService {


        /** instead of using wrappers such as Integers, custom (possible extensible) data model wrappers are preferred */
        private volatile Map<GameUser,Map<GameLevel,List<GameScore>>> users = new ConcurrentHashMap<>();
		private final static Logger LOGGER = Logger.getLogger(ScoreServiceImpl.class.getName());



	/**
	 * The methods inserts the score for the user in a defined level
	 * @param userid is the userId
	 * @param levelid desired level
	 * @param score the new score for the that level
	 *
	 */
    public void insertScore(int userid, int levelid, int score) throws GamePuzzleException
	{

        List<GameScore> addScore ;

        Validation.validateAddingScore(userid, levelid, score);

        synchronized(users) {


            if (users.containsKey(userid)) {

                Map<GameLevel,List<GameScore>> storedUserLevels = users.get(userid);

                LOGGER.log(Level.INFO, "user " + userid + " was found");

                if (storedUserLevels.containsKey(levelid)) {

                    LOGGER.log(Level.INFO, "adding a another score " + score + " for user's " + userid + " level " + levelid);
                    List<GameScore> storedScores =  storedUserLevels.get(levelid);

                    storedScores.add(new GameScore(score));
                }
                else {

                    LOGGER.log(Level.INFO, "adding a new score " + score + " for user's " + userid + " level " + levelid);

                    addScore = Collections.synchronizedList(new ArrayList<GameScore>());

                    addScore.add(new GameScore(score));
                    storedUserLevels.put(new GameLevel(levelid), addScore);
                    users.put(new GameUser(userid), storedUserLevels);
                }
            } else {

                LOGGER.log(Level.INFO, "no user was found, creating a new entry");
                Map<GameLevel,List<GameScore>> addLevel = new ConcurrentHashMap<>();
                addScore = Collections.synchronizedList(new ArrayList<GameScore>());
                addScore.add(new GameScore(score));
                addLevel.put(new GameLevel(levelid), addScore);
                users.put(new GameUser(userid), addLevel);
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
        GameLevel gameLevelId = new GameLevel(levelId);
        GameScore userMaxScore;
        int index = 0;

        LOGGER.log(Level.INFO, "gathering the first " + ServerConfig.MAX_RESULT_LIST + " scores for level  " + levelId);

        synchronized(users) {

            for(Map.Entry<GameUser,Map<GameLevel,List<GameScore>>> userEntry: users.entrySet()) {

                GameUser gameUser = userEntry.getKey();
                Map<GameLevel,List<GameScore>> gameLevelListMap = users.get(gameUser);
                List<GameScore> gameScoreList = gameLevelListMap.get(gameLevelId);


                if (gameUser != null && gameScoreList != null && gameScoreList.size() > 0) {
                    userMaxScore = Collections.max(gameScoreList);
                    scoreResults.add(new GameBoard(userEntry.getKey().getUserId(), userMaxScore.getScore()));

                }
                if (++index >= ServerConfig.MAX_RESULT_LIST) break;
            }

        }

		Collections.sort(scoreResults);
        /* trims the string from the unnecessary chars of the method toString */
        return scoreResults.toString().replaceAll("\\[|\\]|,| ", "").trim();
    }


}
