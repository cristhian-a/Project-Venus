package com.next;

import com.next.graphics.GamePanel;
import com.next.graphics.awt.AwtPanel;
import com.next.graphics.awt.Renderer;
import com.next.io.Loader.SettingsLoader;
import com.next.system.AssetRegistry;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.event.KeyListener;

public class Main {

    static void main() {
        // IO operations
        Settings settings = SettingsLoader.load();
        AssetRegistry assets = new AssetRegistry();
        assets.load();

        // Configuration
        Input input = new Input();
        KeyListener keyboardDevice = input.mapActions(settings.controls);

        // Setup and injection
        Game game = new Game(input, settings);
        Renderer renderer = new Renderer(game, assets);
        GamePanel panel = new AwtPanel(keyboardDevice, settings.video, renderer);

        new Loop(game, panel, input).start();
    }
}
