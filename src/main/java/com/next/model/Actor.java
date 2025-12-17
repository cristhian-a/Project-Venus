package com.next.model;

import com.next.core.CollisionType;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;
import com.next.system.Debugger;
import lombok.Getter;

@Getter
public abstract class Actor {
    protected int spriteId;
    protected int worldX;
    protected int worldY;
    protected CollisionBox collisionBox;        // TODO: remember to initialize it anytime (or move it to children)
    protected CollisionType collisionType = CollisionType.NONE;

    protected boolean discarded;

    public RenderRequest getRenderRequest() {
        if (collisionBox != null) Debugger.publishCollision("ACTOR BOX" + this, collisionBox);
        return new RenderRequest(Layer.ACTORS, worldX, worldY, spriteId);
    }

    public void setPosition(int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        if (collisionBox != null) {
            collisionBox.update(worldX, worldY);
        }
    }

    protected void moveX(float dx, CollisionInspector collisions) {
        if (dx == 0) return;

        worldX += (int) dx;
        collisionBox.update(worldX, worldY);

        if (collisions.isColliding(this)) {
            clampX(dx, collisions.getTileSize());
        }
    }

    protected void moveY(float dy, CollisionInspector collisions) {
        if (dy == 0) return;

        worldY += (int) dy;
        collisionBox.update(worldX, worldY);

        if (collisions.isColliding(this)) {
            clampY(dy, collisions.getTileSize());
        }
    }

    public void clampX(float dx, int tileSize) {
        var box = collisionBox.getBounds();

        if (dx > 0) {
            int tileCol = (int) (box.x + box.width) / tileSize;
            worldX = tileCol * tileSize - (int) (box.width + collisionBox.offsetX);
        } else {
            int tileCol = (int) box.x / tileSize;
            worldX = (tileCol + 1) * tileSize - (int) collisionBox.offsetX;
        }

        collisionBox.update(worldX, worldY);
    }

    public void clampY(float dy, int tileSize) {
        var box = collisionBox.getBounds();

        if (dy > 0) {
            int tileRow = (int) (box.y + box.height) / tileSize;
            worldY = tileRow * tileSize - (int) (box.height + collisionBox.offsetY);
        } else {
            int tileRow = (int) box.y / tileSize;
            worldY = (tileRow + 1) * tileSize - (int) collisionBox.offsetY;
        }

        collisionBox.update(worldX, worldY);
    }

    public void onTrigger(Actor other) {}

    public void discard() {
        discarded = true;
    }
}
