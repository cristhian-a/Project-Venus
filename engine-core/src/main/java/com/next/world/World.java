package com.next.world;

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
        tiles[0] = new Tile(0, false);  // grass
        tiles[1] = new Tile(1, true);   // wall
        tiles[2] = new Tile(25, true);  // water
        tiles[3] = new Tile(27, false); // dirt
        tiles[4] = new Tile(43, true);  // tree mid portion
        tiles[5] = new Tile(28, false); // sand
        tiles[6] = new Tile(42, true);  // tree top
        tiles[7] = new Tile(44, true); // tree base
    }

    public boolean isSolid(int row, int col) {
        int tileId = map[row][col];
        return tiles[tileId].solid();
    }

    public int getTileSize() {
        return rules.tileSize();
    }

}
