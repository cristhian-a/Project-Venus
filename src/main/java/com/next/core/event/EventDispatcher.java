package com.next.core.event;

import com.next.core.data.Mailbox;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;

public class EventDispatcher {

    public static void dispatch(Mailbox mailbox) {
        for (GameEvent event : mailbox.events) {
            if (event instanceof GameEvent.KeyPickedUp) {
                mailbox.renderQueue.submit(
                        Layer.UI,
                        "Got a Key!",
                        -32,
                        -25,
                        RenderRequest.Position.CENTERED,
                        240
                );
            }
        }

        clearQueue(mailbox);
    }

    public static void clearQueue(Mailbox mailbox) {
        mailbox.events.clear();
    }
}
