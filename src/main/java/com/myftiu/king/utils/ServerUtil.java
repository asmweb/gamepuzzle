package com.myftiu.king.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by myftiu on 04/11/14.
 */
public class ServerUtil {

    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_FORBIDDEN = 403;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_STATUS_NOT_IMPLEMENTED  = 501;

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
