package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"player", "game"})})
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident; //identifikator
    private String player;
    private String game;
    private int rating;
    private Date ratedAt;

    public Rating(String player, String game, int rating, Date ratedAt) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedAt = ratedAt;
    }

    public Rating() {

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Value must be between 1 and 5"); // throw an exception if the value is outside the range
        }
    }

    public Date getRatedAt() {
        return ratedAt;
    }
    public void setRatedAt(Date ratedAt) {
        this.ratedAt = ratedAt;
    }

    @Override
    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", rating=" + rating +
                ", playedAt=" + ratedAt +
                '}';
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }
}
