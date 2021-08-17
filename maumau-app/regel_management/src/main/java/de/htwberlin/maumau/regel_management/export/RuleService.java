package de.htwberlin.maumau.regel_management.export;

import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.player_management.export.Player;

import java.util.List;

/**
 * The interface comprises the rules for a game.
 * @author Süleyman Demir
 */
public interface RuleService {

     /**
      * Checks whether a player is the current player or not
      * @param lastPlayer current player
      * @param player the examining player
      * @return whether it's the player's turn
      */
     boolean isPlayersTurn(Player lastPlayer, Player player);

     /**
      * checks whether a card is playable, i.e. whether it is permitted
      * @param deck deck of cards
      * @param nextCard next played card
      * @param compulsoryColor the possibly desired color
      * @param lastMove the last game move
      * @return whether the card is valid
      */
     boolean isCardPlayable(Deck deck, Card nextCard, Color compulsoryColor, GameMove lastMove);

     /**
      * Checks whether a player has to announce "MAU"
      * @param player the current player
      * @return if he must announce mau
      */
     boolean mustCallMau(Player player);

     /**
      * Returns the number of penalty cards, if he does/does not announced "mau".
      * @param mustSayMau - the mau obligation
      * @param mauSaid - if player said mau
      * @return - the penalty card amount
      */
     int getMauCallPenalty(boolean mustSayMau, boolean mauSaid);

     /**
      * Checks whether the player who played a card, has to choose a color.
      * @param card - the certain card
      * @return - must the player choose a color (True/false)
      */
     boolean mustChooseColor(Card card);

     /**
      * The method returns direction of the game.
      * @param card - if certain card is played, to change the direction
      * @param gameDirection - true (clockwise) / false (counterclockwise)
      * @return gameDirection - true (clockwise) / false (counterclockwise)
      */
     boolean getGameDirection(Card card, boolean gameDirection);

     /**
      * checks whether a player has to be changed or not with certain card.
      * @param card - if ASS, then the player has not to be changed
      * @return true or false for player change
      */
     boolean checkPlayerChange(Card card);

     /**
      * The method returns the player index of the next player.
      * @param gameDirection true (clockwise) / false (counterclockwise)
      * @param playerAmount - the amount of player as int
      * @param lastPlayer - the index of the last player
      * @return the index of the next player
      */
     int getNextPlayerIndex(boolean gameDirection, int playerAmount, int lastPlayer);

     /**
      * Returns the number of penalty cards, when one or more 7 cards are played in row.
      * @param gameMoves - the list of the game moves
      * @return amount of the penalty cards
      */
     int getPenaltyCardsAmountBy7(List<GameMove> gameMoves);

     /**
      * Determines the eventual game winner
      * @param playerList - a list of the players
      * @return - winner of the game or null if there not a winner right now.
      */
     Player getWinner(List<Player> playerList);
}
