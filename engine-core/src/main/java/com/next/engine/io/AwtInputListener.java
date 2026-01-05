package com.next.engine.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AwtInputListener implements RawInputListener, KeyListener {

    // Written ONLY by EDT (event thread)
    private final boolean[] realKeys = new boolean[256];

    // Used ONLY by game thread
    private final boolean[] currentKeys = new boolean[256];
    private final boolean[] previousKeys = new boolean[256];

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < realKeys.length) {
            realKeys[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < realKeys.length) {
            realKeys[code] = false;
        }
    }

    @Override
    public void snapshot() {
        System.arraycopy(currentKeys, 0, previousKeys, 0, currentKeys.length);
        System.arraycopy(realKeys, 0, currentKeys, 0, realKeys.length);
    }

    @Override
    public boolean isDown(int keyCode) {
        return currentKeys[keyCode];
    }

    @Override
    public boolean isTyped(int keyCode) {
        return currentKeys[keyCode] && !previousKeys[keyCode];
    }

    @Override
    public boolean isReleased(int keyCode) {
        return !currentKeys[keyCode] && previousKeys[keyCode];
    }
}
