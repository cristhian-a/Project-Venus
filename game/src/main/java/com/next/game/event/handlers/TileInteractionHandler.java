package com.next.game.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.game.event.TreeHitEvent;

public class TileInteractionHandler {
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public TileInteractionHandler(Mailbox mailbox, EventDispatcher dispatcher) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(TreeHitEvent.class, this::onFire);
    }

    public void onFire(TreeHitEvent event) {
        var tile = event.tile();
        var hbSpc = event.spec();
        tile.takeDamage(hbSpc.damage());
    }
}
