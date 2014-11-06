package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.Session;
import com.myftiu.king.utils.SessionUtil;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by myftiu on 04/11/14.
 */
public enum SessionService {

    SERVICE;


    private volatile Map<String,Session> usedSessionKeys = new ConcurrentHashMap<>();
    private long lastCleanup = Calendar.getInstance().getTimeInMillis();
	private final long MAX_SESSION_TIME = 600000;



	/**
	 * Method creates a new session key for the given userId
	 * @param user
	 * @return session key for the user
	 * @throws GamePuzzleException
	 */
    public String createSession(int user) throws GamePuzzleException
	{
        SessionUtil sessionUtils = new SessionUtil();


        if (user < 0) {
			throw new GamePuzzleException("Invalid userId", 401);
		}

        Calendar cal = Calendar.getInstance();
        Session session = new Session(cal.getTimeInMillis(), user);

        String sessionKey = sessionUtils.createSessionKey();
        usedSessionKeys.put(sessionKey, session);

        return sessionKey;
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
			throw new GamePuzzleException("Unauthorized user", 401);
		}

        Calendar cal = Calendar.getInstance();
        Session session = usedSessionKeys.get(sessionKey);

        long currentTime = cal.getTimeInMillis();

        if (session == null) {
			throw new GamePuzzleException("Unauthorized user", 401);
        } else if (currentTime - session.getStoredTime() > MAX_SESSION_TIME) {
			throw new GamePuzzleException("Session has expired", 401);
        }

        if (currentTime - lastCleanup > MAX_SESSION_TIME)
            doCleanup();

        return session.getUser();
    }


	/**
	 * removes from session all the expired session keys
	 */
    private void doCleanup() {
        Calendar cal = Calendar.getInstance();
        lastCleanup = cal.getTimeInMillis();

		for(Map.Entry<String,Session> entry: usedSessionKeys.entrySet()) {
			if (lastCleanup - entry.getValue().getStoredTime() > MAX_SESSION_TIME) {
				usedSessionKeys.remove(entry.getKey());
			}
		}
    }
}
