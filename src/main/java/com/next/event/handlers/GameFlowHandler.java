package com.next.event.handlers;

import com.next.Game;
import com.next.engine.GameState;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GracefullyStopEvent;
import com.next.event.FinishGameEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderRequest;
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

        double delta = accumulator.getDeltaTime();
        String timeMessage = String.format("%.0f", delta);
        String finalMessage = "You Win! Final Time: " + timeMessage + "s";

        mailbox.renderQueue.submit(Layer.UI, finalMessage, -150, -25, RenderRequest.Position.CENTERED, 1);
        dispatcher.dispatch(new GracefullyStopEvent());
    }

    public void update(double delta) {
        accumulator.update(delta);
    }

}
