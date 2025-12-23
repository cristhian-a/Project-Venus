package com.next.graphics;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;

public class PausedUIState implements UIState {

    @Override
    public void update(double delta) {
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.submit(
                Layer.UI,
                "PAUSED",
                "arial_30",
                "white",
                10,
                30,
                RenderPosition.AXIS,
                0
        );
    }
}
