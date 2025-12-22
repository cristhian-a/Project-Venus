package com.next.event.handlers;

import com.next.Game;
import com.next.engine.GameState;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GracefullyStopEvent;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.engine.sound.StopSound;
import com.next.event.FinishGameEvent;
import com.next.graphics.FinishUIState;
import com.next.util.Sounds;
import com.next.util.TimeAccumulator;

public class GameFlowHandler {
    private final Game game;
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    private TimeAccumulator accumulator = new TimeAccumulator();

    public GameFlowHandler(EventDispatcher dispatcher, Mailbox mailbox, Game game) {
        this.game = game;
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(FinishGameEvent.class, this::onFire);
    }

    public void onFire(FinishGameEvent event) {
        if (game.getGameState() != GameState.RUNNING) return;
        game.setGameState(GameState.FINISHED);

        game.getUi().setState(new FinishUIState(accumulator.getDeltaTime()));
        dispatcher.dispatch(new GracefullyStopEvent());
        dispatcher.dispatch(new StopSound(Sounds.WIND));
        dispatcher.dispatch(new PlaySound(Sounds.FANFARE, SoundChannel.SFX, false));
    }

    public void update(double delta) {
        accumulator.update(delta);
    }

}
