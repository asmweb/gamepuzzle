package com.myftiu.king.filter;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.utils.ServerUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author by ali myftiu.
 */
public class CustomFilter extends Filter {

    private final static Logger LOGGER = Logger.getLogger(CustomFilter.class.getName());
    private ServerUtil utils;

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

            try {

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
            } catch (IOException ex) {
                LOGGER.log(Level.INFO, "An exception " + ex.getMessage() + " occurred");
                exceptionHandledResponse(ex.getMessage(), exchange);
            }
        }


    /**
     * Get parameters are parsed and saved into the parameter map
     * @param exchange
     * @throws UnsupportedEncodingException
     */
        private void parseGetParameters(HttpExchange exchange) throws UnsupportedEncodingException {

            Map<String, Object> parameters = new HashMap<>();
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
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                throw new IOException("GameUser was not found");

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
            String[] params = uri.split("[/?=]");

            if(params.length > 2) {
				switch (params[2].toLowerCase()) {
					case "score":
						parameters.put("levelid", params[1]);
						parameters.put("request",params[2]);
						parameters.put("sessionkey", params[4]);
						break;
					case "highscorelist":
						parameters.put("levelid", params[1]);
						parameters.put("request",params[2]);
						break;
					case "login":
						parameters.put("userid", params[1]);
						parameters.put("request",params[2]);
						break;
					default:
						throw new IOException(ServerConfig.WRONG_API_REQUEST);

				}
            } else {
				throw new IOException(ServerConfig.WRONG_API_REQUEST);
            }
        }

        @Override
        public String description() {
            return ServerConfig.FILTER_DESCRIPTION;
        }


    /**
     *
     * @param message
     * @param exchange
     */
    private void exceptionHandledResponse(String message, HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
        // sending response to client
        OutputStream os = exchange.getResponseBody();
        os.write(message.toString().getBytes());
        os.close();
        LOGGER.log(Level.INFO, message);
    }


}
