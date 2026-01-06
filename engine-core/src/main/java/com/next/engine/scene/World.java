package com.next.engine.scene;

import com.next.engine.data.Registry;
import lombok.Getter;
import lombok.Setter;

public class World {

    @Getter private final WorldRules rules;

    @Getter private Tile[] tiles;
    @Getter @Setter private Integer[][] map;

    public World(WorldRules rules, Integer[][] map) {
        this.rules = rules;
        this.map = map;
        mapTiles();
    }

    private void mapTiles() {
        this.tiles = new Tile[10];
        tiles[0] = new Tile(Registry.textureIds.get("grass-1.png"), false);     // grass
        tiles[1] = new Tile(Registry.textureIds.get("wall-1.png"), true);       // wall
        tiles[2] = new Tile(Registry.textureIds.get("water-1.png"), true);      // water
        tiles[3] = new Tile(Registry.textureIds.get("dirt-1.png"), false);      // dirt
        tiles[4] = new Tile(Registry.textureIds.get("mid-trees.png"), true);    // tree mid portion
        tiles[5] = new Tile(Registry.textureIds.get("sand-1.png"), false);      // sand
        tiles[6] = new Tile(Registry.textureIds.get("top-trees.png"), true);    // tree top
        tiles[7] = new Tile(Registry.textureIds.get("bottom-trees.png"), true); // tree base
    }

    public boolean isSolid(int row, int col) {
        int tileId = map[row][col];
        return tiles[tileId].solid();
    }

    public int getTileSize() {
        return rules.tileSize();
    }

}
