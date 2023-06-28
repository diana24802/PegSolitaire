package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService{

    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "dianka2002";
    public static final String INSERT_STATEMENT = "INSERT INTO score (player, game, boardMode, points, played_at, time) VALUES (?,?,?,?,?,?)";
    public static final String SELECT_STATEMENT = "SELECT player, game, boardMode, points, played_at FROM score WHERE game = ? ORDER BY points ASC LIMIT 10";
    public static final String DELETE_STATEMENT = "DELETE FROM score";

    @Override
    public void addScore(Score score) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)
        ){
            statement.setString(1, score.getPlayer());
            statement.setString(2, score.getGame());
            statement.setString(3, score.getBoardMode());
            statement.setInt(4, score.getPoints());
            statement.setTimestamp(5, new Timestamp(score.getPlayedAt().getTime()));
            statement.setLong(6, score.getTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)
        ){
            statement.setString(1,game);
            try (var rs = statement.executeQuery()){
                var scores = new ArrayList<Score>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getTimestamp(5),
                            rs.getLong(6)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void reset() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.createStatement();
        ){
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }
}
