package com.next;

import com.next.graphics.RenderQueue;
import com.next.model.Actor;
import com.next.model.Camera;
import com.next.model.Player;
import com.next.model.World;
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

    public Game(Input input, Settings settings, AssetRegistry assets) {
        this.input = input;
        this.assets = assets;
        this.settings = settings;

        setupWorld("map_01");
        player = new Player(2);
        objects = new Actor[30];
        objects[0] = player;

        renderQueue = new RenderQueue();
        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT);
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        player.update(delta, input);
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
