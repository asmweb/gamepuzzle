package com.myfiu.king.unit;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.CustomHandler;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.service.SessionService;
import com.myftiu.king.service.TimeDefinition;
import com.myftiu.king.service.impl.ScoreServiceImpl;
import com.myftiu.king.service.impl.SessionServiceImpl;
import com.myftiu.king.service.impl.TimeDefinitionImpl;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author by ali myftiu.
 */

@RunWith(MockitoJUnitRunner.class)
public class SessionTest {


	 @Rule
	 public ExpectedException expectedException = ExpectedException.none();

	@Spy
	private TimeDefinition timeDefinition =  new TimeDefinitionImpl();
	private SessionService sessionService;
	private Calendar calendar;

	@Before
	public void initTest() {

		sessionService = new SessionServiceImpl(timeDefinition);
		calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, +1);
	}

    @Test
    public void shouldFailSessionExpired() throws IOException {

		//given
		String sessionKey = sessionService.createSession(2);
		when(timeDefinition.getCurrentTime()).thenReturn(calendar.getTimeInMillis());

		//when
		expectedException.expect(GamePuzzleException.class);
		expectedException.expectMessage(ServerConfig.UNAUTHORIZED_USER);
		int user = sessionService.validateSessionKey(sessionKey);

		// then
		assertEquals(user, 2);
    }


	@Test
	public void shouldCorrectlyCreateANewSession() throws GamePuzzleException
	{

		//given
		int user = 2;

		//when
		String sessionKey = sessionService.createSession(user);
		int retrivedUser = sessionService.validateSessionKey(sessionKey);

		assertEquals(user, retrivedUser);


	}




}
