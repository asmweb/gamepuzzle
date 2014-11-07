package com.myfiu.king.integration;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.myftiu.king.GameServerMain;

/**
 * @author by ali myftiu on 07/11/14.
 */


public class GenericTestCase {

	private  GameServerMain gameServerMain;


	@Before
	public  void startServer() throws IOException {
		gameServerMain = new GameServerMain();
		gameServerMain.startServer();

	}

	@After
	public  void stopServer() throws IOException {
		gameServerMain.stopServer();
	}

	protected HttpURLConnection createConnection(String url, String requestMethod) throws IOException
	{
		HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
		httpConnection.setRequestMethod(requestMethod);
		return httpConnection;
	}


	protected String getResponseBody(InputStream inputStream)
	{
		String responseBody = "";
		Scanner scanner = new Scanner(inputStream);
		if(scanner.hasNext()) {
		 responseBody = scanner.next();
		}
		return responseBody;
	}

}
