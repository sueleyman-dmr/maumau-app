package de.htwberlin.maumau.dao.export;

import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;

import javax.persistence.EntityManager;
import java.util.List;

public interface DAOService {

    void persist(MauMau game);

    MauMau update(MauMau game);

    MauMau findGameById(Long id);

    List<MauMau> findAllGames();

    int removeAllGames();

    EntityManager getEntityManager();
}
