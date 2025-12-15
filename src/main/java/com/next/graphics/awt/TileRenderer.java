package com.next.graphics.awt;

import com.next.model.Camera;
import com.next.model.Tile;
import com.next.model.World;
import com.next.system.AssetRegistry;
import com.next.system.Settings;

import java.awt.*;

public class TileRenderer {

    public static final int TILE_SIZE = Settings.TILE_SIZE; // should retrieve this from settings later

    private final AssetRegistry assets;

    private Tile[] tiles;
    private Integer[][] tileMap;

    public TileRenderer(AssetRegistry assets, World world) {
        this.assets = assets;
        tiles = world.getTiles();
        tileMap = world.getMap();
    }

    public void render(Graphics2D g, Camera camera) {
        int startCol = Math.max(0, camera.getX() / TILE_SIZE);
        int endCol = Math.min(tileMap[0].length, (camera.getX() + camera.getViewportWidth()) / TILE_SIZE + 1);

        int startRow = Math.max(0, camera.getY() / TILE_SIZE);
        int endRow = Math.min(tileMap.length, (camera.getY() + camera.getViewportHeight()) / TILE_SIZE + 1);

        for (int row = startRow; row < endRow; row++) {

            for (int col = startCol; col < endCol; col++) {
                int tileIndex = tileMap[row][col];
                Tile tile = tiles[tileIndex];

                int worldX = col * TILE_SIZE;
                int worldY = row * TILE_SIZE;
                int screenX = camera.worldToScreenX(worldX);
                int screenY = camera.worldToScreenY(worldY);

                g.drawImage(assets.getSpriteSheet("world").getSprite(tile.spriteId()), screenX, screenY, null);
            }
        }
    }
}
