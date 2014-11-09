package com.myfiu.king.integration;

import com.myftiu.king.ServerConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by myftiu on 09/11/14.
 */


public class GameServerTest extends GenericTestCase{

    private HttpURLConnection httpConnection;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldGetErrorOnWrongRequestPath() throws IOException {
        //given
        String createUserUrl = "http://localhost:8009/2/test";

        // when
        httpConnection = createConnection(createUserUrl, "GET");
        int status = httpConnection.getResponseCode();
        expectedException.expect(IOException.class);
        String sessionKey = getResponseBody(httpConnection.getInputStream());
        httpConnection.disconnect();

        //then
        assertNotNull(sessionKey);
        assertEquals(status, HttpURLConnection.HTTP_BAD_REQUEST);
    }


}
