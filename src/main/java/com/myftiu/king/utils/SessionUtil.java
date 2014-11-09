package com.myftiu.king.utils;

import java.util.UUID;

/**
 * @author by ali myftiu.
 */
public class SessionUtil {


    /*
     * Usage: create a new session key
     *
     * Returns:
     *    Session key
     */
    public static String createSessionKey() {
        UUID uniqueUUID = UUID.randomUUID();
        return uniqueUUID.toString().replace("-", "");
    }


}
