package com.myfiu.king.unit;

import com.myftiu.king.service.CustomHandler;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.service.SessionService;
import com.myftiu.king.service.impl.ScoreServiceImpl;
import com.myftiu.king.service.impl.SessionServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by myftiu on 09/11/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class SessionTest {


    private SessionService sessionService = mock(SessionServiceImpl.class);
    private ScoreService scoreService = mock(ScoreServiceImpl.class);
    private @Spy CustomHandler customHander = new CustomHandler(sessionService, scoreService);
    private HttpExchange httpExchange = mock(HttpExchange.class);
    private OutputStream outputStream = mock(OutputStream.class);

    @Before
    public void initTest() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("request", "score");
        parameters.put("levelid", "2");
        parameters.put("points", "3");
        when(httpExchange.getAttribute("parameters")).thenReturn(parameters);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

    }

    @Test
    public void shouldFail() throws IOException {
        customHander.handle(httpExchange);
        int test = httpExchange.getResponseCode();
        assertEquals(test, HttpURLConnection.HTTP_BAD_REQUEST);
    }


}
