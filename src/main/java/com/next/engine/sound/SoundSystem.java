package com.next.engine.sound;

import java.util.EnumMap;

public class SoundSystem {

    private final AudioBackend backend;

    public SoundSystem(AudioBackend backend) {
        this.backend = backend;
    }

    public void fire(AudioCommand command) {
        switch (command) {
            case PlaySound p -> backend.play(p.clip(), p.channel(), p.loop());
            case StopSound s -> backend.stop(s.clip());
            case SetVolume v -> backend.setVolume(v.volume(), v.channel());
        }
    }
}
