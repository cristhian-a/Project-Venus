package com.next;

import com.next.core.data.Mailbox;
import com.next.core.event.EventDispatcher;
import com.next.core.model.Actor;
import com.next.core.model.Camera;
import com.next.core.model.Prop;
import com.next.core.physics.CollisionInspector;
import com.next.core.physics.Physics;
import com.next.io.Loader;
import com.next.model.*;
import com.next.model.factory.*;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;
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
    private final Mailbox mailbox;
    private final Settings settings;
    private final AssetRegistry assets;
    private final CollisionInspector collisionInspector;

    private final Physics physics = new Physics();

    @Getter private final Camera camera;
    @Getter private Scene scene;

    public Game(Input input, Mailbox mailbox, Settings settings, AssetRegistry assets) {
        this.input = input;
        this.assets = assets;
        this.mailbox = mailbox;
        this.settings = settings;

        try {
            scene = loadScene("world_1.json", "level_1.json", "map_01");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT);
        collisionInspector = new CollisionInspector();
        physics.ruleOver(scene);
        physics.setInspector(collisionInspector);
    }

    public void update(double delta) {
        long start = System.nanoTime();

        scene.player.update(delta, input, mailbox);
        for (int i = 0; i < scene.actors.length; i++) {
            var actor = scene.actors[i];
            actor.update(delta, mailbox);
        }

        physics.apply(delta, mailbox); // always after update

        EventDispatcher.dispatch(mailbox);    // for now, this should happen after physics

        //
        // render portion
        scene.dismissDisposedActors();   // PLEASE, dismiss before rendering

        for (int i = 0; i < scene.actors.length; i++) {
            var actor = scene.actors[i];
            actor.submitRender(mailbox);
        }
        scene.player.submitRender(mailbox);     // player always for last

        camera.follow(scene.player);

        long end = System.nanoTime();
        Debugger.publish("UPDATE", new Debugger.DebugLong(end - start), 500, 30, Debugger.TYPE.INFO);
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
