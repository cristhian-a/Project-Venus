package com.next;

import com.next.engine.Loop;
import com.next.engine.data.Mailbox;
import com.next.engine.data.Registry;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.ExitEvent;
import com.next.engine.event.GracefullyStopEvent;
import com.next.engine.event.WorldTransitionEvent;
import com.next.engine.graphics.GamePanel;
import com.next.engine.graphics.awt.AwtPanel;
import com.next.engine.graphics.awt.Renderer;
import com.next.engine.io.AwtInputListener;
import com.next.engine.sound.*;
import com.next.engine.sound.jxsound.JavaSoundBackend;
import com.next.io.Loader;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.event.KeyListener;
import java.util.Map;

public class Main {

    static void main(String[] args) {
        // IO operations
        Settings settings = Loader.Settings.load();
        AssetRegistry assets = new AssetRegistry();
        assets.load();
        Map<String, Integer> actionMap = Loader.Controls.loadActionMap();

        // Configuration
        Input input = new Input();
        var keyboardDevice = new AwtInputListener();
        input.mapActions(actionMap, keyboardDevice);

        // Communication channels
        Mailbox mailbox = new Mailbox();
        EventDispatcher centralDispatcher = new EventDispatcher();

        // Loop setup
        Game game = new Game(input, mailbox, settings, assets, centralDispatcher);
        Renderer renderer = new Renderer(game, mailbox, settings.video, assets);
        GamePanel panel = new AwtPanel(keyboardDevice, settings.video, renderer);

        Loop gameLoop = new Loop(game, panel, input);

        // Setting default listeners up
        AudioBackend audio = new JavaSoundBackend(Registry.audioTracks);
        SoundSystem sound = new SoundSystem(audio);

        GracefullyStopEvent.Handler G = new GracefullyStopEvent.Handler(gameLoop);
        ExitEvent.Handler E = new ExitEvent.Handler();

        centralDispatcher.register(GracefullyStopEvent.class, G::onFire);
        centralDispatcher.register(ExitEvent.class, E::onFire);
        centralDispatcher.register(WorldTransitionEvent.class, renderer::onWorldTransition);
//        centralDispatcher.register(PlaySound.class, sound::fire);
//        centralDispatcher.register(StopSound.class, sound::fire);
//        centralDispatcher.register(SetVolume.class, sound::fire);
//        centralDispatcher.register(PauseSound.class, sound::fire);
//        centralDispatcher.register(RestartSound.class, sound::fire);

        gameLoop.start();   // loop start should always happen last, to guarantee that listeners are properly set up
    }
}
