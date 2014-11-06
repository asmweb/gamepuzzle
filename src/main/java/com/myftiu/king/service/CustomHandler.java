package com.myftiu.king.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.*;

/**
 * Created by myftiu on 04/11/14.
 */
public class CustomHandler  implements HttpHandler {


	private String response = null;
	private int responseCode = 200;
	private Map<String, Object> params;

	private final static Logger LOGGER = Logger.getLogger(CustomHandler.class.getName());

	/**
         * A custom handler for client requests
         * @param exchange can be the request or the response object
         * @throws IOException
         */
        public void handle(HttpExchange exchange) throws IOException {

            try {
				params = (Map<String, Object>)exchange.getAttribute("parameters");

                if (params.get("request").equals("login")) {
					loginRequest();
                }
                else if (params.get("request").equals("score")) {
					scoreRequest();
                }
                else if (params.get("request").equals("highscorelist")) {

					Headers headers = exchange.getResponseHeaders();
					highscorelistRequest(headers);
                }
                else {
                   unHandledRequest();
                }
            }
            catch (NumberFormatException exception) {
				exceptionHandledResponse(exception.getMessage());
            }
            catch (Exception exception) {
				exceptionHandledResponse(exception.getMessage());
            } finally {

                if (response != null)
                    exchange.sendResponseHeaders(responseCode, response.length());
                else
                    exchange.sendResponseHeaders(responseCode, 0);

                // sending response to client
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            }

        }


	private void loginRequest() {

		LOGGER.log(Level.INFO, "new user was created, user id is: " + (String)params.get("userid") + " ");

		response = SessionService.SERVICE.createSession(Integer.parseInt((String) params.get("userid")));

		if(response == null) responseCode = 500; // Server error
	}

	private void scoreRequest() {
		LOGGER.log(Level.INFO, "the new score: " + (String)params.get("score") + " ");


		int userId = SessionService.SERVICE.validateSessionKey((String)params.get("sessionkey"));

		if (userId  == -1)    {
			responseCode = 401;
			response = "unauthorized user";
		}

		else {
			int result = ScoreService.SCORE.insertScore(userId, Integer.parseInt((String)params.get("levelid")),Integer.parseInt((String)params.get("score")));
			if(result == -1)
				responseCode = 500; // Server error
		}

	}


	private void highscorelistRequest(Headers headers) {
		// A list of the best scores has been requested
		System.out.println("Received a new request for the highest scores" +
				"the level " + (String)params.get("levelid"));

		response = ScoreService.SCORE.getHighestScores(
				Integer.parseInt((String)params.get("levelid")));

		// This is a header to permit the download of the csv

		headers.add("Content-Type", "text/csv");
		headers.add("Content-Disposition", "attachment;filename=myfilename.csv");

	}

	private void unHandledRequest() {
		response = "Method not implemented";
		System.out.println(response);
		responseCode = 400; // Request type not implemented
	}

	private void exceptionHandledResponse(String message) {
		responseCode = 400;
		response = message;
		System.out.println(message);
	}

}
