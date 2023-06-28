package sk.tuke.gamestudio.game.pegsolitaire;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

public class TestJDBC {
    public static void main(String[] args) throws Exception{
        ScoreService service = new sk.tuke.gamestudio.service.ScoreServiceJDBC();
        service.addScore(new Score("jaro","PegSolitaire", "custom", 5, new Date(), 100));
        service.addScore(new Score("jarka","PegSolitaire", "custom", 1, new Date(), 100));
        service.addScore(new Score("cyril","PegSolitaire", "custom", 10, new Date(), 100));

        var scores = service.getTopScores("PegSolitaire");
        System.out.println(scores);
    }
}
