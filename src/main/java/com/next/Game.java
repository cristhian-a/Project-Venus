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
import com.next.world.Scene;
import com.next.world.World;
import com.next.world.WorldRules;
import lombok.Getter;

import java.io.IOException;

public class Game {

    private final Input input;
    private final Settings settings;
    private final AssetRegistry assets;

    @Getter private final Camera camera;
    @Getter private final RenderQueue renderQueue;

    @Getter private Scene scene;

    private final CollisionInspector collisionInspector;

    public Game(Input input, Settings settings, AssetRegistry assets) {
        this.input = input;
        this.assets = assets;
        this.settings = settings;

        try {
            scene = loadScene("world_1.json", "level_1.json", "map_01");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        renderQueue = new RenderQueue();
        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT);
        collisionInspector = new CollisionInspector(scene.world);
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        scene.player.update(delta, input, collisionInspector);

        for (Actor object : scene.actors) {
            collisionInspector.isColliding(scene.player, object);
        }

        camera.follow(scene.player);
        queueRendering();
    }

    private void queueRendering() {
        renderQueue.clear();

        for (Actor object : scene.actors) {
            if (object != null) {
                renderQueue.submit(object.getRenderRequest());
            }
        }

        renderQueue.submit(scene.player.getRenderRequest());
    }

    private Scene loadScene(String worldFile, String levelFile, String map) throws IOException {
        WorldRules rules = Loader.World.load(worldFile);
        LevelData level = Loader.Level.load(levelFile);

        var world = new World(rules, assets.getTileMap(map));
        Actor[] objects = new PropFactory(world, level).createScene1Props().toArray(new Prop[0]);
        Player player = new PlayerFactory(world, level).create();

        return new Scene(world, player, objects);
    }
}
