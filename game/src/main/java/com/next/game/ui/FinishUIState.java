package com.next.game.ui;

import com.next.engine.graphics.*;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public final class FinishUIState implements UIState {

    private final double time;
    private int fade;

    public FinishUIState(double time) {
        this.time = time;
    }

    @Override
    public void update(double delta) {
        fade++;
    }

    @Override
    public void submitRender(RenderQueue queue) {
        String congratsMsg = "Congratulations!";
        String finalMessage = "You are a treasure hunter master!";
        String timeMessage = "Your Time: " + String.format("%.2f", time) + "s";

        queue.overlay(Layer.UI_SCREEN);
        queue.submit(Layer.UI_SCREEN, congratsMsg, Fonts.DEFAULT_80_BOLD, Colors.ORANGE, 0, -125, RenderPosition.CENTERED, 1);
        queue.submit(Layer.UI_SCREEN, finalMessage, Fonts.DEFAULT, Colors.WHITE, 0, 60, RenderPosition.CENTERED, 1);
        queue.submit(Layer.UI_SCREEN, timeMessage, Fonts.DEFAULT, Colors.WHITE, 0, 100, RenderPosition.CENTERED, 1);
    }
}
