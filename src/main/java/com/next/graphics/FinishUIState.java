package com.next.graphics;

import com.next.engine.graphics.*;
import com.next.util.Colors;
import com.next.util.Fonts;

public class FinishUIState implements UIState {

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
        String finalMessage = "You Win!";
        String timeMessage = "Your Time: " + String.format("%.2f", time) + "s";

        queue.submit(Layer.UI, RenderType.OVERLAY);
        queue.submit(Layer.UI, congratsMsg, Fonts.DEFAULT_80_BOLD, Colors.ORANGE, -310, -125, RenderPosition.CENTERED, 1);
        queue.submit(Layer.UI, finalMessage, Fonts.DEFAULT, Colors.WHITE, -60, 60, RenderPosition.CENTERED, 1);
        queue.submit(Layer.UI, timeMessage, Fonts.DEFAULT, Colors.WHITE, -115, 100, RenderPosition.CENTERED, 1);
    }
}
