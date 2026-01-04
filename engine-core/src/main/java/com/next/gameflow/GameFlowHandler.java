package com.next.gameflow;

import com.next.Game;
import com.next.event.StartGameEvent;
import com.next.event.PauseEvent;
import com.next.engine.event.EventDispatcher;
import com.next.event.FinishGameEvent;

public class GameFlowHandler {

    private final Game game;

    public GameFlowHandler(EventDispatcher dispatcher, Game game) {
        this.game = game;

        dispatcher.register(FinishGameEvent.class, this::onFire);
        dispatcher.register(StartGameEvent.class, this::onFire);
        dispatcher.register(PauseEvent.class, this::onFire);
    }

    public void onFire(FinishGameEvent event) {
        if (game.isRunning()) {
            game.setMode(new FinishedMode());
        }
    }

    public void onFire(PauseEvent event) {
        if (game.isRunning()) {
            game.setMode(new PausedMode());
        } else if (game.isPaused()) {
            game.setMode(new RunningMode());
        }
    }

    public void onFire(StartGameEvent event) {
        game.start();
        game.setMode(new RunningMode());
    }

}
