package com.next.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.event.DoorUnlockedEvent;
import com.next.event.KeyPickedUpEvent;
import com.next.event.NoKeysEvent;
import com.next.engine.graphics.Layer;
import com.next.util.Colors;
import com.next.util.Fonts;
import com.next.util.Sounds;

public class DoorHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public DoorHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

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

        dispatcher.dispatch(new PlaySound(Sounds.OPEN_DOOR, SoundChannel.SFX, false));
    }

    public void onFire(NoKeysEvent event) {
        mailbox.postRender().submit(
                Layer.UI_SCREEN,
                "First grab a Key!",
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                -50,
                RenderPosition.CENTERED,
                240
        );
    }

    public void onFire(KeyPickedUpEvent event) {
        var player = event.player();
        var key = event.key();

        player.getHeldKeys().add(key);
        key.dispose();
        IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());

        mailbox.postRender().submit(
                Layer.UI_SCREEN,
                "Got a Key!",
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                -50,
                RenderPosition.CENTERED,
                240
        );

        dispatcher.dispatch(new PlaySound(Sounds.PICK_UP, SoundChannel.SFX, false));
    }
}
