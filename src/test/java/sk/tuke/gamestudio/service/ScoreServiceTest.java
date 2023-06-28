package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreServiceTest {
    @Autowired
    private ScoreService scoreService;
    @Test
    public void reset() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("PegSolitaire").size());
    }

    @Test
    public void addScore() {
        scoreService.reset();
        var date = new Date();

        scoreService.addScore(new Score("Jaro", "PegSolitaire", "custom",100, date, 100));

        var scores = scoreService.getTopScores("PegSolitaire");
        assertEquals(1, scores.size());
        assertEquals("PegSolitaire", scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedAt());
    }

    @Test
    public void getTopScores() {
        scoreService.reset();
        var date = new Date();
        scoreService.addScore(new Score("Jaro", "PegSolitaire", "custom",120, date, 100));
        scoreService.addScore(new Score("Katka", "PegSolitaire", "custom",150, date, 100));
        scoreService.addScore(new Score("Jaro", "PegSolitaire", "custom",100, date, 100));

        var scores = scoreService.getTopScores("PegSolitaire");

        assertEquals(3, scores.size());

        assertEquals("PegSolitaire", scores.get(2).getGame());
        assertEquals("Katka", scores.get(2).getPlayer());
        assertEquals(150, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedAt());

        assertEquals("PegSolitaire", scores.get(1).getGame());
        assertEquals("Jaro", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedAt());

        assertEquals("PegSolitaire", scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedAt());
    }
}
