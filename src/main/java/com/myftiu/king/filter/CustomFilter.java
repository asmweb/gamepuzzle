package com.myftiu.king.filter;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.utils.ServerUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


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

			switch (exchange.getRequestMethod().toLowerCase()) {
				case "post":
					parsePostParameters(exchange);
					break;
				case "get":
					parseGetParameters(exchange);
					break;
				default:
					throw new IOException("Method " + exchange.getRequestMethod() + "is not supported ");
			}

            parseUrlEncodedParameters(exchange);
            chain.doFilter(exchange);
        }


    /**
     * Get parameters are parsed and saved into the parameter map
     * @param exchange
     * @throws UnsupportedEncodingException
     */
        private void parseGetParameters(HttpExchange exchange) throws UnsupportedEncodingException {

            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
			if(query != null) {
				utils.parseRequest(query, parameters);
			}
			exchange.setAttribute("parameters", parameters);


        }



    /**
     * post parameters are parsed and saved into the parameter map
     * @param exchange
     * @throws IOException
     */
        private void parsePostParameters(HttpExchange exchange) throws IOException {

        	@SuppressWarnings("unchecked")
			Map<String, Object> parameters = (Map<String, Object>)exchange.getAttribute("parameters");
			if(parameters == null){
                exchange.sendResponseHeaders(ServerUtil.HTTP_STATUS_BAD_REQUEST, 0);
                throw new IOException("User was not found");

            }
			Scanner scanner = new Scanner(exchange.getRequestBody());
			String query = "points="+scanner.nextLine();
			utils.parseRequest(query, parameters);

        }



    /**
     * the encoded parameters are saved in the HttpExchange as attributes
     * @param exchange
     * @throws UnsupportedEncodingException
     */
        private void parseUrlEncodedParameters(HttpExchange exchange) throws IOException
		{

            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>)exchange.getAttribute("parameters");

            String uri = exchange.getRequestURI().toString();
            String[] tokens = uri.split("[/?=]");

            if(tokens.length > 2) {
				switch (tokens[2].toLowerCase()) {
					case "score":
						parameters.put("levelid", tokens[1]);
						parameters.put("request",tokens[2]);
						parameters.put("sessionkey", tokens[4]);
						break;
					case "highscorelist":
						parameters.put("levelid", tokens[1]);
						parameters.put("request",tokens[2]);
						break;
					case "login":
						parameters.put("userid", tokens[1]);
						parameters.put("request",tokens[2]);
						break;
					default:
						throw new IOException("Request is not part of the api");

				}
            } else {
				throw new IOException("Request is not part of the api");
            }
        }

        @Override
        public String description() {
            return "Custom filtering for retrieving correctly the parameters for get/post";
        }

}
