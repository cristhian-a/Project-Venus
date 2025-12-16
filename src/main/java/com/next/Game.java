package com.next;

import com.next.core.Animator;
import com.next.graphics.RenderQueue;
import com.next.model.*;
import com.next.model.factory.PlayerFactory;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;
import lombok.Getter;

public class Game {

    private final Input input;
    private final Settings settings;
    private final AssetRegistry assets;

    @Getter private World world;
    @Getter private final Player player;
    @Getter private final Camera camera;
    @Getter private final Actor[] objects;
    @Getter private volatile RenderQueue renderQueue;

    private final CollisionInspector collisionInspector;

    public Game(Input input, Settings settings, AssetRegistry assets) {
        this.input = input;
        this.assets = assets;
        this.settings = settings;

        setupWorld("map_01");
        player = PlayerFactory.createPlayer();
        objects = new Actor[30];
        objects[0] = player;

        renderQueue = new RenderQueue();
        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT);
        collisionInspector = new CollisionInspector(Settings.TILE_SIZE, world);
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        player.update(delta, input, collisionInspector);

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
    }

    private void setupWorld(String map) {
        world = new World(assets.getTileMap("map_01"));
    }
}
