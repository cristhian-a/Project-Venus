package com.next.game.gameflow;

import com.next.game.Game;
import com.next.engine.event.GracefullyStopEvent;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.engine.sound.StopSound;
import com.next.game.util.Sounds;
import com.next.game.ui.FinishUIState;

public final class FinishedMode implements GameMode {

    @Override
    public void onEnter(Game game) {
        var dispatcher = game.getDispatcher();

        game.getUi().setState(new FinishUIState(0d));
        dispatcher.dispatch(new GracefullyStopEvent());
        dispatcher.dispatch(new StopSound(Sounds.WIND));
        dispatcher.dispatch(new PlaySound(Sounds.FANFARE, SoundChannel.SFX, false));
    }

    @Override
    public void onExit(Game game) {
    }

    @Override
    public void update(Game game, double delta) {
    }
}
