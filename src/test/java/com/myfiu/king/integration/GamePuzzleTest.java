package com.myfiu.king.integration;

import com.myftiu.king.service.CustomHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author by ali myftiu.
 */

@RunWith(MockitoJUnitRunner.class)
public class GamePuzzleTest extends GenericTestCase
{

	private HttpURLConnection httpConnection;
    private final CustomHandler CUSTOM_HANDLER = new CustomHandler();


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

    @Test
    public void shouldNotCreateNewSessionForActiveUser() throws IOException {

        //given
        String createUserUrl = "http://localhost:8009/411/login";
        httpConnection = createConnection(createUserUrl, "GET");
        String sessionKey = getResponseBody(httpConnection.getInputStream());
        httpConnection.disconnect();

        //when
        String recallActiveUserLogin = "http://localhost:8009/411/login";
        httpConnection = createConnection(recallActiveUserLogin, "GET");
        String activeSessionKey = getResponseBody(httpConnection.getInputStream());
        httpConnection.disconnect();

        //then
        assertEquals(sessionKey, activeSessionKey);

    }

}
