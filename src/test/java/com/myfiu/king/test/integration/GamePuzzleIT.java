package com.myfiu.king.test.integration;

import com.myftiu.king.GameServerMain;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by myftiu on 06/11/14.
 */

@RunWith(JUnit4.class)
public class GamePuzzleIT {

    private  GameServerMain gameServerMain;
    private HttpURLConnection httpConnection;


    @Before
    public  void startServer() throws IOException {
        gameServerMain = new GameServerMain();
        gameServerMain.startServer();

    }

    @After
    public  void stopServer() throws IOException {
        gameServerMain.stopServer();
    }


    @Test
    public void shouldCreateNewUser() throws IOException {


        //given
        String createUserUrl =  "http://localhost:8009/411/login";

        // when
        httpConnection = createConnection(createUserUrl, "GET");
        int status = httpConnection.getResponseCode();
        String sessionKey = getResponseBody(httpConnection.getInputStream());


        //then
        assertNotNull(sessionKey);
        assertEquals(status, 200);

    }




    @Test
    public void shouldFailToReturnResult() throws IOException {
        //given
        String createScoreUrl = "http://localhost:8009/4711/score?sessionkey= 2086050375ee41878b2e6ca0143de1b8";
        String score = "1500";

        //when
        httpConnection = createConnection(createScoreUrl, "POST");
        httpConnection.setRequestProperty( "Content-Type", "TEXT" );
        httpConnection.setRequestProperty( "Content-Length", Integer.toString(score.length()) );
        httpConnection.setDoOutput(true);
        OutputStream os = httpConnection.getOutputStream();
        os.write( score.getBytes() );
        os.flush();
        os.close();

        //then
        int status = httpConnection.getResponseCode();
        assertEquals(status, HttpURLConnection.HTTP_BAD_REQUEST);

    }

    private HttpURLConnection createConnection(String url, String requestMethod) throws IOException {
        httpConnection = (HttpURLConnection) new URL(url).openConnection();
        httpConnection.setRequestMethod(requestMethod);
        return httpConnection;
    }


    private String getResponseBody(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String responseBody = scanner.next();
        return responseBody;
    }



}
