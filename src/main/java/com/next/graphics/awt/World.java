package com.next.graphics.awt;

import com.next.Game;
import com.next.model.Camera;
import com.next.model.Tile;
import com.next.system.AssetRegistry;
import com.next.system.Settings;

import java.awt.*;

public class World {

    public static final int TILE_SIZE = Settings.ORIGINAL_TILE_SIZE; // should retrieve this from settings later

    private final AssetRegistry assets;
    private final Game game;

    private Tile[] tiles;
    private Integer[][] tileMap;

    public World(AssetRegistry assets, Game game) {
        this.assets = assets;
        this.game = game;
        this.tiles = new Tile[100];
        loadTiles();

        this.tileMap = assets.getTileMap("map_01");
    }

    private void loadTiles() {
        tiles[0] = new Tile(0, false);  // grass
        tiles[1] = new Tile(1, true);   // wall
        tiles[2] = new Tile(25, true);  // water
        tiles[3] = new Tile(27, false); // dirt
        tiles[4] = new Tile(26, true);  // tree
        tiles[5] = new Tile(28, false); // sand
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

                // drawing collision box
                if (tile.solid()) {
                    g.setColor(Color.RED);
                    Rectangle r = new Rectangle(screenX, screenY, TILE_SIZE, TILE_SIZE);
                    g.draw(r);
                }
            }
        }
    }
}
