package com.next;

import com.next.graphic.GamePanel;
import com.next.graphic.Renderer;
import com.next.io.InputReader;
import com.next.io.Loader.SettingsLoader;
import com.next.system.Settings;

public class Main {

    static void main() {
        Settings settings = SettingsLoader.load();
        InputReader input = new InputReader();

        Game game = new Game(input, settings);
        GamePanel panel = new GamePanel(game, input, settings.video);
        Renderer renderer = new Renderer(game, panel);

        new Loop(game, renderer, input).start();
    }
}
