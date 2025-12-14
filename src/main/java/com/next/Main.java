package com.next;

import com.next.graphics.GamePanel;
import com.next.graphics.awt.AwtPanel;
import com.next.graphics.awt.Renderer;
import com.next.io.Loader.SettingsLoader;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.event.KeyListener;

public class Main {

    static void main() {
        Settings settings = SettingsLoader.load();
        Input input = new Input();
        KeyListener keyboardDevice = input.mapActions(settings.controls);

        Game game = new Game(input, settings);

        Renderer renderer = new Renderer(game);
        GamePanel panel = new AwtPanel(keyboardDevice, settings.video, renderer);

        new Loop(game, panel, input).start();
    }
}
