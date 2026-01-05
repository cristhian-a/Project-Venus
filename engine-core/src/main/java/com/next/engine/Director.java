package com.next.engine;

import com.next.engine.model.Camera;

public interface Director {
    void init();
    void update(double delta);
    Camera getCamera();
}
