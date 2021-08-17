package de.htwberlin.maumau.maumau_management.export.mauGame;


import de.htwberlin.maumau.card_management.export.CardNotAllowedActivityException;
import de.htwberlin.maumau.card_management.export.EmptyStackException;
import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.card_management.export.Stack;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.regel_management.export.Deck;

import java.util.List;

/**
 * The interface is the service for the class MauMau.
 * @author Süleyman Demir, Sercan Sanli
 */
public interface MauMauService {

     /**
      * Creates a mau mau game with the specified configuration
      * @param playerAmount amount of players
      * @param cardAmountPerPlayer cards of players
      * @param gameDirection direction of game(counterclockwise/clockwise)
      * @return mau mau game
      * @throws MauMauPlayerSizeException - thrown Exception
      */
     MauMau createGame(int playerAmount, int humanPlayerAmount,int botPlayerAmount,int cardAmountPerPlayer, boolean gameDirection) throws MauMauPlayerSizeException, EmptyStackException;

     /**
      * Certain number of virtual bots will be added to the game.
      * @param maumau the specified game
      * @param numberOfBotPlayer how many should be added
      * @throws MauMauPlayerSizeException - thrown Exception
      */
     void addBotPlayer(MauMau maumau, int numberOfBotPlayer) throws MauMauPlayerSizeException;
     
     /**
      * New player added to a mau mau game
      * @param maumau game player is added to maumau game
      * @param player to be added player
      * @throws MauMauPlayerSizeException - thrown Exception
      */
     void addPlayerToGame(MauMau maumau, Player player) throws MauMauPlayerSizeException;

     /**
      * Checks if turn is possible and allowed
      * @param maumau the game
      * @param player player
      * @return true or false if is allowed
      * @throws PlayerNotAllowedActivityException - thrown Exception
      * @throws MauMauTechnicalException - thrown Exception
      */
     boolean isTurnPossible(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

     /**
      * Surrender the game of specific player
      * @param maumau game in which the player surrenders
      * @param player surrendering player
      */
     void endGame(MauMau maumau, Player player);

     /**
      * Player calls Mau in the game
      * @param maumau the game
      * @param player the player who says mau
      * @throws PlayerNotAllowedActivityException - thrown Exception
      * @throws MauMauTechnicalException - thrown Exception
      */
     void sayMau(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

     /**
      * Player wants to play a card in maumau-game.
      * @param maumau the game
      * @param player player who plays card
      * @param card card which is put on table
      * @throws CardNotAllowedActivityException - thrown Exception
      * @throws PlayerNotAllowedActivityException - thrown Exception
      * @throws MauMauTechnicalException - thrown Exception
      */
     void playCard(MauMau maumau, Player player, Card card) throws CardNotAllowedActivityException, PlayerNotAllowedActivityException, MauMauTechnicalException;

     /**
      * Player draws card from stack in maumau game.
      * @param maumau the game
      * @param player player who gets the card
      * @throws PlayerNotAllowedActivityException - thrown Exception
      * @throws MauMauTechnicalException - thrown Exception
      */
     void drawCard(MauMau maumau, Player player) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

     int checkPunishPlayer(MauMau game, Player player);

     int checkPunishPlayerByNotSayMau(MauMau game);

     /**
      * A player sets a compulsory color for the next move in game.
      * @param maumau the game
      * @param player player who chooses the color
      * @param wishedColor the chosen color
      * @throws PlayerNotAllowedActivityException - thrown Exception
      * @throws MauMauTechnicalException - thrown Exception
      */
     void chooseColor(MauMau maumau, Player player, Color wishedColor) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

     /**
      * transfers the first card from the stack to the deck for let game begin.
      * @param maumau the game
      */
     void firstCardFromStackToDeck(MauMau maumau);

     /**
      * player gets a specified number of penalty cards
      * @param maumau the game
      * @param player player who gets punishment
      */
     void punishPlayer(MauMau maumau, Player player);

     /**
      * transfers the deck cards to the stack, if the stack is empty and player cannot more draw cards from there during the game.
      * @param stack stack with cards
      * @param deck deck which the stack is transferred to
      */
     void deckCardsToStack(Stack stack, Deck deck);

     /**
      * returns the winner of the maumau-game
      * @param maumau - actual game
      * @return the winner of the game
      */
     Player getWinner(MauMau maumau);

     /**
      * The method checks if a list of card contains a SEVEN.
      * @param list - playerList
      * @return true or false, whether it contains a SEVEN or NOT.
      */
     boolean containsSpecificCard(List<Card> list);

     void setGameDate(MauMau game);
}
