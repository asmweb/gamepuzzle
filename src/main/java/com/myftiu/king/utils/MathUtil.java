package com.myftiu.king.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by myftiu on 04/11/14.
 */
public class MathUtil {

    /*
     * SecureRandom guarantee secure random strings, very hard to hack, but
     * the initialization is expensive and for this reason we are making
     * it static.
     */
    static private SecureRandom random = new SecureRandom();

    /*
     * Usage: create a new session key
     *
     * Returns:
     *    Session key
     */
    public String createSessionKey() {
        return new BigInteger(130, random).toString(32);
    }

    /*
     * Usage: chek if a string is a positive integer and convert it
     *
     * Input:
     *    str = string containing a number
     *
     * Returns:
     *    -1 The string doesn't contain a valid positive integer
     *    n  The converted  number
     */
    public int getPositiveInt(String str) {

        int retInt = -1;

        try {
            retInt = Integer.parseInt(str);

            if (retInt < 0) {
                // Non positive integer
                return -1;
            }
        }
        catch(NumberFormatException nfe) {
            // Exception because the input is not a number
            System.out.println("Error: " + nfe.getMessage());
            return -1;
        }

        return retInt;
    }
}
