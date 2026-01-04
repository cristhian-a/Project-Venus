package com.next.ui;

import com.next.engine.graphics.RenderQueue;
import lombok.Setter;

public class UISystem {

    @Setter private UIState state;

    public void update(double delta) {
        state.update(delta);
    }

    public void submit(RenderQueue queue) {
        state.submitRender(queue);
    }

}
