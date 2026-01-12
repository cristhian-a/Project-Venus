package com.next.engine.model;

import com.next.engine.graphics.RenderQueue;

public interface Renderable {
    void collectRender(RenderQueue queue);
}
