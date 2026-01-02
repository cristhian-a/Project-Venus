package com.next.model.factory;

import com.next.engine.data.Registry;
import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionType;
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
        return new Spell(Registry.textureIds.get("spell.png"), worldX, worldY, CollisionType.TRIGGER);
    }

    public Door createDoor(int worldX, int worldY) {
        return new Door(Registry.textureIds.get("door-1.png"), worldX, worldY, CollisionType.SOLID);
    }

    public Chest createChest(int worldX, int worldY) {
        return new Chest(Registry.textureIds.get("chest-1.png"), worldX, worldY, CollisionType.SOLID);
    }

    public Key createKey(int worldX, int worldY) {
        return new Key(Registry.textureIds.get("key-1.png"), worldX, worldY, CollisionType.TRIGGER);
    }

    public Prop createStoneBlock(int worldX, int worldY) {
        return new Prop(Registry.textureIds.get("stone-block-1.png"), worldX, worldY, 1f, CollisionType.SOLID, -6, -6, 12, 12);
    }

    public DisplayHeart createHeart(int worldX, int worldY) {
        int full = Registry.textureIds.get("heart-1.png");
        int half = Registry.textureIds.get("heart-2.png");
        int empty = Registry.textureIds.get("heart-3.png");
        return new DisplayHeart(full, half, empty);
    }

    public List<Prop> createScene1Props() {
        var props = new ArrayList<Prop>(8);
        int pivot = 8; // offset in relation to the pivot

        props.add(createChest(10 * world.getTileSize() + pivot, 7 * world.getTileSize() + pivot));
        props.add(createDoor(10 * world.getTileSize() + pivot, 11 * world.getTileSize() + pivot));
        props.add(createDoor(8 * world.getTileSize() + pivot, 28 * world.getTileSize() + pivot));
        props.add(createDoor(12 * world.getTileSize() + pivot, 22 * world.getTileSize() + pivot));
        props.add(createKey(23 * world.getTileSize() + pivot, 7 * world.getTileSize() + pivot));
        props.add(createKey(23 * world.getTileSize() + pivot, 40 * world.getTileSize() + pivot));
        props.add(createKey(37 * world.getTileSize() + pivot, 9 * world.getTileSize() + pivot));
        props.add(createSpell(37 * world.getTileSize() + pivot, 42 * world.getTileSize() + pivot));
        props.add(createStoneBlock(23 * world.getTileSize() + pivot, 23 * world.getTileSize() + pivot));
        return props;
    }
}
