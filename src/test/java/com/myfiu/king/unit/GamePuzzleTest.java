package com.myfiu.king.unit;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.CustomHandler;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.service.SessionService;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;


import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author by ali myftiu.
 */

@RunWith(MockitoJUnitRunner.class)
public class GamePuzzleTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ScoreService scoreService = new ScoreService();

    @Test
    public void shouldReturnTheHighestScore() throws GamePuzzleException {

        //given
        scoreService.insertScore(121, 2, 1101);
        scoreService.insertScore(411, 2, 5101);
        scoreService.insertScore(341, 2, 3213);
        scoreService.insertScore(123, 4, 9101);


        //when
        String result = scoreService.getHighestScores(2);

        //then
        assertEquals("411=5101\n341=3213\n121=1101", result);

    }





}