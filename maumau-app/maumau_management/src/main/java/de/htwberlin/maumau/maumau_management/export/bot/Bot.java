package de.htwberlin.maumau.maumau_management.export.bot;

import de.htwberlin.maumau.player_management.export.Player;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Bot extends Player implements Serializable {

    public Bot(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Bot: " + this.getName();
    }
}
