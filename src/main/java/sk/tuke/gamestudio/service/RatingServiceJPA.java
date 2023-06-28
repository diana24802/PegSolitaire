package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Objects;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addRating(Rating rating) {
        Rating existingRating = getExistingRating(rating.getGame(), rating.getPlayer());

        if (existingRating != null) {
            existingRating.setRating(rating.getRating());
        } else {
            entityManager.persist(rating);
        }
    }

    @Override
    public int getRating(String game, String player) {
        return (int) Objects.requireNonNull(entityManager.createQuery("select r.rating from Rating r where r.game = :game and r.player = :player"))
                .setParameter("game", game).setParameter("player", player)
                .getSingleResult();
    }
    private Rating getExistingRating(String game, String player) {
        try {
            return (Rating) entityManager.createQuery("select r from Rating r where r.game = :game and r.player = :player")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public int getAverageRating(String game) {
        Double avgRating = (Double) entityManager.createQuery("select avg(r.rating) from Rating r where r.game = :game")
                .setParameter("game", game).getSingleResult();

        if (avgRating == null) {
            return 0;
        }
        return (int) Math.round(avgRating);
    }


    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}
