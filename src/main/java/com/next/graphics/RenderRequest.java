package com.next.graphics;

import com.next.engine.physics.CollisionBox;
import lombok.Getter;

/**
 * A request to be pushed to the renderer pipeline.
 *
 * @value {@code layer}
 * @value {@code worldX}
 * @value {@code worldY}
 * @value {@code spriteId}
 */
public class RenderRequest {
    public enum Type {SPRITE, TEXT, COLLISION}
    public enum Position {AXIS, CENTERED, COLLISION}

    @Getter protected final Layer layer;
    @Getter protected final Type type;
    @Getter protected final Position position;
    @Getter protected final int x;
    @Getter protected final int y;
    @Getter protected final int spriteId;
    @Getter protected final String message;
    @Getter protected final CollisionBox box;
    @Getter protected final int framesToDie;

    public RenderRequest(Layer layer, int x, int y, int spriteId) {
        this.type = Type.SPRITE;
        this.layer = layer;
        this.position = Position.AXIS;
        this.x = x;
        this.y = y;
        this.spriteId = spriteId;
        this.message = null;
        this.box = null;
        this.framesToDie = 0;
    }

    public RenderRequest(Layer layer, CollisionBox box) {
        this.type = Type.COLLISION;
        this.layer = layer;
        this.position = Position.COLLISION;
        this.x = 0;
        this.y = 0;
        this.spriteId = 0;
        this.message = null;
        this.box = box;
        this.framesToDie = 0;
    }

    public RenderRequest(Layer layer, String message, int x, int y, Position position, int framesToDie) {
        this.type = Type.TEXT;
        this.layer = layer;
        this.position = position;
        this.x = x;
        this.y = y;
        this.spriteId = 0;
        this.message = message;
        this.box = null;
        this.framesToDie = framesToDie;
    }
}
