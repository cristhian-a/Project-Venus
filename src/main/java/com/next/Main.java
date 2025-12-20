package com.next;

import com.next.engine.Global;
import com.next.engine.data.Mailbox;
import com.next.graphics.GamePanel;
import com.next.graphics.awt.AwtPanel;
import com.next.graphics.awt.Renderer;
import com.next.io.Loader;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.event.KeyListener;

public class Main {

    static void main() {
        // IO operations
        Settings settings = Loader.Settings.load();
        AssetRegistry assets = new AssetRegistry();
        assets.load();

        // Configuration
        Input input = new Input();
        KeyListener keyboardDevice = input.mapActions(settings.controls);

        // Central registry
        Mailbox mailbox = new Mailbox();

        // Setup and injection
        Game game = new Game(input, mailbox, settings, assets);
        Renderer renderer = new Renderer(game, mailbox, settings.video, assets);
        GamePanel panel = new AwtPanel(keyboardDevice, settings.video, renderer);

        Loop gameLoop = new Loop(game, panel, input);
        Global.setGameLoop(gameLoop);   // I might reconsider this later

        gameLoop.start();
    }
}
