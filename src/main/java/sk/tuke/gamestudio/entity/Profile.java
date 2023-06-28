package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Profile implements Serializable {
    @Id
    private String login;
    private String password;
    private String game;

    public Profile(String login, String password, String game) {
        this.login = login;
        this.password = password;
        this.game = game;
    }

    public Profile() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
