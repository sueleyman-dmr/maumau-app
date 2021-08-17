package de.htwberlin.maumau.card_management.impl;

import de.htwberlin.maumau.card_management.export.*;

import java.util.*;

public class CardServiceImpl implements CardService {

    private static final List<Card> ALLE_KARTEN = new ArrayList<>();

    public List<Card> createCards() {
        if(this.ALLE_KARTEN.isEmpty()){
            for (CardValue werte : CardValue.values()) {
                for(Color farbe: Color.values()){
                        Card card = new Card(farbe, werte);
                        this.ALLE_KARTEN.add(card);
                }
            }
        }
        return this.ALLE_KARTEN;
    }

    @Override
    public List<Card> getCards(CardValue... cardValues) {
        List<Card> karten = new ArrayList<>();
        for (Card card: this.ALLE_KARTEN) {
            for (CardValue value : cardValues) {
                if (value == card.getCardValue()) {
                    karten.add(card);
                }
            }
        }
        return karten;
    }

    @Override
    public void mixCards(List<Card> cards) {
        Collections.shuffle(cards);
    }

    @Override
    public void sortCards(List<Card> cards) {
        cards.sort(Comparator.comparing(Card::getCardValue));
        cards.sort(Comparator.comparing(Card::getColor));
    }

}
