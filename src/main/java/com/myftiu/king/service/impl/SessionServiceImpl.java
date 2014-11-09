package com.myftiu.king.service.impl;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.model.Session;
import com.myftiu.king.service.SessionService;
import com.myftiu.king.utils.SessionUtil;
import com.myftiu.king.utils.Validation;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author by ali myftiu.
 */
public class SessionServiceImpl implements SessionService {

    private volatile Map<String,Session> usedSessionKeys = new ConcurrentHashMap<>();
    private long lastCleanup;
    private final static Logger LOGGER = Logger.getLogger(SessionServiceImpl.class.getName());



    /**
     * Method creates a new session key for the given userId
     * @param user
     * @return session key for the user
     * @throws GamePuzzleException
     */
    public String createSession(int user) throws GamePuzzleException
    {
        synchronized (usedSessionKeys) {

            cleanSessions();

            String sessionKey = findActive(user);

            /** user might be alread active, no need to create a new session */
            if (sessionKey == null || sessionKey == "") {

                SessionUtil sessionUtils = new SessionUtil();
                Validation.validateUser(user);

                Session session = new Session(user, Calendar.getInstance().getTimeInMillis());

                sessionKey = sessionUtils.createSessionKey();
                LOGGER.log(Level.INFO, "A new session " + sessionKey + " is created for user " + user);
                usedSessionKeys.put(sessionKey, session);
            }

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

            cleanSessions();

            Session session = usedSessionKeys.get(sessionKey);

            long currentTime = Calendar.getInstance().getTimeInMillis();

            if (session == null) {
                throw new GamePuzzleException("Unauthorized user", HttpURLConnection.HTTP_UNAUTHORIZED);
            } else if (currentTime - session.getStoredTime() > ServerConfig.MAX_SESSION_TIME) {
                throw new GamePuzzleException("Session has expired", HttpURLConnection.HTTP_FORBIDDEN);
            }

            return session.getUser();
        }
    }


    /**
     * removes from session all the expired session keys
     */
    private void cleanSessions() {
        synchronized (usedSessionKeys) {
            lastCleanup = Calendar.getInstance().getTimeInMillis();
            LOGGER.log(Level.INFO, "Cleaning up session at " + lastCleanup);
            for(Map.Entry<String,Session> entry: usedSessionKeys.entrySet()) {
                if (lastCleanup - entry.getValue().getStoredTime() > ServerConfig.MAX_SESSION_TIME) {
                    LOGGER.log(Level.INFO, "Session has expired for user " + entry.getValue().getUser() + " user is removed");
                    usedSessionKeys.remove(entry.getKey());
                }
            }
        }
    }

    private String findActive(int user) {
        String sessionKey = "";
        LOGGER.log(Level.INFO, "Checking if the user " + user + " has already an active session ");
        synchronized (usedSessionKeys) {
            for(Map.Entry<String,Session> entry: usedSessionKeys.entrySet()) {
                if(entry.getValue().getUser() == user) {
                    sessionKey = entry.getKey();
                    LOGGER.log(Level.INFO, "Active session " + sessionKey + " is found for user " + user);
                    break;
                }
            }
        }
        return sessionKey;

    }
}