package com.next.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SetVolume;
import com.next.engine.sound.SoundChannel;
import com.next.event.SpellPickedUpEvent;
import com.next.engine.graphics.Layer;
import com.next.util.Colors;
import com.next.util.Fonts;
import com.next.util.Sounds;

public class SpellHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public SpellHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(SpellPickedUpEvent.class, this::onFire);
    }

    public void onFire(SpellPickedUpEvent event) {
        event.spell().dispose();
        event.player().boostSpeed(3f);

        mailbox.postRender().submit(
                Layer.UI_SCREEN,
                "Mercury's Bless!",
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                -50,
                RenderPosition.CENTERED,
                240
        );

        dispatcher.dispatch(new PlaySound(Sounds.SPELL_UP, SoundChannel.SFX, false));
        // Test
        dispatcher.dispatch(new SetVolume(-10f, SoundChannel.MUSIC));
    }
}
