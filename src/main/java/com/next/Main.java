package com.next;

import com.next.graphic.GamePanel;
import com.next.graphic.Renderer;
import com.next.io.InputReader;
import com.next.io.Loader.SettingsLoader;
import com.next.system.Input;
import com.next.system.Settings;

public class Main {

    static void main() {
        Settings settings = SettingsLoader.load();
        Input input = new Input();
        InputReader keyboardDevice = input.mapActions(settings.controls);

        Game game = new Game(input, settings);

        GamePanel panel = new GamePanel(game, keyboardDevice, settings.video);
        Renderer renderer = new Renderer(game, panel);

        new Loop(game, renderer, input).start();
    }
}
