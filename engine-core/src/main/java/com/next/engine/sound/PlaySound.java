package com.next.engine.sound;

public record PlaySound(SoundClip clip, SoundChannel channel, boolean loop) implements AudioCommand {
}
