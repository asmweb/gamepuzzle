package com.myftiu.king.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.myftiu.king.utils.ServerUtil;
import com.sun.net.httpserver.*;


/**
 * Created by myftiu on 04/11/14.
 */
public class CustomFilter extends Filter {

        ServerUtil utils;

        public CustomFilter() {
            this.utils = new ServerUtil();
        }

    /**
     * Performs different parsings
     * @param exchange
     * @param chain
     * @throws IOException
     */
        public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            parseGetParameters(exchange);
            parsePostParameters(exchange);
            parseUrlEncodedParameters(exchange);
            chain.doFilter(exchange);
        }


    /**
     * Get parameters are parsed and saved into the parameter map
     * @param exchange
     * @throws UnsupportedEncodingException
     */
        private void parseGetParameters(HttpExchange exchange)
                throws UnsupportedEncodingException {

            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            utils.parseRequest(query, parameters);
            exchange.setAttribute("parameters", parameters);
        }



    /**
     * post parameters are parsed and saved into the parameter map
     * @param exchange
     * @throws IOException
     */
        private void parsePostParameters(HttpExchange exchange)
                throws IOException {

            if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parameters = (Map<String, Object>)exchange.getAttribute("parameters");

                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();

                //Scanner scanner = new Scanner(exchange.getRequestBody());
                //String query = scanner.nextLine();
                utils.parseRequest(query, parameters);
                exchange.setAttribute("parameters", parameters);
            }
        }



    /**
     * the encoded parameters are saved in the HttpExchange as attributes
     * @param exchange
     * @throws UnsupportedEncodingException
     */
        private void parseUrlEncodedParameters(HttpExchange exchange)
                throws UnsupportedEncodingException {

            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>)exchange.getAttribute("parameters");

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
                    parameters.put("request","request is not part of the api");
                }
            }
            else {
                parameters.put("request","request is not part of the api");
            }
        }

        @Override
        public String description() {
            return "Custom filtering for retrieving correctly the parameters for get/post";
        }

}
