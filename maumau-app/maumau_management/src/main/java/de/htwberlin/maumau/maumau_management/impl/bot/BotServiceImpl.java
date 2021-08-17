package de.htwberlin.maumau.maumau_management.impl.bot;

import de.htwberlin.maumau.card_management.export.CardNotAllowedActivityException;
import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.maumau_management.export.bot.Bot;
import de.htwberlin.maumau.maumau_management.export.bot.BotService;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMauService;

import java.util.Random;


public class BotServiceImpl implements BotService {


    private final MauMauService mauMauService;

    public BotServiceImpl(MauMauService mauMauService) {
        this.mauMauService=mauMauService;
    }

    @Override
    public void botPlaying(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        if(maumau.isNeedCompulsoryColor()) {
            defineColor(maumau, bot);
        } else {
            if(bot.getPlayerCards().size() <= 2) {
                announceMau(maumau, bot);
            }
            Card card = playCard(maumau, bot);
            if(card == null) {
                drawCard(maumau, bot);
            }
        }
    }

    @Override
    public Card playCard(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        Card playedCard = null;
        for (Card card: bot.getPlayerCards()) {
            try{
                mauMauService.playCard(maumau, bot, card);
                playedCard = card;
                break;
            } catch (CardNotAllowedActivityException e){
                //continued in dem Falle, sucht solange, bis eine Karte spielbar ist, wenn nicht wird null geliefert
                //und der Bot muss dann im nächsten Schritt eine neue Karte ziehen
            }
        }
        return playedCard;
    }


    @Override
    public void drawCard(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        mauMauService.drawCard(maumau, bot);
    }

    @Override
    public void announceMau(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
            mauMauService.sayMau(maumau, bot);
    }

    @Override
    public void defineColor(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException {
        Color color = Color.values()[new Random().nextInt(Color.values().length)];
        mauMauService.chooseColor(maumau, bot, color);
    }

}
