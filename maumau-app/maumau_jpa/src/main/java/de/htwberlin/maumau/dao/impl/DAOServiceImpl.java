package de.htwberlin.maumau.dao.impl;

import de.htwberlin.maumau.dao.export.DAOService;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class DAOServiceImpl implements DAOService {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mauPU");
    EntityManager em = emf.createEntityManager();

    @Override
    public void persist(MauMau game) {
        try{
            em.getTransaction().begin();
            em.persist(game);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MauMau update(MauMau game) {
        try{
            em.getTransaction().begin();
            game = em.merge(game);
            em.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return game;
    }

    @Override
    public MauMau findGameById(Long id) {
        MauMau game = null;
        try{
            em.getTransaction().begin();
            game = em.find(MauMau.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game;
    }

    @Override
    public List<MauMau> findAllGames() {
        List<MauMau> allGames = new ArrayList<>();
        try{
            em.getTransaction().begin();
            TypedQuery<MauMau> query = em.createQuery("SELECT m FROM MauMau m ORDER BY m.id", MauMau.class);
            allGames = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allGames;
    }

    @Override
    public int removeAllGames() {
        int deletedCount = 0;
        try {
            em.getTransaction().begin();
            deletedCount = em.createQuery("DELETE FROM MauMau").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deletedCount;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
