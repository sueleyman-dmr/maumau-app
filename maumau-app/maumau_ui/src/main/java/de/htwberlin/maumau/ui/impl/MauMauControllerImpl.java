package de.htwberlin.maumau.ui.impl;

import de.htwberlin.maumau.card_management.export.*;
import de.htwberlin.maumau.dao.export.DAOService;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.maumau_management.export.bot.Bot;
import de.htwberlin.maumau.maumau_management.export.bot.BotService;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMauService;
import de.htwberlin.maumau.player_management.export.Player;

import de.htwberlin.maumau.ui.export.MauMauController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The controller class implements from the interface MauMauController and it contains the logic/procedure of the game.
 */
public class MauMauControllerImpl implements MauMauController {
    private final MauMauService mauMauService;
    private final BotService botService;
    private final CardService cardService;
    private DAOService daoService;
    private MauMau game;
    private final MauMauView view;

    private static final Logger LOGGER = LogManager.getLogger(MauMauControllerImpl.class);

    /**
     * Constructor of the class MauMauControllerImpl
     * @param mauMauService - the mauMauService for functions of the game
     * @param view - the view Object for output messages
     */
    public MauMauControllerImpl(MauMauService mauMauService, MauMauView view, BotService botService, CardService cardService, DAOService daoService) {
        this.mauMauService=mauMauService;
        this.botService = botService;
        this.cardService = cardService;
        this.daoService = daoService;
        this.view = view;
    }

