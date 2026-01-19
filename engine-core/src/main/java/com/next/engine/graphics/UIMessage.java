package com.next.engine.graphics;

public final class UIMessage {
    public final String text;
    public final String font;
    public final int color;
    public final RenderPosition position;

    public int x, y;
    public int remainingFrames;

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
