package de.htwberlin.maumau.ui.impl;

import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.CardService;
import de.htwberlin.maumau.card_management.export.CardValue;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.maumau_management.export.bot.Bot;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.player_management.export.Player;

import java.util.List;
import java.util.Scanner;

/**
 * The class is for separating the outputs and inputs in game.
 */
public class MauMauView {
    Scanner scanner = new Scanner(System.in);

    /**
     * Welcome Message Method
     */
    public void welcomeMsg() {
        System.out.println("==============================" +
        "\n\t  MAU-MAU CARD GAME\n"+"==============================");
    }

    /**
     * Message to asking for the rules
     */
    public void askForShowingRules() {
        System.out.println("Do you want to see the game rules?\n" +
                "(1) Yes | (2) No");
    }

    /**
     * Message for asking for the amount of maximal player
     */
    public void askForMaxPlayerAmount() {
        System.out.println("==============================\nHow many maximal players will play? (From 2 - 5)");
    }

    /**
     * Message for asking for the amount of human player
     */
    public void askForHumanPlayers() {
        System.out.println("==============================\nHow many human players will play? (From 1 - 5)");
    }

    /**
     * Message for asking for the amount of bot player
     *
     * @param max The max value is the maximal amount of player
     * @param value The value is the amount of human player
     */
    public void askForBotPlayers(int max, int value) {
        System.out.println("==============================\nHow many bots will play? (0-" + (max-value) + ")");
    }

    /**
     * Message for asking how many cards per player.
     */
    public void askForCardsPerPlayer() {
        System.out.println("==============================\nHow many cards per player? (From 4 - 6)");
    }

    /**
     * Message for asking for the name of human player
     * @param index - index of the player
     */
    public void askForName(int index) {
        System.out.println("\nWhat is your name, player " + index + "?");
    }

    /**
     * Message to print all player in game at the start.
     * @param playerList - all player in game
     */
    public void printPlayer(List<Player> playerList) {
        for (Player p:
                playerList) {
            int index = p.getPlayerIndex() + 1;
            if(p instanceof Bot) {
                System.out.println("Bot: " + index + " is " + p.getName());
            } else {
                System.out.println("Player: " + index + " is " + p.getName());
            }

        }
    }

    /**
     * The method shows a message for the played card of the player.
     * If there was played a NINE then it prints also the change of the game direction
     * @param name - name of the player
     * @param c - played card
     * @param gameDirection - the game direction
     */
    public void playedCardMsg(String name, Card c, boolean gameDirection) {
        System.out.println(name + " played a " + c +"!");
        if(c.getCardValue() == CardValue.NEUN) {
            String gameDirectionStr = gameDirection ? "clockwise" : "counter-clockwise";
            System.out.println("The game direction is changed. It is now: " +  gameDirectionStr + ".");
        }
    }

    /**
     * The exception message for trying it again.
     * @param msg - Exception msg
     */
    public void tryItAgainMsg(String msg) {
        System.err.println(msg + "\nTry it again.");
    }

    /**
     * Message for printing the draw turn of a player
     * @param name -  name of the player
     */
    public void drawnCardMsg(String name) {
        System.out.println(name + " draw a card!");
    }

    /**
     * The method shows a message that a player has been punished.
     * @param name - name of the punished player
     * @param count - the amount of cards
     */
    public void punishedMsg(String name, int count) {
        System.out.println( name + " is punished by " + count + " cards.");
    }

    /**
     * Method to signal the player, that he can play a SEVEN and he does not must be punished.
     */
    public void InListIs7Msg() {
        System.out.println("You have a playable card! Play it.\n(1) Play");
    }

    /**
     * Method that says to the player, that he cannot play a SEVEN and that he just being punished by the 7's
     */
    public void mustDrawCardMsg() {
        System.out.println("\nIt seems, that you do not have a card to play. You must be punished by the 7's..");
    }

