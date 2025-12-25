package com.next.engine.graphics.awt;

import com.next.engine.model.Camera;
import com.next.world.Tile;
import com.next.world.World;
import com.next.system.AssetRegistry;

import java.awt.*;

final class TileRenderer {

    private final AssetRegistry assets;
    private World world;

    private Tile[] tiles;
    private Integer[][] tileMap;

    private boolean render;

    public TileRenderer(AssetRegistry assets) {
        this.assets = assets;
    }

    public void setWorld(World world) {
        this.world = world;
        tiles = world.getTiles();
        tileMap = world.getMap();

        render = true;
    }

    public void stopRendering() {
        render = false;
    }

    public void render(Graphics2D g, Camera camera) {
        if (!render) return;

        final int TILE_SIZE = world.getTileSize();
        
        int startCol = (int) Math.max(0, camera.getX() / TILE_SIZE);
        int endCol = (int) Math.min(tileMap[0].length, (camera.getX() + camera.getViewportWidth()) / TILE_SIZE + 1);

        int startRow = (int) Math.max(0, camera.getY() / TILE_SIZE);
        int endRow = (int) Math.min(tileMap.length, (camera.getY() + camera.getViewportHeight()) / TILE_SIZE + 1);

        for (int row = startRow; row < endRow; row++) {

            for (int col = startCol; col < endCol; col++) {
                int tileIndex = tileMap[row][col];
                Tile tile = tiles[tileIndex];

                int worldX = col * TILE_SIZE;
                int worldY = row * TILE_SIZE;
                int screenX = camera.worldToScreenX(worldX);
                int screenY = camera.worldToScreenY(worldY);

                g.drawImage(
                        assets.getSpriteSheet("world").getSprite(tile.spriteId()),
                        screenX,
                        screenY,
                        null
                );
            }
        }
    }
}
