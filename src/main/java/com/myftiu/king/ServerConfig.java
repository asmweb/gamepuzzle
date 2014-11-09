package com.myftiu.king;

/**
 * @author by ali myftiu.
 */
public class ServerConfig {

    public static final int SERVER_PORT = 8009;
    public static final long MAX_SESSION_TIME = 600000;
    public static final int MAX_USER_ID = Integer.MAX_VALUE;
    public static final int MAX_LEVEL_ID = Integer.MAX_VALUE;
    public static final int MAX_SCORE = Integer.MAX_VALUE;
    public static final int MAX_RESULT_LIST = 15;
    public static final String WRONG_API_REQUEST = "Request is not part of the api";
    public static final String FILTER_DESCRIPTION = "Custom filtering for retrieving correctly the parameters for get/post";

    /**
     * This is a pure utility class, no need to be instantiated
     */
    private ServerConfig() {

    }

}
