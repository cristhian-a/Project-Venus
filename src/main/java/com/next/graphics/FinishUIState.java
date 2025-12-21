package com.next.graphics;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.RenderRequest;

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

        queue.submit(Layer.UI, RenderRequest.Type.OVERLAY);
        queue.submit(Layer.UI, congratsMsg, "arial_80b", "orange", -310, -125, RenderRequest.Position.CENTERED, 1);
        queue.submit(Layer.UI, finalMessage, "arial_30", "white", -60, 60, RenderRequest.Position.CENTERED, 1);
        queue.submit(Layer.UI, timeMessage, "arial_30", "white", -100, 100, RenderRequest.Position.CENTERED, 1);
    }
}
