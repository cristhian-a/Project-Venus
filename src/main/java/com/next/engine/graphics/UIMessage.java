package com.next.engine.graphics;

public class UIMessage {
    public String text;
    public int x, y;
    public String font;
    public String color;
    public int remainingFrames;

    public UIMessage(String text, String font, String color, int x, int y, int remainingFrames) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.remainingFrames = remainingFrames;
    }
}
