package sk.tuke.gamestudio.game.pegsolitaire;

import sk.tuke.gamestudio.game.pegsolitaire.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.CommentServiceJPA;
import sk.tuke.gamestudio.service.ProfileServiceJPA;
import sk.tuke.gamestudio.service.RatingServiceJPA;
import sk.tuke.gamestudio.service.ScoreServiceJPA;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(new ScoreServiceJPA(), new CommentServiceJPA(), new RatingServiceJPA(), new ProfileServiceJPA());
        ui.play();
    }
}