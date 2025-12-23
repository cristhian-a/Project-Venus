package com.next.engine.graphics;

import com.next.engine.physics.CollisionBox;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A request to be pushed to the renderer pipeline.
 * @deprecated Use pure {@link RenderQueue} instead.
 *
 * @value {@code layer}
 * @value {@code worldX}
 * @value {@code worldY}
 * @value {@code spriteId}
 */
@Deprecated(forRemoval = true)
public class RenderRequest {
    public enum Type {SPRITE, TEXT, COLLISION, OVERLAY}
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
    @Getter protected final String font;
    @Getter protected final String color;

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
        this.font = null;
        this.color = null;
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
        this.font = null;
        this.color = null;
    }

    public RenderRequest(Layer layer, String message, String font, String color, int x, int y, Position position, int framesToDie) {
        this.type = Type.TEXT;
        this.layer = layer;
        this.position = position;
        this.x = x;
        this.y = y;
        this.spriteId = 0;
        this.message = message;
        this.box = null;
        this.framesToDie = framesToDie;
        this.font = font;
        this.color = color;
    }

    public RenderRequest(Type type, Layer layer) {
        this.type = type;
        this.layer = layer;
        this.position = Position.AXIS;
        this.x = 0;
        this.y = 0;
        this.spriteId = 0;
        this.message = null;
        this.box = null;
        this.framesToDie = 0;
        this.font = null;
        this.color = null;
    }
}
