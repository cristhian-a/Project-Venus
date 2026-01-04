package com.next.ui;

import com.next.engine.graphics.RenderQueue;

public interface UIState {
    void update(double delta);
    void submitRender(RenderQueue queue);
}
