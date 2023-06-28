package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class ProfileServiceJPA implements ProfileService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addProfile(Profile profile) {
        entityManager.persist(profile);
    }

    @Override
    public Profile getProfile(String game, String login) {
        Profile profile = null;
        try {
            profile = (Profile) entityManager.createQuery("select p from Profile p where p.login = :login and p.game = :game")
                    .setParameter("login", login).setParameter("game", game).getSingleResult();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("------------");
            return profile;
        }
        return profile;
    }

    @Override
    public void reset() {
         entityManager.createNativeQuery("delete from profile").executeUpdate();
    }
}