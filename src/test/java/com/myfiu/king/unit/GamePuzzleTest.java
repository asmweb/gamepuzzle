package com.myfiu.king.unit;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.impl.ScoreServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author by ali myftiu.
 */

@RunWith(MockitoJUnitRunner.class)
public class GamePuzzleTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ScoreServiceImpl scoreService = new ScoreServiceImpl();




    @Before
    public void initTest() {

    }

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

    @Test
    public void shouldReturnOneScore() throws GamePuzzleException {

        //given
        scoreService.insertScore(121, 2, 1101);
        scoreService.insertScore(121, 2, 5101);
        scoreService.insertScore(121, 2, 3213);
        scoreService.insertScore(121, 2, 9101);


        //when
        String result = scoreService.getHighestScores(2);

        //then
        assertEquals("121=9101", result);

    }


    @Test
    public void shouldNotReturnMoreThanMaxResult() throws GamePuzzleException {

        //given
        for(int i=0; i<=(ServerConfig.MAX_RESULT_LIST+5); i++) {
            scoreService.insertScore(121+i, 2, 11+i+i);
        }

        //when
        String result = scoreService.getHighestScores(2);

        //then
        String resultArray[] = result.split("\n");
        assertEquals(resultArray.length, ServerConfig.MAX_RESULT_LIST);

    }


}
