package de.htwberlin.maumau.player_management.impl;

import de.htwberlin.maumau.card_management.export.Stack;
import de.htwberlin.maumau.card_management.export.EmptyStackException;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.player_management.export.PlayerService;

import java.util.ArrayList;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    @Override
    public List<Player> createPlayer(int playerAmount){

        List<Player> playerList = new ArrayList<>();
        for (long i = 1L; i <= playerAmount; i++) {
            Player player = new Player("Player: " + i);
            player.setPlayerId(i);
            player.setPlayerIndex((int) i - 1);
            player.setBot(false);
            playerList.add(player);
        }
        return playerList;
    }

    @Override
    public int getPlayerIndexInList(List<Player> playerList, Player player) {
        return playerList.indexOf(player);
    }

    @Override
    public void distributeCards(Stack stack, List<Player> playerList, int cardsPerPlayer) throws EmptyStackException {
        if(stack.getStackSize() < playerList.size()*cardsPerPlayer) {
            throw new EmptyStackException("Not enough cards on stack for all player.");
        }
        for (Player player : playerList) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.getPlayerCards().add(stack.drawCard());
            }
        }
    }

    @Override
    public void playerCardsToStack(Stack stack, Player player) {
        stack.addCardsToStack(player.getPlayerCards());
    }

    @Override
    public void adjustPlayerIndex(List<Player> playerList) {
        int i = 0;
        for (Player p:
                playerList) {
            p.setPlayerIndex(i);
            i++;
        }
    }
}
