package com.next;

import com.next.engine.data.Registry;
import com.next.engine.data.AtlasImporter;
import com.next.engine.event.*;
import com.next.engine.model.*;
import com.next.event.DisplayStatsEvent;
import com.next.event.FallDamageEvent;
import com.next.event.PauseEvent;
import com.next.event.PitFallEvent;
import com.next.event.handlers.*;
import com.next.gameflow.*;
import com.next.engine.data.Mailbox;
import com.next.engine.physics.Physics;
import com.next.io.Loader;
import com.next.model.*;
import com.next.model.factory.*;
import com.next.system.AssetRegistry;
import com.next.engine.system.Debugger;
import com.next.system.Input;
import com.next.system.Settings;
import com.next.ui.GameplayUIState;
import com.next.ui.UISystem;
import com.next.world.LevelData;
import com.next.world.Scene;
import com.next.world.World;
import com.next.world.WorldRules;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
public class Game {

    // Dependencies
    private final Input input;
    private final Mailbox mailbox;
    private final AssetRegistry assets;
    private final Settings settings;
    private final EventDispatcher dispatcher;

    // Systems
    private final UISystem ui = new UISystem();
    private final Physics physics = new Physics();

    // Handlers
    private final GameFlowHandler gameFlowHandler;
    private final CombatHandler combatHandler;
    private PlayerHandler playerHandler;

    // States (if needed)
    private GameMode mode;
    @Setter private GameplayUIState gameplayUIState;

    private Camera camera;
    private Scene scene;
    private Player player;

    public Game(Input input, Mailbox mailbox, Settings settings, AssetRegistry assets, EventDispatcher dispatcher) {
        this.input = input;
        this.assets = assets;
        this.mailbox = mailbox;
        this.settings = settings;
        this.dispatcher = dispatcher;

        new PitFallEvent.Handler(dispatcher);
        new FallDamageEvent.Handler(dispatcher);
        new DoorHandler(dispatcher, mailbox);
        new SpellHandler(dispatcher, mailbox);
        combatHandler = new CombatHandler(mailbox, dispatcher);
        gameFlowHandler = new GameFlowHandler(dispatcher, this);

        Registry.audioTracks.putAll(Loader.Audio.load());
    }

    public void setMode(GameMode newMode) {
        if (mode != null) mode.onExit(this);
        mode = newMode;
        mode.onEnter(this);
    }

    public void boot() {
        try {
            // TODO this probably will go elsewhere
            Registry.textures.put(1, Loader.Textures.loadImage("light-mask-2.png"));
            Registry.textures.put(2, Loader.Textures.loadImage("light-mask-3.png"));
            Registry.textures.put(3, Loader.Textures.loadImage("light-mask-4.png"));
            Registry.textures.put(4, Loader.Textures.loadImage("lmask-64x-halo-strong.png"));
            Registry.textures.put(5, Loader.Textures.loadImage("lmask_64x_less.png"));

            var sheetMetadata = Loader.Textures.loadMetadata("sprites.json");
            var sheet = Loader.Textures.loadImage("sprites.png");
            AtlasImporter.register(sheet, sheetMetadata);

            // TODO this should be moved to start(), but can't until I fix the renderer queueing world render
            scene = loadScene("world_1.json", "level_1.json", "map_01");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        camera = new Camera(settings.video.WIDTH, settings.video.HEIGHT, 0, 0, settings.video.SCALE);
        scene.camera = camera;
        setMode(new TitleMode());
    }

    public void start() {
        gameplayUIState = new GameplayUIState(scene, player, dispatcher, settings.video);
        camera = new Camera(settings.video.UNSCALED_WIDTH, settings.video.UNSCALED_HEIGHT, 0, 0, settings.video.SCALE);
        scene.camera = camera;
        physics.ruleOver(scene);
        playerHandler = new PlayerHandler(dispatcher, gameplayUIState);
        dispatcher.dispatch(new WorldTransitionEvent(scene.world));
    }

    public void update(double delta) {
        long start = System.nanoTime();

        mailbox.beginFrame();
        processInputs();

        mode.update(this, delta);
        ui.update(delta);
        ui.submit(mailbox.postRender());

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

        Scene s = new Scene(world);
        s.addAll(props);
        s.add(npc);
        s.add(fc);
        s.add(light);
//        s.add(lightB);
//        s.add(lightC);
//        s.add(light2);
        s.add(MobFactory.create(342, 649));
        s.add(MobFactory.create(380, 610));

        HitboxFactory hitboxFactory = new HitboxFactory(s);
        player = new PlayerFactory(world, level, hitboxFactory).create();
        player.setInput(input); // TODO meh
        s.add(player);

        // TODO take care: the same rule, for now, share state within multiple sensors, that means a once-use is REALLY once,
        // TODO don't matter how many sensors receive that policy instance (TriggerRule)
//        var triggerRule = TriggerRules
//                .when(Conditions.IS_PLAYER)
//                .and((self, other) -> ((Player) other).getHealth() > 0)
//                .then(Actions.damagePlayer(1));
//        triggerRule = Sensors.once(triggerRule);
//
//        Sensor dmg = new Sensor(305, 580, 6, 6, triggerRule);
//        Sensor sus = Sensors.singleUse(400, 596, 6, 6,
//                TriggerRules.when(Conditions.IS_PLAYER).then(Actions.damagePlayer(1))
//        );
//
//        Sensor tracker = Sensors.builder()
//                .onEnter(Conditions.IS_PLAYER).then(Actions.damagePlayer(1))
//                .onCollision(Conditions.IS_PLAYER).then((self, other) -> {
//                    Debugger.publish("test", new Debugger.DebugText("PIT"), 250, 250, Debugger.TYPE.INFO);
//                    return null;
//                })
//                .onExit(Conditions.IS_PLAYER).then((self, other) -> {
//                    IO.println("Exited");
//                    return null;
//                })
//                .build(400, 340, 32, 32);
//
//        s.add(dmg);
//        s.add(sus);
//        s.add(tracker);

        return s;
    }

    public void processInputs() {
        if (input.isTyped(Input.Action.PAUSE)) {
            dispatcher.dispatch(new PauseEvent());
        } else if (input.isTyped(Input.Action.DISPLAY_STATS)) {
            dispatcher.dispatch(new DisplayStatsEvent());
        }
    }

    public boolean isRunning() {
        return mode instanceof RunningMode;
    }

    public boolean isPaused() {
        return mode instanceof PausedMode;
    }

    public boolean isDisplayingStats() {
        return mode instanceof StatsViewMode;
    }
}
