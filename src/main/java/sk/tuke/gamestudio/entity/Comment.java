package sk.tuke.gamestudio.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable{
    @Id
    @GeneratedValue//(strategy= GenerationType.IDENTITY)
    private int ident;

    private String player;

    private String game;

    private String comment;

    private Date commentedAt;

    public Comment(String player, String game, String comment, Date commentedAt) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedAt = commentedAt;
    }

    public Comment() {

    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedAt=" + commentedAt +
                '}';
    }

}
