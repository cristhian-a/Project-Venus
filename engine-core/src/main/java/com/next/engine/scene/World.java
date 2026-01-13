package com.next.engine.scene;

import com.next.engine.physics.Body;
import lombok.Getter;
import lombok.Setter;

public class World {

    @Getter private final WorldRules rules;

    @Getter private final Tile[] tiles;
    @Getter @Setter private Integer[][] map;

    public World(WorldRules rules, Integer[][] map, Tile[] tiles) {
        this.rules = rules;
        this.map = map;
        this.tiles = tiles;
    }

    public boolean makesContact(int row, int col, Body body) {
        int tileId = map[row][col];
        return (tiles[tileId].collisionMask() & body.getLayer()) != 0 ||
                (tiles[tileId].collisionLayer() & body.getCollisionMask()) != 0;
    }

    public int getTileSize() {
        return rules.tileSize();
    }

}
