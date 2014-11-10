package com.myfiu.king.integration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author by ali myftiu.
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
