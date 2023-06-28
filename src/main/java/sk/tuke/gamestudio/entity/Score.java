package sk.tuke.gamestudio.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery( name = "Score.getTopScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points ASC")
@NamedQuery( name = "Score.resetScores",
        query = "DELETE FROM Score")

public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident; //identifikator
    private String player;
    private String game;
    private String boardMode;
    private int points;
    private Date playedAt;
    private long time;

    public Score(String player, String game, String mode, int points, Date playedAt, long time) {
        this.player = player;
        this.game = game;
        this.boardMode = mode;
        this.points = points;
        this.playedAt = playedAt;
        this.time = time;
    }

    public Score() {

    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedAt() {
        return playedAt;
    }

    public void setMode(String mode) {
        this.boardMode = mode;
    }

    public String getBoardMode() {
        return boardMode;
    }

    public void setPlayedAt(Date playedAt) {
        this.playedAt = playedAt;
    }

    @Override
    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", points=" + points +
                ", playedAt=" + playedAt +
                '}';
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
