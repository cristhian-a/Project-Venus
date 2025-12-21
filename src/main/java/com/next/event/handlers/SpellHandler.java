package com.next.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.SpellPickedUpEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderRequest;

public class SpellHandler {

    private final Mailbox mailbox;

    public SpellHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;

        dispatcher.register(SpellPickedUpEvent.class, this::onFire);
    }

    public void onFire(SpellPickedUpEvent event) {
        event.spell().dispose();
        event.player().boostSpeed(3f);

        mailbox.renderQueue.submit(
                Layer.UI,
                "Mercury's Bless!",
                "arial_30",
                "white",
                -75,
                -25,
                RenderRequest.Position.CENTERED,
                240
        );
    }
}
