package de.htwberlin.maumau.configuration;

import de.htwberlin.maumau.card_management.export.CardNotAllowedActivityException;
import de.htwberlin.maumau.card_management.export.EmptyStackException;
import de.htwberlin.maumau.card_management.impl.CardServiceImpl;
import de.htwberlin.maumau.dao.impl.DAOServiceImpl;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.maumau_management.impl.bot.BotServiceImpl;
import de.htwberlin.maumau.maumau_management.impl.mauGame.MauMauServiceImpl;
import de.htwberlin.maumau.player_management.impl.PlayerServiceImpl;
import de.htwberlin.maumau.regel_management.impl.RuleServiceImpl;
import de.htwberlin.maumau.ui.export.MauMauController;
import de.htwberlin.maumau.ui.impl.MauMauControllerImpl;
import de.htwberlin.maumau.ui.impl.MauMauView;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ConstructorInjection;

/**
 * Configuration class of the MauMau Game
 */
public class MauMauConfiguration {

    private static final MutablePicoContainer container = new DefaultPicoContainer(new ConstructorInjection());
    private static Logger LOGGER = LogManager.getLogger(MauMauConfiguration.class);

    public static void main(String[] args){
        registerComponents();
        container.getComponent(MauMauController.class).run();
    }

    /**
     * The method injects all components with the MutablePicoContainer
     */
    private static void registerComponents() {
        try {
            LOGGER.info("Komponenten werden injiziert");
            container.addComponent(CardServiceImpl.class);
            container.addComponent(RuleServiceImpl.class);
            container.addComponent(PlayerServiceImpl.class);
            container.addComponent(MauMauControllerImpl.class);
            container.addComponent(MauMauView.class);
            container.addComponent(BotServiceImpl.class);
            container.addComponent(MauMauServiceImpl.class);
            container.addComponent(DAOServiceImpl.class);
        } catch(NullPointerException e) {
            LOGGER.error("Komponente fehlt");
        }

    }
}
