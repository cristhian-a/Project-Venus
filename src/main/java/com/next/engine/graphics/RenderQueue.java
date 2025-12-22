package com.next.engine.graphics;

import com.next.engine.physics.CollisionBox;

public final class RenderQueue {

    private final int DEFAULT_SIZE = 50;
    private int BUFFER_SIZE = DEFAULT_SIZE;
    private int current = 0;

    public Layer[] layer = new Layer[DEFAULT_SIZE];
    public RenderType[] type = new RenderType[DEFAULT_SIZE];
    public RenderPosition[] position = new RenderPosition[DEFAULT_SIZE];
    public int[] sprite = new int[DEFAULT_SIZE];
    public int[] x = new int[DEFAULT_SIZE];
    public int[] y = new int[DEFAULT_SIZE];
    public int[] frames = new int[DEFAULT_SIZE];
    public String[] message = new String[DEFAULT_SIZE];
    public String[] font = new String[DEFAULT_SIZE];
    public String[] color = new String[DEFAULT_SIZE];
    public CollisionBox[] box = new CollisionBox[DEFAULT_SIZE];

    private void advance() {
        current++;
        if (current >= DEFAULT_SIZE) throw new RuntimeException("Render queue is full!");
    }

    public int size() {
        return current;
    }

    public int capacity() {
        return BUFFER_SIZE;
    }

    public void clear() {
        current = 0;
    }

    public void allocate(int size) {
        BUFFER_SIZE = size;
        current = 0;
        clear();

        layer = new Layer[BUFFER_SIZE];
        type = new RenderType[BUFFER_SIZE];
        position = new RenderPosition[BUFFER_SIZE];
        sprite = new int[BUFFER_SIZE];
        x = new int[BUFFER_SIZE];
        y = new int[BUFFER_SIZE];
        frames = new int[BUFFER_SIZE];
        message = new String[BUFFER_SIZE];
        font = new String[BUFFER_SIZE];
        color = new String[BUFFER_SIZE];
        box = new CollisionBox[BUFFER_SIZE];
    }

    public void submit(Layer layer, RenderType type, RenderPosition position, int sprite, int x, int y, int frames, String message,
                       String font, String color, CollisionBox box) {
        this.layer[current] = layer;
        this.type[current] = type;
        this.position[current] = position;
        this.sprite[current] = sprite;
        this.x[current] = x;
        this.y[current] = y;
        this.frames[current] = frames;
        this.message[current] = message;
        this.font[current] = font;
        this.color[current] = color;
        this.box[current] = box;
        advance();
    }

    public void submit(Layer layer, int x, int y, int spriteId) {
        submit(layer, RenderType.SPRITE, RenderPosition.AXIS, spriteId, x, y, 0, null, null, null, null);
    }

    public void submit(Layer layer, CollisionBox box) {
        submit(layer, RenderType.COLLISION, RenderPosition.COLLISION, 0, 0, 0, 0, null, null, null, box);
    }

    public void submit(Layer layer, String message, String font, String color, int x, int y, RenderPosition pos, int frames) {
        submit(layer, RenderType.TEXT, pos, 0, x, y, frames, message, font, color, null);
    }

    public void submit(Layer layer, RenderType type) {
        submit(layer, type, RenderPosition.AXIS, 0, 0, 0, 0, null, null, null, null);
    }

}
