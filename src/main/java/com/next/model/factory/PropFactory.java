package com.next.model.factory;

import com.next.model.Prop;
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

    public Prop createSpell(int worldX, int worldY) {
        return new Prop(15, worldX, worldY, true);
    }

    public Prop createDoor(int worldX, int worldY) {
        return new Prop(29, worldX, worldY, true);
    }

    public Prop createChest(int worldX, int worldY) {
        return new Prop(30, worldX, worldY, true);
    }

    public Prop createKey(int worldX, int worldY) {
        return new Prop(31, worldX, worldY, true);
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
