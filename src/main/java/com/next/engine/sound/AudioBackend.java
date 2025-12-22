package com.next.engine.sound;

public interface AudioBackend {
    void play(SoundClip clip, SoundChannel channel, boolean loop);
    void stop(SoundClip clip);
    void setVolume(float volume, SoundChannel channel);
}