    @Override
    public void run(){
            LOGGER.debug("Method run() started!");
            view.welcomeMsg();
            rulesInput();
            gameType();
        try {
            gameProcedure();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("Method run() finished!");
    }

    private void createNewGame(){
        try {
            configureGame();
            view.setHumanPlayerName(game);
            mauMauService.setGameDate(game);
            firstCardOnDeck();
            daoService.persist(game);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }

    }

    private void firstCardOnDeck() throws EmptyStackException {
        //erste Karte aufdecken
        game.getDeck().discoverCard(game.getStack().drawCard());
        if(game.getDeck().getDiscoveredCard().getCardValue() == CardValue.BUBE) {
            LOGGER.debug("Program chose a JACK as first discovered card (Block started)");
            game.setCompulsoryColor(game.getDeck().getDiscoveredCard().getColor());
            System.out.println(); //Umbruch nur für die Stelle
            view.newColorMsg(game.getCompulsoryColor());
            LOGGER.debug("Program chose a JACK as first discovered card (Block finished)");
        }
    }

    private void gameType(){
        view.askForGameType();
        int choice = view.userInput(1, 2);
        if (choice == 1) {
            createNewGame();
        } else if(choice == 2) {
            List<MauMau> allGames = daoService.findAllGames();
            if(allGames.size() == 0) {
                view.mustCreateNewGameMsg();
                createNewGame();
            } else {
                    if(allGames.size() == 0) {
                        view.mustCreateNewGameMsg();
                        createNewGame();
                    } else {
                        view.printAllGames(allGames);
                        boolean exit = false;
                        do {
                            long input = view.userInput(1, allGames.size());
                            game = daoService.findGameById(input);
                            if(game.getWinner() != null) {
                                view.winnerExistsAlready();
                            } else {
                                exit=true;
                            }
                        } while(!exit);
                    }
                }
            }
        }


    /**
     * The procedure of the game. The game continues, while there is not a winner and checks if the next player is a human or bot.
     * Accordingly it is the turn of the player. The first discovered card is chosen randomly and if it is a JACK, it takes as compulsory color the same COLOR.
     * The first player is the first player in List.
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     * @throws EmptyStackException - thrown Exception
     */
    private void gameProcedure() throws PlayerNotAllowedActivityException, MauMauTechnicalException, EmptyStackException {
        LOGGER.debug("Method gameProcedure() started!");
        while(game.getWinner() == null) {
            //wenn jeder aufgibt und nur ein Spieler übrig ist.
            if(game.getPlayerList().size() == 1){
                game.setWinner(game.getPlayerList().get(0));
                view.printWinnerMsg(game.getWinner().getName());
                game = daoService.update(game);
                break;
            }
            if(mauMauService.getWinner(game) != null) {
                LOGGER.debug("Winner if block started!");
                view.printWinnerMsg(mauMauService.getWinner(game).getName());
                LOGGER.debug("Winner if block finished!");
                game = daoService.update(game);
                break;
            }
            if(!game.getActualPlayer().isBot()) {
                humanTurn();
            } else {
                botTurn();
            }
            view.printCardSize(game.getPlayerList());
        }
        LOGGER.debug("Method gameProcedure() finished!");
    }

    /**
     * This method is needed for the bot for his turn and it contains the function of the botService.
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private void botTurn() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        LOGGER.debug("Method botTurn() started!");
            botService.botPlaying(game, (Bot) game.getActualPlayer());
            Bot bot = (Bot) game.getPlayerList().get(game.getLastGameMove().getPlayerIndex());

            if (game.getLastGameMove().getPlayedCard() == null) {
                LOGGER.debug("Bot draw card block started!");
                view.drawnCardMsg(bot.getName());
                LOGGER.debug("Bot draw card block finished!");
            } else {
                LOGGER.debug("Bot played card block started!");
                Card cOnDeck = game.getLastGameMove().getPlayedCard();
                view.playedCardMsg(bot.getName(), cOnDeck, game.getGameDirectionClockwise());
                view.nextPlayersTurnMsg(game, bot);
                LOGGER.debug("Bot played card block finished!");
            }
            if(game.getCompulsoryColor() != null) {
                LOGGER.debug("Bot chose color block started!");
                view.newColorMsg(game.getCompulsoryColor());
                LOGGER.debug("Bot chose color block finished!");
            }
        game = daoService.update(game);
        LOGGER.debug("Method botTurn() finished!");
    }

    /**
     * This method is needed for the turn of a human.
     * First it checks, if the last game move was a SEVEN, which is played by a player.
     * If it is true, then it checks, if the player can play a SEVEN to get not punished.
     * Otherwise, it asks for the turn, so if he wants to play, draw, say mau or to surrender.
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private void humanTurn() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        LOGGER.debug("Method humanTurn() started!");
        boolean exit;
        do {
            try {
                if(game.getLastGameMove().getPlayedCard().getCardValue() == CardValue.SIEBEN) {
                    exit = checkIfToPlay7OrPunish();
                } else {
                    exit = humanTurnChoices();
                }
            } catch (NullPointerException e) {
                /*
                 * Catch-block is needed, because of the game start and there is the LastGameMove = null, so we just
                 * implement a NullPointerException.
                 */
                exit = humanTurnChoices();
            }
            }while(!exit);
        game = daoService.update(game);
        LOGGER.debug("Method humanTurn() finished!");
        }

    /**
     * The method checks if the player can play a SEVEN or if he must be punished by the 7'(s)
     * @return true or false, if he can play or not.
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private boolean checkIfToPlay7OrPunish() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        LOGGER.debug("Intern method checkIfToPlay7OrPunish() started!");
        boolean exit = false;
        /*
         * Here you can see, if the player have a SEVEN or NOT in his cards.
         */
        if(mauMauService.containsSpecificCard(game.getActualPlayer().getPlayerCards())) {
            cardService.sortCards(game.getActualPlayer().getPlayerCards());
            view.printCards(game, game.getActualPlayer());
            view.InListIs7Msg();
            int inputPlay = view.userInput(1, 1);
            if (inputPlay==1) {
                exit = playedCardTurn();
            }
            /*
             * Otherwise, he must be punished.
             */
        } else {
            cardService.sortCards(game.getActualPlayer().getPlayerCards());
            view.printCards(game, game.getActualPlayer());
            view.mustDrawCardMsg();
            int sizeBeforePunish = game.getActualPlayer().getPlayerCards().size();
            mauMauService.drawCard(game, game.getActualPlayer());
            int sizeAfterPunish = game.getPlayerList().get(game.getLastGameMove().getPlayerIndex()).getPlayerCards().size();
            Player lastP = game.getPlayerList().get(game.getLastGameMove().getPlayerIndex());
            if(game.getDeck().getDiscoveredCard().getCardValue() == CardValue.SIEBEN) {
                view.punishedMsg(game.getPlayerList().get(game.getLastGameMove().getPlayerIndex()).getName(), sizeAfterPunish-sizeBeforePunish);
            }
            view.nextPlayersTurnMsg(game, lastP);
            exit = true;
        }
        LOGGER.debug("Intern method checkIfToPlay7OrPunish() finished!");
        return exit;

    }


    /**
     * The method shows all normal choices for a human turn.
     * 1) is to play a card
     * 2) is to draw a card
     * 3) say mau if card size is <= 2
     * and the last one is to surrender.
     * @return true or false, if everything was correct or not
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private boolean humanTurnChoices() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        LOGGER.debug("Intern method humanTurnChoices() started!");
        boolean exit = false;
        cardService.sortCards(game.getActualPlayer().getPlayerCards());
        view.printCards(game, game.getActualPlayer());
        view.askForTurn();
        char turnInput = view.turnInput();

        if(turnInput == 'p') {
            LOGGER.debug("Human play card block started!");
            exit = playedCardTurn();
            LOGGER.debug("Human play card block finished!");
        } else if (turnInput == 'd'){
            LOGGER.debug("Human draw card block started!");
            mauMauService.drawCard(game, game.getActualPlayer());
            Player lastP = game.getPlayerList().get(game.getLastGameMove().getPlayerIndex());
            view.drawnCardMsg(lastP.getName());
            view.nextPlayersTurnMsg(game, lastP);
            exit = true;
            LOGGER.debug("Human draw card block finished!");
        } else if (turnInput == 'm'){
            LOGGER.debug("Human say 'mau' block started!");
            if (game.getActualPlayer().getPlayerCards().size() > 2) {
                view.cannotSayMauMsg();
            } else {
                game.sayMau(true);
                exit = playedCardTurn();
            }
            LOGGER.debug("Human say 'mau' block finished!");
        } else if(turnInput == 's') {
            LOGGER.debug("Human surrender block started!");
                Player lastP = game.getActualPlayer();
                mauMauService.endGame(game, game.getActualPlayer());
                view.surrenderedPlayerMsg(lastP.getName());
                exit = true;
            LOGGER.debug("Human surrender block finished!");
        }
        LOGGER.debug("Intern method humanTurnChoices() finished!");
        return exit;
    }

    /**
     * The method shows the playedCardTurn and returns if it was correct played or not.
     * Also it checks if the player played a JACK, so he must choose a color and if the player must be punished of not saying MAU to show the message.
     * @return true or false of correct played card turn
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private boolean playedCardTurn() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        int penalty = mauMauService.checkPunishPlayerByNotSayMau(game);
        boolean correctPlayed = correctTurn();
        if(correctPlayed) {
            Card cOnDeck = game.getDeck().getDiscoveredCard();
            Player lastP = game.getPlayerList().get(game.getLastGameMove().getPlayerIndex());
            if (cOnDeck.getCardValue() == CardValue.BUBE) {
                chooseColorInGame();
            } else {
                view.playedCardMsg(lastP.getName(), cOnDeck, game.getGameDirectionClockwise());
                if(penalty > 0 ) {
                    view.punishedMsg(game.getPlayerList().get(game.getLastGameMove().getPlayerIndex()).getName(), 2);
                }
                view.nextPlayersTurnMsg(game, lastP);
            }
        }
        return correctPlayed;
    }

    /**
     * The method is for the players turn to choose a compulsory color after playing a JACK.
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private void chooseColorInGame() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(game.isNeedCompulsoryColor()) {
            view.chooseColorMsg();
            Color color = chosenColor();
            mauMauService.chooseColor(game, game.getActualPlayer(), color);
            view.newColorMsg(color);
        }
    }

    /**
     * The method requests the player to play a card after he choose this choice.
     * @return - returns true if the card was correct
     * @throws PlayerNotAllowedActivityException - thrown Exception
     * @throws MauMauTechnicalException - thrown Exception
     */
    private boolean correctTurn() throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        boolean exit;
        view.playACardMsg();
        try {
            mauMauService.playCard(game, game.getActualPlayer(), chooseCard());
            exit = true;
        } catch (CardNotAllowedActivityException e) {
            view.tryItAgainMsg(e.getMessage());
            exit = false;
        }
        return exit;
    }


    /**
     * The method lets the player to choose the wanted card to play.
     * @return - the chosen card of the player
     */
    private Card chooseCard() {
        List<Card> playerCards = game.getActualPlayer().getPlayerCards();
        int input = view.userInput(1, playerCards.size());
        return playerCards.get(input-1);
    }



    /**
     * Method for returning the chosen color in {@link #chooseColorInGame()}.
     * @return - the chosen color
     */
    private Color chosenColor() {
        int input = view.userInput(1, 4);
        Color c = game.getLastGameMove().getPlayedCard().getColor();
        switch (input) {
            case 1: return Color.HERZ;
            case 2: return Color.KARO;
            case 3: return Color.PIK;
            case 4: return Color.KREUZ;
        }
        return c;
    }

    /**
     * The method asks the player, if he wants to see the rules or not.
     */
    private void rulesInput() {
        view.askForShowingRules();
        int input = view.userInput(1, 2);
            switch (input) {
                case 1: view.showGameRules();
                    break;
                case 2:
                    break;
                default:
                    view.showIncorrectInputMsg();
            }
    }

    /**
     * The method configures the game at the start. Configuring means in that case, how many player, bot player, cards per player etc.
     * @throws EmptyStackException - thrown Exception
     * @throws MauMauPlayerSizeException - thrown Exception
     */
    private void configureGame() throws EmptyStackException, MauMauPlayerSizeException {
        LOGGER.debug("Method configureGame() started!");
        int maximalPlayerAmount;
        int botPlayerAmount = 0;
        int humanPlayerAmount;
        int cardsPerPlayer;
        boolean exit;

        do {
            view.askForMaxPlayerAmount();
            maximalPlayerAmount = view.userInput(2, 5);

            view.askForHumanPlayers();
            humanPlayerAmount = view.userInput(1, 5);

            if (!(humanPlayerAmount == maximalPlayerAmount)) {
                view.askForBotPlayers(maximalPlayerAmount, humanPlayerAmount);
                botPlayerAmount = view.userInput(0, maximalPlayerAmount-humanPlayerAmount);
            }

            view.askForCardsPerPlayer();
            cardsPerPlayer = view.userInput(4, 6);
            if (humanPlayerAmount == 1 && botPlayerAmount == 0) {
                view.toLowPlayerAmount();
                exit=false;
            } else {
                exit = true;
            }
        } while (!exit);


        game = mauMauService.createGame(maximalPlayerAmount, humanPlayerAmount, botPlayerAmount, cardsPerPlayer, true);
        view.createdGameMsg(maximalPlayerAmount, humanPlayerAmount, botPlayerAmount,cardsPerPlayer, true);
        LOGGER.debug("Method configureGame() finished!");
    }

}
