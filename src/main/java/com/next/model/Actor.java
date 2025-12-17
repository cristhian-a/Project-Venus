package com.next.model;

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

        if (collisions.isCollidingWithTile(this)) {
            var box = collisionBox.getBounds();

            if (dx > 0) {
                int tileCol = (int) (box.x + box.width) / collisions.getTileSize();
                worldX = tileCol * collisions.getTileSize() - (int) (box.width + collisionBox.offsetX);
            } else {
                int tileCol = (int) box.x / collisions.getTileSize();
                worldX = (tileCol + 1) * collisions.getTileSize() - (int) collisionBox.offsetX;
            }

            collisionBox.update(worldX, worldY);
        }
    }

    protected void moveY(float dy, CollisionInspector collisions) {
        if (dy == 0) return;

        worldY += (int) dy;
        collisionBox.update(worldX, worldY);

        if (collisions.isCollidingWithTile(this)) {
            var box = collisionBox.getBounds();

            if (dy > 0) {
                int tileRow = (int) (box.y + box.height) / collisions.getTileSize();
                worldY = tileRow * collisions.getTileSize() - (int) (box.height + collisionBox.offsetY);
            } else {
                int tileRow = (int) box.y / collisions.getTileSize();
                worldY = (tileRow + 1) * collisions.getTileSize() - (int) collisionBox.offsetY;
            }

            collisionBox.update(worldX, worldY);
        }
    }
}
