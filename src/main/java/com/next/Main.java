package com.next;

import com.next.graphic.GamePanel;
import com.next.graphic.Renderer;

public class Main {
    static void main() {
        Game game = new Game();
        GamePanel panel = new GamePanel(game);
        Renderer renderer = new Renderer(game, panel);
        Loop loop = new Loop(game, renderer);

        loop.start();
    }
}
