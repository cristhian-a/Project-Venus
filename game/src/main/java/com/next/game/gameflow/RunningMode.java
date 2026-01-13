package com.next.game.gameflow;

import com.next.engine.data.Mailbox;
import com.next.game.Game;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.game.util.Sounds;
import com.next.engine.scene.Scene;

public final class RunningMode implements GameMode {

    @Override
    public void onEnter(Game game) {
        game.getUi().setState(game.getGameplayUIState());
        game.getDispatcher().dispatch(new PlaySound(Sounds.WIND, SoundChannel.MUSIC, true));
    }

    @Override
    public void onExit(Game game) {
    }

    @Override
    public void update(Game game, double delta) {
        Scene scene = game.getScene();
        Mailbox mailbox = game.getMailbox();

        scene.update(delta, mailbox);
        game.getCombatHandler().update(delta);
        game.getPhysics().apply(delta, mailbox.motionQueue, mailbox);

        game.getDispatcher().dispatch(mailbox);
        scene.dismissDisposed();

        scene.camera.follow(game.getPlayer());
        scene.submitRender(mailbox.postRender());
    }
}
