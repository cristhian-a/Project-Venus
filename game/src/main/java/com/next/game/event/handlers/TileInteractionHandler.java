package com.next.game.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.game.event.DropItemEvent;
import com.next.game.event.TreeHitEvent;
import com.next.game.util.Tags;

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
        if (tile.getHealth() <= 0) {
            if (tile.getType().equals(Tags.INTERACT_TREE)) {
                dispatcher.dispatch(new DropItemEvent(Tags.WOOD_LOG, tile.getX(), tile.getY() - 8));
            }
        }
    }
}
