package com.myftiu.king.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by myftiu on 04/11/14.
 */
public class MiscUtil {

        /*
         * Usage: parse a query string in the format key=value&key=value and
         *      retrieve the values
         *
         * Input:
         *    query = query string
         *    parameters (out) = list of key-value parameters
         *
         */
        public void parseQuery(String query, Map<String, Object> parameters)
                throws UnsupportedEncodingException {

            if (query != null) {
                String pairs[] = query.split("[&]");

                for (String pair : pairs) {
                    String param[] = pair.split("[=]");

                    String key = null;
                    String value = null;
                    if (param.length > 0) {
                        // Retrieve the key
                        key = URLDecoder.decode(param[0],
                                System.getProperty("file.encoding"));
                    }

                    if (param.length > 1) {
                        // Retrieve the value
                        value = URLDecoder.decode(param[1],
                        System.getProperty("file.encoding"));
                    }

                    parameters.put(key.toLowerCase(), value);
                }
            }
        }

}
