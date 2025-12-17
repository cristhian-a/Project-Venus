package com.next;

import com.next.graphics.RenderQueue;
import com.next.io.Loader;
import com.next.model.*;
import com.next.model.factory.PlayerFactory;
import com.next.model.factory.PropFactory;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;
import com.next.world.LevelData;
import com.next.world.World;
import com.next.world.WorldRules;
import lombok.Getter;

import java.io.IOException;

public class Game {

    private final Input input;
    private final Settings settings;
    private final AssetRegistry assets;

    @Getter private final Player player;
    @Getter private final Camera camera;
    @Getter private final RenderQueue renderQueue;

    @Getter private World world;
    @Getter private Actor[] objects;

    private final CollisionInspector collisionInspector;

    public Game(Input input, Settings settings, AssetRegistry assets) {
        this.input = input;
        this.assets = assets;
        this.settings = settings;

        player = setupScene("map_01");  // TODO: this is gambiarra, I need to fix it later

        renderQueue = new RenderQueue();
        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT);
        collisionInspector = new CollisionInspector(world);
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        player.update(delta, input, collisionInspector);

        for (Actor object : objects) {
            collisionInspector.isColliding(player, object);
        }

        camera.follow(player);
        queueRendering();
    }

    private void queueRendering() {
        renderQueue.clear();

        for (Actor object : objects) {
            if (object != null) {
                renderQueue.submit(object.getRenderInstruction());
            }
        }

        renderQueue.submit(player.getRenderInstruction());
    }

    private Player setupScene(String map) {
        try {
            WorldRules rules = Loader.World.load("world_1.json");
            LevelData level = Loader.Level.load("level_1.json");

            world = new World(rules, assets.getTileMap(map));
            objects = new PropFactory(world, level).createScene1Props().toArray(new Prop[0]);
            return new PlayerFactory(world, level).create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
