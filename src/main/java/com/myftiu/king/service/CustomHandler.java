package com.myftiu.king.service;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.utils.ServerUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by myftiu on 04/11/14.
 */
public class CustomHandler  implements HttpHandler {


	private String response = null;
	private int responseCode = HttpURLConnection.HTTP_OK;
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
				exceptionHandledResponse(ex.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
            }
			catch (GamePuzzleException ex) {
				exceptionHandledResponse(ex.getMessage(), ex.getStatusCode());
			}
            catch (Exception ex) {
				exceptionHandledResponse(ex.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
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

		LOGGER.log(Level.INFO, "new user was created, user id is: " + params.get("userid") + " ");
		response = SessionService.SERVICE.createSession(Integer.parseInt((String) params.get("userid")));
        responseCode = HttpURLConnection.HTTP_OK;

	}


	/**
	 *
	 * @throws GamePuzzleException
	 */
	private void scoreRequest() throws GamePuzzleException
	{
		LOGGER.log(Level.INFO, "the new score: " + params.get("score") + " ");

		String sessionKey = (String)params.get("sessionkey");
		int levelId = Integer.parseInt((String)params.get("levelid"));
		int scoreNr = Integer.parseInt((String)params.get("points"));


		int userId = SessionService.SERVICE.validateSessionKey(sessionKey);
		ScoreService.SCORE.insertScore(userId, levelId,scoreNr);
        responseCode = HttpURLConnection.HTTP_CREATED;

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
