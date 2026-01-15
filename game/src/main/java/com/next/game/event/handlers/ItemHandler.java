package com.next.game.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.game.event.DoorUnlockedEvent;
import com.next.game.event.ItemPickedUpEvent;
import com.next.game.event.NoKeysEvent;
import com.next.engine.graphics.Layer;
import com.next.game.model.Key;
import com.next.game.model.SilverCoin;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;
import com.next.game.util.Sounds;

public class ItemHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public ItemHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(DoorUnlockedEvent.class, this::onFire);
        dispatcher.register(ItemPickedUpEvent.class, this::onFire);
        dispatcher.register(NoKeysEvent.class, this::onFire);
    }

    public void onFire(DoorUnlockedEvent event) {
        var player = event.player();
        var door = event.door();

        var key = player.getInventory().get(Key.class);
        key.ifPresent(item -> {
            player.getInventory().pop(item);
            door.dispose();
            dispatcher.dispatch(new PlaySound(Sounds.OPEN_DOOR, SoundChannel.SFX, false));
            IO.println("AAAAAAAAI CHAVES!");
        });
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

    public void onFire(ItemPickedUpEvent event) {
        var player = event.player();
        var item = event.item();

        if (item instanceof SilverCoin coin) {
            player.getAttributes().coin += coin.value;
            coin.dispose();
            return;
        }

        boolean got = player.getInventory().add(item.getInventoryVersion());
        if (got) {
            item.dispose();

            mailbox.postRender().submit(
                    Layer.UI_SCREEN,
                    "You got a " + item.getName() + "!",
                    Fonts.DEFAULT,
                    Colors.WHITE,
                    0,
                    -50,
                    RenderPosition.CENTERED,
                    240
            );

            dispatcher.dispatch(new PlaySound(Sounds.PICK_UP, SoundChannel.SFX, false));
        } else {
            mailbox.postRender().submit(
                    Layer.UI_SCREEN,
                    "Backpack is full!",
                    Fonts.DEFAULT,
                    Colors.WHITE,
                    0,
                    -50,
                    RenderPosition.CENTERED,
                    240
            );
        }
    }
}
