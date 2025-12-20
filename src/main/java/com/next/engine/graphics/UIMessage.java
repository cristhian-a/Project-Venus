package com.next.engine.graphics;

public class UIMessage {
    public String text;
    public int x, y;
    public int remainingFrames;

    public UIMessage(String text, int x, int y, int remainingFrames) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.remainingFrames = remainingFrames;
    }
}
