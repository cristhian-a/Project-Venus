package com.next.event.handlers;

import com.next.Game;
import com.next.engine.sound.PauseSound;
import com.next.event.PauseEvent;
import com.next.graphics.GameplayUIState;
import com.next.graphics.PausedUIState;
import com.next.util.GameState;
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
import lombok.Setter;

public class GameFlowHandler {
    private final Game game;
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    @Setter private GameplayUIState gameplayUIState;

    private TimeAccumulator accumulator = new TimeAccumulator();

    public GameFlowHandler(EventDispatcher dispatcher, Mailbox mailbox, Game game) {
        this.game = game;
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(FinishGameEvent.class, this::onFire);
        dispatcher.register(PauseEvent.class, this::onFire);
    }

    public void update(double delta) {
        accumulator.update(delta);
    }

    public void onFire(FinishGameEvent event) {
        if (game.getGameState() != GameState.RUNNING) return;
        game.setGameState(GameState.FINISHED);

        game.getUi().setState(new FinishUIState(accumulator.getDeltaTime()));
        dispatcher.dispatch(new GracefullyStopEvent());
        dispatcher.dispatch(new StopSound(Sounds.WIND));
        dispatcher.dispatch(new PlaySound(Sounds.FANFARE, SoundChannel.SFX, false));
    }

    public void onFire(PauseEvent event) {
        if (game.getGameState() == GameState.RUNNING) {
            game.setGameState(GameState.PAUSED);
            game.getUi().setState(new PausedUIState());
            dispatcher.dispatch(new PauseSound(Sounds.WIND));
        } else if (game.getGameState() == GameState.PAUSED) {
            game.setGameState(GameState.RUNNING);
            game.getUi().setState(gameplayUIState);
            dispatcher.dispatch(new PlaySound(Sounds.WIND, SoundChannel.MUSIC, true));
        }
    }
}
