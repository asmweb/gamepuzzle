package com.myftiu.king.service;

import com.myftiu.king.model.Session;
import com.myftiu.king.utils.*;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by myftiu on 04/11/14.
 */
public enum SessionService {

    SERVICE;


    private volatile Map<String,Session> usedSessionKeys = new ConcurrentHashMap<String,Session>();
    private long lastCleanup = Calendar.getInstance().getTimeInMillis();


    private final long cleanupEverySecs = 1000 * 60 * 15; // 15 minutes;
    private final long MAX_SESSION_TIME = 1000 * 60 * 10; // 10 minutes





    /*
     * Usage: return a new session key
     *
     * Input:
     *    user = user id
     *
     * Returns:
     *    null Error
     *    String containing a new session key in base 32
     */
    public String createSession(int user)
    {
        SessionUtil sessionUtils = new SessionUtil();


        if (user < 0) return null;

        Calendar cal = Calendar.getInstance();
        Session session = new Session(cal.getTimeInMillis(), user);

        String sessionKey = sessionUtils.createSessionKey();
        usedSessionKeys.put(sessionKey, session);

        return sessionKey;
    }

    /*
     * Usage: validate a session key and check if a session is still valid
     *
     * Input:
     *    sessionKey = base 32 session key string
     *
     * Returns:
     *    -1 Error
     *    user id associated to the session
     */
    public int validateSessionKey(String sessionKey)
    {
        if (sessionKey == null || sessionKey == "")
            return -1;

        Calendar cal = Calendar.getInstance();
        boolean val = usedSessionKeys.containsKey(sessionKey);
        Session session = usedSessionKeys.get(sessionKey);

        long currentTime = cal.getTimeInMillis();

        if (session == null) {
            // SessionKey not valid
            return -1;
        } else if (currentTime - session.getStoredTime() > MAX_SESSION_TIME) {
            // Session elapsed
            return -1;
        }

        if (currentTime - lastCleanup > cleanupEverySecs)
            doCleanup();

        // Valid session
        return session.getUser();
    }

    /*
     * Usage: clean the list from unused session key
     */
    private void doCleanup() {
        Calendar cal = Calendar.getInstance();
        lastCleanup = cal.getTimeInMillis();
        Iterator<String> iter = usedSessionKeys.keySet().iterator();
        String key = "";

        while(iter.hasNext()) { // For each seassion...
            key = iter.next();
            if (lastCleanup - usedSessionKeys.get(key).getStoredTime() > MAX_SESSION_TIME) {
                usedSessionKeys.remove(key);
            }
        }
    }
}
