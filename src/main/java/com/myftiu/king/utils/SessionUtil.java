package com.myftiu.king.utils;

import java.util.UUID;

/**
 * @author by ali myftiu.
 */
public class SessionUtil {


    /**
     *
     * @return  unique uuid
     */
    public static String createSessionKey() {
        UUID uniqueUUID = UUID.randomUUID();
        return uniqueUUID.toString().replace("-", "");
    }


}
