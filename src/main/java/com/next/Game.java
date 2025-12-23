package com.next;

import com.next.engine.Global;
import com.next.engine.graphics.RenderQueue;
import com.next.event.PauseEvent;
import com.next.util.GameState;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.model.Actor;
import com.next.engine.model.Camera;
import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionInspector;
import com.next.engine.physics.Physics;
import com.next.engine.sound.PlaySound;
import com.next.engine.sound.SoundChannel;
import com.next.event.handlers.DoorHandler;
import com.next.event.handlers.GameFlowHandler;
import com.next.event.handlers.SpellHandler;
import com.next.io.Loader;
import com.next.model.*;
import com.next.model.factory.*;
import com.next.system.AssetRegistry;
import com.next.engine.system.Debugger;
import com.next.system.Input;
import com.next.system.Settings;
import com.next.graphics.GameplayUIState;
import com.next.graphics.UISystem;
import com.next.util.Sounds;
import com.next.world.LevelData;
import com.next.world.Scene;
import com.next.world.World;
import com.next.world.WorldRules;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Game {

    // Dependencies
    private final Input input;
    private final Mailbox mailbox;
    private final Settings settings;
    private final AssetRegistry assets;
    private final EventDispatcher dispatcher;

    // Systems
    @Getter private final UISystem ui = new UISystem();
    private final Physics physics = new Physics();
    private final CollisionInspector collisionInspector = new CollisionInspector();

    // Handlers
    private final GameFlowHandler gameFlowHandler;

    @Getter @Setter private GameState gameState = GameState.RUNNING;
    @Getter private Camera camera;
    @Getter private Scene scene;

    public Game(Input input, Mailbox mailbox, Settings settings, AssetRegistry assets, EventDispatcher dispatcher) {
        this.input = input;
        this.assets = assets;
        this.mailbox = mailbox;
        this.settings = settings;
        this.dispatcher = dispatcher;

        // TODO this should be moved to start(), but can't until I fix the renderer queueing world render
        try {
            scene = loadScene("world_1.json", "level_1.json", "map_01");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new DoorHandler(dispatcher, mailbox);
        new SpellHandler(dispatcher, mailbox);
        gameFlowHandler = new GameFlowHandler(dispatcher, mailbox, this);
    }

    public void start() {
        int tileSize = scene.world.getRules().tileSize();   // Just to adjust the camera following
        camera = new Camera(settings.video.ORIGINAL_WIDTH, settings.video.ORIGINAL_HEIGHT, tileSize, tileSize);

        physics.ruleOver(scene);
        physics.setInspector(collisionInspector);

        var gameplayUIState = new GameplayUIState(scene.player);
        gameFlowHandler.setGameplayUIState(gameplayUIState);
        ui.setState(gameplayUIState);

        dispatcher.dispatch(new PlaySound(Sounds.WIND, SoundChannel.MUSIC, true));
    }

    public void update(double delta) {
        long start = System.nanoTime();

        RenderQueue writeQueue = mailbox.render.write();
        processInputs();

        if (gameState == GameState.RUNNING) {
            // TODO this is very incomplete

//            scene.player.update(delta, input, mailbox);
            scene.update(delta, mailbox);

            physics.apply(Global.fixedDelta, mailbox.motionQueue, mailbox); // always after update

            dispatcher.dispatch(mailbox);   // for now, this should happen after physics

            //
            // render portion
            scene.dismissActors();   // PLEASE, dismiss before rendering
        }

        scene.submitRender(writeQueue);
//        scene.player.submitRender(writeQueue);     // player always for last

        camera.follow(scene.player);    // the camera follows after events' resolution

        // First handlers, then systems updates
        gameFlowHandler.update(delta);
        ui.update(delta);

        ui.submit(writeQueue);

        mailbox.swap();
        long end = System.nanoTime();
        Debugger.publish("UPDATE", new Debugger.DebugLong(end - start), 500, 30, Debugger.TYPE.INFO);
    }

    public Scene loadScene(String worldFile, String levelFile, String map) throws IOException {
        WorldRules rules = Loader.World.load(worldFile);
        LevelData level = Loader.Level.load(levelFile);

        var world = new World(rules, assets.getTileMap(map));
        // TODO I might want to change to make player goes inside Actor's array
        Actor[] objects = new PropFactory(world, level).createScene1Props().toArray(new Actor[0]);
        Player player = new PlayerFactory(world, level).create();
        NpcGhost npc = new NpcFactory().create();

        player.setInput(input); // TODO meh

        Scene s = new Scene(world, player, objects);
        s.addActor(npc);
        s.addActor(player);

        return s;
    }

    public void processInputs() {
        if (input.isPressed(Input.Action.PAUSE)) {
            dispatcher.dispatch(new PauseEvent());
        }
    }
}
