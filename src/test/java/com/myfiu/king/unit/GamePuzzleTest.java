package com.myfiu.king.unit;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.ScoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Created by myftiu on 06/11/14.
 */

@RunWith(JUnit4.class)
public class GamePuzzleTest {


    @Test
    public void shouldReturnTheHighestScore() throws GamePuzzleException {

        //given
        ScoreService.SCORE.insertScore(121, 2, 1101);
        ScoreService.SCORE.insertScore(411, 2, 5101);
        ScoreService.SCORE.insertScore(341, 2, 3213);
        ScoreService.SCORE.insertScore(123, 4, 9101);


        //when
        String result = ScoreService.SCORE.getHighestScores(2);

        //then
        assertEquals("411=5101\n341=3213\n121=1101", result);

    }


}
