package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RatingServiceRestClient implements RatingService{
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addRating(Rating rating) {
        if (rating != null)
            restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getRating(String gameName, String playerName) {
        return Objects.requireNonNull(restTemplate.getForObject(url + "/" + gameName + "/" + playerName, Integer.class));
    }

    @Override
    public int getAverageRating(String game) {
        return Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game, float.class).getBody()).intValue();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
