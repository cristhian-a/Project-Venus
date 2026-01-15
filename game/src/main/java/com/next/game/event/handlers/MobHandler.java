package com.next.game.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.util.PCG32;
import com.next.engine.util.Rng;
import com.next.game.Game;
import com.next.game.event.MobDeathEvent;
import com.next.game.model.Mob;
import com.next.game.model.WorldItem;
import com.next.game.model.factory.MaterialFactory;

public class MobHandler {

    private final Game game;
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    private final Rng rng = new PCG32(1, 1);

    public MobHandler(Game game, Mailbox mailbox, EventDispatcher dispatcher) {
        this.game = game;
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(MobDeathEvent.class, this::onFire);
    }

    public void onFire(MobDeathEvent event) {
        var drop = checkDrop(event.mob());
        if (drop != null) {
            game.getScene().add(drop);
        }
    }

    private WorldItem checkDrop(Mob mob) {
        int v = rng.rollDice(100);

        if (v <= 25) return MaterialFactory.purpleEssenceWorldItem(mob.getX(), mob.getY());
        else if (v <= 50) return MaterialFactory.silverCoin(mob.getX(), mob.getY());
        else return null;
    }
}
