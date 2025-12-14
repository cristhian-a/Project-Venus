package com.next;

import com.next.graphics.RenderData;
import com.next.model.Actor;
import com.next.model.Player;
import com.next.system.Input;
import com.next.system.Settings;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Game {

    private final Input input;
    private final Settings settings;

    private Player player;
    private Actor[] objects;
    @Getter private volatile Map<Integer, RenderData> renderBuffer;

    public Game(Input input, Settings settings) {
        this.input = input;
        this.settings = settings;

        player = new Player(2);
        objects = new Actor[30];
        objects[0] = player;

        renderBuffer = Map.of();
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        player.update(delta, input);
        setRenderBuffer();
    }

    private void setRenderBuffer() {
        Map<Integer, RenderData> snapshot = new LinkedHashMap<>(objects.length);

        for (int i = 0; i < objects.length; i++) {
            Actor object = objects[i];
            if (object != null) {
                snapshot.put(i, object.getRenderState());
            }
        }

        renderBuffer = Map.copyOf(snapshot);
    }
}
