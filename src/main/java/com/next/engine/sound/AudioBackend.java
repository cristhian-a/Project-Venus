package com.next.engine.sound;

public interface AudioBackend {
    void play(SoundClip clip, SoundChannel channel, boolean loop);
    void stop(SoundClip clip);
    void pause(SoundClip clip);
    void restart(SoundClip clip);
    void setVolume(float volume, SoundChannel channel);
}
