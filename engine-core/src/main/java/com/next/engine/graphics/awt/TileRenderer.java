package com.next.engine.graphics.awt;

import com.next.engine.data.Registry;
import com.next.engine.debug.DebugTimer;
import com.next.engine.debug.DebugTimers;
import com.next.engine.graphics.Sprite;
import com.next.engine.model.Camera;
import com.next.engine.scene.Tile;
import com.next.engine.scene.World;

import java.awt.*;

final class TileRenderer {

    private static final DebugTimer debugtimer = DebugTimers.of(DebugTimers.RENDER_TILES);

    private World world;
    private Tile[] tiles;
    private Integer[][] tileMap;

    private boolean render;

    public TileRenderer() {
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
        debugtimer.begin();

        if (!render) return;

        final var tileSheet = Registry.masterSheet;
        final int TILE_SIZE = world.getTileSize();
        
        int startCol = (int) Math.max(0, camera.getX() / TILE_SIZE);
        int endCol = (int) Math.min(tileMap[0].length, (camera.getX() + camera.getViewportWidth()) / TILE_SIZE + 1);

        int startRow = (int) Math.max(0, camera.getY() / TILE_SIZE);
        int endRow = (int) Math.min(tileMap.length, (camera.getY() + camera.getViewportHeight()) / TILE_SIZE + 1);

        for (int row = startRow; row < endRow; row++) {

            for (int col = startCol; col < endCol; col++) {
                int tileIndex = tileMap[row][col];
                Tile tile = tiles[tileIndex];
                Sprite s = Registry.sprites[tile.spriteId()];

                int dx = col * TILE_SIZE;
                int dy = row * TILE_SIZE;

                tileSheet.draw(
                        g,
                        dx, dy, dx + s.srcWidth(), dy + s.srcHeight(),
                        s.srcX(), s.srcY(), s.srcX2(), s.srcY2()
                );
            }
        }

        debugtimer.end();
    }

}
