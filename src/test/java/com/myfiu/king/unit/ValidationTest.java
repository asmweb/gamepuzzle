package com.myfiu.king.unit;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.service.SessionService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * @author by ali myftiu.
 */
public class ValidationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFailCreatingNewUser() throws GamePuzzleException, IOException {

        //given
        int user = -123;

        // when
        expectedException.expect(GamePuzzleException.class);
        SessionService.SERVICE.createSession(user);

        //then

    }


    @Test
    public void shouldFailAddingScoreInvalidUser() throws GamePuzzleException {

        //given
        int failingUser = ((int) Math.pow(2.0, 31.0)) + 1;
        int okScore = 13131;
        int okLevel = 4;


        //when
         expectedException.expect(GamePuzzleException.class);
         expectedException.expectMessage("Invalid userId " + failingUser);
         ScoreService.SCORE.insertScore(failingUser, okScore, okLevel);

        //then



    }

    @Test
    public void shouldFailAddingScoreInvalidScore() throws GamePuzzleException {

        //given
        int okUser = 412;
        int failingScore = ((int) Math.pow(2.0, 31.0)) + 1;
        int okLevel = 4;


        //when
        expectedException.expect(GamePuzzleException.class);
        expectedException.expectMessage("Invalid score " + failingScore);
        ScoreService.SCORE.insertScore(okUser, okLevel, failingScore);

    }

    @Test
    public void shouldFailAddingScoreInvalidLevelId() throws GamePuzzleException {

        //given
        int okUser = 412;
        int okScore = 13131;
        int failingLevel = ((int) Math.pow(2.0, 31.0)) + 1;


        //when
        expectedException.expect(GamePuzzleException.class);
        expectedException.expectMessage("Invalid levelId " + failingLevel);
        ScoreService.SCORE.insertScore(okUser, failingLevel, okScore);

    }

}
