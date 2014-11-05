package com.myftiu.king.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by myftiu on 04/11/14.
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
