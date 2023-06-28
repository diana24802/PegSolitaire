package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Profile;
import java.util.Objects;

public class ProfileServiceRestClient implements ProfileService {
    private final String url = "http://localhost:8080/api/profile";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addProfile(Profile profile) {
        restTemplate.postForEntity(url, profile, Profile.class);
    }

    @Override
    public Profile getProfile(String game, String login) {
        return Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game + "/" + login, Profile.class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}