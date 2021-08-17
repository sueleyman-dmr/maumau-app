import de.htwberlin.maumau.card_management.export.*;
import de.htwberlin.maumau.card_management.impl.CardServiceImpl;
import de.htwberlin.maumau.dao.export.DAOService;
import de.htwberlin.maumau.dao.impl.DAOServiceImpl;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMauService;
import de.htwberlin.maumau.maumau_management.impl.mauGame.MauMauServiceImpl;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.player_management.export.PlayerService;
import de.htwberlin.maumau.player_management.impl.PlayerServiceImpl;
import de.htwberlin.maumau.regel_management.export.RuleService;
import de.htwberlin.maumau.regel_management.impl.RuleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DAOServiceTest {

    private DAOService daoService;
    private MauMauService mauMauService;

    @Before
    public void setUp(){
        daoService = new DAOServiceImpl();
        CardService cardService = new CardServiceImpl();
        RuleService ruleService = new RuleServiceImpl();
        PlayerService playerService = new PlayerServiceImpl();

        MauMauServiceImpl mauMauServiceI = new MauMauServiceImpl(ruleService, cardService, playerService);

        this.mauMauService = mauMauServiceI;
    }

    @Test
    public void testPersistGame(){
        try {
            MauMau game = mauMauService.createGame(5, 2, 2, 4, true);
            daoService.persist(game);
            Long gameId = game.getId();
            //GeneratedValue
            Assert.assertTrue(gameId > 0);
        } catch (MauMauPlayerSizeException e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateGame() {
        try {
            MauMau gameBefore = mauMauService.createGame(5, 2, 2, 4, true);
            gameBefore.getDeck().setDiscoveredCard(new Card(Color.HERZ, CardValue.ACHT));
            daoService.persist(gameBefore);
            Long gameBeforeId = gameBefore.getId();

            MauMau gameAfter = daoService.findGameById(gameBeforeId);
            mauMauService.addPlayerToGame(gameAfter, new Player("Süloooo"));
            gameAfter.getDeck().setDiscoveredCard(new Card(Color.HERZ, CardValue.ASS));
            gameBefore = daoService.update(gameAfter);


            Assert.assertEquals(gameAfter.getDeck().getDiscoveredCard(), gameBefore.getDeck().getDiscoveredCard());
            Assert.assertEquals(gameAfter.getPlayerList().size(), gameBefore.getPlayerList().size());
        } catch (MauMauPlayerSizeException e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFindGameById(){
        try {
            MauMau game = mauMauService.createGame(5, 2, 2, 4, true);
            daoService.persist(game);
            Long gameId = game.getId();
            MauMau game2 = daoService.findGameById(gameId);
            Assert.assertEquals(gameId, game2.getId());
        } catch (MauMauPlayerSizeException e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAllGames() {
        try {
            List<MauMau> allGames = new ArrayList<>();
            MauMau game = mauMauService.createGame(5, 2, 2, 4, true);
            MauMau game2 = mauMauService.createGame(5, 2, 2, 4, true);
         //   MauMau game3 = mauMauService.createGame(5, 2, 1, 5, true);
            daoService.persist(game);
            daoService.persist(game2);
         //   daoService.persist(game3);

            allGames = daoService.findAllGames();

            Assert.assertEquals(2, allGames.size());

        } catch (MauMauPlayerSizeException e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveAllGames() {
        try{
            daoService.removeAllGames();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
