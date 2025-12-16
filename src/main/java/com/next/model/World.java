package com.next.model;

import lombok.Getter;
import lombok.Setter;

public class World {

    @Getter private Tile[] tiles;
    @Getter @Setter private Integer[][] map;

    public World(Integer[][] map) {
        this.map = map;
        mapTiles();
    }

    private void mapTiles() {
        this.tiles = new Tile[10];
        tiles[0] = new Tile(0, false);  // grass
        tiles[1] = new Tile(1, true);   // wall
        tiles[2] = new Tile(25, true);  // water
        tiles[3] = new Tile(27, false); // dirt
        tiles[4] = new Tile(26, true);  // tree
        tiles[5] = new Tile(28, false); // sand
    }

    public boolean isSolid(int row, int col) {
        int tileId = map[row][col];
        return tiles[tileId].solid();
    }


}
