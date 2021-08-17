package de.htwberlin.maumau.regel_management.impl;

import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.CardValue;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.regel_management.export.Deck;
import de.htwberlin.maumau.regel_management.export.RuleService;
import de.htwberlin.maumau.regel_management.export.GameMove;

import java.util.List;

public class RuleServiceImpl implements RuleService {

    @Override
    public boolean isPlayersTurn(Player lastPlayer, Player player) {
        return lastPlayer.getPlayerIndex() == player.getPlayerIndex();
    }

    @Override
    public boolean isCardPlayable(Deck deck, Card nextCard, Color compulsoryColor, GameMove lastMove) {
        boolean playable = false;
        Card discoveredCard = deck.getDiscoveredCard();

        if(discoveredCard.getCardValue() == CardValue.SIEBEN && nextCard.getCardValue() == CardValue.BUBE) {
            return lastMove != null && (lastMove.getPlayedCard() == null || lastMove.getPlayedCard().getCardValue() != CardValue.SIEBEN);
        }

        if(discoveredCard.getCardValue() == CardValue.SIEBEN) {
            if(lastMove != null && lastMove.getPlayedCard() != null) {
                return nextCard.getCardValue() == CardValue.SIEBEN;
            }
        }

        if(discoveredCard.getCardValue() == CardValue.BUBE) {
            return nextCard.getColor().equals(compulsoryColor) && nextCard.getCardValue() != CardValue.BUBE;
        }

        if(nextCard.getCardValue() == CardValue.BUBE) {
            playable = true;
        }

        if(discoveredCard.getCardValue() == nextCard.getCardValue() || discoveredCard.getColor().equals(nextCard.getColor())) {
            return true;
        }

        return playable;
    }

    @Override
    public boolean mustCallMau(Player player) {
        return player.getPlayerCards().size() <= 2;
    }

    @Override
    public int getMauCallPenalty(boolean mustSayMau, boolean mauSaid) {
        int penaltyCards = 0;
        if (mustSayMau && !mauSaid) {
            penaltyCards = 2;
        }
        return penaltyCards;
    }

    @Override
    public boolean mustChooseColor(Card card) {
        return card.getCardValue() == CardValue.BUBE;
    }

    @Override
    public boolean getGameDirection(Card card, boolean gameDirectionClockwise) {
        if(card.getCardValue() == CardValue.NEUN) {
            gameDirectionClockwise = !gameDirectionClockwise;
        }
        return gameDirectionClockwise;
    }

    @Override
    public boolean checkPlayerChange(Card card) {
        return card.getCardValue() != CardValue.ASS;
    }

    @Override
    public int getNextPlayerIndex(boolean gameDirection, int playerAmount, int lastPlayerIndex) {
        int nextPlayerIndex;
        if (gameDirection) {
            nextPlayerIndex = lastPlayerIndex + 1;
            if (nextPlayerIndex >= playerAmount) {
                nextPlayerIndex = 0;
            }
        } else {
            nextPlayerIndex = lastPlayerIndex -1;
            if(nextPlayerIndex < 0) {
                nextPlayerIndex = playerAmount -1;
            }
        }
        return nextPlayerIndex;
    }

    @Override
    public int getPenaltyCardsAmountBy7(List<GameMove> gameMoves) {
        int countCard7 = 0;
        for (int i = gameMoves.size()-1; i >= 0; i--) {
            GameMove gameMove = gameMoves.get(i);
            if(gameMove.getPlayedCard() == null ) {
                break;
            }
            else if (gameMove.getPlayedCard().getCardValue() == CardValue.SIEBEN && gameMove.getPlayedCard() != null) {
                countCard7++;
            } else {
                break;
            }
        }
        return countCard7*2;
    }

    @Override
    public Player getWinner(List<Player> playerList) {
        Player winner = null;
        for (Player p: playerList) {
            if(p.getPlayerCards().isEmpty()) {
                winner = p;
                break;
            }
        }
        return winner;
    }
}
