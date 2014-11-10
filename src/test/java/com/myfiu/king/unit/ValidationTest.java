package com.myfiu.king.unit;

import com.myftiu.king.exception.GamePuzzleException;
import com.myftiu.king.service.TimeDefinition;
import com.myftiu.king.service.impl.ScoreServiceImpl;
import com.myftiu.king.service.impl.SessionServiceImpl;
import com.myftiu.king.service.impl.TimeDefinitionImpl;
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
	private TimeDefinition timeDefinition = new TimeDefinitionImpl();
    private SessionServiceImpl sessionService = new SessionServiceImpl(timeDefinition);
    private ScoreServiceImpl scoreService = new ScoreServiceImpl();

    @Test
    public void shouldFailCreatingNewUser() throws GamePuzzleException, IOException {

        //given
        int user = -123;

        // when
        expectedException.expect(GamePuzzleException.class);
        sessionService.createSession(user);

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
         scoreService.insertScore(failingUser, okScore, okLevel);

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
        scoreService.insertScore(okUser, okLevel, failingScore);

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
        scoreService.insertScore(okUser, failingLevel, okScore);

    }

}
