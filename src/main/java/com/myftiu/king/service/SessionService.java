package com.myftiu.king.service;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.Session;
import com.myftiu.king.utils.SessionUtil;
import com.myftiu.king.utils.Validation;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by ali myftiu.
 */
public class SessionService {

    private volatile Map<String,Session> usedSessionKeys = new ConcurrentHashMap<>();
    private long lastCleanup = Calendar.getInstance().getTimeInMillis();




	/**
	 * Method creates a new session key for the given userId
	 * @param user
	 * @return session key for the user
	 * @throws GamePuzzleException
	 */
    public String createSession(int user) throws GamePuzzleException
	{
        synchronized (usedSessionKeys) {

            SessionUtil sessionUtils = new SessionUtil();
            Validation.validateUser(user);

            Calendar cal = Calendar.getInstance();
            Session session = new Session(user, cal.getTimeInMillis());

            String sessionKey = sessionUtils.createSessionKey();
            usedSessionKeys.put(sessionKey, session);

        return sessionKey;
        }
    }



	/**
	 * Given the session key returns userId
	 * @param sessionKey
	 * @return userId
	 * @throws GamePuzzleException
	 */
    public int validateSessionKey(String sessionKey) throws GamePuzzleException
	{
        if (sessionKey == null || sessionKey == "") {
			throw new GamePuzzleException("Unauthorized user", HttpURLConnection.HTTP_UNAUTHORIZED);
		}
        synchronized (usedSessionKeys) {
            Calendar cal = Calendar.getInstance();
            Session session = usedSessionKeys.get(sessionKey);

            long currentTime = cal.getTimeInMillis();

            if (session == null) {
                throw new GamePuzzleException("Unauthorized user", HttpURLConnection.HTTP_UNAUTHORIZED);
            } else if (currentTime - session.getStoredTime() > ServerConfig.MAX_SESSION_TIME) {
                throw new GamePuzzleException("Session has expired", HttpURLConnection.HTTP_FORBIDDEN);
            }

            if (currentTime - lastCleanup > ServerConfig.MAX_SESSION_TIME)
                cleanSessions();

            return session.getUser();
        }
    }


	/**
	 * removes from session all the expired session keys
	 */
    private void cleanSessions() {
        synchronized (usedSessionKeys) {
            Calendar cal = Calendar.getInstance();
            lastCleanup = cal.getTimeInMillis();

            for(Map.Entry<String,Session> entry: usedSessionKeys.entrySet()) {
                if (lastCleanup - entry.getValue().getStoredTime() > ServerConfig.MAX_SESSION_TIME) {
                    usedSessionKeys.remove(entry.getKey());
                }
            }
        }
    }
}
