package com.next;

import com.next.engine.Global;
import com.next.engine.data.Cache;
import com.next.engine.data.Registry;
import com.next.engine.event.*;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.*;
import com.next.event.DamageEvent;
import com.next.event.PauseEvent;
import com.next.event.PitFallEvent;
import com.next.event.handlers.PlayerHandler;
import com.next.graphics.StartMenuUIState;
import com.next.graphics.UIState;
import com.next.rules.Actions;
import com.next.rules.Conditions;
import com.next.engine.util.GameState;
import com.next.engine.data.Mailbox;
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
import com.next.engine.util.Sounds;
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
    private final AssetRegistry assets;
    @Getter private final Settings settings;
    private final EventDispatcher dispatcher;

    // Systems
    @Getter private final UISystem ui = new UISystem();
    private final Physics physics = new Physics();

    // Handlers
    private final GameFlowHandler gameFlowHandler;
    private PlayerHandler playerHandler;

    // States (if needed)
    private GameplayUIState gameplayUIState;

    @Getter @Setter private GameState gameState = GameState.START_MENU;
    @Getter private Camera camera;
    @Getter private Scene scene;

    public Game(Input input, Mailbox mailbox, Settings settings, AssetRegistry assets, EventDispatcher dispatcher) {
        this.input = input;
        this.assets = assets;
        this.mailbox = mailbox;
        this.settings = settings;
        this.dispatcher = dispatcher;

        new PitFallEvent.Handler(dispatcher);
        new DamageEvent.Handler(dispatcher);
        new DoorHandler(dispatcher, mailbox);
        new SpellHandler(dispatcher, mailbox);
        gameFlowHandler = new GameFlowHandler(dispatcher, mailbox, input, this);
    }

    public void boot() {
        try {
            // TODO this should be moved to start(), but can't until I fix the renderer queueing world render
            scene = loadScene("world_1.json", "level_1.json", "map_01");

            Registry.textureSheets.put(0, Loader.Textures.loadSheet("light.png", 16, 16));
            Registry.textures.put(1, Loader.Textures.loadImage("light-mask-2.png"));
            Registry.textures.put(2, Loader.Textures.loadImage("light-mask-3.png"));
            Registry.textures.put(3, Loader.Textures.loadImage("light-mask-4.png"));
            Registry.textures.put(4, Loader.Textures.loadImage("lmask-64x-halo-strong.png"));
            Registry.textures.put(5, Loader.Textures.loadImage("lmask_64x_less.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        camera = new Camera(settings.video.WIDTH, settings.video.HEIGHT, 0, 0, settings.video.SCALE);
        ui.setState(new StartMenuUIState(input, dispatcher));
    }

    public void start(UIState uiState) {
        int tileSize = scene.world.getRules().tileSize();   // Just to adjust the camera following
        camera = new Camera(settings.video.UNSCALED_WIDTH, settings.video.UNSCALED_HEIGHT, tileSize, tileSize, settings.video.SCALE);

        physics.ruleOver(scene);

        playerHandler = new PlayerHandler(dispatcher, (GameplayUIState) uiState);

        dispatcher.dispatch(new WorldTransitionEvent(scene.world));
        dispatcher.dispatch(new PlaySound(Sounds.WIND, SoundChannel.MUSIC, true));
    }

    public void update(double delta) {
        long start = System.nanoTime();

        mailbox.beginFrame();

        RenderQueue writeQueue = mailbox.postRender();
        processInputs();

        // TODO GameFlowHandler and Game are competing as conductors now
        gameFlowHandler.update(delta);

        // TODO game states are poorly managed right now
        if (gameState == GameState.START_MENU) {
            // Nothing?
        }
        if (gameState == GameState.RUNNING) {
            // TODO this is very incomplete
            scene.update(delta, mailbox);

            physics.apply(Global.fixedDelta, mailbox.motionQueue, mailbox); // always after update

            dispatcher.dispatch(mailbox);   // for now, this should happen after physics

            scene.dismissDisposed();   // PLEASE, dismiss before rendering
        }

        camera.follow(scene.player);    // the camera follows after events' resolution
        scene.submitRender(writeQueue);

        ui.update(delta);
        ui.submit(writeQueue);

        mailbox.publish();
        long end = System.nanoTime();
        Debugger.publish("UPDATE", new Debugger.DebugLong(end - start), 500, 30, Debugger.TYPE.INFO);
    }

    public Scene loadScene(String worldFile, String levelFile, String map) throws IOException {
        // TODO this whole method is a mess soup for testing right now
        WorldRules rules = Loader.Worlds.load(worldFile);
        LevelData level = Loader.Levels.load(levelFile);

        var world = new World(rules, assets.getTileMap(map));
        // TODO I might want to change to make player goes inside Actor's array
        Entity[] props = new PropFactory(world, level).createScene1Props().toArray(new Entity[0]);
        NpcDummy npc = new NpcFactory().createDummy();
        ObjectFireCamp fc = ObjectFactory.create();

        Light light = LightFactory.create(376, 344);
        Light lightB = LightFactory.create(376, 344);
        Light lightC = LightFactory.create(376, 344);
        Light light2 = LightFactory.create(300, 344);

        Player player = new PlayerFactory(world, level).create();
        player.setInput(input); // TODO meh

        Scene s = new Scene(world, player);
        s.addAll(props);
        s.add(npc);
        s.add(fc);
        s.add(light);
//        s.add(lightB);
//        s.add(lightC);
//        s.add(light2);
        s.add(player);

        // TODO take care: the same rule, for now, share state within multiple sensors, that means a once-use is REALLY once,
        // TODO don't matter how many sensors receive that policy instance (TriggerRule)
        var triggerRule = TriggerRules
                .when(Conditions.IS_PLAYER)
                .and((self, other) -> ((Player) other).getHealth() > 0)
                .then(Actions.damagePlayer(1));
        triggerRule = Sensors.once(triggerRule);

        Sensor dmg = new Sensor(305, 580, 6, 6, triggerRule);
        Sensor sus = Sensors.singleUse(400, 596, 6, 6,
                TriggerRules.when(Conditions.IS_PLAYER).then(Actions.damagePlayer(1))
        );

        s.add(dmg);
        s.add(sus);

        return s;
    }

    public void processInputs() {
        if (input.isPressed(Input.Action.PAUSE)) {
            dispatcher.dispatch(new PauseEvent());
        }
    }
}
