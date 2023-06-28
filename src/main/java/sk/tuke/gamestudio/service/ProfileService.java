package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Profile;

public interface ProfileService {
    void addProfile(Profile profile);
    Profile getProfile(String game, String login);
    void reset();
}