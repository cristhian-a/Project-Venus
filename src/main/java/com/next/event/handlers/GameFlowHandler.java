package com.next.event.handlers;

import com.next.Game;
import com.next.engine.sound.PauseSound;
import com.next.event.StartGameEvent;
import com.next.event.PauseEvent;
import com.next.graphics.GameplayUIState;
import com.next.graphics.PausedUIState;
import com.next.system.Input;
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

public class GameFlowHandler {
    private final Game game;
    private final Input input;
    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    private GameplayUIState gameplayUIState;

    public GameFlowHandler(EventDispatcher dispatcher, Mailbox mailbox, Input input, Game game) {
        this.game = game;
        this.input = input;
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(FinishGameEvent.class, this::onFire);
        dispatcher.register(StartGameEvent.class, this::onFire);
        dispatcher.register(PauseEvent.class, this::onFire);
    }

    public void update(double delta) {
        if (game.getGameState() == GameState.RUNNING) return;
    }

    public void onFire(FinishGameEvent event) {
        if (game.getGameState() != GameState.RUNNING) return;
        game.setGameState(GameState.FINISHED);

        game.getUi().setState(new FinishUIState(0d));
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

    public void onFire(StartGameEvent event) {
        gameplayUIState = new GameplayUIState(game.getScene().player);
        game.start(gameplayUIState);
        game.getUi().setState(gameplayUIState);
        game.setGameState(GameState.RUNNING);
    }

}