    /**
     * Method that shows the next players turn.
     * @param game - the actual game
     * @param lastPlayer - the last player
     */
    public void nextPlayersTurnMsg(MauMau game, Player lastPlayer) {
        if(lastPlayer.getPlayerCards().size() == 0) {
            return;//noinspection UnnecessaryReturnStatement
        } else if (game.getLastGameMove().getPlayedCard() == null) {
            System.out.println("\nThe next player is: " + game.getActualPlayer().getName());
        }
        else if (game.getLastGameMove().getPlayedCard().getCardValue() == CardValue.ASS) {
            System.out.println("It is still your turn!");
        }
        else {
            System.out.println("\nThe next player is: " + game.getActualPlayer().getName());
        }
    }

    /**
     * Method to print the cards of the actual player
     * @param game - actual game
     * @param p - actual player
     */
    public void printCards(MauMau game, Player p) {
        int cardIndex = 1;
        System.out.println("\nWhich card do you want to play, " + p.getName() + "?");
        lastCardOnDeck(game.getDeck().getDiscoveredCard(), game.getCompulsoryColor());
        for (Card c:
             p.getPlayerCards()) {
            System.out.println("("+ cardIndex + ") - " + c.toString());
            cardIndex++;
        }
        System.out.println("Card amount: " + p.getPlayerCards().size());
    }

    /**
     * Method to ask the player which choice he wants to do.
     */
    public void askForTurn() {
        System.out.println("\nDo you want to play a card or draw a card?\n(p) Play | 'd' Draw | 'm' Say Mau | 's' Surrender");
    }

    /**
     * Message that shows the name of the player, who surrendered.
     * @param name -  the player, who surrendered the game.
     */
    public void surrenderedPlayerMsg(String name) {
        System.out.println("The player " + name + " has surrendered.");
    }

    /**
     * Message for the player to play a card.
     */
    public void playACardMsg(){
        System.out.println("Okay, play a card!");
    }

    /**
     * Method to choose a color, if the player played a JACK
     */
    public void chooseColorMsg() {
        System.out.println("Oh, you played a BUBE\nWhich color do you want?\n(1) HERZ | (2) KARO | (3) PIK | (4) KREUZ");
    }

    /**
     * Message for all player for a new compulsory color
     * @param c - new compulsory color
     */
    public void newColorMsg(Color c) {
        System.out.println("The new color is now: " + c);
    }

    /**
     * Message that the player cannot say 'mau' right now, if he choose this choice.
     */
    public void cannotSayMauMsg() {
        System.err.println("You cannot say 'mau' right now.");
    }

    /**
     * Message to print the card sizes of all player after each turn.
     * @param playerList - all players in game
     */
    public void printCardSize(List<Player> playerList) {
        System.out.println();
        for (Player player : playerList) {
            System.out.println(player.getName() + " has " + player.getPlayerCards().size() + " cards.");
        }
    }

    /**
     * Message of all configuration infos of a new created game.
     * @param playerAmount -  maximal player amount
     * @param humanPlayerAmount - human player amount
     * @param botPlayerAmount - bot player amount
     * @param cardsPerPlayer - cards each player
     * @param gameDirection - the game direction
     */
    public void createdGameMsg(int playerAmount, int humanPlayerAmount,int botPlayerAmount, int cardsPerPlayer, boolean gameDirection) {
        String gameDirectionTxt = gameDirection ? "Clockwise" : "Counter-Clockwise";
        System.out.println("\n==============================\nYou have successfully created the game." +
                "\n\nThe maximal player amount is: " + playerAmount +
                "\nEach player has: " + cardsPerPlayer + " cards." +
                "\nThere are: " + humanPlayerAmount + " human player." +
                "\nThere are: " + botPlayerAmount + " bot player." +
                "\nThe game direction starts at beginning: " + gameDirectionTxt);
    }

