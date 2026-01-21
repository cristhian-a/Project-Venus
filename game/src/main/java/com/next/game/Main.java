package com.next.game;

import com.next.engine.Conductor;
import com.next.engine.Director;
import com.next.engine.data.Mailbox;
import com.next.engine.data.Registry;
import com.next.engine.debug.*;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.ExitEvent;
import com.next.engine.event.GracefullyStopEvent;
import com.next.engine.event.WorldTransitionEvent;
import com.next.engine.graphics.GamePanel;
import com.next.engine.graphics.awt.AwtCanvas;
import com.next.engine.graphics.awt.Renderer;
import com.next.engine.io.AwtInputListener;
import com.next.engine.sound.*;
import com.next.engine.sound.jxsound.JavaSoundBackend;
import com.next.engine.system.*;
import com.next.game.io.Loader;
import com.next.game.util.Inputs;

final class Main {

    void main() {
        // IO operations
        Loader.Founts.register();
        Settings settings = Loader.Settings.load();

        // Configuration
        Input input = new Input();
        var keyboardDevice = new AwtInputListener();
        input.mapActions(Loader.Controls.loadActionMap(), keyboardDevice);
        InputBindings inputBindings = new InputBindings(input);
        inputBindings.bindActionToChannel(Inputs.DEBUG_MODE_1, DebugChannel.INFO);
        inputBindings.bindActionToChannel(Inputs.DEBUG_MEMORY, DebugChannel.MEMORY);
        inputBindings.bindActionToChannel(Inputs.DEBUG_COLLISIONS, DebugChannel.PHYSICS);

        // Debug tools registering (for now I'll do it here, but this should be moved elsewhere latter)
        Tools.registerTools();  // This utility class, I have to think twice about it

        // Communication channels
        Mailbox mailbox = new Mailbox();
        EventDispatcher centralDispatcher = new EventDispatcher();

        // Loop setup
        Director game = new Game(input, mailbox, settings, centralDispatcher);
        Renderer renderer = new Renderer(game, mailbox, settings.video);
        GamePanel panel = new AwtCanvas(renderer, settings.video, keyboardDevice);

        Conductor loop = new Conductor(game, panel, input, inputBindings);

        // Setting default listeners up
        AudioBackend audio = new JavaSoundBackend(Registry.audioTracks);
        SoundSystem sound = new SoundSystem(audio);

        GracefullyStopEvent.Handler G = new GracefullyStopEvent.Handler(loop);
        ExitEvent.Handler E = new ExitEvent.Handler();

        centralDispatcher.register(GracefullyStopEvent.class, G::onFire);
        centralDispatcher.register(ExitEvent.class, E::onFire);
        centralDispatcher.register(WorldTransitionEvent.class, renderer::onWorldTransition);
//        centralDispatcher.register(PlaySound.class, sound::fire);
//        centralDispatcher.register(StopSound.class, sound::fire);
//        centralDispatcher.register(SetVolume.class, sound::fire);
//        centralDispatcher.register(PauseSound.class, sound::fire);
//        centralDispatcher.register(RestartSound.class, sound::fire);

        loop.start();   // loop start should always happen last, to guarantee that listeners are properly set up
    }
}
