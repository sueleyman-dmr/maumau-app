package de.htwberlin.maumau.maumau_management.export.bot;

import de.htwberlin.maumau.card_management.export.CardNotAllowedActivityException;
import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;

/**
 * The botService interface defines which functions virtual players have.
 * @author Süleyman Demir
 */
public interface BotService {

    /**
     * If bots turn, then the method should be called, but it have to be implemented.
     * @param maumau - the specified maumau game
     * @param bot - the actual virtual player
     * @throws Exception
     */
    void botPlaying(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

    /**
     * Bot plays a card in maumau game.
     * @param maumau - the specified maumau game
     * @param bot - the actual virtual player
     * @return the played card.
     * @throws PlayerNotAllowedActivityException
     * @throws CardNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    Card playCard(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

    /**
     * Bot draws cards from stack
     * @param maumau the game
     * @param bot virtual player
     * @throws MauMauTechnicalException
     * @throws PlayerNotAllowedActivityException
     */
    void drawCard(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

    /**Bot calls Mau
     * @param maumau
     * @param bot virtual player
     * @throws Exception
     */
    void announceMau(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

    /**
     * The bot sets a compulsory color.
     * @param maumau the game
     * @param bot virtual player
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    void defineColor(MauMau maumau, Bot bot) throws PlayerNotAllowedActivityException, MauMauTechnicalException;

}
