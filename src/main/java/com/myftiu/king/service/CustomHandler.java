package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

				switch ((String)params.get("request")) {
					case "login":
						loginRequest();
						break;
					case "score" :
						scoreRequest();
						break;
					case "highscorelist":
						Headers headers = exchange.getResponseHeaders();
						highscorelistRequest(headers);
						break;
					default:
						throw new GamePuzzleException("The request can not be handle", 501);
				}
            }
            catch (NumberFormatException ex) {
				exceptionHandledResponse(ex.getMessage(), 400);
            }
			catch (GamePuzzleException ex) {
				exceptionHandledResponse(ex.getMessage(), ex.getStatusCode());
			}
            catch (Exception ex) {
				exceptionHandledResponse(ex.getMessage(), 501);
            } finally {

				exchange.sendResponseHeaders(responseCode, response.length());
                // sending response to client
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            }

        }

	/**
	 * Creates a new users and starts the session
	 * @throws GamePuzzleException
	 */
	private void loginRequest() throws GamePuzzleException
	{

		LOGGER.log(Level.INFO, "new user was created, user id is: " + (String)params.get("userid") + " ");
		response = SessionService.SERVICE.createSession(Integer.parseInt((String) params.get("userid")));

	}


	/**
	 *
	 * @throws GamePuzzleException
	 */
	private void scoreRequest() throws GamePuzzleException
	{
		LOGGER.log(Level.INFO, "the new score: " + params.get("score") + " ");

		int userId = SessionService.SERVICE.validateSessionKey((String)params.get("sessionkey"));
		ScoreService.SCORE.insertScore(userId, Integer.parseInt((String)params.get("levelid")),Integer.parseInt((String)params.get("score")));

	}


	/**
	 *
	 * @param headers
	 */

	private void highscorelistRequest(Headers headers) {

		LOGGER.log(Level.INFO, "generating csv file for highestScoreList for levelId : " + params.get("levelid") + " ");
		response = ScoreService.SCORE.getHighestScores(Integer.parseInt((String)params.get("levelid")));
		headers.add("Content-Type", "text/csv");
		headers.add("Content-Disposition", "attachment;filename=gamepuzzle.csv");
	}


	/**
	 *
	 * @param message
	 * @param statusCode
	 */
	private void exceptionHandledResponse(String message, int statusCode) {
		responseCode = statusCode;
		response = message;
		System.out.println(message);
	}

}
