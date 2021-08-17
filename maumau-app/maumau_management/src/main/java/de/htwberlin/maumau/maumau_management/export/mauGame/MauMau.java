package de.htwberlin.maumau.maumau_management.export.mauGame;

import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.card_management.export.Stack;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.regel_management.export.Deck;
import de.htwberlin.maumau.regel_management.export.GameMove;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * MauMau.java<br>
 * The class describes a maumau game.
 * @author Süleyman Demir
 */

@Entity
@Table(name="MauMau")
public class MauMau implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "gameId")
    private Long id;


    private Color compulsoryColor;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Deck deck;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Stack stack;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Player> playerList;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Player actualPlayer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Player winner;

    private boolean mustSayMau;
    private int maxPlayerCount;
    private int cardsPerPlayer;
    private boolean gameDirectionClockwise;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<Integer, Boolean> mauPlayer;

    private boolean needCompulsoryColor;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GameMove> gameMoves;

    private Timestamp date;

    /**
     * Constructor of MauMau
     * @param maxPlayerCount - int maximal player count
     * @param cardsPerPlayer - count how many cards per player
     * @param stack - the stack of maumau game
     * @param deck - the deck of maumau game
     */
    public MauMau(int maxPlayerCount, int cardsPerPlayer, Stack stack, Deck deck) {
        this.maxPlayerCount = maxPlayerCount;
        this.cardsPerPlayer = cardsPerPlayer;
        this.deck = deck;
        this.stack = stack;
        this.playerList = new LinkedList<>();
        this.gameMoves = new ArrayList<>();
        this.mauPlayer = new HashMap<>();
        for (int i = 0; i < this.getMaxPlayerCount(); i++){
            this.mauPlayer.put(i, false);
        }
    }

    /**
     * The method resets the game.
     */
    public void resetGame() {
        this.getDeck().deckCardsToStack(this.getStack());
        this.getGameMoves().clear();
        this.setGameDirectionClockwise(true);
    }

    /**
     * The method returns if there a compulsory color needed.
     * @return - true or false
     */
    public boolean isNeedCompulsoryColor() {
        return needCompulsoryColor;
    }

    /**
     * The method sets the status, if a compulsory is needed.
     * @param needCompulsoryColor - boolean true or false
     */
    public void setNeedCompulsoryColor(boolean needCompulsoryColor) {
        this.needCompulsoryColor = needCompulsoryColor;
    }

    /**
     * The method protocols, if the actual player said mau (true or false)
     * @param mau - boolean, if said
     */

    public void sayMau(boolean mau) {
        this.mauPlayer.put(this.getActualPlayer().getPlayerIndex(), mau);
    }

    /**
     * The method returns a Map of all player in game, with the status if mau is said.
     * @return map of all player in game with "saidMau"
     */
    public Map<Integer, Boolean> getMauPlayer() {
        return mauPlayer;
    }

    /**
     * The method returns the last game move of a player.
     * @return Last game move
     */
    public GameMove getLastGameMove() {
        return this.gameMoves.isEmpty() ? null : this.gameMoves.get(this.gameMoves.size()-1);
    }

    /**
     * The method adds a game move to the list.
     * @param gameMove - GameMove (card played or drawn)
     */
    public void addGameMove(GameMove gameMove) {
        this.gameMoves.add(gameMove);
    }

    /**
     * The method returns the compulsory color of MauMau game.
     * @return - Enum Color
     */
    public Color getCompulsoryColor() {
        return compulsoryColor;
    }

    /**
     * The method sets the compulsory color of the MauMau game.
     * @param compulsoryColor - Enum Color
     */
    public void setCompulsoryColor(Color compulsoryColor) {
        this.compulsoryColor = compulsoryColor;
    }

    /**
     * The method returns the deck of MauMau game.
     * @return - Deck deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * The method returns the stack of MauMau game.
     * @return Stack stack
     */
    public Stack getStack() {
        return stack;
    }

    /**
     * The method returns a list of all players in MauMau game.
     * @return list of players in game.
     */
    public List<Player> getPlayerList() {
        return playerList;
    }


    /**
     * The method returns the actual player in MauMau game.
     * @return Player actual player of game.
     */
    public Player getActualPlayer() {
        return actualPlayer;
    }


    /**
     * The method sets the actual player in MauMau game.
     * @param actualPlayer - Player actual player in game.
    */
    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }


    /**
     * The method returns the winner in MauMau game.
     * @return Player winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * The method sets the winner of the game.
     * @param winner - Player winner
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * The method returns the maximal player count of the game.
     * @return int maximal player count
     */
    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    /**
     * The method returns if the game direction is clockwise.
     * @return boolean true or false
     */
    public boolean getGameDirectionClockwise() {
        return gameDirectionClockwise;
    }

    /**
     * The method sets the game direction in game.
     * @param gameDirectionClockwise - boolean game direction
     */
    public void setGameDirectionClockwise(boolean gameDirectionClockwise) {
        this.gameDirectionClockwise = gameDirectionClockwise;
    }

    /**
     * The method returns a list of the game moves in maumau.
     * @return - a list of all game moves in game.
     */
    public List<GameMove> getGameMoves() {
        return gameMoves;
    }

    public int getCardsPerPlayer() {
        return cardsPerPlayer;
    }


    public void setGameMoves(List<GameMove> gameMoves) {
        this.gameMoves = gameMoves;
    }

    public boolean isMustSayMau() {
        return mustSayMau;
    }

    public void setMustSayMau(boolean mustSayMau) {
        this.mustSayMau = mustSayMau;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
