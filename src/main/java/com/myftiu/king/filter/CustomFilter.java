package com.myftiu.king.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.myftiu.king.utils.MiscUtil;
import com.sun.net.httpserver.*;


/**
 * Created by myftiu on 04/11/14.
 */
public class CustomFilter extends Filter {

        MiscUtil utils;

        public CustomFilter() {
            utils = new MiscUtil();
        }

        /*
         * Usage: filter the request before to be handled and prepare the
         *     parameters
         *
         * Input:
         *    exchange = request/response object
         *
         */
        @Override
        public void doFilter(HttpExchange exchange, Chain chain)
                throws IOException {
            parseGetParameters(exchange);
            parsePostParameters(exchange);
            parseUrlEncodedParameters(exchange);
            chain.doFilter(exchange);
        }

        /*
         * Usage: retrieve the GET parameters
         *
         * Input:
         *    exchange = request/response object
         *
         */
        private void parseGetParameters(HttpExchange exchange)
                throws UnsupportedEncodingException {

            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            utils.parseQuery(query, parameters);
            exchange.setAttribute("parameters", parameters);
        }

        /*
         * Usage: retrieve the POST parameters
         *
         * Input:
         *    exchange = request/response object
         *
         */
        private void parsePostParameters(HttpExchange exchange)
                throws IOException {

            if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parameters =
                        (Map<String, Object>)exchange.getAttribute("parameters");
                InputStreamReader isr =
                        new InputStreamReader(exchange.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                utils.parseQuery(query, parameters);
            }
        }

        /*
         * Usage: retrieve the URL encoded parameters
         *
         * Input:
         *    exchange = request/response object
         *
         */
        private void parseUrlEncodedParameters(HttpExchange exchange)
                throws UnsupportedEncodingException {

            @SuppressWarnings("unchecked")
            Map<String, Object> parameters =
                    (Map<String, Object>)exchange.getAttribute("parameters");

            String uri = exchange.getRequestURI().toString();
            String[] tokens = uri.split("[/?=]");

            if(tokens.length > 2) {
                if(tokens[2].equals("score") || tokens[2].equals("highscorelist")) {
                    parameters.put("levelid", tokens[1]);
                    parameters.put("request",tokens[2]);
                } else if(tokens[2].equals("login")) {
                    parameters.put("userid", tokens[1]);
                    parameters.put("request",tokens[2]);
                }
                else {
                    parameters.put("request","not supported");
                }
            }
            else {
                parameters.put("request","not supported");
            }
        }

        @Override
        public String description() {
            return "Class to retrieve Get/Post parameters";
        }

}
