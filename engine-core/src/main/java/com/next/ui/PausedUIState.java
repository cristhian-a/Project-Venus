package com.next.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.util.Colors;
import com.next.engine.util.Fonts;

public class PausedUIState implements UIState {

    String PAUSED = "PAUSED";

    @Override
    public void update(double delta) {
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.submit(
                Layer.UI,
                PAUSED,
                Fonts.DEFAULT,
                Colors.WHITE,
                10,
                30,
                RenderPosition.AXIS,
                0
        );
    }
}
