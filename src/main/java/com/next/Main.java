package com.next;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GracefullyStopEvent;
import com.next.graphics.GamePanel;
import com.next.graphics.awt.AwtPanel;
import com.next.graphics.awt.Renderer;
import com.next.io.Loader;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.event.KeyListener;

public class Main {

    static void main(String[] args) {
        // IO operations
        Settings settings = Loader.Settings.load();
        AssetRegistry assets = new AssetRegistry();
        assets.load();

        // Configuration
        Input input = new Input();
        KeyListener keyboardDevice = input.mapActions(settings.controls);

        // Communication channels
        Mailbox mailbox = new Mailbox();
        EventDispatcher centralDispatcher = new EventDispatcher();

        // Loop setup
        Game game = new Game(input, mailbox, settings, assets, centralDispatcher);
        Renderer renderer = new Renderer(game, mailbox, settings.video, assets);
        GamePanel panel = new AwtPanel(keyboardDevice, settings.video, renderer);

        Loop gameLoop = new Loop(game, panel, input);

        // Setting default listeners up
        GracefullyStopEvent.Handler G = new GracefullyStopEvent.Handler(gameLoop);
        centralDispatcher.register(GracefullyStopEvent.class, G::onFire);

        gameLoop.start();
    }
}
