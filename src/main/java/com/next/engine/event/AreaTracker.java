package com.next.engine.event;

import com.next.engine.model.Sensor;
import com.next.engine.physics.Body;

import java.util.HashSet;
import java.util.Set;

public class AreaTracker {

    private final Sensor area;
//    private final Sensor outer;
    private final Set<Body> previous = new HashSet<>();
    private final Set<Body> current = new HashSet<>();

    public AreaTracker(int x, int y, int width, int height) {
        area = new Sensor(x, y, width, height,
                TriggerRules.when((self, other) -> !previous.contains(other))
                        .then((self, other) -> {
                            current.add(other);
                            IO.println("In!");
                            return null;
                        })
        );
    }

    public void endFrame() {
        previous.addAll(current);
        current.clear();
    }

}
