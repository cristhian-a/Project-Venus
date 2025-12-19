package com.next.core.event;

import com.next.core.data.Mailbox;
import com.next.event.*;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;
import com.next.model.*;

public class EventDispatcher {

    public static void dispatch(Mailbox mailbox) {
        for (GameEvent event : mailbox.events) {
            if (event instanceof KeyPickedUpEvent(Key key)) {
                key.dispose();
                mailbox.renderQueue.submit(
                        Layer.UI,
                        "Got a Key!",
                        -32,
                        -25,
                        RenderRequest.Position.CENTERED,
                        240
                );
            } else if (event instanceof NoKeysEvent) {
                mailbox.renderQueue.submit(
                        Layer.UI,
                        "First grab a Key!",
                        -75,
                        -25,
                        RenderRequest.Position.CENTERED,
                        240
                );
            } else if (event instanceof DoorUnlockedEvent(Door door, Player player)) {
                door.dispose();
                player.getHeldKeys().removeLast();
                IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());
            } else if (event instanceof SpellPickedUpEvent(Spell spell, Player player)) {
                spell.dispose();
                player.boostSpeed(3f);

                mailbox.renderQueue.submit(
                        Layer.UI,
                        "Mercury's Bless!",
                        -75,
                        -25,
                        RenderRequest.Position.CENTERED,
                        240
                );
            } else if (event instanceof FinishGameEvent f) {
                IO.println("YOU WIN!");
            }
        }

        clearQueue(mailbox);
    }

    public static void clearQueue(Mailbox mailbox) {
        mailbox.events.clear();
    }
}
