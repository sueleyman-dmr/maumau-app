package de.htwberlin.maumau.maumau_management.impl.mauGame;

import de.htwberlin.maumau.card_management.export.*;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.bot.Bot;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMauService;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.player_management.export.PlayerService;
import de.htwberlin.maumau.regel_management.export.Deck;
import de.htwberlin.maumau.regel_management.export.GameMove;
import de.htwberlin.maumau.regel_management.export.RuleService;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MauMauServiceImpl implements MauMauService {

    private RuleService ruleService;
    private CardService cardService;
    private PlayerService playerService;
    private String[] botNames = {"Laura-Bot", "Clara-Bot", "Kris-Bot", "Lisa-Bot", "Hans-Bot"};

    public MauMauServiceImpl(RuleService ruleService, CardService cardService, PlayerService playerService) {
        this.ruleService=ruleService;
        this.cardService=cardService;
        this.playerService=playerService;
    }

    @Override
    public MauMau createGame(int maxPlayerAmount, int humanPlayerAmount,int botPlayerAmount,int cardAmountPerPlayer, boolean gameDirection) throws MauMauPlayerSizeException, EmptyStackException {
        List<Player> playerList = playerService.createPlayer(humanPlayerAmount);
        Stack stack = new Stack(cardService.createCards());
        cardService.mixCards(stack.getStackCards());
        Deck deck = new Deck();
        MauMau maumau = new MauMau(maxPlayerAmount, cardAmountPerPlayer, stack, deck);

        for (Player p: playerList) {
            addPlayerToGame(maumau, p);
        }
        addBotPlayer(maumau, botPlayerAmount);

        maumau.setGameDirectionClockwise(gameDirection);
        playerService.distributeCards(stack, maumau.getPlayerList(), cardAmountPerPlayer);
        //erste Spieler beginnt.
        maumau.setActualPlayer(maumau.getPlayerList().get(0));
        return maumau;
    }

    @Override
    public void addBotPlayer(MauMau maumau, int numberOfBotPlayer) throws MauMauPlayerSizeException {
        for (int i = 1; i <= numberOfBotPlayer; i++) {
            Bot bot = new Bot(randomName());
            int lastIndexInPlayerList = maumau.getPlayerList().size()-1;
            lastIndexInPlayerList++;
            bot.setPlayerIndex(lastIndexInPlayerList);
            bot.setBot(true);
            addPlayerToGame(maumau, bot);
        }
    }

    /**
     * Random name for the bot, if created
     * @return - random chosen name
     */
    private String randomName() {
        int randIndex = new Random().nextInt(botNames.length-1);
        String chosenName = botNames[randIndex];
        botNames = ArrayUtils.removeElement(botNames, botNames[randIndex]);
        return chosenName;
    }

    @Override
    public void addPlayerToGame(MauMau maumau, Player player) throws MauMauPlayerSizeException {
        if(maumau.getPlayerList().size() >= maumau.getMaxPlayerCount()) {
            throw new MauMauPlayerSizeException("To much player.");
        } else {
            maumau.getPlayerList().add(player);
        }
    }

    @Override
    public boolean isTurnPossible(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(maumau == null || player == null) {
            throw new MauMauTechnicalException("Arguments are null. Please check the game and player.");
        }
        if(!ruleService.isPlayersTurn(maumau.getActualPlayer(), player)) {
            throw new PlayerNotAllowedActivityException("It is not your turn.");
        }
        return true;
    }

    @Override
    public void endGame(MauMau maumau, Player player) {
        maumau.resetGame();
        playerService.playerCardsToStack(maumau.getStack(), player);
        int lastPlayerIndex = playerService.getPlayerIndexInList(maumau.getPlayerList(), player);
        int nextPlayerIndex = ruleService.getNextPlayerIndex(maumau.getGameDirectionClockwise(), maumau.getPlayerList().size()-1, lastPlayerIndex);
        maumau.setActualPlayer(maumau.getPlayerList().get(nextPlayerIndex));
        maumau.getPlayerList().remove(player);
        playerService.adjustPlayerIndex(maumau.getPlayerList());
    }


    @Override
    public void sayMau(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(isTurnPossible(maumau, player)) {
            if(!ruleService.mustCallMau(player)) {
                throw new PlayerNotAllowedActivityException("You cannot say \"mau\" right now.");
            }
            maumau.sayMau(true);
        }
    }

    @Override
    public void playCard(MauMau maumau, Player player, Card card) throws CardNotAllowedActivityException, PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(this.isTurnPossible(maumau, player)) {
            int playerIndex = player.getPlayerIndex();
            boolean mustSayMau = ruleService.mustCallMau(player);
            boolean saidMau = maumau.getMauPlayer().get(playerIndex);
            boolean cardPlayable = ruleService.isCardPlayable(maumau.getDeck(), card, maumau.getCompulsoryColor(), maumau.getLastGameMove());
            if(!cardPlayable) {
                throw new CardNotAllowedActivityException("You cannot play this card.");
            }
            this.playCard(maumau.getDeck(), card, player);
            maumau.addGameMove(new GameMove(playerIndex, card));
            maumau.sayMau(false);
            maumau.setNeedCompulsoryColor(ruleService.mustChooseColor(card));
            if(!maumau.isNeedCompulsoryColor()) {
                player.setNumberOfPenaltyCards(ruleService.getMauCallPenalty(mustSayMau, saidMau));
                punishPlayer(maumau, player);
                boolean gameDirectionClockwise = ruleService.getGameDirection(card, maumau.getGameDirectionClockwise());
                maumau.setGameDirectionClockwise(gameDirectionClockwise);
                boolean isNextPlayersTurn = ruleService.checkPlayerChange(card);
                if (isNextPlayersTurn) {
                    int lastPlayerIndex = playerService.getPlayerIndexInList(maumau.getPlayerList(), maumau.getActualPlayer());
                    int nextPlayerIndex = ruleService.getNextPlayerIndex(maumau.getGameDirectionClockwise(), maumau.getPlayerList().size(), lastPlayerIndex);
                    maumau.setActualPlayer(maumau.getPlayerList().get(nextPlayerIndex));
                }
            }
            maumau.setCompulsoryColor(null);
        }
    }

    private void playCard(Deck deck, Card card, Player player){
        if(player.isCardRemoved(card)) {
            deck.discoverCard(card);
            deck.setDiscoveredCard(card);
        }
    }

    /*
    Exception bearbeiten
     */
    @Override
    public void drawCard(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(isTurnPossible(maumau, player)) {
            int numberOfPenaltyCards = checkPunishPlayer(maumau, player);
            if (numberOfPenaltyCards > 0) {
                player.setNumberOfPenaltyCards(numberOfPenaltyCards);
                this.punishPlayer(maumau, player);
            } else {
                Card drawnCard = null;
                int drawnTries = 0;
                while (drawnCard == null && drawnTries < 2) {
                    try {
                        drawnTries++;
                        drawnCard = drawCard(maumau.getStack());
                        player.getPlayerCards().add(drawnCard);
                    }catch (EmptyStackException e) {
                        e.printStackTrace();
                        deckCardsToStack(maumau.getStack(), maumau.getDeck());
                    }
                }

            }
            cardService.sortCards(player.getPlayerCards());
            GameMove gameMove = new GameMove(player.getPlayerIndex(), null);
            maumau.addGameMove(gameMove); //Card gezogen, wird protokolliert
            int lastPlayerIndex = playerService.getPlayerIndexInList(maumau.getPlayerList(), maumau.getActualPlayer());
            int nextPlayerIndex = ruleService.getNextPlayerIndex(maumau.getGameDirectionClockwise(), maumau.getPlayerList().size(), lastPlayerIndex);
            maumau.setActualPlayer(maumau.getPlayerList().get(nextPlayerIndex));
        }
    }

    @Override
    public int checkPunishPlayer(MauMau game, Player player) {
        return ruleService.getPenaltyCardsAmountBy7(game.getGameMoves());
    }

    @Override
    public int checkPunishPlayerByNotSayMau(MauMau game) {
        boolean mustSayMau = ruleService.mustCallMau(game.getActualPlayer());
        boolean saidMau = game.getMauPlayer().get(game.getActualPlayer().getPlayerIndex());
        return ruleService.getMauCallPenalty(mustSayMau, saidMau);
    }

    private Card drawCard(Stack stack) throws EmptyStackException {
        return stack.drawCard();
    }

    @Override
    public void chooseColor(MauMau maumau, Player player, Color wishedColor) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(isTurnPossible(maumau, player)) {
            maumau.setCompulsoryColor(wishedColor);
            this.punishPlayer(maumau, player);
            cardService.sortCards(player.getPlayerCards());
            maumau.setNeedCompulsoryColor(false);
            int lastPlayerIndex = playerService.getPlayerIndexInList(maumau.getPlayerList(), maumau.getActualPlayer());
            int nextPlayerIndex = ruleService.getNextPlayerIndex(maumau.getGameDirectionClockwise(), maumau.getPlayerList().size(), lastPlayerIndex);
            maumau.setActualPlayer(maumau.getPlayerList().get(nextPlayerIndex));
        }
    }

    @Override
    public void firstCardFromStackToDeck(MauMau maumau) {
        Card firstCard = null;
        while (firstCard == null) {
            try {
                firstCard = maumau.getStack().drawCard();
                maumau.getDeck().discoverCard(firstCard);
            } catch (EmptyStackException e){
                e.printStackTrace();
                deckCardsToStack(maumau.getStack(), maumau.getDeck());
            }
        }
    }

    @Override
    public void punishPlayer(MauMau maumau, Player player) {
        for (int i = 0; i < player.getNumberOfPenaltyCards(); i++) {
            Card card = null;
            while (card == null) {
                try {
                    card = maumau.getStack().drawCard();
                    player.getPlayerCards().add(card);
                } catch (EmptyStackException e){
                    deckCardsToStack(maumau.getStack(), maumau.getDeck());
                }
            }
        }
        player.setNumberOfPenaltyCards(0);
    }

    @Override
    public void deckCardsToStack(Stack stack, Deck deck) {
        deck.deckCardsToStack(stack);
    }

    @Override
    public Player getWinner(MauMau maumau) {
        Player winner = ruleService.getWinner(maumau.getPlayerList());
        maumau.setWinner(winner);
        return maumau.getWinner();
    }

    @Override
    public boolean containsSpecificCard(List<Card> list){
        return list.stream().anyMatch(o -> o.getCardValue().equals(CardValue.SIEBEN));
    }

    @Override
    public void setGameDate(MauMau game) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        game.setDate(ts);
    }
}
