package com.next.game.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.game.Game;
import com.next.game.event.DropItemEvent;
import com.next.game.model.factory.MaterialFactory;
import com.next.game.util.Tags;

public class DropHandler {

    private final Game game;
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public DropHandler(Mailbox mailbox, EventDispatcher dispatcher, Game game) {
        this.game = game;
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(DropItemEvent.class, this::onFire);
    }

    public void onFire(DropItemEvent event) {
        if (event.tag().equals(Tags.WOOD_LOG)) {
            game.getScene().add(MaterialFactory.woodLogWorldItem(event.x(), event.y()));
        }
    }
}
