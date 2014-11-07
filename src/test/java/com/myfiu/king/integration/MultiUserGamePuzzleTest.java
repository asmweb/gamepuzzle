package com.myfiu.king.integration;

import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author by ali myftiu on 07/11/14.
 */
public class MultiUserGamePuzzleTest extends  GenericTestCase {

	private HttpURLConnection httpConnection;



	@Test
	public void shoudlReturnCorrectResultWithMultipleUsers() throws IOException
	{

		//given
		List<String> sessionKeys = createMultipleUsers();
		createMultipleScores(sessionKeys);

		// when
		String createUserUrl = "http://localhost:8009/2/highscorelist";

		// when
		httpConnection = createConnection(createUserUrl, "GET");
		int status = httpConnection.getResponseCode();
		InputStream inputStream =  httpConnection.getInputStream();
		InputStream in = new BufferedInputStream(httpConnection.getInputStream());
		InputStreamReader inr = new InputStreamReader(inputStream);
		httpConnection.disconnect();


		//then
		assertEquals(status, 200);

	}



	private List<String>  createMultipleUsers() throws IOException
	{

		List<String> sessionKeys = new ArrayList<>();
		int i = 0;
		while(i < 10) {
			String createUserUrl = "http://localhost:8009/41"+i+"/login";
			httpConnection = createConnection(createUserUrl, "GET");
			String sessionKey = getResponseBody(httpConnection.getInputStream());
			sessionKeys.add(sessionKey);
			httpConnection.disconnect();
			i++;
		}
		return sessionKeys;

	}

	public void createMultipleScores(List<String> sessionKeys) throws IOException
	{
		int score = 1500;

		for(String sessionKey: sessionKeys) {
			String createScoreUrl = "http://localhost:8009/2/score?sessionkey="+sessionKey;

			//when
			httpConnection = createConnection(createScoreUrl, "POST");
			httpConnection.setRequestProperty("Content-Type", "TEXT");
			httpConnection.setRequestProperty("Content-Length", Integer.toString(Integer.toString(score).length()));
			httpConnection.setDoOutput(true);
			OutputStream os = httpConnection.getOutputStream();
			os.write(Integer.toString(score).getBytes());
			os.flush();
			os.close();
			httpConnection.disconnect();
			score = score + 10;
		}




	}



}
