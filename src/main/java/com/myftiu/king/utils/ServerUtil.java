package com.myftiu.king.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author by ali myftiu.
 */
public class ServerUtil {


    /**
     * Method parses http request to a map of key value of parameters
     * @param request
     * @param parameters
     * @throws UnsupportedEncodingException
     */
        public  void parseRequest(String request, Map<String, Object> parameters)
                throws UnsupportedEncodingException {

            if (request != null) {
                String pairs[] = request.split("[&]");

                for (int i = 0; i < pairs.length; i++) {
                    String param[] = pairs[i].split("[=]");

                    String key = null;
                    String value = null;
                    if (param.length > 0) {
                        key = URLDecoder.decode(param[0], System.getProperty("file.encoding")).toLowerCase();
                    }

                    if (param.length > 1) {
                        value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                    }

                    parameters.put(key, value);
                }
            }
        }




}
