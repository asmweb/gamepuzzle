package com.myftiu.king;

import java.util.Set;

/**
 * @author by ali myftiu.
 */
public class ServerConfig {

    public final static int SERVER_PORT = 8009;
    public final static long MAX_SESSION_TIME = 600000;
    public static final int MAX_USER_ID = Integer.MAX_VALUE;
    public static final int MAX_LEVEL_ID = Integer.MAX_VALUE;
    public static final int MAX_SCORE = Integer.MAX_VALUE;

    /**
     * This is a pure utility class, no need to be instantiated
     */
    private ServerConfig() {

    }

}
