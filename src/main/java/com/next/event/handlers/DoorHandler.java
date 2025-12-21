package com.next.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.DoorUnlockedEvent;
import com.next.event.KeyPickedUpEvent;
import com.next.event.NoKeysEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderRequest;

public class DoorHandler {

    private final Mailbox mailbox;

    public DoorHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;

        dispatcher.register(DoorUnlockedEvent.class, this::onFire);
        dispatcher.register(KeyPickedUpEvent.class, this::onFire);
        dispatcher.register(NoKeysEvent.class, this::onFire);
    }

    public void onFire(DoorUnlockedEvent event) {
        var player = event.player();
        var door = event.door();

        door.dispose();
        player.getHeldKeys().removeLast();
        IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());
    }

    public void onFire(NoKeysEvent event) {
        mailbox.renderQueue.submit(
                Layer.UI,
                "First grab a Key!",
                "arial_30",
                "white",
                -75,
                -25,
                RenderRequest.Position.CENTERED,
                240
        );
    }

    public void onFire(KeyPickedUpEvent event) {
        var player = event.player();
        var key = event.key();

        player.getHeldKeys().add(key);
        key.dispose();
        IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());

        mailbox.renderQueue.submit(
                Layer.UI,
                "Got a Key!",
                "arial_30",
                "white",
                -32,
                -25,
                RenderRequest.Position.CENTERED,
                240
        );
    }
}
