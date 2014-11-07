package com.myfiu.king.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
public class GamePuzzleTest extends GenericTestCase
{

	private HttpURLConnection httpConnection;


	@Test
	public void shouldCreateNewUser() throws IOException
	{

		//given
		String createUserUrl = "http://localhost:8009/411/login";

		// when
		httpConnection = createConnection(createUserUrl, "GET");
		int status = httpConnection.getResponseCode();
		String sessionKey = getResponseBody(httpConnection.getInputStream());
		httpConnection.disconnect();

		//then
		assertNotNull(sessionKey);
		assertEquals(status, 200);

	}


	@Test
	public void shouldFailToReturnResult() throws IOException
	{
		//given
		String createScoreUrl = "http://localhost:8009/4711/score?sessionkey=2086050375ee41878b2e6ca0143de1b8";
		String score = "1500";

		//when
		httpConnection = createConnection(createScoreUrl, "POST");
		httpConnection.setRequestProperty("Content-Type", "TEXT");
		httpConnection.setRequestProperty("Content-Length", Integer.toString(score.length()));
		httpConnection.setDoOutput(true);
		OutputStream os = httpConnection.getOutputStream();
		os.write(score.getBytes());
		os.flush();
		os.close();
		httpConnection.disconnect();

		//then
		int status = httpConnection.getResponseCode();
		assertEquals(status, HttpURLConnection.HTTP_BAD_REQUEST);

	}




}
