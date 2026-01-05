package com.next.engine.io;

public interface RawInputListener {
    void snapshot();
    boolean isDown(int keyCode);
    boolean isTyped(int keyCode);
    boolean isReleased(int keyCode);
}