    /**
     * Method to print the last card on deck.
     * @param cOnDeck - the card on deck
     * @param co - the compulsory color, if there is one.
     */
    public void lastCardOnDeck(Card cOnDeck, Color co) {
        System.out.println("The card on deck is: " + cOnDeck);
        if(co != null) {
            System.out.println("The compulsory color is: " + co);
        }
    }

    /**
     * Method to print the game rules.
     */
    public void showGameRules() {
        System.out.println("\t\t\tRULES\n" +
                "The player who was able to play all cards first wins the game." +
                "\nBut before the player plays his last card, he must announce \"Mau\"!" +
                "\nIf not, he will get punished by drawing two cards." +
                "\nAt the beginning each player receives the same number of cards." +
                "\nA card can only be played if the card color suit or the value " +
                "matches the discovered card." +
                "\nIf the player cannot or does not want to play a card, he must draw one from the stack" +
                "\n==============================\nSpecific rules:" +
                "\nIf the discovered card is valued at 7, the next player must draw two cards." +
                "\nHowever, if the player can play a 7 himself, the next player must draw 4 cards, etc." +
                "\nIf the discovered card is valued at ACE, then the actual player can play again." +
                "\nIf the discovered card is valued at JACK, then the actual player can choose a COLOR.");
    }

    /**
     * Method to warn the player for his input.
     */
    public void showIncorrectInputMsg() {
        System.err.print("Please check your input.");
    }

    /**
     * Method to print the winner
     * @param name - name of the winner
     */
    public void printWinnerMsg(String name) {
        System.out.println(name + " won the game!");
    }

    public void toLowPlayerAmount() {
        System.err.println("There is only one player. The game needs at least two active players.");
    }

    public void askForGameType(){
        System.out.println("\nDo you want to start a new game or load a game?\n(1) New game | (2) Load a game");
    }

    public void printAllGames(List<MauMau> allGames) {
        for (MauMau game:
             allGames) {
            if(game.getWinner() != null) {
                System.out.println("Game ID: (" + game.getId() + ") Date: " + game.getDate() + " Game finished: Yes");
            } else {
                System.out.println("Game ID: (" + game.getId() + ") Date: " + game.getDate() + " Game finished: No");
            }

        }
        System.out.println("Which game do you want to load?\nType the ID");
    }

    public void mustCreateNewGameMsg() {
        System.out.println("Oh, it seem's, that there is not any game to load!");
    }

    /**
     * Method for returning the integers in humanTurnChoices}.
     * @return - the chosen integer
     */
    public char turnInput() {
        boolean exit;
        char c;
        do {
                c = scanner.next().charAt(0);
                switch(c) {
                    case 'p':
                    case 'd':
                    case 'm':
                    case 's':
                        exit = true;
                        break;
                    default:
                        exit = false;
                        showIncorrectInputMsg();
                        break;
                }
        }while(!exit);
        return c;
    }

    /**
     * Overall method for inputs of human player
     * @param min - min value of input
     * @param max - max value of input
     * @return chosen integer
     */
    public int userInput(int min, int max) {
        int inputValue = 0;
        boolean exit;
        do {
            try {
                inputValue = Integer.parseInt(scanner.next());
                if(inputValue < min || inputValue > max) {
                    showIncorrectInputMsg();
                    exit = false;
                } else {
                    exit = true;
                }
            } catch (NumberFormatException e) {
                showIncorrectInputMsg();
                exit = false;
            }
        }while(!exit);
        return inputValue;
    }

    /**
     * The method asks for the names of all human players.
     */
    public void setHumanPlayerName(MauMau game) {
        for (Player p:
                game.getPlayerList()) {
            if(!p.isBot()) {
                askForName(p.getPlayerIndex() + 1);
                String name = scanner.next();
                p.setName(name);
            }
        }
        printPlayer(game.getPlayerList());
    }

    public void winnerExistsAlready() {
        System.out.println("Oh sorry, a winner exists already! Choose another game.");
    }
}
