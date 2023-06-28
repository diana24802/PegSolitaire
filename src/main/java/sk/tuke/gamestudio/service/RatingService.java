package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

public interface RatingService {
    void addRating(Rating rating);
    int getRating(String game, String name);
    int getAverageRating(String game);
    void reset();
}
