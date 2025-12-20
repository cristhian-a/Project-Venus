package com.next.model.factory;

import com.next.core.model.Prop;
import com.next.core.physics.CollisionType;
import com.next.model.*;
import com.next.world.LevelData;
import com.next.world.World;

import java.util.ArrayList;
import java.util.List;

public class PropFactory {

    private final World world;
    private final LevelData level;

    public PropFactory(World world, LevelData level) {
        this.world = world;
        this.level = level;
    }

    public Spell createSpell(int worldX, int worldY) {
        return new Spell(15, worldX, worldY, CollisionType.NONE);
    }

    public Door createDoor(int worldX, int worldY) {
        return new Door(29, worldX, worldY, CollisionType.SOLID);
    }

    public Chest createChest(int worldX, int worldY) {
        return new Chest(30, worldX, worldY, CollisionType.SOLID);
    }

    public Key createKey(int worldX, int worldY) {
        return new Key(31, worldX, worldY, CollisionType.NONE);
    }

    public List<Prop> createScene1Props() {
        var props = new ArrayList<Prop>(8);
        props.add(createChest(10 * world.getTileSize(), 7 * world.getTileSize()));
        props.add(createDoor(10 * world.getTileSize(), 11 * world.getTileSize()));
        props.add(createDoor(8 * world.getTileSize(), 28 * world.getTileSize()));
        props.add(createDoor(12 * world.getTileSize(), 22 * world.getTileSize()));
        props.add(createKey(23 * world.getTileSize(), 7 * world.getTileSize()));
        props.add(createKey(23 * world.getTileSize(), 40 * world.getTileSize()));
        props.add(createKey(37 * world.getTileSize(), 9 * world.getTileSize()));
        props.add(createSpell(37 * world.getTileSize(), 42 * world.getTileSize()));
        return props;
    }
}
