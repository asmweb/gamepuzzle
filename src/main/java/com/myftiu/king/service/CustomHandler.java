package com.myftiu.king.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import com.sun.net.httpserver.*;

/**
 * Created by myftiu on 04/11/14.
 */
public class CustomHandler  implements HttpHandler {


    /*
     * Usage: handle a client request
     *
     * Input:
     *    exchange = request/response object
     *
     */
        public void handle(HttpExchange exchange) throws IOException {

            String response = null;
            int statusCode = 200;

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> params = (Map<String, Object>)exchange.getAttribute("parameters");

                if (params.get("request").equals("login")) {

                    // The user is logging-in, asking for a session key
                    System.out.println("New login request received for the user" +
                            (String)params.get("userid"));

                    response = SessionService.SERVICE.getSessionKey(
                            Integer.parseInt((String)params.get("userid")));

                    if(response == null) statusCode = 500; // Server error
                }
                else if (params.get("request").equals("score")) {

                    // A new store has been received to be stored
                    System.out.println("New request received to save the score" +
                            (String)params.get("score"));

                    int userId = SessionService.SERVICE.validateSessionKey(
                            (String)params.get("sessionkey"));

                    if (userId  == -1)
                        statusCode = 401; // Unhautorized user
                    else if(ScoreService.SCORE.insertScore(
                            userId, Integer.parseInt((String)params.get("levelid")),
                            Integer.parseInt((String)params.get("score"))) == -1)
                        statusCode = 500; // Server error
                }
                else if (params.get("request").equals("highscorelist")) {

                    // A list of the best scores has been requested
                    System.out.println("Received a new request for the highest scores" +
                            "the level " + (String)params.get("levelid"));

                    response = ScoreService.SCORE.getHighestScores(
                            Integer.parseInt((String)params.get("levelid")));

                    // This is a header to permit the download of the csv
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Content-Type", "text/csv");
                    headers.add("Content-Disposition", "attachment;filename=myfilename.csv");
                }
                else {
                    response = "Method not implemented";
                    System.out.println(response);
                    statusCode = 400; // Request type not implemented
                }
            }
            catch (NumberFormatException exception) {
                statusCode = 400;
                response = "Wrong number format";
                System.out.println(response);
            }
            catch (Exception exception) {
                statusCode = 400;
                response = exception.getMessage().toString();
                System.out.println(response);
            }

            // Send the header response
            if (response != null)
                exchange.sendResponseHeaders(statusCode, response.length());
            else
                exchange.sendResponseHeaders(statusCode, 0);

            // Send the body response
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }

}
