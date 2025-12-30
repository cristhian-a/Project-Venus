package com.next.engine.graphics;

public class UIMessage {
    public String text;
    public int x, y;
    public String font;
    public int color;
    public int remainingFrames;
    public RenderPosition position;

    public UIMessage(String text, String font, int color, int x, int y, RenderPosition position, int remainingFrames) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.position = position;
        this.remainingFrames = remainingFrames;
    }
}
